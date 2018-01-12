package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.SearchDao;
import com.richinfo.module.entity.Search;

@Repository("SearchDao")
public class SearchDaoImpl extends BaseDaoImpl<Search, Integer> implements SearchDao {

}
