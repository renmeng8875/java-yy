package com.yy.ent.platform.signcar.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.clients.halb.proxy.YYPClientHALBProxy;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.adapter.ClientResponse;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.mongodb.HeartStatistic;
import com.yy.ent.platform.signcar.common.yyp.HeartStatisticMarshal;
import com.yy.ent.platform.signcar.repository.mongo.HeartStatisticRepository;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.CommonService;

@Service
public class TestHeartStatisticService  extends BaseService implements InitializingBean{

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
    
    @Autowired
    private CommonService commonService ;
    
    @Autowired
    @Qualifier("entSignCar")
    private YYPClientHALBProxy yypClientPc;
    
    
    @Autowired
    private HeartStatisticRepository heartStatisticRepository;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
    
    public void createStatData(long uid){
        List<HeartStatistic> list = new ArrayList<HeartStatistic>();
        for(int i=90;i>-1;i--){
            HeartStatistic stat = new HeartStatistic();
            stat.setCoverNum(getRandomNum((91-i)*10000));
            stat.setFansNum(getRandomNum((91-i)*10000));
            stat.setHeartNum(getRandomNum((91-i)*10000));
            stat.setIdolUid(uid);
            stat.setStatDate(commonService.getYYYYMMDD(-i));
            list.add(stat);
        }
        heartStatisticRepository.insertAll(list);
    }
    
    public JSONObject testIdolStatisticInfo(long uid) throws Exception{
        JSONObject json = new JSONObject();
        try {
            yypClientPc.initDaemonConfig();
            ClientRequest clientRequest = new ClientRequest(YYPConstant.SERVER.REQUEST_IDOL_STATISTICINFO);
            clientRequest.putLong(uid);
            ClientResponse response = yypClientPc.getClient().sendAndWait(clientRequest);
            HeartStatisticMarshal marshal = new HeartStatisticMarshal();
            response.popMarshallable(marshal);
            json.put("code", marshal.result);
            
            logger.info("testIdolStatisticInfo result:{}", marshal.toString());
        } catch (Exception e) {
            logger.error("testIdolStatisticInfo error",e);        }
        
        json.put("message", "success");
        return json;
    }
    
    
    public long getRandomNum(int range){
        Random rn = new Random();
        return rn.nextInt(range);
    }
    
    
}
