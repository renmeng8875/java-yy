package com.richinfo.openapi.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.openapi.dao.RoleManageDao;
import com.richinfo.openapi.entity.AppRole;
import com.richinfo.openapi.service.RoleManageService;

@Service("RoleManageService")
public class RoleManageServiceImpl extends BaseServiceImpl<AppRole, Integer> implements
		RoleManageService 
{

	@SuppressWarnings("unused")
	@Autowired
	@Qualifier("RoleManageDao")
	private RoleManageDao roleManageDao;
	
	@Autowired
	@Override
	@Qualifier("RoleManageDao")
	public void setBaseDao(BaseDao<AppRole, Integer> baseDao) {
		// TODO Auto-generated method stub
		this.baseDao = (RoleManageDao)baseDao;
	}


}
