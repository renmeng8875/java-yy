package com.richinfo.logs.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.logs.dao.TraceDao;
import com.richinfo.logs.entity.Trace;

@Repository("TraceDao")
public class TraceDaoImpl extends BaseDaoImpl<Trace, Integer> implements TraceDao {

}
