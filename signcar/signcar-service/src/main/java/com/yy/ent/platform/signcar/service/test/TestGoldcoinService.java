package com.yy.ent.platform.signcar.service.test;

import com.yy.ent.clients.halb.proxy.YYPClientHALBProxy;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.adapter.ClientResponse;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.mongodb.ExchangeRule;
import com.yy.ent.platform.signcar.common.mongodb.GiftTotal;
import com.yy.ent.platform.signcar.common.util.TimeUtil;
import com.yy.ent.platform.signcar.common.yyp.CoinBeanMarshal;
import com.yy.ent.platform.signcar.common.yyp.ExchangeBeanMarshal;
import com.yy.ent.platform.signcar.repository.mongo.ExchangeRuleRespository;
import com.yy.ent.platform.signcar.repository.mongo.GiftTotalRepository;
import com.yy.ent.platform.signcar.service.common.BaseService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TestGoldcoinService extends BaseService implements InitializingBean{

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
    
    @Autowired
    @Qualifier("entSignCar")
    private YYPClientHALBProxy yypClientPc;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
    
    @Autowired
    private ExchangeRuleRespository exchangeRuleRespository;
    
    @Autowired
    private GiftTotalRepository giftTotalRepository;
    
    
            /*
             * selfTicket.id=5205
             * channelTicket.id=5210
             * 1张个人票=10金币   
            2张个人票=20金币
            5张个人票=50金币
            10张个人票=100金币
            1张频道票=10金币
            2张频道票=20金币
            5张频道票=50金币
            10张频道票=100金币*/
    public List<ExchangeRule> testInserRules(String start,String end) throws Exception{
        List<ExchangeRule> ruleList = new ArrayList<ExchangeRule>();
        
        ExchangeRule rule = new ExchangeRule();
        rule.setRuleName("1张个人票=10金币");
        rule.setAfterNum(1);
        rule.setAfterGiftBizId(5205l);
        rule.setPreNum(10);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        rule = new ExchangeRule();
        rule.setRuleName("2张个人票=20金币");
        rule.setAfterNum(2);
        rule.setAfterGiftBizId(5205l);
        rule.setPreNum(20);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        rule = new ExchangeRule();
        rule.setRuleName("5张个人票=50金币");
        rule.setAfterNum(5);
        rule.setAfterGiftBizId(5205l);
        rule.setPreNum(50);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        rule = new ExchangeRule();
        rule.setRuleName("10张个人票=100金币");
        rule.setAfterNum(10);
        rule.setAfterGiftBizId(5205l);
        rule.setPreNum(100);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        rule = new ExchangeRule();
        rule.setRuleName("1张频道票=10金币");
        rule.setAfterNum(1);
        rule.setAfterGiftBizId(5210l);
        rule.setPreNum(10);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        rule = new ExchangeRule();
        rule.setRuleName("2张频道票=20金币");
        rule.setAfterNum(2);
        rule.setAfterGiftBizId(5210l);
        rule.setPreNum(20);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        rule = new ExchangeRule();
        rule.setRuleName("5张频道票=50金币");
        rule.setAfterNum(5);
        rule.setAfterGiftBizId(5210l);
        rule.setPreNum(50);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        rule = new ExchangeRule();
        rule.setRuleName("10张频道票=100金币");
        rule.setAfterNum(10);
        rule.setAfterGiftBizId(5210l);
        rule.setPreNum(100);
        setValue(rule,start,end);
        ruleList.add(rule);
        
        exchangeRuleRespository.insertAll(ruleList);
        return ruleList;
    }
    
    private void setValue(ExchangeRule rule,String start,String end) throws Exception{
        rule.setPreGiftBizId(1000000L);
        rule.setStatus(1);
        rule.setType(0);
        rule.setStartTime(TimeUtil.parseDate(start));
        rule.setEndTime(TimeUtil.parseDate(end));
        rule.setInvokeClass("");
        rule.setLimitNum(20);
    }
    
    public void testQueryCoin(long uid) throws Exception{
        logger.info("testQueryCoin begin.....");
        try {
            yypClientPc.initDaemonConfig();
            ClientRequest clientRequest = new ClientRequest(YYPConstant.SERVER.REQUEST_QUERYCOINANDTICKET);
            clientRequest.putLong(uid);
            clientRequest.putLong(1000000L);
            ClientResponse response = yypClientPc.getClient().sendAndWait(clientRequest);
            CoinBeanMarshal marshal = new CoinBeanMarshal();
            response.popMarshallable(marshal);
            logger.info("testQueryCoin result:{}", marshal.toString());
        } catch (Exception e) {
            logger.error("testQueryCoin error", e);
        }
        logger.info("end testQueryCoin!");
    }
    
    
  
    
    public void testExchange(long uid,String ruleId) throws Exception{
        logger.info("testExchange begin.....");
        try {
            yypClientPc.initDaemonConfig();
            ClientRequest clientRequest = new ClientRequest(YYPConstant.SERVER.REQUEST_EXCHANGE);
            clientRequest.putLong(uid);
            clientRequest.putInteger(1);
            clientRequest.putString(ruleId);
            clientRequest.putInteger(1);
            ClientResponse response = yypClientPc.getClient().sendAndWait(clientRequest);
            ExchangeBeanMarshal marshal = new ExchangeBeanMarshal();
            response.popMarshallable(marshal);
            logger.info("test testExchange result:{}", marshal.toString());
        } catch (Exception e) {
            logger.error("testExchange error", e);
        }
        logger.info("end testExchange!");
    }
    
    
    public GiftTotal testAddGoldCoin(long uid,int addNum)
    {
        GiftTotal t = giftTotalRepository.updateGiftTotalNum(uid, 1000000L, addNum);
        logger.info("testAddGoldCoin result:{}",t);
        if(t==null)
        {
            t = new GiftTotal();
            t.setConsumeNum(0);
            t.setGiftBizId(1000000L);
            t.setNum(addNum);
            t.setUid(uid);
            giftTotalRepository.insert(t);
        }    
        return t;
    }

}
