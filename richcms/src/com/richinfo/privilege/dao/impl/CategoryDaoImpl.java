package com.richinfo.privilege.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.privilege.dao.CategoryDao;
import com.richinfo.privilege.entity.Category;

@Repository("CategoryDao")
public class CategoryDaoImpl extends BaseDaoImpl<Category, Integer> implements CategoryDao {

}
