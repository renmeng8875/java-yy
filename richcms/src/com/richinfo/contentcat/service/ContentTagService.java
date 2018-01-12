package com.richinfo.contentcat.service;


import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.BaseService;
import com.richinfo.contentcat.entity.SysDataCat;

public interface ContentTagService extends BaseService<SysDataCat, Integer> {
	
	public Page<SysDataCat> listContentTag();
}
