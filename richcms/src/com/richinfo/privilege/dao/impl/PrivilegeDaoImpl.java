package com.richinfo.privilege.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.privilege.dao.PrivilegeDao;
import com.richinfo.privilege.entity.Privilege;

@Repository("PrivilegeDao")
public class PrivilegeDaoImpl extends BaseDaoImpl<Privilege, Integer> implements PrivilegeDao {

}
