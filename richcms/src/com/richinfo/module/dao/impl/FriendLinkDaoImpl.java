package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.FriendLinkDao;
import com.richinfo.module.entity.FriendLink;

@Repository("FriendLinkDao")
public class FriendLinkDaoImpl extends BaseDaoImpl<FriendLink, Integer> implements FriendLinkDao {

}
