package com.richinfo.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.openapi.dao.ApplicationDao;
import com.richinfo.openapi.entity.Application;
import com.richinfo.openapi.service.ApplicationService;

@Service("ApplicationService")
public class ApplicationServiceImpl extends BaseServiceImpl<Application, Integer> implements ApplicationService
{

	
	@Autowired
    @Qualifier("ApplicationDao")
	@Override
	public void setBaseDao(BaseDao<Application, Integer> baseDao) 
	{
		this.baseDao = (ApplicationDao)baseDao;
	}
	
	public boolean isApperInApp(int apperId)
	{
		List<Application> list=baseDao.list("from Application a where a.account.apperId=?", new Object[]{apperId}, null);
		return (list!=null&&list.size()>0)?true:false;
	}
	
	public boolean isRoleInApp(int roleId)
	{
		List<Application> list=baseDao.list("from Application a where a.role.roleId=?", new Object[]{roleId}, null);
		return (list!=null&&list.size()>0)?true:false;
	}
	
}
