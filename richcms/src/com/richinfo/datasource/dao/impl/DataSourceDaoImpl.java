package com.richinfo.datasource.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.datasource.dao.DataSourceDao;
import com.richinfo.datasource.entity.DataSource;

@Repository("DataSourceDao")
public class DataSourceDaoImpl extends BaseDaoImpl<DataSource, Integer> implements DataSourceDao {

	public List<?> fetchPage(String sqlCountRows,
			String sqlFetchRows, Object[] args, int pageNo, final int pageSize) {
		// TODO Auto-generated method stub
		  // determine how many rows are available  
        final int rowCount = this.getJdbcTemplate().queryForInt(sqlCountRows, args);  
        // calculate the number of pages  
        int pageCount = rowCount / pageSize;  
        if (rowCount > pageSize * pageCount) {  
            pageCount++;  
        }  
        StringBuffer sqlQuery = new StringBuffer();
        sqlQuery.append("select * from (select temp.*,rownum rownumber from (")
                         .append(sqlFetchRows)
                         .append("");
        // objects 参数数组Object[]
       //总记录数
       int totalRows = this.getJdbcTemplate().queryForInt(sqlCountRows, args);  
       //开始索引位置
       int startIndex = (pageNo - 1)*pageSize;
       //最后一条记录的索引位置
       int lastIndex = 0;
       //总页数
       int totalPages = 0;
       if (totalRows % pageSize == 0) {
        totalPages = totalRows / pageSize;
       } else {
        totalPages = (totalRows / pageSize) + 1;
       }
      
       if (totalRows < pageSize){
        lastIndex = totalRows;
       } else if ((pageNo == totalPages && totalRows % pageSize == 0) || pageNo < totalPages) {
        lastIndex = pageNo*pageSize;
       } else if ((pageNo == totalPages && totalRows % pageSize != 0)) {
        lastIndex = totalRows;
       }
       sqlQuery.append(") temp where ROWNUM <= ")
              .append(lastIndex)
              .append(" ) where rownumber > ")
              .append(startIndex);
       List<?> list = this.getJdbcTemplate().queryForList(sqlQuery.toString(), args);
       return list;
	}

}
