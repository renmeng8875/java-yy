package com.richinfo.privilege.service;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.Category;

public interface CategoryService extends BaseService<Category,Integer>
{
	public Category getRootCategory();
	
	public void delCategory(int catId);
	
}
