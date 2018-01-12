package com.yy.ent.platform.signcar.repository.mongo;

import com.mongodb.BasicDBObject;
import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.HeartTotal;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HeartTotalRepository extends BaseMongoRepository<HeartTotal> {

    public int getHeartTotalNum(Long fansUid) {
        Bson query = new BasicDBObject("fansUid", fansUid);
        Document one = mongoClient.findOne(HeartTotal.TABLE_NAME, query, null);
        int num = 0;
        if (one != null) {
            Number num1 = one.get("num", Number.class);
            if (num1 != null) {
                num = num1.intValue();
            }
        }
        return num;
    }

    public HeartTotal incrHeartTotalNum(Long fansUid, Integer incrNum, Date date) {
        Update update = new Update();
        update.inc("num", incrNum);
        update.set("lastUpdate", date);
        FindAndModifyOptions options = FindAndModifyOptions.options().upsert(true).returnNew(true);
        HeartTotal t = mongoTemplate.findAndModify(Query.query(Criteria.where("fansUid").is(fansUid)), update, options, HeartTotal.class);
        return t;
    }

    public Map<Long, Integer> getHeartTotalIn(Set<Long> fansUids) {
        Bson query = new BasicDBObject("fansUid", new BasicDBObject("$in", fansUids));
        List<Document> queryResult = mongoClient.find(HeartTotal.TABLE_NAME, query, null);
        logger.debug("fansUids.size={},result.size={}", fansUids.size(), queryResult.size());
        Map<Long, Integer> result = new HashMap<Long, Integer>();
        for (Document document : queryResult) {
            result.put(document.getLong("fansUid"), document.getInteger("num"));
        }
        return result;
    }
}
