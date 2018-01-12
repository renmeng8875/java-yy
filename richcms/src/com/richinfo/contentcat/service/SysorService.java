package com.richinfo.contentcat.service;


import com.richinfo.common.service.BaseService;
import com.richinfo.contentcat.entity.Sysor;

public interface SysorService extends BaseService<Sysor, Integer> {
 
	public long getTodayDelNum(int userid);
	
}
