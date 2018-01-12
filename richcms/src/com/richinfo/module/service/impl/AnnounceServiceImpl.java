package com.richinfo.module.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.AnnounceDao;
import com.richinfo.module.entity.Announce;
import com.richinfo.module.service.AnnounceService;

@Service("AnnounceService")
public class AnnounceServiceImpl extends BaseServiceImpl<Announce, Integer> implements AnnounceService{

	private AnnounceDao announceDao;

	
	@Autowired
    @Qualifier("AnnounceDao")
	public void setAnnounceDao(AnnounceDao announceDao) 
	{
		this.announceDao = announceDao;
	}

	@Autowired
    @Qualifier("AnnounceDao")
	@Override
	public void setBaseDao(BaseDao<Announce, Integer> baseDao) 
	{
		this.baseDao = (AnnounceDao)baseDao;
	}

	public void updatePriority(int announceid, int priority) 
	{
		String hql="update Announce a set a.priority=? where a.announceid=?";
		announceDao.updateByHql(hql, new Object[]{priority,announceid});
	}

	public List<Announce> listByParam(String announce) {
		String hql="from Announce a where a.announce=?"; 
		return announceDao.list(hql, new Object[]{announce}, null);
	}

    

}
