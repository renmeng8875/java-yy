package com.richinfo.module.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.StyleDao;
import com.richinfo.module.entity.Style;
import com.richinfo.module.service.StyleService;

@Service("StyleService")
public class StyleServiceImpl extends BaseServiceImpl<Style, Integer> implements StyleService{

	private StyleDao styleDao;

	
	@Autowired
    @Qualifier("StyleDao")
	public void setStyleDao(StyleDao styleDao) 
	{
		this.styleDao = styleDao;
	}

	@Autowired
    @Qualifier("StyleDao")
	@Override
	public void setBaseDao(BaseDao<Style, Integer> baseDao) 
	{
		this.baseDao = (StyleDao)baseDao;
	}

	public List<Style> listByParam(String key, String value, int styleid) 
	{
		return styleDao.list("from Style t where t."+key+" = ? and t.styleid !=?",new Object[]{value,styleid}, null);
	}

 

}
