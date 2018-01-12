package com.yy.ent.platform.core.cache;

/**
 * Cache，缓存操作类
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
public interface Cache {
    /**
     * 缓存名称
     *
     * @return
     */
    String getName();

    /**
     * 取缓存
     *
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 根据类型取缓存
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> T get(String key, Class<T> type);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    void put(String key, Object value);

    /**
     * 设置带失效时间的缓存
     *
     * @param key
     * @param value
     * @param expireSecond
     */
    void put(String key, Object value, int expireSecond);

    /**
     * 清除缓存
     *
     * @param key
     */
    void evict(String key);

    /**
     * 清除所有缓存
     */
    void clear();

    /**
     * 取key名字
     *
     * @param key
     * @return
     */
    String getKey(String key);
}
