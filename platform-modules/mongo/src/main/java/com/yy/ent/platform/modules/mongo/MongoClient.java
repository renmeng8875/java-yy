package com.yy.ent.platform.modules.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoClient
 *
 * @author suzhihua
 * @date 2015/11/3.
 */
public class MongoClient implements InitializingBean {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private com.mongodb.MongoClient client;
    private String databaseName;

    /**
     * 查询
     *
     * @param collection 表名
     * @param query      查询条件，null全表查询
     * @param callback   回调FindIterable，一般留空，当要分页、排序时候需要操作。
     * @param clazz      要转换的类型
     * @param <T>
     * @return
     */
    public <T extends BaseMongoModel> List<T> find(String collection, Bson query, FindIterableCallback callback, Class<T> clazz) {
        List<Document> list = find(collection, query, callback);
        List<T> result = new ArrayList<T>(list.size());
        T bean;
        try {
            for (Document document : list) {
                bean = clazz.newInstance();
                bean.valueOf(document);
                result.add(bean);
            }
        } catch (InstantiationException e) {
            logger.warn("mongo error", e);
        } catch (IllegalAccessException e) {
            logger.warn("mongo error", e);
        }

        return result;
    }

    /**
     * 查询
     *
     * @param collection 表名
     * @param query      查询条件，null全表查询
     * @param callback   回调FindIterable，一般留空，当要分页、排序时候需要操作。
     * @param clazz      要转换的类型
     * @param <T>
     * @return
     */
    public <T extends BaseMongoModel> T findOne(String collection, Bson query, FindIterableCallback callback, Class<T> clazz) {
        Document doc = findOne(collection, query, callback);
        T result = null;
        try {
            result = clazz.newInstance();
            result.valueOf(doc);
        } catch (InstantiationException e) {
            logger.warn("mongo error", e);
        } catch (IllegalAccessException e) {
            logger.warn("mongo error", e);
        }

        return result;
    }

    /**
     * 查询
     *
     * @param collection 表名
     * @param query      查询条件，null全表查询
     * @param callback   回调FindIterable，一般留空，当要分页、排序时候需要操作。
     * @return
     */
    public List<Document> find(String collection, Bson query, FindIterableCallback callback) {
        FindIterable<Document> documents = (query != null ? getCollection(collection).find(query) : getCollection(collection).find());
        if (callback != null) callback.doInFindIterable(documents);
        MongoCursor<Document> cursor = documents.iterator();
        List<Document> result = getResult(cursor);
        return result;
    }

    /**
     * 查询
     *
     * @param collection 表名
     * @param query      查询条件，null全表查询
     * @param callback   回调FindIterable，一般留空，当要分页、排序时候需要操作。
     * @return
     */
    public Document findOne(String collection, Bson query, FindIterableCallback callback) {
        FindIterable<Document> documents = (query != null ? getCollection(collection).find(query) : getCollection(collection).find());
        if (callback != null) callback.doInFindIterable(documents);
        MongoCursor<Document> cursor = documents.iterator();
        Document result = getResultOne(cursor);
        return result;
    }

    /**
     * 取结果
     *
     * @param cursor
     * @return
     */
    private List<Document> getResult(MongoCursor<Document> cursor) {
        List<Document> result = new ArrayList<Document>();
        try {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        } catch (Throwable e) {
            logger.warn("mongo error", e);
        } finally {
            cursor.close();
        }
        return result;
    }

    /**
     * 取结果
     *
     * @param cursor
     * @return
     */
    private Document getResultOne(MongoCursor<Document> cursor) {
        try {
            while (cursor.hasNext()) {
                return cursor.next();
            }
        } catch (Throwable e) {
            logger.warn("mongo error", e);
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * 组合统计
     * https://docs.mongodb.org/manual/reference/method/db.collection.aggregate/#db.collection.aggregate
     * https://docs.mongodb.org/manual/reference/operator/aggregation/group/
     *
     * @param collection
     * @param pipeline
     * @return
     */
    public List<Document> aggregate(String collection, List<Bson> pipeline) {
        MongoCollection<Document> collection1 = client.getDatabase(databaseName).getCollection(collection);
        AggregateIterable<Document> aggregate = collection1.aggregate(pipeline);
        MongoCursor<Document> cursor = aggregate.iterator();
        List<Document> result = getResult(cursor);
        return result;
    }

    /**
     * 组合统计
     *
     * @param collection
     * @param match      过滤条件 可留空
     * @param group      分组返回结果 可留空
     * @param sort       排序 可留空
     * @return
     */
    public List<Document> aggregate(String collection, Bson match, Bson group, Bson sort) {
        MongoCollection<Document> collection1 = client.getDatabase(databaseName).getCollection(collection);
        List<Bson> pipeline = new ArrayList<Bson>();
        if (match != null) pipeline.add(new BasicDBObject("$match", match));
        if (group != null) pipeline.add(new BasicDBObject("$group", group));
        if (sort != null) pipeline.add(new BasicDBObject("$sort", sort));
        AggregateIterable<Document> aggregate = collection1.aggregate(pipeline);
        MongoCursor<Document> cursor = aggregate.iterator();
        List<Document> result = getResult(cursor);
        return result;
    }

    public MongoCollection<Document> getCollection(String collection) {
        return client.getDatabase(databaseName).getCollection(collection);
    }

    public com.mongodb.MongoClient getClient() {
        return client;
    }

    public void setClient(com.mongodb.MongoClient client) {
        this.client = client;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(databaseName, "databaseName must not be null");
        Assert.notNull(client, "client must not be null");
    }
}
