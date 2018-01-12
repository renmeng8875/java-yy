package com.richinfo.openapi.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.openapi.dao.FunManageDao;
import com.richinfo.openapi.entity.FunManage;

@Repository("FunManageDao")
public class FunManageDaoImpl extends BaseDaoImpl<FunManage, Integer> implements FunManageDao{

}
