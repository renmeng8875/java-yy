package com.richinfo.contentcat.service;

import java.util.List;
import java.util.Map;

import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.BaseService;
import com.richinfo.contentcat.entity.ContentData;

public interface ContentDataService  extends BaseService<ContentData, Long> {

	public Page<ContentData> find(ContentData c);
	
	public void updatePriority(Long pkid,int priority);
	
	public String multImportApplication(Map<String,Object>paramMap);
	
	public List<Map<String,Object>> getCountByCatId(int catId);
	
	public void updateStatus(Long pkid,int status,String author,Long updateTime);
	
	public void deleteContentData(Long contentDataId);

	public String getDataTypeByCat(int catid);
	
}
