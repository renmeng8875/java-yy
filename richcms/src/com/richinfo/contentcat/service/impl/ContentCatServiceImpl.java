package com.richinfo.contentcat.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.richinfo.common.SystemProperties;
import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.contentcat.dao.ContentCatDao;
import com.richinfo.contentcat.service.ContentCatService;
import com.richinfo.privilege.entity.Category;

@Service("ContentCatService")
public class ContentCatServiceImpl extends BaseServiceImpl<Category, Integer> implements ContentCatService {

	private ContentCatDao contentCatDao;

	@Autowired
	@Qualifier("ContentCatDao")
	public void setContentCatDao(ContentCatDao contentCatDao) {
		this.contentCatDao = contentCatDao;
	}

	@Autowired
	@Qualifier("ContentCatDao")
	@Override
	public void setBaseDao(BaseDao<Category, Integer> baseDao) {
		this.baseDao=(ContentCatDao)baseDao;
	}
	
	@RenmSelf(methodDesc="根据栏目id查找栏目")
	public int queryCategoryLevelByCategoryId(int catId) {
		Category c = (Category) contentCatDao.queryObject(
				"select new Category(catLevel) from Category where catId=?",
				new Object[] { catId }, null);
		return c!=null?c.getCatLevel():-1;
	}
	
	@RenmSelf(methodDesc="更改隐藏属性")
	public void updateHidden(int isHidden,int catId)
	{
		String hql = "update Category c set c.isHidden=? where c.catId=?";
		contentCatDao.updateByHql(hql, new Object[]{isHidden,catId});
	}
	
	@RenmSelf(methodDesc="更改栏目的排序")
	public void updatePriority(int pkid,int priority)
	{
		String hql = "update Category c set c.priority=? where c.catId=?";
		contentCatDao.updateByHql(hql, new Object[]{priority,pkid});
	}

	public Category getRootCategory() 
	{
		String hql = "from Category c where c.parent.catId is null";
		return (Category)contentCatDao.queryObject(hql, null, null);
	}
	
	public List<Category> getChildCategory(int pid){
		String hql = "from Category c where c.parent.catId=? order by priority";
		List<Category> list= contentCatDao.list(hql, new Object[]{pid}, null);
		return list;
	}
	
	public String getCatNameByCatId(int catId){
		Object c = contentCatDao.queryObject(
				"select c.name from Category c where catId=?",
				new Object[] { catId }, null);
		return c!=null?(String)c:"";
	}
	
	public List<Map<String,Object>> getChildren(int pid){
		String sql = "select c.isHidden,c.cat,c.catname,c.pid,c.catId,c.catLevel,c.catId as pk from MM_CONTENT_CAT c where c.pid =? order by c.priority";
		return contentCatDao.getJdbcTemplate().queryForList(sql,pid);
	}
	
	public List<Map<String,Object>> getChildrenByRole(int pid,int roleId){
		String sql = "select c.isHidden,c.cat,c.catname,c.pid,c.catId,c.catLevel from MM_CONTENT_CAT c right join MM_CAT_PRIVILEGE p on p.catid = c.catid where c.pid =? and p.roleid=? and p.catid is not null";
		return contentCatDao.getJdbcTemplate().queryForList(sql,pid,roleId);
	}
	
	public int getChildrenCount(int pid){
		String sql = "select count(c.catId)countNum from MM_CONTENT_CAT c where c.pid =?";
		return contentCatDao.getJdbcTemplate().queryForInt(sql,pid);
	}
	
	public List<Map<String,Object>> getRoot(){
		String sql = "select c.isHidden,c.cat,c.catname,c.pid,c.catId,c.catLevel,c.catId as pk from MM_CONTENT_CAT c where c.pid is null";
		return contentCatDao.getJdbcTemplate().queryForList(sql);
	}

	public String updateChildId() {
		JdbcTemplate jdbcTemplate=contentCatDao.getJdbcTemplate();
		final String callProcedureSql = "{call PROC_RICHCMS_UPDATE_CHILDID(?)}";
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("retCode", Types.INTEGER));
        
        Map<String, Object> outValues = jdbcTemplate.call(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                CallableStatement cstmt = conn.prepareCall(callProcedureSql);
                cstmt.registerOutParameter(1, Types.INTEGER);
                return cstmt;
            }
        }, params);
        
		return String.valueOf(outValues.get("retCode"));
	}
	
	//0/30 * *  * * ? 
	@Scheduled(cron="0 30 17 ? * FRI")
	public void exportChannelContentCat(){
		String path=SystemProperties.getInstance().getProperty("contentMgt.fileDir");
		String filePattern=SystemProperties.getInstance().getProperty("contentMgt.filePattern");
		filePattern=filePattern.replace("{yyyyMMdd}", DateTimeUtil.getNowDateTime("yyyyMMdd"));
		List<String>resultList=contentCatDao.getChannelContentCat();
		if(resultList!=null&&resultList.size()>0){
			try {
				FileUtils.writeLines(new File(path+File.separator+filePattern), resultList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
