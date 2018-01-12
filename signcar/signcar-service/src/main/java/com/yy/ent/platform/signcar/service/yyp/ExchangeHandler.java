package com.yy.ent.platform.signcar.service.yyp;

import java.util.HashMap;
import java.util.Map;

import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.yyp.CoinBeanMarshal;
import com.yy.ent.platform.signcar.common.yyp.ExchangeBeanMarshal;
import com.yy.ent.platform.signcar.service.goldcoin.ExchangeService;

public class ExchangeHandler extends BaseYYPHandler{

    
    private ExchangeService exchangeService = SpringHolder.getBean(ExchangeService.class);
    
    
    private Map<String,String> extendInfo = new HashMap<String,String>();
    
    /**
     * @Desc:金币兑换票接口
     * @throws Exception
     * @return:void
     * @author: renmeng  
     * @date: 2015年11月25日 下午4:43:32
     */
    @URI(YYPConstant.SERVER.REQUEST_EXCHANGE)
    public void exchange() throws Exception
    {
        long uid = getRequest().popLong();
        int source = getRequest().popInteger();
        String ruleId = getRequest().popString();
        int exchangeTimes = getRequest().popInteger();
        
        logger.info("exchange parameters=>uid:{},source:{},ruleId:{},exchangeTimes:{}",uid,source,ruleId,exchangeTimes);
        
        if(exchangeTimes<=0){
            exchangeTimes = 1;
        }
        //0是兑换成功，1是失败，2.票数已经不够,3.兑换时间已过期,4金币数量不足,5
        int coinNum = 0,result=3;
        /*
        ExchangeRule rule = exchangeRuleService.getExchangeRule(ruleId);
        if(rule!=null){
            GiftTotal giftTotal = exchangeService.getGiftTotal(uid, rule.getPreGiftBizId());
            if(giftTotal!=null){
                coinNum = giftTotal.getNum() - (giftTotal.getConsumeNum() != null ? giftTotal.getConsumeNum():0) - exchangeTimes*rule.getPreNum();
                if(coinNum>=0){
                    result = exchangeService.exchangeTicket(uid, source, ruleId,exchangeTimes);
                    if(result==2){
                        coinNum = giftTotal.getNum() - giftTotal.getConsumeNum();
                    }
                }else{
                    coinNum = giftTotal.getNum() - giftTotal.getConsumeNum();
                    result = 4;
                }
            }else{
                result = 4;
            }
            
        }*/
        ExchangeBeanMarshal marshal = new ExchangeBeanMarshal();
        marshal.result = result;
        marshal.uid = uid;
        marshal.coinNum = coinNum;
        marshal.extendInfo = extendInfo;
        logger.info("exchange result:{}",marshal.toString());
        responseServer(YYPConstant.SERVER.RESPONSE_EXCHANGE, marshal);
    }
    
    
    /**
     * @Desc:查询用户余下金币以及可使用的兑换规则
     * @throws Exception
     * @return:void
     * @author: renmeng  
     * @date: 2015年11月25日 下午4:44:10
     */
    @URI(YYPConstant.SERVER.REQUEST_QUERYCOINANDTICKET)
    public void queryGoldCoin() throws Exception
    {
        long uid = getRequest().popLong();
        long bizId = getRequest().popLong();
        logger.info("queryGoldCoin parameters=>uid:{},bizId:{}",uid,bizId);
        int coinNum = 0;
        long selfTicketNum=0,channelTicketNum=0;
        //用户没登录时，调用方uid传0
        if(uid>0){
            coinNum = exchangeService.getRemainCoinNum(uid, bizId); 
            //selfTicketNum = exchangeService.getRemainTicketNum(conf.getSelfTicketId());
            //channelTicketNum = exchangeService.getRemainTicketNum(conf.getChannelTicketId());
        }
        CoinBeanMarshal marshal = new CoinBeanMarshal();
        marshal.result = 0;
        marshal.uid = uid;
        marshal.coinNum = coinNum;
        marshal.selfTicketNum = selfTicketNum;
        marshal.channelTicketNum = channelTicketNum;
        marshal.rules = "{}";//exchangeRuleService.getAllRules();
        marshal.extendInfo = extendInfo;
        logger.info("queryGoldCoin result:{}",marshal.toString());
        responseServer(YYPConstant.SERVER.RESPONSE_QUERYCOINANDTICKET, marshal);
    }
}
