package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.WorkflowUserDao;
import com.richinfo.module.entity.WorkflowUser;

@Repository("WorkflowUserDao")
public class WorkflowUserDaoImpl extends BaseDaoImpl<WorkflowUser, Integer> implements WorkflowUserDao {

}
