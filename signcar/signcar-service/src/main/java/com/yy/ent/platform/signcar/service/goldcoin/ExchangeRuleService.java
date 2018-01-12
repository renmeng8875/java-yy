package com.yy.ent.platform.signcar.service.goldcoin;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.platform.signcar.common.mongodb.ExchangeRule;
import com.yy.ent.platform.signcar.common.util.TimeUtil;
import com.yy.ent.platform.signcar.repository.mongo.ExchangeRuleRespository;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.CommonService;

@Service
public class ExchangeRuleService extends BaseService implements InitializingBean{

    @Autowired
    private ExchangeRuleRespository exchangeRuleRespository;
    
    @Autowired
    private CommonService commonService;
    
    private Map<String, Object> ruleMap;
    
    private String rules;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        refreshRule();
    }
    
    public void refreshRule(){
        Map<String,ExchangeRule> reloadMap = new ConcurrentHashMap<String,ExchangeRule>();
        List<ExchangeRule>  ruleList = exchangeRuleRespository.findByField("status", 1);
        for(ExchangeRule rule:ruleList)
        {
            reloadMap.put(rule.getId(), rule);
        }
        ruleMap = new ConcurrentHashMap<String, Object>(reloadMap);
        JSONObject json = new JSONObject(ruleMap);
        rules = json.toJSONString();
    }
    
    public ExchangeRule getExchangeRule(String id)
    {
        ExchangeRule rule = (ExchangeRule)ruleMap.get(id);
        if(rule!=null){
            long nowTime = commonService.currentTimeMillis();
            logger.debug("getExchangeRule currentTime:{}",TimeUtil.formatTimeEx(new Date(nowTime)));
            if(nowTime<rule.getStartTime().getTime()||nowTime>rule.getEndTime().getTime())
            {
                return null;
            }   
        }
        
        return rule;
    }
    
    /**
     * @Desc:查询用户可使用的兑换规则
     * @return
     * @return:String
     * @author: renmeng  
     * @date: 2015年11月25日 下午4:48:36
     */
    public String getAllRules(long bizId,int remainNum,long uid){
        if(uid==0){
            JSONObject json = new JSONObject(ruleMap);
            return json.toJSONString();
        }
        Map<String, Object> canUseRuleMap = new ConcurrentHashMap<String,Object>();
        for(String key:ruleMap.keySet()){
            ExchangeRule rule = (ExchangeRule)ruleMap.get(key);
            if(rule.getPreGiftBizId()==bizId&&remainNum<rule.getPreNum()){
               continue; 
            }
            canUseRuleMap.put(rule.getId(),rule);
        }
        JSONObject json = new JSONObject(canUseRuleMap);
        return json.toJSONString();
    }
    
    public String getAllRules(){
        return rules;
    }

    
    
}
