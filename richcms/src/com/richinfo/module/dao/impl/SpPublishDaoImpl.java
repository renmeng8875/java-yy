package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.SpPublishDao;
import com.richinfo.module.entity.SpPublish;

@Repository("SpPublishDao")
public class SpPublishDaoImpl extends BaseDaoImpl<SpPublish, Integer> implements SpPublishDao {

}
