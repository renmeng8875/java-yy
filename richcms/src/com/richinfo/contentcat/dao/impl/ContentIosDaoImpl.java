package com.richinfo.contentcat.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.contentcat.dao.ContentIosDao;
import com.richinfo.contentcat.entity.ContentIos;

@Repository("ContentIosDao")
public class ContentIosDaoImpl extends BaseDaoImpl<ContentIos, Integer> implements
		ContentIosDao {
	
}
