package com.richinfo.module.service;


import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.ContentSp;

public interface ContentSpService extends BaseService<ContentSp,Integer>{
	
	public void updatePriority(int companyId, int priority);
	
	public Page<ContentSp> find(ContentSp c);
	
	public  byte[] hexStr2ByteArr(String strIn);
	
	public  byte[] parseHexStr2Byte(String hexStr);
}
