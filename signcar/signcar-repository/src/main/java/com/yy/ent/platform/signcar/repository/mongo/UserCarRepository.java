package com.yy.ent.platform.signcar.repository.mongo;

import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.UserCar;
import com.yy.ent.platform.signcar.common.util.TimeUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class UserCarRepository extends BaseMongoRepository<UserCar>
{
    public void updateUserCarLevel(Long activityId,Long uid,int carLevel){
        Update update = new Update();
        update.set("carLevel", carLevel);
        update.set("outDate", TimeUtil.getWeekDateLater());
        mongoTemplate.updateFirst(Query.query(Criteria.where("uid").is(uid).andOperator(Criteria.where("activityId").is(activityId))), update, UserCar.class);
        
    }

    public UserCar getUserCar(Long uid,Long activityId) {
        Criteria criteria = new Criteria("uid");
        criteria.is(uid);
        criteria.andOperator(Criteria.where("activityId").is(activityId));
        UserCar userCar = mongoTemplate.findOne(Query.query(criteria), UserCar.class );
        return userCar;
    }

}
