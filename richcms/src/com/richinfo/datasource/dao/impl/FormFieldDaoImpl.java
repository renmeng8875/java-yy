package com.richinfo.datasource.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.datasource.dao.FormFieldDao;
import com.richinfo.datasource.entity.FormField;

@Repository("FormFieldDao")
public class FormFieldDaoImpl extends BaseDaoImpl<FormField, Integer> implements FormFieldDao {

}
