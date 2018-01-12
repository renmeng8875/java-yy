package com.richinfo.privilege.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.privilege.dao.SysConfigDao;
import com.richinfo.privilege.entity.SysConfig;

@Repository("SysConfigDao")
public class SysConfigDaoImpl extends BaseDaoImpl<SysConfig, Integer> implements SysConfigDao {

}
