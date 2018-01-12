package com.yy.ent.platform.signcar.service.goldcoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yy.ent.platform.signcar.common.mongodb.GiftTotal;
import com.yy.ent.platform.signcar.repository.mongo.ExchangeDetailRepository;
import com.yy.ent.platform.signcar.repository.mongo.GiftTotalRepository;
import com.yy.ent.platform.signcar.service.BussinessConf;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.ProfitService;

@Service
public class ExchangeService extends BaseService {
    @Autowired
    private ProfitService profitService;

    @Autowired
    private ExchangeRuleService exchangeRuleService;

    @Autowired
    private ActivityConfigService activityConfigService;

    @Autowired
    private ExchangeDetailRepository exchangeDetailRepository;

    @Autowired
    private GiftTotalRepository giftRecordRepository;

    @Autowired
    private BussinessConf config;


    

    /**
     * @param uid
     * @param giftBizId
     * @Desc:查询用户还有多少金币
     * @return:int
     * @author: renmeng
     * @date: 2015年11月25日 下午4:13:52
     */
    public int getRemainCoinNum(long uid, long giftBizId) {
        int coinNum = 0;
        GiftTotal giftRecord = giftRecordRepository.findRemainCoinNum(uid, giftBizId);
        if (giftRecord != null) {
            coinNum = giftRecord.getNum() - (giftRecord.getConsumeNum() == null ? 0 : giftRecord.getConsumeNum());
        }
        return coinNum;

    }

    public GiftTotal getGiftTotal(long uid, long giftBizId) {
        GiftTotal giftRecord = giftRecordRepository.findRemainCoinNum(uid, giftBizId);
        return giftRecord;
    }


   
}
