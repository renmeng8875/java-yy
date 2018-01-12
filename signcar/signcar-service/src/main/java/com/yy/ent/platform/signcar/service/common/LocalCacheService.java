package com.yy.ent.platform.signcar.service.common;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class LocalCacheService {
    private CacheManager manager;
    
    public LocalCacheService(String configPath) throws CacheException, IOException 
    {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(configPath);
        manager = CacheManager.create(resource.getInputStream());
    }
    
    
    public void destory() throws Exception
    {
        if(manager!=null){
            manager.shutdown();
        }
    }
    
    public void add(String cacheName,Object key,Object value) 
    {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }
    
    public boolean remove(String cacheName,Object key)
    {
        Cache cache = manager.getCache(cacheName);
        return cache.remove(key);
    }
    
    public Object get(String cacheName,Object key){
        Cache cache = manager.getCache(cacheName);
        Element ele = cache.get(key); 
        if(ele!=null){
            return ele.getObjectValue();
        }
        return null;
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Collection get(String cacheName,Collection keys,Collection collection){
        Cache cache = manager.getCache(cacheName);
        Map<Object, Element>  map = cache.getAll(keys);
        for(Object key:map.keySet())
        {
            if(map.containsKey(key))
            {
                collection.add(map.get(key).getObjectValue());
            }
            
        }
        return collection;
    }
    
    public void removeAll(String cacheName,Collection<?> keys){
        Cache cache = manager.getCache(cacheName);
        cache.removeAll(keys); 
    }
    
    
    public void removeAll(String cacheName){
        Cache cache = manager.getCache(cacheName);
        cache.removeAll(); 
    }
    
    
    
}
