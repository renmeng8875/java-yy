package com.richinfo.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.SpPublishDao;
import com.richinfo.module.entity.SpPublish;
import com.richinfo.module.service.SpPublishService;

@Service("SpPublishService")
public class SpPublishServiceImpl extends BaseServiceImpl<SpPublish, Integer> implements SpPublishService {

	private SpPublishDao spPublishDao;

	@Autowired
    @Qualifier("SpPublishDao")
	public void setStyleDao(SpPublishDao spPublishDao) 
	{
		this.spPublishDao = spPublishDao;
	}

	@Autowired
    @Qualifier("SpPublishDao")
	@Override
	public void setBaseDao(BaseDao<SpPublish, Integer> baseDao) 
	{
		this.baseDao = (SpPublishDao)baseDao;
	}

	public int getSpPublishCountByCompanyId(int companyId) {
		String hql="select count(s.pubId) from SpPublish s where s.companyId=?";
		Long count=spPublishDao.getCountByHql(hql, new Object[]{companyId}, null);
		return count!=null?count.intValue():0;
	}
}
