package com.yy.ent.platform.modules.mongo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.yy.ent.commons.base.dto.page.Page;

/**
 * BaseMongodbRepository
 *
 * @author suzhihua
 * @date 2015/10/27.
 */
public class BaseMongoRepository<T extends BaseMongoModel> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MongoTemplate mongoTemplate;
    @Autowired
    protected MongoClient mongoClient;

    protected Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseMongoRepository() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void insert(T t) {
        mongoTemplate.insert(t);
    }

    public void insert(T t, String collectionName) {
        mongoTemplate.insert(t, collectionName);
    }

    public void insertAll(Collection<T> list) {
        mongoTemplate.insertAll(list);
    }

    public void insertAll(Collection<T> list, String collectionName) {
        mongoTemplate.insert(list, collectionName);
    }

    public void update(T t) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(t.getId())), reflectSetValue(t), entityClass);
    }

    public void update(T t, String collectionName) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(t.getId())), reflectSetValue(t), entityClass, collectionName);
    }

    public void delete(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), entityClass);
    }

    public void delete(String id, String collectionName) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), entityClass, collectionName);
    }


    public void delete(Query query) {
        mongoTemplate.remove(query);
    }

    public void delete(Query query, String collectionName) {
        mongoTemplate.remove(query, collectionName);
    }

    public List<T> findAllAndRemove(Query query) {
        List<T> allAndRemove = mongoTemplate.findAllAndRemove(query, entityClass);
        return allAndRemove;
    }

    public List<T> findAllAndRemove(Query query, String collectionName) {
        List<T> allAndRemove = mongoTemplate.findAllAndRemove(query, entityClass, collectionName);
        return allAndRemove;
    }

    public void deleteByField(String filedName, Object fieldValue) {
        mongoTemplate.remove(Query.query(Criteria.where(filedName).is(fieldValue)), entityClass);
    }

    public void deleteByField(String filedName, Object fieldValue, String collectionName) {
        mongoTemplate.remove(Query.query(Criteria.where(filedName).is(fieldValue)), entityClass, collectionName);
    }

    public T get(String id) {
        T t = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), entityClass);
        return t;
    }

    public T get(String id, String collectionName) {
        T t = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), entityClass, collectionName);
        return t;
    }

    public List<T> findByField(String fieldName, Object fieldValue) {
        List<T> list = mongoTemplate.find(Query.query(Criteria.where(fieldName).is(fieldValue)), entityClass);
        return list;
    }

    public List<T> findByField(String fieldName, Object fieldValue, String collectionName) {
        List<T> list = mongoTemplate.find(Query.query(Criteria.where(fieldName).is(fieldValue)), entityClass, collectionName);
        return list;
    }

    public List<T> findByFieldSort(String fieldName, Object fieldValue, String sortFieldName, Sort.Direction direction) {
        List<T> list = mongoTemplate.find(Query.query(Criteria.where(fieldName).is(fieldValue)).with(new Sort(direction, sortFieldName)), entityClass);

        return list;
    }

    public List<T> findByFieldSort(String fieldName, Object fieldValue, final String sortFieldName, final Sort.Direction direction, String collectionName) {
        List<T> list = mongoClient.find(collectionName, new BasicDBObject(fieldName, fieldValue), new FindIterableCallback() {
            @Override
            public void doInFindIterable(FindIterable<Document> documents) {
                documents.sort(new BasicDBObject(sortFieldName, direction == Sort.Direction.DESC ? -1 : 1));
            }
        }, entityClass);
        //List<T> list = mongoTemplate.find(Query.query(Criteria.where(fieldName).is(fieldValue)).with(new Sort(direction, sortFieldName)), entityClass,collectionName);

        return list;
    }

    public T findOneByField(String fieldName, Object fieldValue) {
        T t = mongoTemplate.findOne(Query.query(Criteria.where(fieldName).is(fieldValue)), entityClass);
        return t;
    }

    public T findOneByField(String fieldName, Object fieldValue, String collectionName) {
        T t = mongoTemplate.findOne(Query.query(Criteria.where(fieldName).is(fieldValue)), entityClass, collectionName);
        return t;
    }

    public List<T> findAll() {
        List<T> list = mongoTemplate.findAll(entityClass);
        return list;
    }

    public List<T> findAll(String collectionName) {
        List<T> list = mongoTemplate.findAll(entityClass, collectionName);
        return list;
    }

    public Page<T> findByPage(int offset, int pageSize, Map<String, Object> conditionMap) {
        Page<T> page = new Page<T>();
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (conditionMap != null) {
            for (String key : conditionMap.keySet()) {
                criteria.andOperator(Criteria.where(key).is(conditionMap.get(key)));
            }
        }
        long totalCount = mongoTemplate.count(Query.query(criteria), entityClass);
        query.skip(offset);
        query.limit(pageSize);
        List<T> list = mongoTemplate.find(Query.query(criteria), entityClass);
        page.setResult(list);
        page.setTotalCount((int) totalCount);
        return page;
    }

    public Page<T> findByPage(int offset, int pageSize, Map<String, Object> conditionMap, String collectionName) {
        Page<T> page = new Page<T>();
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (conditionMap != null) {
            for (String key : conditionMap.keySet()) {
                criteria.andOperator(Criteria.where(key).is(conditionMap.get(key)));
            }
        }
        long totalCount = mongoTemplate.count(Query.query(criteria), entityClass, collectionName);
        query.skip(offset);
        query.limit(pageSize);
        List<T> list = mongoTemplate.find(Query.query(criteria), entityClass, collectionName);
        page.setResult(list);
        page.setTotalCount((int) totalCount);
        return page;
    }
    
    private Update reflectSetValue(T model) 
    {
        Field[] field = model.getClass().getDeclaredFields();
        Update update = new Update();
        for(int j=0 ; j<field.length ; j++){    
            String name = field[j].getName();   
            Method m;
            try {
                m = model.getClass().getMethod("get"+captureName(field[j].getName()));
                Object value = m.invoke(model);
                update.set(name, value);
            } catch (Exception e) {
               throw new RuntimeException("field name or method type error,name:"+name);
            }
            
            
        }
        return update;
        
    }
    
    private String captureName(String name) 
    {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }


}
