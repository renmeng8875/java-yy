package com.richinfo.contentcat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.contentcat.dao.ContentAndroidDao;
import com.richinfo.contentcat.entity.ContentAndroid;
import com.richinfo.contentcat.service.ContentAndroidService;

@Service("ContentAndroidService")
public class ContentAndroidServiceImpl extends BaseServiceImpl<ContentAndroid, Integer> implements ContentAndroidService {
	
	@Autowired
	@Qualifier("ContentAndroidDao")
	@Override
	public void setBaseDao(BaseDao<ContentAndroid, Integer> baseDao) {
		this.baseDao=(ContentAndroidDao)baseDao;
	}

}
