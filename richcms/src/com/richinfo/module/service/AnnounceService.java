package com.richinfo.module.service;


import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.Announce;

public interface AnnounceService extends BaseService<Announce,Integer>
{
	public void updatePriority(int announceid,int priority);
	
	public List<Announce> listByParam(String announce);
}
