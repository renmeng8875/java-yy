package com.richinfo.openapi.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.openapi.entity.SysPrivilegeOfRole;

public interface SysPrivilegeOfRoleService extends BaseService<SysPrivilegeOfRole, Integer> {
	public List<SysPrivilegeOfRole> listByRoleId(Integer roleId);
	public Object queryObject(String hql, Object[] args); 
	public int deleteByFunId(Integer funId);
	public int deleteByRoleId(Integer roleId);
	public boolean canDeleteFun(Integer funId);
}
