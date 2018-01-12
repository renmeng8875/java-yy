package com.yy.ent.platform.signcar.service.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yy.ent.clients.halb.proxy.YYPClientHALBProxy;
import com.yy.ent.commons.protopack.base.Marshallable;
import com.yy.ent.commons.protopack.marshal.IntegerMarshal;
import com.yy.ent.commons.protopack.marshal.LongMarshal;
import com.yy.ent.commons.protopack.marshal.StringMarshal;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.adapter.ClientResponse;
import com.yy.ent.external.service.EntProxyHalbService;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.mongodb.IdolCarTeam;
import com.yy.ent.platform.signcar.repository.mongo.IdolCarTeamRepository;
import com.yy.ent.platform.signcar.repository.mongo.UserPrizeRepository;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.car.RankService;
import com.yy.ent.platform.signcar.service.common.AllChidService;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.YYPService;
import com.yy.ent.platform.signcar.service.yyp.ResultWrapper;
import com.yy.ent.srv.handler.MessageResponse;

/**
 * DemoService
 *
 * @author suzhihua
 * @date 2015/10/27.
 */
@Service
public class TestSigncarService extends BaseService implements InitializingBean {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;


    @Autowired
    AllChidService allChidService;

    @Autowired
    private ActivityConfigService activityConfigService;


    @Autowired
    private YYPService yypService;

    @Autowired
    private RankService rankService;

    @Autowired
    private DataMockService dataMockService;
    @Autowired
    private IdolCarTeamRepository idolCarTeamRepository;
    
    @Autowired
    private UserPrizeRepository userPrizeRepository;
    
   

    @Autowired
    @Qualifier("entSignCar")
    private YYPClientHALBProxy yypClientPc;


    public void testUserSign(long fansuid, int prizeType, int addNum) throws Exception {
        logger.info("testUserSign begin.....");
        try {
            yypClientPc.initDaemonConfig();
            ClientRequest clientRequest = new ClientRequest(YYPConstant.SERVER.REQUEST_USERSIGN);
            clientRequest.putLong(fansuid);
            clientRequest.putInteger(prizeType);
            clientRequest.putInteger(addNum);
            ClientResponse response = yypClientPc.getClient().sendAndWait(clientRequest);
            Marshallable mar = new LongMarshal();
            response.popMarshallable(mar);
            logger.info("test user sign result:{}", mar.toString());
        } catch (Exception e) {
            logger.error("testUserSign error", e);
        }
        logger.info("end add fragment!");
    }

    

   

    public void testGetUserInfo(long fansUid) throws Exception {
        logger.info("testGetUserInfo begin.....");
        yypClientPc.initDaemonConfig();
        ClientRequest clientRequest = new ClientRequest(YYPConstant.SERVER.REQUEST_QUERYUSERINFO);
        clientRequest.putLong(fansUid);
        ClientResponse response = yypClientPc.getClient().sendAndWait(clientRequest);
        StringMarshal marshal = new StringMarshal();
        response.popMarshallable(marshal);
        logger.info("getuserInfo marshal result:{}", marshal.data);

    }

   

   

    public void testUserEntry() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        MessageResponse res = new MessageResponse(YYPConstant.PC.MAX, YYPConstant.PC.RESPONSE_USER_ENTRY);
        result.put("carLevel", 2);
        result.put("carName", "豪华座驾");
        result.put("nick", "苏");
        result.put("uid", 50014644);
        res.putString(ResultWrapper.wrapJsonResultSuccess(result));
        yypService.sendBraodcastPC(res, new Uint(82473792), new Uint(2374686008L));
    }

   
    public void clearRedis() throws Exception {
        logger.info("clear all cache data..");
        rankService.clearCache();
    }


   

    
    public JSONObject testHeartBeat() throws Exception {
        yypClientPc.initDaemonConfig();
        ClientRequest clientRequest = new ClientRequest(YYPConstant.SERVER.REQUEST_HEARTBEAT);
        ClientResponse response = yypClientPc.getClient().sendAndWait(clientRequest);
        Marshallable mar = new IntegerMarshal();
        response.popMarshallable(mar);
        logger.info("testHeartBeat result:{}", mar);
        JSONObject json = new JSONObject();
        json.put("code", mar.toString());
        return json;
    }

    @Autowired
    EntProxyHalbService entProxyHalbService;


    public int test(String chid, String sid) throws Exception {
        ClientRequest req = new ClientRequest(YYPConstant.REQUEST_ROOM_TYPE);
        req.putUint(new Uint(chid));
        req.putUint(new Uint(sid));
        req.putMap(Maps.<String,String>newHashMap(),String.class,String.class);
        ClientResponse resp = entProxyHalbService.sendAndWait(req);
        Uint a = resp.popUint();
        logger.info("a={}", a);
        Uint chanType = resp.popUint();
        logger.info("chanType={}", chanType);
        a = resp.popUint();
        logger.info("c={}", a);
        logger.info("d={}", resp.popMap(String.class, String.class));
        return chanType.intValue();
    }

    
   
    


    @Override
    public void afterPropertiesSet() throws Exception {


    }


    public void addIdolCarTeam() {
        List<IdolCarTeam> all = idolCarTeamRepository.findAll();
        for (IdolCarTeam idolCarTeam : all) {
            redisTemplate.sadd(RedisKeyConstant.UID_CHID_CAR_SET.getKey(idolCarTeam.getActivityId()), idolCarTeam.getUid() + "_" + idolCarTeam.getChid());
        }

    }

}