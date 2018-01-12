package com.richinfo.module.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.Tpl;

public interface TplService extends BaseService<Tpl,Integer>{
	public List<Tpl> listByStyleId(int styleId);
	
	public void deleteByStyleId(int styleId);
}
