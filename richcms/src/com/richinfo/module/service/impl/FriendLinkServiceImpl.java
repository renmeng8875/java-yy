package com.richinfo.module.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.FriendLinkDao;
import com.richinfo.module.entity.FriendLink;
import com.richinfo.module.service.FriendLinkService;

@Service("FriendLinkService")
public class FriendLinkServiceImpl extends BaseServiceImpl<FriendLink, Integer> implements FriendLinkService{

	private FriendLinkDao friendLinkDao;

	@Autowired
    @Qualifier("FriendLinkDao")
	public void setFriendLinkDao(FriendLinkDao friendLinkDao) 
	{
		this.friendLinkDao = friendLinkDao;
	}

	@Autowired
    @Qualifier("FriendLinkDao")
	@Override
	public void setBaseDao(BaseDao<FriendLink, Integer> baseDao) 
	{
		this.baseDao = (FriendLinkDao)baseDao;
	}

	public boolean nameExists(String name,Integer linkid) 
	{
		String hql = "from FriendLink f where f.site=? and f.linkid!=?";
		List<FriendLink> list = friendLinkDao.list(hql, new Object[]{name,linkid}, null);
		if(list!=null&&list.size()>0)
		{
			return true;
		}
		return false;
	}

}
