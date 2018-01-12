package com.richinfo.module.service;


import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.FriendLink;

public interface FriendLinkService extends BaseService<FriendLink,Integer>{

	public boolean nameExists(String name,Integer linkid);
}
