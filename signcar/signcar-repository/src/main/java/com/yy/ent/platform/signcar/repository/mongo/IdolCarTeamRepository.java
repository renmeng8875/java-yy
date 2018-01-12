package com.yy.ent.platform.signcar.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.IdolCarTeam;
import com.yy.ent.platform.signcar.common.util.TimeUtil;


@Repository
public class IdolCarTeamRepository extends BaseMongoRepository<IdolCarTeam>
{

    public void updateUserCarLevel(Long activityId,Long uid,int carLevel){
        Update update = new Update();
        update.set("carLevel", carLevel);
        update.set("outDate", TimeUtil.getWeekDateLater());
        mongoTemplate.updateMulti(Query.query(Criteria.where("uid").is(uid).andOperator(Criteria.where("activityId").is(activityId))), update, IdolCarTeam.class);
    }
    
   
    public List<IdolCarTeam> findIdolCarTeam(Long uid,Long activityId){
        return mongoTemplate.find(Query.query(Criteria.where("uid").is(uid).andOperator(Criteria.where("activityId").is(activityId))), IdolCarTeam.class);
    }
    
    public List<IdolCarTeam> findIdolCarTeam(String chid,int carLevel){
        return mongoTemplate.find(Query.query(Criteria.where("chid").is(chid).andOperator(Criteria.where("carLevel").gt(carLevel))), IdolCarTeam.class);
    }
    
}
