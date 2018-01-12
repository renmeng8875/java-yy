package com.richinfo.module.service;


import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.Style;

public interface StyleService extends BaseService<Style,Integer>{

	public List<Style> listByParam(String key,String value,int styleid);
}
