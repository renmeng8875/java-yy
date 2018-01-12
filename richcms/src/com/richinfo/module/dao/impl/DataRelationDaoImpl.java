package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.DataRelationDao;
import com.richinfo.module.entity.DataRelation;

@Repository("DataRelationDao")
public class DataRelationDaoImpl extends BaseDaoImpl<DataRelation, Integer> implements DataRelationDao {

}
