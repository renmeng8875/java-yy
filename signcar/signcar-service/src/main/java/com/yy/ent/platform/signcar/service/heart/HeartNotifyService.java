package com.yy.ent.platform.signcar.service.heart;

import com.alibaba.fastjson.JSON;
import com.yy.ent.clients.halb.proxy.YYPClientHALBProxy;
import com.yy.ent.commons.protopack.marshal.StringMarshal;
import com.yy.ent.commons.protopack.marshal.UintMarshal;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.adapter.ClientResponse;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.MarqueeBatchInfo;
import com.yy.ent.platform.signcar.common.FansIntimacyBatchInfo;
import com.yy.ent.platform.signcar.common.MarqueeInfo;
import com.yy.ent.platform.signcar.common.TaskbarInfo;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.service.common.*;
import com.yy.ent.platform.signcar.service.yyp.Result;
import com.yy.ent.srv.handler.MessageResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * HeartNotifyService
 *
 * @author suzhihua
 * @date 2015/12/19.
 */
@Service
public class HeartNotifyService extends BaseService {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private HeartService heartService;
    @Autowired
    private CommonService commonService;

    @Autowired
    private ExecuteService executeService;
    @Autowired
    private YYPService yypService;

    @Autowired
    private WebdbService webdbService;

    @Autowired
    @Qualifier("srvquery")
    private YYPClientHALBProxy yypclientSrvquery;
    
    @Autowired
    @Qualifier("littleArtist")
    private YYPClientHALBProxy yypclientLittleArtist;
    
    /**
     * 签到送点单播通知用户增加心数,自动查找用户所有频道
     *
     * @param fansUid
     * @param num
     */
    public void notifySignHeart(long fansUid, int num, final boolean isPc, final long time) {
        try {
            ClientRequest req = new ClientRequest(YYPConstant.REQUEST_CHID_USER);
            req.putContent(new UintMarshal(new Uint(fansUid)));
            req.putContent(new StringMarshal(""));//appdata 原样返回
            ClientResponse clientResponse = yypclientSrvquery.getClient().sendAndWait(req);
            Uint result = clientResponse.popUint();
            if (result.intValue() == 0) {
                long uid = clientResponse.popUint().longValue();
                long topChid = clientResponse.popUint().longValue();
                long subChid = clientResponse.popUint().longValue();
                String appdata = clientResponse.popString();
                logger.debug("REQUEST_CHID_USER uid={},topChid={},subChid={}", uid, topChid, subChid);
                if (fansUid == uid && topChid > 0 & subChid > 0)
                    notifySignHeart(topChid, subChid, fansUid, num, isPc, time);
            }
        } catch (Throwable e) {
            logger.warn("notifySignHeart error:uid={}", fansUid, e);
        }
    }

    /**
     * 签到送点单播通知用户增加心数
     *
     * @param topChid
     * @param subChid
     * @param uid
     * @param num
     * @param isPc
     */
    public void notifySignHeart(final long topChid, final long subChid, final long uid, final int num, final boolean isPc, final long time) {
        executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                Result result = Result.newResult("fansUid", uid);
                result.put("fansHeartNum", num < 0 ? heartService.getFansHeartNum(uid) : num);
                if (isPc) {
                    result.put("time", num < 0 ? commonService.currentTimeMillis() : time);
                    MessageResponse res = result.toMessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_SIGN_HEART);
                    yypService.sendUnicastPC(res, new Uint(topChid), new Uint(subChid), new Uint(uid));
                } else {
                    MessageResponse res = result.toMessageResponse(YYPConstant.MOBILE.MAX, YYPConstant.MOBILE.RESPONSE_SIGN_HEART);
                    yypService.sendUnicastMobile(res, new Uint(topChid), new Uint(subChid), new Uint(uid));
                }
            }
        }, RandomStringUtils.randomAlphanumeric(10), "notifySignHeart_" + (isPc ? "PC" : "MOBILE"));
    }

    public void notifyHeart(final long topChid, final long subChid, final long uid) {
        long now = getTime();
        redisTemplate.sadd(RedisKeyConstant.HEART_NOTIFY_SET.getKey(now), topChid + "_" + subChid + "_" + uid);
    }

    /**
     * 执行加心广播,兑心用，到时可以下掉
     */
    public void doNotifyHeart() {
        String keyTime = RedisKeyConstant.HEART_NOTIFY_HANDLE_TIME.getKey();
        String numStr = redisTemplate.get(keyTime);
        long now = getTime() - 1;
        long minPast = now - 2 - 14;//最近15回
        long past = StringUtils.isNotBlank(numStr) ? Integer.parseInt(numStr) : minPast;
        if (past < minPast) {
            past = minPast;
        }
        logger.info("doNotifyHeart [{},{})", past, now);
        redisTemplate.set(keyTime, String.valueOf(now));
        for (long time = past; time < now; time++) {
            doNotifyHeartOneKey(time);
        }
    }

    /**
     * 执行加心广播-单个redis key set
     *
     * @param time
     */
    private void doNotifyHeartOneKey(long time) {
        try {
            logger.info("doNotifyHeartOneKey time:{}", time);
            String key = RedisKeyConstant.HEART_NOTIFY_SET.getKey(time);
            Set<String> smembers = redisTemplate.smembers(key);
            if (smembers == null || smembers.size() == 0) {
                return;
            }
            redisTemplate.del(key);
            logger.info("doNotifyHeartOneKey smembers.size:{}", smembers.size());
            String[] tmp;
            for (String smember : smembers) {
                tmp = smember.split("_");//topchid_subchid_uid
                if (tmp.length == 3) {
                    notifySignHeart(Long.parseLong(tmp[0]), Long.parseLong(tmp[1]), Long.parseLong(tmp[2]), -1, true, -1);
                }
            }
        } catch (Throwable e) {
            logger.warn("doNotifyHeartOneKey error", e);
        }
    }

    /**
     * 送心记录通知
     *
     * @param topChid
     * @param subChid
     * @param taskbarInfo
     */
    public void notifyConsumeHeart(final Uint topChid, final Uint subChid, final long idolUid, final long fansUid, final int currentNum, final TaskbarInfo taskbarInfo) {
        if (!heartService.getIsOpen()) {
            return;
        }
        MarqueeInfo info = new MarqueeInfo();
        info.setCurrentNum(currentNum);
        info.setFansUid(fansUid);
        info.setIdolUid(idolUid);
        info.setSubChid(subChid.longValue());
        info.setTopChid(topChid.longValue());
        String key = RedisKeyConstant.HEART_MARQUEE_SORTED_SET.getKey(getTime());
        redisTemplate.zadd(key, commonService.currentTimeMillis(), JSON.toJSONString(info));
//        String _key = RedisKeyConstant.HEART_FANS_INTIMACY_LIST.getKey(getTime());
//        redisTemplate.lpush(_key, JSON.toJSONString(info));
        String fieldName = info.getIdolUid()+"_"+info.getFansUid();
        String mapKey = RedisKeyConstant.HEART_FANS_INTIMACY_MAP.getKey();
        redisTemplate.hincrBy(mapKey, fieldName, info.getCurrentNum());
     }
    
   private static final int MAX_MARQUEE_NUM = 0;//取总记录数，0表示取所有

    /**
     * 执行送心广播
     */
    public void doNotifyConsumeHeart() {
        if (!heartService.getIsOpen()) {
            return;
        }
        String keyTime = RedisKeyConstant.HEART_MARQUEE_HANDLE_TIME.getKey();
        String numStr = redisTemplate.get(keyTime);
        long now = getTime() - 1;
        long minPast = now - 21;//最近15回
        long past = StringUtils.isNotBlank(numStr) ? Integer.parseInt(numStr) : minPast;
        logger.info("doNotifyConsumeHeart parameters,keyTime:{},numStr:{},now:{},minPast:{},past:{}",keyTime,numStr,now,minPast,past);
        
        if (past < minPast) {
            past = minPast;
        }
        logger.info("doNotifyConsumeHeart [{},{}]", past, now);
        redisTemplate.set(keyTime, String.valueOf(now));
        for (long time = past; time < now; time++) {
            doNotifyConsumeHeartOneKey(time);
        }
    }
    
   
    /**
     * 处理redis一个sorted_set key
     *
     * @param time
     */
    private void doNotifyConsumeHeartOneKey(long time) {
        try {
            //logger.info("doNotifyConsumeHeartOneKey time: {}", time);
            String key = RedisKeyConstant.HEART_MARQUEE_SORTED_SET.getKey(time);
            Set<Tuple> tuples = redisTemplate.zrevrangeWithScores(key, 0, MAX_MARQUEE_NUM - 1);
            if (tuples == null || tuples.size() == 0) {
                return;
            }
            redisTemplate.del(key);
            //logger.info("doNotifyConsumeHeartOneKey tuples.size: {}", tuples.size());
            MarqueeInfo info;
            String mapKey;
            MarqueeBatchInfo marqueeBatchInfo;
            Map<String, MarqueeBatchInfo> batchMap = new HashMap<String, MarqueeBatchInfo>();
            for (Tuple tuple : tuples) {
                info = JSON.parseObject(tuple.getElement(), MarqueeInfo.class);
                mapKey = info.getTopChid() + "_" + info.getSubChid();
                marqueeBatchInfo = batchMap.get(mapKey);
                if (marqueeBatchInfo == null) {
                    marqueeBatchInfo = new MarqueeBatchInfo();
                    marqueeBatchInfo.setTopChid(info.getTopChid());
                    marqueeBatchInfo.setSubChid(info.getSubChid());
                    marqueeBatchInfo.setIdolUid(info.getIdolUid());
                    batchMap.put(mapKey, marqueeBatchInfo);
                }
                marqueeBatchInfo.addMarqueeInfo(info);

                 
            }
            logger.info("doNotifyConsumeHeartOneKey batchMap.size: {}", batchMap.size());
            for (Map.Entry<String, MarqueeBatchInfo> entry : batchMap.entrySet()) {
                doNotifyConsumeHeartOneBatchInfo(entry.getValue(), time);
            }
        } catch (Throwable e) {
            logger.warn("doNotifyConsumeHeartOneKey error", e);
        }
        
        
    }
     /**
         * 通知粉丝主播亲密度
         */
    public void doNotifyFansIdolIntimacy(){
    	if (!heartService.getIsOpen()) {
            return;
        }
    	String mapKey = RedisKeyConstant.HEART_FANS_INTIMACY_MAP.getKey();
    	long size = redisTemplate.hlen(mapKey);
    	if(size < 10){
    		logger.info("doNotifyFansIdolIntimacyOneKey: HEART_FANS_INTIMACY_MAP.size is less than 10  ");
    		return;
    	}
        Map<String, String> all = (Map<String, String>)redisTemplate.hgetAll(mapKey);
        if (all == null || all.size() == 0) {
        	logger.info("doNotifyFansIdolIntimacyOneKey: HEART_FANS_INTIMACY_MAP is empty ");
            return;
        }
        redisTemplate.del(mapKey);
        
        FansIntimacyBatchInfo fansIntimacyBatchInfo = new FansIntimacyBatchInfo();
        
        for (Map.Entry<String, String> entry : all.entrySet()) {
        	 String key = entry.getKey();
        	 Map<Uint,Uint> map = new HashMap<Uint,Uint>();
        	 String[] str = key.split("_");
        	 if(str.length != 2){
        		 continue;
        	 }
        	 Uint idolUid = new Uint(str[0]);
        	 Uint fansUid = new Uint(str[1]);
        	 Uint num = new Uint(entry.getValue());
        	 
        	 map.put(new Uint(1),fansUid);
        	 map.put(new Uint(2), idolUid);
        	 map.put(new Uint(3), num);
        	 if(fansIntimacyBatchInfo.getList().size() >= 80){
        		 doNotifyFansIdolIntimacyOneBatchInfo(fansIntimacyBatchInfo);
        		 fansIntimacyBatchInfo = new FansIntimacyBatchInfo();
        	 }else{
        		 fansIntimacyBatchInfo.addMap(map);        		 
        		 logger.info("doNotifyFansIdolIntimacyOneKey.addFansIdolIntimacy:  fansUid: {} ,idolUid: {} ,num: {} ", fansUid,idolUid,num);
        	 }
        }
       
       if(fansIntimacyBatchInfo.getList().size() > 0){
    	   doNotifyFansIdolIntimacyOneBatchInfo(fansIntimacyBatchInfo);    	   
       }
    }
    
    private void doNotifyFansIdolIntimacyOneBatchInfo(final FansIntimacyBatchInfo fansIntimacyBatchInfo){
    	executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
            	if(fansIntimacyBatchInfo.getList().size() <= 0){
            		logger.info("doNotifyFansIdolIntimacyOneBatchInfo: IntimacyList size={},is not execute. ",fansIntimacyBatchInfo.getList().size() );
            		return;
            	}
                logger.info("doNotifyFansIdolIntimacyOneBatchInfo: IntimacyList size={} ",fansIntimacyBatchInfo.getList().size() );
                ClientRequest clientRequest = new ClientRequest(YYPConstant.SERVER.REQUEST_CONSUME_NOTIFY);
                clientRequest.putMarshall(fansIntimacyBatchInfo);
                ClientResponse clientResponse = yypclientLittleArtist.getClient().sendAndWait(clientRequest,30000);
                Uint result = clientResponse.popUint();
                logger.info("doNotifyFansIdolIntimacyOneBatchInfo.result=" + result);
                if (result.intValue() != 0) {
                    logger.warn("doNotifyFansIdolIntimacyOneBatchInfo error: result=" + result);
                }
               
            }
        }, getTime() + "_" + RandomStringUtils.randomAlphanumeric(10), "addFansIdolIntimacy");
    }

    /**
     * 处理一个频道
     *
     * @param marqueeBatchInfo
     */
    private void doNotifyConsumeHeartOneBatchInfo(final MarqueeBatchInfo marqueeBatchInfo, final long time) {
    	executeService.execute(new ExecuteCallback() {
            @Override
            public void doInAction() throws Exception {
                String yyyymmdd = commonService.getYYYYMMDD();
                int fansHeartConsumeNum;
                Long fansUid;
                for (MarqueeInfo info : marqueeBatchInfo.getMarquee()) {
                    fansUid = info.getFansUid();
                    fansHeartConsumeNum = heartService.getFansHeartConsumeNum(fansUid, yyyymmdd);
                    info.setTotalNum(fansHeartConsumeNum);
                    info.setFansNick(webdbService.getNice(String.valueOf(fansUid)));
                    info.setTopChid(null);
                    info.setSubChid(null);
                    info.setIdolUid(null);
                }
                Long topChid = marqueeBatchInfo.getTopChid();
                Long subChid = marqueeBatchInfo.getSubChid();
                Long idolUid = marqueeBatchInfo.getIdolUid();
                logger.info("doNotifyConsumeHeartOneBatchInfo topChid: {} ,subChid: {} ,idolUid: {} ,marquee.size: {}", topChid, subChid, idolUid, marqueeBatchInfo.getMarquee().size());

                marqueeBatchInfo.setIdolFansNum(heartService.getIdolFansNum(idolUid, yyyymmdd));
                marqueeBatchInfo.setIdolHeartNum(heartService.getIdolHeartNum(idolUid, yyyymmdd));
                marqueeBatchInfo.setTopChid(null);
                marqueeBatchInfo.setSubChid(null);
                marqueeBatchInfo.setMap(null);
                Result result = Result.newResult().setDataRoot(marqueeBatchInfo);
                MessageResponse res = result.toMessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_MARQUEE);

                yypService.sendBraodcastPC(res, new Uint(topChid), new Uint(subChid));
            }
        }, time + "_" + RandomStringUtils.randomAlphanumeric(10), "marquee");
    	
    }

    private long getTime() {
        return commonService.currentTimeMillis() / 1000;
    }
}
