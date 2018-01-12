package com.richinfo.module.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.TravelSetDao;
import com.richinfo.module.entity.TravelSet;
import com.richinfo.module.service.TravelSetService;

@Service("TravelSetService")
public class TravelSetServiceImpl extends BaseServiceImpl<TravelSet, Integer> implements TravelSetService{

	@Autowired
    @Qualifier("TravelSetDao")
	@Override
	public void setBaseDao(BaseDao<TravelSet, Integer> baseDao) 
	{
		this.baseDao = (TravelSetDao)baseDao;
	}

	public boolean checkTravelset(String travelsetName)
	{
		String hql = "from TravelSet t where t.travelname=?";
		List<TravelSet> list = this.baseDao.list(hql, new String[]{travelsetName}, null);
		if(list!=null&&list.size()>0)
		{
			return true;
		}	
		return false;
	}

	
 

}
