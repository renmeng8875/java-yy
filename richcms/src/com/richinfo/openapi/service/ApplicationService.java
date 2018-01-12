package com.richinfo.openapi.service;

import com.richinfo.common.service.BaseService;
import com.richinfo.openapi.entity.Application;

public interface ApplicationService extends BaseService<Application, Integer>
{
	public boolean isApperInApp(int apperId);
	
	public boolean isRoleInApp(int roleId);
}
