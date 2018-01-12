package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.WorkflowDao;
import com.richinfo.module.entity.Workflow;

@Repository("WorkflowDao")
public class WorkflowDaoImpl extends BaseDaoImpl<Workflow, Integer> implements WorkflowDao {

}
