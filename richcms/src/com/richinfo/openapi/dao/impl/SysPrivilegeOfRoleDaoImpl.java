package com.richinfo.openapi.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.openapi.dao.SysPrivilegeOfRoleDao;
import com.richinfo.openapi.entity.SysPrivilegeOfRole;

@Repository("SysPrivilegeOfRoleDao")
public class SysPrivilegeOfRoleDaoImpl extends BaseDaoImpl<SysPrivilegeOfRole, Integer> implements
		SysPrivilegeOfRoleDao {
}
