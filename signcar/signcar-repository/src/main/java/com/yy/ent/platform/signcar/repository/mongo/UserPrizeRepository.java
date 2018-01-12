package com.yy.ent.platform.signcar.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.UserPrize;

@Repository
public class UserPrizeRepository extends BaseMongoRepository<UserPrize>
{

    public UserPrize getUserPrize(Long uid,Integer prizeType,Long activityId){
        Criteria criteria= Criteria.where("uid").is(uid)
                .and("prizeType").is(prizeType)
                .and("activityId").is(activityId);
        UserPrize userPrize = mongoTemplate.findOne(Query.query(criteria), UserPrize.class );
        return userPrize;
    }
    
    public List<UserPrize> getUserPrizeList(Long uid, Long activityId) {
        Criteria criteria= Criteria.where("uid").is(uid)
                .and("activityId").is(activityId);
        List<UserPrize> userPrizeList = mongoTemplate.find(Query.query(criteria), UserPrize.class );
        return userPrizeList;
    }
    
   
    public void updateUserPrize(Long activityId,UserPrize u){
        Criteria criteria= Criteria.where("uid").is(u.getUid())
                .and("prizeType").is(u.getPrizeType())
                .and("activityId").is(activityId);
        Update update = new Update();
        update.set("totalNum", u.getTotalNum());
        update.set("consumeNum", u.getConsumeNum());
        mongoTemplate.updateFirst(Query.query(criteria), update, UserPrize.class);
    }
    
    
   
    
    
    
}
