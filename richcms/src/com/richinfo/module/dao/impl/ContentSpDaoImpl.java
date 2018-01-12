package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.ContentSpDao;
import com.richinfo.module.entity.ContentSp;

@Repository("ContentSpDao")
public class ContentSpDaoImpl extends BaseDaoImpl<ContentSp, Integer> implements ContentSpDao
{

}
