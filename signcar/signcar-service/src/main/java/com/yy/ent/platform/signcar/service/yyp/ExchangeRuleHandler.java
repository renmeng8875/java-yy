package com.yy.ent.platform.signcar.service.yyp;

import java.util.HashMap;
import java.util.Map;

import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.commons.protopack.marshal.IntegerMarshal;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.yyp.CoinResultMarshal;
import com.yy.ent.platform.signcar.service.goldcoin.ExchangeCoinToHeartService;
import com.yy.ent.platform.signcar.service.goldcoin.ExchangeRuleService;
import com.yy.ent.platform.signcar.service.heart.HeartNotifyService;

public class ExchangeRuleHandler extends BaseYYPHandler {

    private ExchangeRuleService exchangeRuleService = SpringHolder.getBean(ExchangeRuleService.class);

    private ExchangeCoinToHeartService exchangeCoinToHeartService = SpringHolder.getBean(ExchangeCoinToHeartService.class);

    private HeartNotifyService notifyService = SpringHolder.getBean(HeartNotifyService.class);


    @URI(YYPConstant.SERVER.REQUEST_REFRESHRULE)
    public void refrestRule() throws Exception {
        logger.info("refrestRule method is invoked!");
        exchangeRuleService.refreshRule();
        IntegerMarshal marshal = new IntegerMarshal(1);
        responseServer(YYPConstant.SERVER.RESPONSE_REFRESHRULE, marshal);
    }

    /**
     * @Desc:金币兑换心(10金币==1心)
     * @return:code 0是兑换成功，1是失败，2.金币数量不足,3.参数不合法,4心数量已达上限
     * @author: leijing
     * @date: 2015年1月5日 下午13:20:30
     */
    @URI(YYPConstant.SERVER.REQUEST_EXCHANGE_COIN_HEART)
    public void exchangeCoinToHeart() throws Exception {
        logger.info("exchangeCoinToHeart method is invoked!");
        long topChid = getRequest().popUint().longValue();
        long subChid = getRequest().popUint().longValue();
        long uid = getRequest().popLong();
        int heartNum = getRequest().popInteger();
        Boolean isPC = true;//签到类型 pc端返回true，手机返回false
        logger.debug("exchangeCoinToHeart request paramters=>>topChid:{},subChid:{},isPC:{},uid:{},heartNum:{}", topChid, subChid, isPC, uid, heartNum);
        CoinResultMarshal marshal = null;
        Map<String, Integer> result = new HashMap<String, Integer>();
        if (heartNum > 0) {
            marshal = exchangeCoinToHeartService.exchangeCoinToHeart(uid, heartNum);
            logger.info("exchangeCoinToHeart uid={},result={},coinNum={}", uid, marshal.getResult(), marshal.getCoinNum());
            if (marshal.getResult() == 0 && topChid > 0 && subChid > 0) {
                notifyService.notifyHeart(topChid, subChid, uid);
            }
        } else {
            //参数不合法
            marshal = new CoinResultMarshal();
            marshal.setResult(3);
        }
        responseServer(YYPConstant.SERVER.RESPONSE_EXCHANGE_COIN_HEART, marshal);

    }

}
