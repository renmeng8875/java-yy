package com.richinfo.privilege.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.privilege.dao.MenuDao;
import com.richinfo.privilege.entity.Menu;

@Repository("MenuDao")
public class MenuDaoImpl extends BaseDaoImpl<Menu, Integer> implements MenuDao{

}
