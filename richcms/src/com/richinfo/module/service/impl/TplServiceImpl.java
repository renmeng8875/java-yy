package com.richinfo.module.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.TplDao;
import com.richinfo.module.entity.Tpl;
import com.richinfo.module.service.TplService;

@Service("TplService")
public class TplServiceImpl extends BaseServiceImpl<Tpl, Integer> implements TplService{

	@Autowired
    @Qualifier("TplDao")
	@Override
	public void setBaseDao(BaseDao<Tpl, Integer> baseDao) 
	{
		this.baseDao = (TplDao)baseDao;
	}
	
	public List<Tpl> listByStyleId(int styleId) {
		return baseDao.list("from Tpl t where t.styleId = ? ",new Object[]{styleId}, null);
	}

	public void deleteByStyleId(int styleId) 
	{
		String hql = "delete Tpl t where t.styleId=?";
		baseDao.updateByHql(hql, new Object[]{styleId});
	}
}
