package com.richinfo.privilege.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.privilege.dao.UserDao;
import com.richinfo.privilege.entity.User;
@Repository("UserDao")
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao{
	
}
