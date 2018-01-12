package com.yy.ent.platform.core.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * CacheableInterceptor
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
public class CacheInterceptor implements InitializingBean {
    protected final Logger logger = LoggerFactory.getLogger(CacheInterceptor.class);
    private CacheManager cacheManager;

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Object aroundCacheEvictInterceptor(ProceedingJoinPoint call) throws Throwable {
        CacheEvict cacheEvict = getAnnotation(call, CacheEvict.class);
        if (cacheEvict == null) return call.proceed();
        Object result;
        String key = cacheEvict.key();
        String condition = cacheEvict.condition();
        SpelParser spel = new SpelParser(call);
        String type = cacheEvict.type();
        Cache cache = cacheManager.getCache(type);
        result = call.proceed();

        Boolean conditionResult = spel.getValue(condition, Boolean.class, result);
        if (conditionResult) {
            key = spel.getValue(key, String.class);
            logger.info("cache.evict:" + cache.getKey(key));
            cache.evict(key);
        }
        return result;
    }

    public Object aroundCachePutInterceptor(ProceedingJoinPoint call) throws Throwable {
        CachePut cachePut = getAnnotation(call, CachePut.class);
        if (cachePut == null) return call.proceed();
        Object result;
        String key = cachePut.key();
        String condition = cachePut.condition();
        SpelParser spel = new SpelParser(call);
        int expire = cachePut.expire();
        String type = cachePut.type();
        Cache cache = cacheManager.getCache(type);
        result = call.proceed();
        Boolean conditionResult = spel.getValue(condition, Boolean.class, result);
        if (conditionResult && result != null) {
            key = spel.getValue(key, String.class, result);
            logger.info("cache.put:" + cache.getKey(key));
            cache.put(key, result, expire);
        }
        return result;
    }

    public Object aroundCacheableInterceptor(ProceedingJoinPoint call) throws Throwable {
        Cacheable cacheable = getAnnotation(call, Cacheable.class);
        if (cacheable == null) return call.proceed();
        Object result;
        String key = cacheable.key();
        String condition = cacheable.condition();
        SpelParser spel = new SpelParser(call);
        key = spel.getValue(key, String.class);
        int expire = cacheable.expire();
        String type = cacheable.type();
        Cache cache = cacheManager.getCache(type);
        result = cache.get(key, getReturnType(call));
        if (result == null) {
            result = call.proceed();
            Boolean conditionResult = spel.getValue(condition, Boolean.class, result);
            if (conditionResult && result != null) {
                logger.info("cache.put:" + cache.getKey(key));
                cache.put(key, result, expire);
            }
        } else {
            logger.info("use cache:" + cache.getKey(key));
        }
        return result;
    }

    /**
     * 获得Annotation对象
     *
     * @param jp
     * @param clazz
     * @param <T>
     * @return
     */
    private <T extends Annotation> T getAnnotation(ProceedingJoinPoint jp, Class<T> clazz) {
        MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
        Method method = joinPointObject.getMethod();
        return method.getAnnotation(clazz);
    }

    /**
     * 取方法返回类型
     *
     * @param jp
     * @return
     */
    private Class<?> getReturnType(ProceedingJoinPoint jp) {
        MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
        Method method = joinPointObject.getMethod();
        Class<?> returnType = method.getReturnType();
        return returnType;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheManager, "cacheManager 不能为空");
    }
}
