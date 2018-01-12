package com.richinfo.contentcat.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.richinfo.common.Constants;
import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.contentcat.dao.ContentCatDao;
import com.richinfo.privilege.entity.Category;

@Repository("ContentCatDao")
public class ContentCatDaoImpl extends BaseDaoImpl<Category, Integer> implements
		ContentCatDao {
	
	@Override
	public List<String> getChannelContentCat() {
		JdbcTemplate jdbcTemplate=getJdbcTemplate();
		final String callProcedureSql = "{call PKG_MM_CONTENTCAT.proc_get_contentCat(?)}";
		List<String> resultList = (List<String>) jdbcTemplate.execute(  
			     new CallableStatementCreator() {  
			        public CallableStatement createCallableStatement(Connection con) throws SQLException {  
			           CallableStatement cs = con.prepareCall(callProcedureSql);  
			           cs.registerOutParameter(1,Constants.ORACLE_TYPES);
			           return cs;  
			        }  
			     }, new CallableStatementCallback<List<String>>() {  
			        public List<String> doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {  
			           List<String> resultsMap = new ArrayList<String>();  
			           cs.execute();  
			           ResultSet rs = (ResultSet) cs.getObject(1);
			           while (rs.next()){
			        	  StringBuffer sf=new StringBuffer();
			        	  sf.append(rs.getString("contentcats"));
			              resultsMap.add(sf.toString());  
			           }  
			           rs.close();  
			           return resultsMap;  
			        }  
			  });  
		return resultList;
	}
}
