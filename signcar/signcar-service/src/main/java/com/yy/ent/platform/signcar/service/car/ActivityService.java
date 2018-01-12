package com.yy.ent.platform.signcar.service.car;

import com.google.common.collect.Maps;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.adapter.ClientResponse;
import com.yy.ent.external.service.EntProxyHalbService;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.RankData;
import com.yy.ent.platform.signcar.common.constant.CacheKeyConstant;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.mongodb.ActivityConfig;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.common.*;
import com.yy.ent.platform.signcar.service.yyp.ResultWrapper;
import com.yy.ent.srv.handler.MessageResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 关闭活动通知
 */
@Service
public class ActivityService extends BaseService {
    @Autowired
    private ExecuteService executeService;
    @Autowired
    private ActivityConfigService activityConfigService;
    @Autowired
    private YYPService yypService;
    @Autowired
    private RankService rankService;
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    private AllChidService allChidService;
    @Autowired
    private EntProxyHalbService entProxyHalbService;
    @Autowired
    private LocalCacheService cacheService;
    @Autowired
    private CommonService commonService;
    

    public boolean isLivingRoom(String chid, String sid) {
        if (!chid.equals(sid)) {
            return false;
        }
        String key = CacheKeyConstant.IS_LIVING_ROOM.getKey(chid + "_" + sid);
        String type = (String) cacheService.get(MIDCACHE, key);
        if (StringUtils.isBlank(type)) {
            try {
                ClientRequest req = new ClientRequest(YYPConstant.REQUEST_ROOM_TYPE);
                req.putUint(new Uint(chid));
                req.putUint(new Uint(sid));
                req.putMap(Maps.<String,String>newHashMap(),String.class,String.class);
                ClientResponse resp = entProxyHalbService.sendAndWait(req);
                Uint result = resp.popUint();
                Uint chnType = resp.popUint();
                Uint isXiaoWo = resp.popUint();
                Map<String,String> property = resp.popMap(String.class,String.class);
                type = chnType.toString();
                logger.info("key={},chnType={},isXiaoWo={},property={}", key, type, isXiaoWo, property);
                if (result.intValue() == 0) cacheService.add(MIDCACHE, key, type);
            } catch (Exception e) {
                type = "-1";
                logger.warn("get isLivingRoom error:{}", key, e);
            }
        }
        logger.debug("chid={},sid={},chnType={}", chid, sid, type);
        //1为直播间,6为快速直播间
        return "1".equals(type) || "6".equals(type);
    }

    /**
     * 取活动相关信息
     *
     * @param chid
     * @return
     */
    public Map<String, Object> getActivityResult(final Long activityId, String chid) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean activityOpen = activityConfigService.isActivityOpen(activityId);
        result.put("isOpen", activityOpen);
        ActivityConfig activityConfig = activityConfigService.getActivityConfig(activityId);
        String url = activityConfig.getUrl();
        if (activityOpen) {
            RankData rankData = rankService.getRankData(activityId, chid);
            logger.debug("rankData={}", rankData);
            result.put("info", rankData);
            result.put("endTime", activityConfig.getEndTime());
            result.put("now", commonService.currentTimeMillis());
            if (StringUtils.isNotBlank(url)) result.put("url", url);
        }
        return result;
    }

    

    /**
     * 活动开启，打开所有频道
     */
    public void openAll(final Long activityId) throws Exception {
        String key = RedisKeyConstant.HAS_OPEN.getKey(activityId);
        if (activityConfigService.isActivityOpen(activityId) && !"1".equals(redisTemplate.get(key))) {
            logger.info("openAll begin:{}", activityId);
            redisTemplate.set(key, "1");
            allChidService.doAllChid(new ChidCallback() {
                @Override
                public void doInChid(String chid) {
                    openOne(activityId, chid);
                }
            });
            logger.info("openAll over");
        } else {
            logger.info("activity:{} needn't openAll!", activityId);
        }
    }

    /**
     * 活动结束关闭所有频道
     */
    public void closeAll(final Long activityId) throws Exception {
        String key = RedisKeyConstant.HAS_CLOSE.getKey(activityId);
        if (activityConfigService.isActivityOver(activityId) && !"1".equals(redisTemplate.get(key))) {
            logger.info("closeAll begin:{}", activityId);
            redisTemplate.set(key, "1");
            //先不改状态，否则重启时候榜单就出不来了
//            ActivityConfig activityConfig = activityConfigService.getActivityConfig(activityId);
//            activityConfig.setStatus(0);
//            activityConfigService.updateActivityConfig(activityConfig);
            allChidService.doAllChid(new ChidCallback() {
                @Override
                public void doInChid(String chid) {
                    closeOne(activityId, chid);
                }
            });
            logger.info("closeAll over");
        } else {
            logger.info("activity:{} needn't closeAll!", activityId);
        }
    }


    private void closeOne(final long activityId, final String channelId) {
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                Uint chid = new Uint(channelId);
                MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_CLOSE);
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("chid", chid.toString());
//                Long newId = activityConfigService.getCurrentActivityId();
//                result.put("url", activityConfigService.getActivityConfig(newId).getUrl());
                result.put("url", "http://interfaces.yy.com/interfaces/sign/signInV2.html");
                res.putString(ResultWrapper.wrapJsonResultSuccess(result));
                yypService.sendBraodcastPC(res, chid, chid);
            }
        }, RandomStringUtils.randomAlphanumeric(10), "closeOne");
    }

    private void openOne(final long activityId, final String channelId) {
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                Uint chid = new Uint(channelId);
                MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_CAR_IS_OPEN);
                Map<String, Object> result = getActivityResult(activityId, channelId);
                res.putString(ResultWrapper.wrapJsonResultSuccess(result));
                yypService.sendBraodcastPC(res, chid, chid);
            }
        }, RandomStringUtils.randomAlphanumeric(10), "openOne");
    }

    //以下测试代码
    public void testClose(final long activityId, final String channelId) {
        closeOne(activityId, channelId);
    }

    public void testOpen(final long activityId, final String channelId) {
        openOne(activityId, channelId);
    }

    public void testOpen2(final long activityId, final String channelId) {
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                Uint chid = new Uint(channelId);
                MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_CAR_IS_OPEN);
                Map<String, Object> result = getActivityResult(activityId, channelId);
                result.put("isOpen", true);
                Calendar now = Calendar.getInstance();
                now.add(Calendar.DAY_OF_YEAR, 1);
                result.put("endTime", now.getTimeInMillis());
                res.putString(ResultWrapper.wrapJsonResultSuccess(result));
                yypService.sendBraodcastPC(res, chid, chid);
            }
        }, RandomStringUtils.randomAlphanumeric(10), "openOne");
    }

    public void testChid() throws Exception {
        allChidService.doAllChid(new ChidCallback() {
            @Override
            public void doInChid(String chid) {
                logger.info("==> {}", chid);
            }
        });
    }

    public void checkChid() throws Exception {
        allChidService.doAllChid(new ChidCallback() {
            @Override
            public void doInChid(String chid) {
                isLivingRoom(chid, chid);
                logger.info("==> {}", chid);
            }
        });
    }
}
