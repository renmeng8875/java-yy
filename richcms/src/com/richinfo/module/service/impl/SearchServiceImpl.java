package com.richinfo.module.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.SearchDao;
import com.richinfo.module.entity.Search;
import com.richinfo.module.service.SearchService;

@Service("SearchService")
public class SearchServiceImpl extends BaseServiceImpl<Search, Integer> implements SearchService{

	@SuppressWarnings("unused")
	private SearchDao searchDao;

	@Autowired
    @Qualifier("SearchDao")
	public void setStyleDao(SearchDao searchDao) 
	{
		this.searchDao = searchDao;
	}

	@Autowired
    @Qualifier("SearchDao")
	@Override
	public void setBaseDao(BaseDao<Search, Integer> baseDao) 
	{
		this.baseDao = (SearchDao)baseDao;
	}
  
	
 

}
