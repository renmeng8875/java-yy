package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.AnnounceDao;
import com.richinfo.module.entity.Announce;

@Repository("AnnounceDao")
public class AnnounceDaoImpl extends BaseDaoImpl<Announce, Integer> implements AnnounceDao {

}
