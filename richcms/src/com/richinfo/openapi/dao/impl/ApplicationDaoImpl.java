package com.richinfo.openapi.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.openapi.dao.ApplicationDao;
import com.richinfo.openapi.entity.Application;

@Repository("ApplicationDao")
public class ApplicationDaoImpl extends BaseDaoImpl<Application, Integer> implements ApplicationDao
{

}
