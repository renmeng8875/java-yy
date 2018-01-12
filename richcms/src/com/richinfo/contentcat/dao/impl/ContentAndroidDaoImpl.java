package com.richinfo.contentcat.dao.impl;


import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.contentcat.dao.ContentAndroidDao;
import com.richinfo.contentcat.entity.ContentAndroid;

@Repository("ContentAndroidDao")
public class ContentAndroidDaoImpl extends BaseDaoImpl<ContentAndroid, Integer>
		implements ContentAndroidDao {
	
}
