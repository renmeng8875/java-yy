package com.richinfo.privilege.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.privilege.dao.CatPrivilegeDao;
import com.richinfo.privilege.entity.CatPrivilege;

@Repository("CatPrivilegeDao")
public class CatPrivilegeDaoImpl extends BaseDaoImpl<CatPrivilege, Integer> implements CatPrivilegeDao{

}
