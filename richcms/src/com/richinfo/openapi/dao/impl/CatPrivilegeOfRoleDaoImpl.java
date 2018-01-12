package com.richinfo.openapi.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.openapi.dao.CatPrivilegeOfRoleDao;
import com.richinfo.openapi.entity.CatPrivilegeOfRole;

@Repository("CatPrivilegeOfRoleDao")
public class CatPrivilegeOfRoleDaoImpl extends BaseDaoImpl<CatPrivilegeOfRole, Integer> implements
		CatPrivilegeOfRoleDao {

}
