package com.richinfo.datasource.dao;



import java.util.List;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.datasource.entity.DataSource;

public interface DataSourceDao extends BaseDao<DataSource, Integer> {

	public List<?> fetchPage(final String sqlCountRows, final String sqlFetchRows,   final Object args[], final int pageNo, final int pageSize);
	

}
