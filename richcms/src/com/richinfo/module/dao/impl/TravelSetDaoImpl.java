package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.TravelSetDao;
import com.richinfo.module.entity.TravelSet;

@Repository("TravelSetDao")
public class TravelSetDaoImpl extends BaseDaoImpl<TravelSet, Integer> implements TravelSetDao {

}
