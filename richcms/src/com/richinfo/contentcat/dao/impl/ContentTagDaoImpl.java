package com.richinfo.contentcat.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.contentcat.dao.ContentTagDao;
import com.richinfo.contentcat.entity.SysDataCat;

@Repository("ContentTagDao")
public class ContentTagDaoImpl  extends BaseDaoImpl<SysDataCat, Integer> implements
ContentTagDao {

}
