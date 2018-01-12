package com.richinfo.privilege.service;


import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.SysConfig;

public interface SysConfigService extends BaseService<SysConfig,Integer>{

	public SysConfig getValue(String type,String key);
	
	/**
	 * 
	 * 取系统参数值
	 * 
	 * @param type 
	 * @param key
	 * @param jsonKey
	 * @return String
	 */
	public String getSysParamValue(String type,String key,String jsonKey);
	
	
	public boolean nameExists(String name,Integer pkid);
}
