package com.richinfo.openapi.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.openapi.dao.RoleManageDao;
import com.richinfo.openapi.entity.AppRole;

@Repository("RoleManageDao")
public class RoleManageDaoImpl extends BaseDaoImpl<AppRole, Integer> implements RoleManageDao {

}
