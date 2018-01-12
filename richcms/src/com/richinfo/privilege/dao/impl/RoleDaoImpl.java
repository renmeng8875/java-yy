package com.richinfo.privilege.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.privilege.dao.RoleDao;
import com.richinfo.privilege.entity.Role;

@Repository("RoleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role, Integer> implements RoleDao{

}
