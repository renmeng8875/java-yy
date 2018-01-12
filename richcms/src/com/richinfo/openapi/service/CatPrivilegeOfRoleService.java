package com.richinfo.openapi.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.openapi.entity.CatPrivilegeOfRole;

public interface CatPrivilegeOfRoleService extends BaseService<CatPrivilegeOfRole, Integer> {


	public List<Integer> getCatPrivilegeByRoleid(int roleid);

	public void deleteCatPrivilegeByRoleid(int roleid);
}
