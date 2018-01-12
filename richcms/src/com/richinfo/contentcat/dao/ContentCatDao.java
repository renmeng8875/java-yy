package com.richinfo.contentcat.dao;

import java.util.List;
import java.util.Map;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.privilege.entity.Category;

public interface ContentCatDao  extends BaseDao<Category, Integer> {

	public List<String>getChannelContentCat();
}
