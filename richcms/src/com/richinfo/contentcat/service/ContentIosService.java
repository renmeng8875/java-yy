package com.richinfo.contentcat.service;

import com.richinfo.common.service.BaseService;
import com.richinfo.contentcat.entity.ContentIos;

public interface ContentIosService extends BaseService<ContentIos, Integer> {
	public ContentIos getContentIosByAppid(String contentid);
	
	public void deleteRationIos(int appid);
}
