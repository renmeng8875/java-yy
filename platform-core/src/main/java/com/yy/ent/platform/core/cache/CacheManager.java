package com.yy.ent.platform.core.cache;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * CacheManager
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
public class CacheManager implements InitializingBean {
    /**
     * 所有caches
     */
    private Map<String, Cache> cachesMap;
    /**
     * 默认的cache key
     */
    private String defaultCacheName = "";
    /**
     * 默认的cache
     */
    private Cache defaultCache;

    public Map<String, Cache> getCachesMap() {
        return Collections.unmodifiableMap(cachesMap);
    }

    public void setDefaultCacheName(String defaultCacheName) {
        this.defaultCacheName = defaultCacheName;
    }

    public void setCachesMap(Map<String, Cache> cachesMap) {
        this.cachesMap = cachesMap;
    }

    /**
     * 根据名字查找缓存操作类
     *
     * @param name
     * @return
     */
    public Cache getCache(String name) {
        Cache cache = cachesMap.get(name);
        if (cache == null) {
            cache = defaultCache;
        }
        return cache;
    }

    public Collection<String> getCacheNames() {
        return cachesMap.keySet();
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(cachesMap, "缓存cachesMap不能为空");
        defaultCache = cachesMap.get(defaultCacheName);
    }
}