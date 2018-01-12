package com.richinfo.serviceImpl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.Test;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import com.richinfo.AbstractTestCase;

public class EhcacheManagerTest extends AbstractTestCase
{
	@Test
	public void ehcacheTest()
	{
		EhCacheCacheManager chcManager = (EhCacheCacheManager)this.getBean("ehcacheManager");
		CacheManager cacheM= (CacheManager) chcManager.getCacheManager();
		Cache cache = cacheM.getCache("jsonCache");
		cache.put(new Element("ren", "mengssssssssss"));
		Element ele = cache.get("ren");
		System.err.println(ele.getObjectValue().toString());
		
	}
}
