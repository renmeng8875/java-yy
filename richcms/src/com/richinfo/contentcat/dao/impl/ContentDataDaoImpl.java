package com.richinfo.contentcat.dao.impl;



import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.contentcat.dao.ContentDataDao;
import com.richinfo.contentcat.entity.ContentData;

@Repository("ContentDataDao")
public class ContentDataDaoImpl extends BaseDaoImpl<ContentData, Long> implements
		ContentDataDao {
	
}
