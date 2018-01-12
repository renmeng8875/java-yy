package com.richinfo.datasource.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.datasource.dao.ContentChoiceDao;
import com.richinfo.datasource.entity.ContentChoice;

@Repository("ContentChoiceDao")
public class ContentChoiceDaoImpl extends BaseDaoImpl<ContentChoice, Integer> implements ContentChoiceDao {

}
