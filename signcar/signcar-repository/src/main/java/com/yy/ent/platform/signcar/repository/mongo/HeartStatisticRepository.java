package com.yy.ent.platform.signcar.repository.mongo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.HeartStatistic;

@Repository
public class HeartStatisticRepository extends BaseMongoRepository<HeartStatistic>{
    public List<HeartStatistic> findHeartStatistic (long uid,String  startDate,String endDate){
        Criteria criteria = Criteria.where("idolUid").is(uid).and("statDate").gte(startDate).lte(endDate);
        Query query = Query.query(criteria).with(new Sort(Direction.ASC, "statDate"));
        List<HeartStatistic> list = mongoTemplate.find(query , HeartStatistic.class);
        return list;
    }
    
    //倒序查询今天之前的数据，包含昨天但不包含今天
    public HeartStatistic findLastHeartStatistic (long uid,String yyyymmdd){
        Criteria criteria = Criteria.where("idolUid").is(uid).and("statDate").lte(yyyymmdd);
        Query query = Query.query(criteria).with(new Sort(Direction.DESC, "statDate"));
        List<HeartStatistic> list = mongoTemplate.find(query , HeartStatistic.class);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return new HeartStatistic(-1l);
    }
    
    public void delHeartStatByDate(String statDate){
        Criteria criteria = Criteria.where("statDate").lt(statDate);
        mongoTemplate.remove(Query.query(criteria), HeartStatistic.class);
    }
}
