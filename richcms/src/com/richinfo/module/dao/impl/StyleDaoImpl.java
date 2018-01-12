package com.richinfo.module.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.module.dao.StyleDao;
import com.richinfo.module.entity.Style;

@Repository("StyleDao")
public class StyleDaoImpl extends BaseDaoImpl<Style, Integer> implements StyleDao {

}
