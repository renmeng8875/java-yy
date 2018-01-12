package com.richinfo.contentcat.service;

import java.util.List;
import java.util.Map;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.Category;

public interface ContentCatService extends BaseService<Category, Integer> {
	
	public int queryCategoryLevelByCategoryId(int catId);

	public void updateHidden(int isHidden, int catId);

	public void updatePriority(int pkid, int priority);
	
	public Category getRootCategory();
	
	public List<Category> getChildCategory(int pid);
	
	public String getCatNameByCatId(int catId);
	
	public List<Map<String,Object>> getChildren(int pid);
	
	public List<Map<String,Object>> getChildrenByRole(int pid,int roleId);
	
	public int getChildrenCount(int pid);
	
	public List<Map<String,Object>> getRoot();
	
	public String updateChildId();
	
	public void exportChannelContentCat();
	
}
