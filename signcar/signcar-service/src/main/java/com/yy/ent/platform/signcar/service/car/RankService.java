package com.yy.ent.platform.signcar.service.car;

import com.alibaba.fastjson.JSON;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.RankData;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.WebdbService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.util.*;

/**
 * 排名榜单相关
 */
@Service
public class RankService extends BaseService {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    private WebdbService webdbService;
    @Autowired
    private ActivityConfigService activityConfigService;
    @Autowired
    private NotifyService notifyService;
    /**
     * 榜单排名最大个数
     */
    private final static int MAX_RANK_NUM = 100;

    /**
     * 取主播排名及车辆信息
     *
     * @param chid
     * @return
     */
    public RankData getRankData(long activityId, String chid) {
        String key = RedisKeyConstant.CHID_CAR_RANK_MAP.getKey(activityId);
        String rankStr = redisTemplate.hget(key, chid);
        int rank = 0;
        if (StringUtils.isNotBlank(rankStr)) {
            rank = Integer.parseInt(rankStr);
            //100特殊处理
            if (rank >= MAX_RANK_NUM) {
                rank = 0;
            }
        }
        Map<String, String> map = redisTemplate.hgetAll(RedisKeyConstant.CHID_CAR_MAP.getKey(activityId, chid));
        RankData rankData = new RankData(map);
        //rankData.setUid(WebdbService.getUid(chid));
        //rankData.setChid(chid);
        rankData.setRank(rank);
        return rankData;
    }

    /**
     * 取排名榜,前5
     *
     * @return
     */
    public List<RankData> getRankDataListTop5(long activityId) {
        String str = redisTemplate.get(RedisKeyConstant.CHID_CAR_RANK_TOP5.getKey(activityId));
        List<RankData> result = JSON.parseArray(str, RankData.class);
        return result;
    }

    /**
     * 取排名榜 全部
     *
     * @return
     */
    public List<RankData> getRankDataList(long activityId) {
        String str = redisTemplate.get(RedisKeyConstant.CHID_CAR_RANK_ALL.getKey(activityId));
        List<RankData> result = JSON.parseArray(str, RankData.class);
        return result;
    }

    /**
     * 榜单排名,定时执行
     *
     * @return
     */
    public void setCacheRank(long activityId) {
        if (activityConfigService.isActivityOver(activityId, 2)) {
            return;
        }
        List<Tuple> chids = getRankTuples(activityId);
        List<RankData> result = sort(chids, activityId);
        setCacheAndNotity(activityId, result);
    }


    /**
     * 取需要排名的频道
     *
     * @return
     */
    private List<Tuple> getRankTuples(long activityId) {
        String key = RedisKeyConstant.CHID_CAR_SORT_SET.getKey(activityId);
        Set<Tuple> tuples = redisTemplate.zrevrangeWithScores(key, 0, MAX_RANK_NUM - 1);
        List<Tuple> chids = new ArrayList<Tuple>();
        for (Tuple tuple : tuples) {
            chids.add(tuple);
        }
        //取最后一名同分的记录
        if (chids.size() == MAX_RANK_NUM) {
            double lastScore = chids.get(MAX_RANK_NUM - 1).getScore();

            tuples = redisTemplate.zrangeByScoreWithScores(key, lastScore, lastScore);
            //存在与最后一名同分的操作
            if (tuples.size() > 1) {
                //删除最后一名同分的
                for (int i = MAX_RANK_NUM - 1; i >= 0; i--) {
                    if (chids.get(i).getScore() == lastScore) {
                        chids.remove(i);
                    } else {
                        break;
                    }
                }
                for (Tuple tuple : tuples) {
                    chids.add(tuple);
                }
            }
        }
        return chids;
    }

    /**
     * 内存排名
     *
     * @param chids
     * @return
     */
    private List<RankData> sort(List<Tuple> chids, long activityId) {
        //查所有记录,FIXME:此处多次查reids需要优化
        List<RankData> result = new ArrayList<RankData>();
        Map<String, String> tmpMap;
        String uid;
        for (Tuple chid : chids) {
            tmpMap = redisTemplate.hgetAll(RedisKeyConstant.CHID_CAR_MAP.getKey(activityId, chid.getElement()));
            tmpMap.put("chid", chid.getElement());
            uid = webdbService.getUid(chid.getElement());
            tmpMap.put("uid", uid);
            tmpMap.put("nick", webdbService.getNice(uid));
            result.add(new RankData(tmpMap));
        }
        Collections.sort(result);
        //限制数量
        if (result.size() > MAX_RANK_NUM) {
            result = result.subList(0, MAX_RANK_NUM);
        }
        return result;
    }

    /**
     * 设置到缓存去,并把排名发生变化的主播加入通知队列
     *
     * @param result
     */
    private void setCacheAndNotity(long activityId, List<RankData> result) {
        int rank = 0;
        final String key = RedisKeyConstant.CHID_CAR_RANK_MAP.getKey(activityId);
        final Map<String, String> rankMap = new HashMap<String, String>();
        for (RankData rankData : result) {
            ++rank;
            rankData.setRank(rank);
            rankMap.put(rankData.getChid(), String.valueOf(rank));
        }
        Map<String, String> oldRankMap = redisTemplate.hgetAll(key);
        if (oldRankMap == null) oldRankMap = new HashMap<String, String>();
        //排名名次数据
        redisTemplate.execute(new RedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction multi = jedis.multi();
                multi.del(key);
                if (rankMap.size() > 0) multi.hmset(key, rankMap);
                multi.exec();
            }
        });
        //排名发生变化的要通知
        addNotity(activityId, rankMap, oldRankMap);
        //榜单所有数据
        redisTemplate.set(RedisKeyConstant.CHID_CAR_RANK_ALL.getKey(activityId), JSON.toJSONString(result));
        if (result.size() > 5) {
            result = result.subList(0, 5);
        }
        redisTemplate.set(RedisKeyConstant.CHID_CAR_RANK_TOP5.getKey(activityId), JSON.toJSONString(result));
    }

    /**
     * 排名发生变化的要通知
     *
     * @param newRankMap
     * @param oldRankMap
     */
    private void addNotity(final long activityId, Map<String, String> newRankMap, Map<String, String> oldRankMap) {
        if (activityConfigService.isActivityOver(activityId)) {
            return;
        }
        String key;
        for (Map.Entry<String, String> entry : newRankMap.entrySet()) {
            key = entry.getKey();
            if (!entry.getValue().equals(oldRankMap.get(key))) {
                notifyService.notifyOne(activityId, key, null);
            }
            oldRankMap.remove(key);
        }
        for (Map.Entry<String, String> entry : oldRankMap.entrySet()) {
            notifyService.notifyOne(activityId, entry.getKey(), null);
        }
    }

    public void clearCache() {
        final String keys = RedisKeyConstant.PREV_KEY + "*";
        redisTemplate.execute(new RedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Set<String> keyset = jedis.keys(keys);
                for (String key : keyset) {
                    jedis.del(key);
                    logger.info("delete redis key:{}", key);
                    ;
                }
            }
        });
    }
}
