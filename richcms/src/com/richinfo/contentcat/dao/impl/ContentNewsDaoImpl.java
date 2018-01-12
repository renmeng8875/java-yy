package com.richinfo.contentcat.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.contentcat.dao.ContentNewsDao;
import com.richinfo.contentcat.entity.ContentNews;

@Repository("ContentNewsDao")
public class ContentNewsDaoImpl extends BaseDaoImpl<ContentNews, Integer> implements
		ContentNewsDao {
	
}
