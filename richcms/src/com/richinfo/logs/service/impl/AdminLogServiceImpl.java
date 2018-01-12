package com.richinfo.logs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.contentcat.entity.ContentData;
import com.richinfo.logs.dao.TraceDao;
import com.richinfo.logs.entity.Trace;
import com.richinfo.logs.service.AdminLogService;

 
@Service("AdminLogService")
public class AdminLogServiceImpl extends BaseServiceImpl<Trace, Integer> implements AdminLogService
{
	@SuppressWarnings("unused")
	private TraceDao traceDao;

	@Autowired
    @Qualifier("TraceDao")
	public void setTraceDao(TraceDao traceDao) 
	{
		this.traceDao = traceDao;
	}

	@Autowired
    @Qualifier("TraceDao")
	@Override
	public void setBaseDao(BaseDao<Trace, Integer> baseDao) 
	{
		this.baseDao = (TraceDao)baseDao;
	}

	@Override
	public Page<Trace> find(Trace c) {
		// TODO Auto-generated method stub
		List<Object> paramList=null;
		String hql = "from Trace c Where 1=1 ";
		paramList=new ArrayList<Object>();
		 
		Page<Trace> page= traceDao.find(hql, paramList.toArray(), null);
		paramList=null;
		return page;
	}

	@Override
	@RenmSelf(methodDesc="批量删除日志")
	public void batchDelete(String[] ids) {
		String sql = "delete from MM_SYS_LOG where LOGID in";
		traceDao.batchDeleteBySql(sql, ids);
	}
  

}
