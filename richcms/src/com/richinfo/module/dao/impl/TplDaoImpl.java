package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.TplDao;
import com.richinfo.module.entity.Tpl;

@Repository("TplDao")
public class TplDaoImpl extends BaseDaoImpl<Tpl, Integer> implements TplDao {
	
}
