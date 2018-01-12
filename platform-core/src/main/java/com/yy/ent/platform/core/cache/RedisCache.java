package com.yy.ent.platform.core.cache;

import com.alibaba.fastjson.JSON;
import com.yy.ent.platform.core.redis.RedisTemplate;

/**
 * RedisCache
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
public class RedisCache implements Cache {
    private String name;
    private String prefix;
    private RedisTemplate redisTemplate;

    public RedisCache(String name, String prefix, RedisTemplate redisTemplate) {
        this.name = name;
        this.prefix = prefix;
        this.redisTemplate = redisTemplate;
    }

    public String getName() {
        return name;
    }

    public Object get(String key) {
        return redisTemplate.get(getKey(key));
    }

    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.get(getKey(key));
        if (value == null) return null;
        return JSON.parseObject((String) value, type);
    }


    public void put(String key, Object value) {
        redisTemplate.set(getKey(key), JSON.toJSONString(value));
    }

    public void put(String key, Object value, int expireSecond) {
        if (expireSecond > 0) {
            redisTemplate.setex(getKey(key), JSON.toJSONString(value), expireSecond);
        } else {
            put(key, value);
        }
    }

    public void evict(String key) {
        redisTemplate.del(getKey(key));
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }


    public String getKey(String key) {
        return prefix + key;
    }
}
