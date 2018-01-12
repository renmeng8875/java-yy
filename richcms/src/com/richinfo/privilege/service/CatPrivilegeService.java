package com.richinfo.privilege.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.CatPrivilege;

public interface CatPrivilegeService extends BaseService<CatPrivilege, Integer>
{

	public List<Integer> getCatPrivilegeByRoleid(int roleid);
	
	public void deleteCatPrivilegeByRoleid(int roleid);
	
	
}
