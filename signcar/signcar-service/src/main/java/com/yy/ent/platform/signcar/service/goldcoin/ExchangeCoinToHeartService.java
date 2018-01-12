package com.yy.ent.platform.signcar.service.goldcoin;

import com.yy.ent.platform.signcar.common.mongodb.GiftTotal;
import com.yy.ent.platform.signcar.common.yyp.CoinResultMarshal;
import com.yy.ent.platform.signcar.repository.mongo.GiftTotalRepository;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.CommonService;
import com.yy.ent.platform.signcar.service.heart.HeartNotifyService;
import com.yy.ent.platform.signcar.service.heart.HeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExchangeCoinToHeartService extends BaseService {

    private static final long COIN_ID = 1000000l;//金币id
    private static final int RESULT_UNKNOWN_ERROR = 1;//1未知是失败
    private static final int RESULT_NOT_ENOUGH_COIN = 2;//2金币数量不足
    private static final int RESULT_HEART_LIMIT = 4;//4心数量已达上限
    private static final int SUCCESS = 0;//成功

    @Autowired
    private GiftTotalRepository giftRecordRepository;
    @Value("${ExchangeCoinToHeartService.rate}")
    private int rate;//1个心对应需要多个金币
    @Autowired
    private HeartService heartService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private HeartNotifyService notifyService;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * @Desc:金币兑换心
     * @return:code 0是兑换成功，1是失败，2.金币数量不足,4.心数量已达上限
     * @author: leijing
     * @date: 2015年1月4日 下午18:22:32
     */
    public CoinResultMarshal exchangeCoinToHeart(long uid, int heartNum) {
        int coinNum = rate * heartNum;
        CoinResultMarshal result = new CoinResultMarshal();
        try {
            GiftTotal giftTotal = giftRecordRepository.findRemainCoinNum(uid, COIN_ID);
            //金币不足
            if (null == giftTotal || giftTotal.getNum() < giftTotal.getConsumeNum() + coinNum) {
                result.setResult(RESULT_NOT_ENOUGH_COIN);
                return result;
            }
            //心数超过限制
            int fansCurrentHeartNum = heartService.getFansHeartNum(uid);
            if (fansCurrentHeartNum + heartNum > HeartService.MAX_HEART_NUM) {
                result.setResult(RESULT_HEART_LIMIT);
                return result;
            }
            giftTotal = giftRecordRepository.updateGiftConsumeNum(uid, COIN_ID, coinNum);
            //金币不足
            if (null == giftTotal || giftTotal.getNum() < giftTotal.getConsumeNum()) {
                giftRecordRepository.updateGiftConsumeNum(uid, COIN_ID, -coinNum);
                result.setResult(RESULT_NOT_ENOUGH_COIN);
                return result;
            }

            //正常流程
            heartService.addHeart(uid, heartNum);
            result.setCoinNum(giftTotal.getNum() - giftTotal.getConsumeNum());
            result.setResult(SUCCESS);
        } catch (Exception e) {
            logger.warn("exchangeCoinToHeart error", e);
            result.setResult(RESULT_UNKNOWN_ERROR);
        }
        return result;
    }


}
