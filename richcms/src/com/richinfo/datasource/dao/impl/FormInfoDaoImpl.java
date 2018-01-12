package com.richinfo.datasource.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.datasource.dao.FormInfoDao;
import com.richinfo.datasource.entity.FormInfo;

@Repository("FormInfoDao")
public class FormInfoDaoImpl extends BaseDaoImpl<FormInfo, Integer> implements FormInfoDao {

}
