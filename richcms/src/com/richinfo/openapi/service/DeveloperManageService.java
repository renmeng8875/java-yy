package com.richinfo.openapi.service;

import com.richinfo.common.service.BaseService;
import com.richinfo.openapi.entity.Account;

public interface DeveloperManageService extends BaseService<Account, Integer> 
{
	public boolean checkApperExist(String apperName);
}
