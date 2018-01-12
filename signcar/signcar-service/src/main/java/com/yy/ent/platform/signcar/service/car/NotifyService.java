package com.yy.ent.platform.signcar.service.car;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.RankData;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.ExecuteCallback;
import com.yy.ent.platform.signcar.service.common.ExecuteService;
import com.yy.ent.platform.signcar.service.common.WebdbService;
import com.yy.ent.platform.signcar.service.common.YYPService;
import com.yy.ent.platform.signcar.service.yyp.ResultWrapper;
import com.yy.ent.srv.handler.MessageResponse;

/**
 * 排名车队等相关数据更新通知
 */
@Service
public class NotifyService extends BaseService implements InitializingBean {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    private WebdbService webdbService;
    @Autowired
    private RankService rankService;
    @Autowired
    private ExecuteService executeService;
    @Autowired
    private YYPService yypService;
    @Autowired
    private ActivityConfigService activityConfigService;
  


    /**
     * 添加需要定时广播通知更新数据的频道
     *
     * @param chid
     * @see #doNotifyAll(long)
     */
    public void addNotify(final long activityId, String chid) {
        String key = RedisKeyConstant.CHID_NOTITY_SET.getKey(activityId, String.valueOf(currentTimeNum()));
        redisTemplate.sadd(key, chid);
    }

    /**
     * 用户送入队卡,即时通知
     *
     * @param chid
     * @param uid
     */
    public void addNotifyCarTeam(final long activityId, final String chid, final long uid) {
        //频道车数加1
        redisTemplate.execute(new RedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Long sadd = jedis.sadd(RedisKeyConstant.UID_CHID_CAR_SET.getKey(activityId), uid + "_" + chid);
                if (sadd == 1) jedis.zincrby(RedisKeyConstant.CHID_CAR_SORT_SET.getKey(activityId), 1d, chid);
            }
        });
        notifyCarTeam(activityId, chid, uid);
        addNotify(activityId, chid);
    }

    private void notifyCarTeam(final long activityId, final String chid, final long uid) {
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                Map<String, Object> result = new HashMap<String, Object>();
                RankData rankData = rankService.getRankData(activityId, chid);
                MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_CAR_TEAM_NOTITY);
                result.put("info", rankData);
                result.put("uid", uid);
                result.put("nick", webdbService.getNice(String.valueOf(uid)));
                String idolUid = webdbService.getUid(chid);
                result.put("idolUid", idolUid);
                result.put("idolNick", webdbService.getNice(String.valueOf(idolUid)));
                res.putString(ResultWrapper.wrapJsonResultSuccess(result));
                String[] split = chid.split("_");
                yypService.sendBraodcastPC(res, new Uint(split[0]), new Uint(split[1]));
            }
        }, RandomStringUtils.randomAlphanumeric(10), "addNotityCarTeam");
    }

   
    
    /**
     * @Desc:车队巡游，主播的车队数据达到一百的整数倍时，在此直播间广播
     * @param chid
     * @param carNum
     * @return:void
     * @author: renmeng  
     * @date: 2015年12月10日 下午7:52:18
     */
    public void notifyCarNum(final String comeinchid,final String idolchid, final long carNum) {
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_CARNUM_CRUISE_NOTITY);
                Map<String, Object> result = new HashMap<String, Object>();
                String idolUid = webdbService.getUid(idolchid);
                result.put("idolUid", idolUid);
                result.put("idolName", webdbService.getNice(String.valueOf(idolUid)));
                result.put("carNum", carNum);
                res.putString(ResultWrapper.wrapJsonResultSuccess(result));
                String[] chids = comeinchid.split("_");
                logger.info("CruiseHandler notifyCarNum result:{}",ResultWrapper.wrapJsonResultSuccess(result));
                yypService.sendBraodcastPC(res, new Uint(chids[0]), new Uint(chids[1]));
            }
        }, RandomStringUtils.randomAlphanumeric(10), "notifyCarNum");
    }
    
    
    /**
     * @Desc:用户进入频道的流光
     * @param chid 当前用户进入的频道
     * @param json
     * @return:void
     * @author: renmeng  
     * @date: 2015年12月18日 下午2:32:32
     */
    public void notifyFansEnter(final String chid,final JSONObject json){
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_FANSENTER_NOTITY);
                JSONObject result = new JSONObject();
                result.put("data", json);
                logger.info("CruiseHandler notifyFansEnter result:{}",ResultWrapper.wrapJsonResultSuccess(result));
                res.putString(ResultWrapper.wrapJsonResultSuccess(result));
                String[] chids = chid.split("_");
                yypService.sendBraodcastPC(res, new Uint(chids[0]), new Uint(chids[1]));
            }
        }, RandomStringUtils.randomAlphanumeric(10), "notifyFansEnter");
    }

    /**
     * 用户进入频道，可能要发跑马灯效果
     *
     * @param uid
     * @param chid
     */
    public void addNotifyUserEntry(final long activityId, final String chid, final long uid) {
        Boolean isIdolCarTeam = redisTemplate.execute(new RedisTemplate.JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.sismember(RedisKeyConstant.UID_CHID_CAR_SET.getKey(activityId), uid + "_" + chid);
            }
        });
        logger.debug("isIdolCarTeam:activityId={},uid={},chid={},result={}", activityId, uid, chid, isIdolCarTeam);
        if (!isIdolCarTeam) {
            return;
        }
        
    }

    

    /**
     * 处理通知
     */
    public void doNotifyAll(final long activityId) {
        if (activityConfigService.isActivityOver(activityId)) {
            return;
        }

        final String hasHandleKey = RedisKeyConstant.CHID_NOTITY_HAS_HANDLE.getKey(activityId);
        long end = currentTimeNum() - 1;
        long hasHandle;
        String s = redisTemplate.get(hasHandleKey);
        if (s == null) {
            hasHandle = end - 1;
        } else {
            hasHandle = Long.parseLong(s);
            if (hasHandle >= end) {
                //一般不会到这里，除非改了系统时间或者改了redis值
                hasHandle = end - 1;
            } else if (hasHandle < end - 100) {
                //时间差太远的
                hasHandle = end - 100;
            }
        }

        logger.info("doNotityAll running,activityId={},end={},hasHandle={}", activityId, end, hasHandle);
        //返回的元素数量通常和 COUNT 选项指定的一样， 或者比 COUNT 选项指定的数量稍多一些。
        final ScanParams params = new ScanParams();
        params.count(500);
        for (long currentHandle = hasHandle + 1; currentHandle <= end; currentHandle++) {
            notifySet(activityId, hasHandleKey, params, currentHandle);
        }
        logger.info("doNotityAll activityId={} end", activityId);
    }

    /**
     * 单位时间内通知所有主播
     *
     * @param hasHandleKey
     * @param params
     * @param currentHandle
     */
    private void notifySet(final long activityId, final String hasHandleKey, final ScanParams params, final long currentHandle) {
        final String key = RedisKeyConstant.CHID_NOTITY_SET.getKey(activityId, currentHandle);
        redisTemplate.execute(new RedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                ScanResult<String> sscan;
                String cursor = "0";
                List<String> list;
                do {
                    sscan = jedis.sscan(key, cursor, params);
                    cursor = sscan.getStringCursor();
                    list = sscan.getResult();
                    if (list != null) {
                        logger.info("currentHandle:{},cursor:{},list.size:{}", currentHandle, cursor, list.size());
                        notifyList(activityId, list, currentHandle);
                    }
                } while (!"0".equals(cursor));//redis.cursor为0时表示结束
                //记录已处理的时间
                jedis.set(hasHandleKey, String.valueOf(currentHandle));
                jedis.del(key);
            }
        });
    }

    /**
     * 处理通知，定时器执行
     *
     * @param list
     */
    private void notifyList(long activityId, List<String> list, long currentHandle) {
        for (String chid : list) {
            notifyOne(activityId, chid, RandomStringUtils.randomAlphanumeric(10) + "_" + currentHandle);
        }
    }

    /**
     * 线程池处理单个通知
     *
     * @param chid
     * @param threadName
     */
    public void notifyOne(final long activityId, final String chid, String threadName) {
        if (StringUtils.isBlank(threadName)) {
            threadName = RandomStringUtils.randomAlphanumeric(10);
        }
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                RankData rankData = rankService.getRankData(activityId, chid);
                MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_INFO_NOTITY);
                res.putString(ResultWrapper.wrapJsonResultSuccess(rankData));
                String[] split = chid.split("_");
                yypService.sendBraodcastPC(res, new Uint(split[0]), new Uint(split[1]));
            }
        }, threadName, "notityOne");

    }

    /**
     * 取当前时间数字
     *
     * @return
     */
    private long currentTimeNum() {
        long num = System.currentTimeMillis() / 30000;
        return num;
    }


    //以下测试代码
    private static NotifyService notifyService;
    private static boolean isTest = false;

    public void setIsTest() throws Exception {
        isTest = !isTest;
        logger.info("isTest:{}", isTest);
    }

    public static void task1() {
        if (!isTest) return;
        String chid = "95726854_95726854";
        Long activityId = 1L;
        Long uid = 50014644L;
        notifyService.notifyCarTeam(activityId, chid, uid);
    }

    public static void task2() {
        if (!isTest) return;
        String chid = "95726854_95726854";
        Long activityId = 1L;
        Long uid = 50014644L;
        notifyService.notifyOne(activityId, chid, RandomStringUtils.randomAlphanumeric(10));
    }

    
    

    @Override
    public void afterPropertiesSet() throws Exception {
        notifyService = this;
//        Task.execute(NotifyService.class, "task1", "1/5 * * ? * *", false);
//        Task.execute(NotifyService.class, "task2", "2/5 * * ? * *", false);
//        Task.execute(NotifyService.class, "task3", "3/5 * * ? * *", false);
//        Task.execute(NotifyService.class, "task4", "4/5 * * ? * *", false);
    }
}
