package com.richinfo.openapi.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.openapi.dao.DeveloperManageDao;
import com.richinfo.openapi.entity.Account;

@Repository("DeveloperManageDao")
public class DeveloperManageDaoImpl extends BaseDaoImpl<Account, Integer> implements DeveloperManageDao {

}
