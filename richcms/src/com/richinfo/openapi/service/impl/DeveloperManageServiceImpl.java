package com.richinfo.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.openapi.dao.DeveloperManageDao;
import com.richinfo.openapi.entity.Account;
import com.richinfo.openapi.service.DeveloperManageService;


@Service("DeveloperManageService")
public class DeveloperManageServiceImpl extends BaseServiceImpl<Account, Integer> implements DeveloperManageService {

	@Autowired
    @Qualifier("DeveloperManageDao")
	@Override
	public void setBaseDao(BaseDao<Account, Integer> baseDao) {
		
		this.baseDao = (DeveloperManageDao)baseDao; 
	}

	public boolean checkApperExist(String apperName) {
		String hql = "from Account a where a.apper=?";
		List<Account> list = this.baseDao.list(hql, new Object[]{apperName}, null);
		if(list!=null&&list.size()>0)
		{
			return true;
		}	
		return false;
	}

}
