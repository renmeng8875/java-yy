package com.richinfo.logs.service;


import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.BaseService;
import com.richinfo.logs.entity.Trace;

public interface AdminLogService  extends BaseService<Trace,Integer>
{

	public Page<Trace> find(Trace c);
	
	public void batchDelete(String[] ids);
}
