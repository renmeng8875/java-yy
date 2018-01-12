package com.richinfo.datasource.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.datasource.dao.ContentChoiceDao;
import com.richinfo.datasource.entity.ContentChoice;
import com.richinfo.datasource.service.ContentChoiceService;

@Service("ContentChoiceService")
public class ContentChoiceServiceImpl extends BaseServiceImpl<ContentChoice, Integer>
		implements ContentChoiceService {

	@SuppressWarnings("unused")
	private ContentChoiceDao contentChoiceDao;
 
	@Autowired
	@Qualifier("ContentChoiceDao")
	public void setContentChoiceDao(ContentChoiceDao contentChoiceDao) {
		this.contentChoiceDao = contentChoiceDao;
	}
	@Autowired
	@Qualifier("ContentChoiceDao")
	@Override
	public void setBaseDao(BaseDao<ContentChoice, Integer> baseDao) {
		this.baseDao = (ContentChoiceDao) baseDao;
	}
	 

 
}
