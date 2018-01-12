package com.richinfo.contentcat.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.contentcat.dao.ContentTagDao;
import com.richinfo.contentcat.entity.SysDataCat;
import com.richinfo.contentcat.service.ContentTagService;

@Service("ContentTagService")
public class ContentTagServiceImpl extends BaseServiceImpl<SysDataCat, Integer> implements ContentTagService{
	
	private ContentTagDao contentTagDao;

	@Autowired
	@Qualifier("ContentTagDao")
	public void setContentTagDao(ContentTagDao ContentTagDao) {
		this.contentTagDao = ContentTagDao;
	}

	@Autowired
	@Qualifier("ContentTagDao")
	@Override
	public void setBaseDao(BaseDao<SysDataCat, Integer> baseDao) {
		this.baseDao = (ContentTagDao)baseDao;
	}
	
	public Page<SysDataCat> listContentTag(){
		String hql = "from SysDataCat c order by c.catId";
		Page<SysDataCat> page= contentTagDao.find(hql, null, null);
		return page;
	}
}
