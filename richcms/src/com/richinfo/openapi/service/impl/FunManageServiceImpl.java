package com.richinfo.openapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.openapi.dao.FunManageDao;
import com.richinfo.openapi.entity.FunManage;
import com.richinfo.openapi.service.FunManageService;

@Service("FunManageService")
public class FunManageServiceImpl extends BaseServiceImpl<FunManage, Integer> implements
		FunManageService {

	@SuppressWarnings("unused")
	@Autowired
	@Qualifier(value = "FunManageDao")
	private FunManageDao funManageDao;

	@Autowired
	@Qualifier(value = "FunManageDao")
	@Override
	public void setBaseDao(BaseDao<FunManage, Integer> baseDao) {
		// TODO Auto-generated method stub
		this.baseDao = (FunManageDao)baseDao;
	}
}
