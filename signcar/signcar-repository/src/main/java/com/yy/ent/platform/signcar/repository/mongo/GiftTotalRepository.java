package com.yy.ent.platform.signcar.repository.mongo;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.GiftTotal;

@Repository
public class GiftTotalRepository extends BaseMongoRepository<GiftTotal>{
    public GiftTotal updateGiftConsumeNum(long uid,long giftBizId,int incrNum){
        Update update = new Update();
        update.inc("consumeNum", incrNum);
        GiftTotal t = mongoTemplate.findAndModify(Query.query(Criteria.where("uid").is(uid).andOperator(Criteria.where("giftBizId").is(giftBizId))), update,FindAndModifyOptions.options().upsert(true).returnNew(true),GiftTotal.class);
        return t;
    }
    
    public GiftTotal updateGiftTotalNum(long uid,long giftBizId,int incrNum){
        Update update = new Update();
        update.inc("num", incrNum);
        GiftTotal t = mongoTemplate.findAndModify(Query.query(Criteria.where("uid").is(uid).andOperator(Criteria.where("giftBizId").is(giftBizId))), update,FindAndModifyOptions.options().upsert(true).returnNew(true),GiftTotal.class);
        return t;
    }
    
    public GiftTotal findRemainCoinNum(long uid,long giftBizId){
        GiftTotal giftRecord = mongoTemplate.findOne(Query.query(Criteria.where("uid").is(uid).andOperator(Criteria.where("giftBizId").is(giftBizId))), GiftTotal.class);
        return giftRecord;
    }
}
