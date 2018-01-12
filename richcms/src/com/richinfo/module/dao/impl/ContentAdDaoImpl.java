package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.ContentAdDao;
import com.richinfo.module.entity.ContentAd;

@Repository("ContentAdDao")
public class ContentAdDaoImpl extends BaseDaoImpl<ContentAd, Integer> implements ContentAdDao {

}
