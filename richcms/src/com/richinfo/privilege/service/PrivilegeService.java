package com.richinfo.privilege.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.Privilege;

public interface PrivilegeService extends BaseService<Privilege, Integer>
{
	public List<Integer>  getPrivilegeByRoleid(Integer roleid);
	
	public void deletePrivilegeByRoleid(Integer roleid);
	
	public boolean deletePrivilegeByMenuid(int menuId);
}
