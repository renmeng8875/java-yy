package com.richinfo.module.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.ContentAdDao;
import com.richinfo.module.entity.ContentAd;
import com.richinfo.module.service.ContentAdService;

@Service("ContentAdService")
public class ContentAdServiceImpl extends BaseServiceImpl<ContentAd, Integer> implements ContentAdService{

	@SuppressWarnings("unused")
	private ContentAdDao contentAdDao;

	
	@Autowired
    @Qualifier("ContentAdDao")
	public void setContentAdDao(ContentAdDao contentAdDao) 
	{
		this.contentAdDao = contentAdDao;
	}

	@Autowired
    @Qualifier("ContentAdDao")
	@Override
	public void setBaseDao(BaseDao<ContentAd, Integer> baseDao) 
	{
		this.baseDao = (ContentAdDao)baseDao;
	}

 

}
