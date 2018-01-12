package com.richinfo.contentcat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.contentcat.dao.ContentIosDao;
import com.richinfo.contentcat.entity.ContentIos;
import com.richinfo.contentcat.service.ContentIosService;

@Service("ContentIosService")
public class ContentIosServiceImpl extends BaseServiceImpl<ContentIos, Integer> implements ContentIosService {

	@Autowired
	@Qualifier("ContentIosDao")
	@Override
	public void setBaseDao(BaseDao<ContentIos, Integer> baseDao) 
	{
		this.baseDao=(ContentIosDao)baseDao;
	}
	
	public ContentIos getContentIosByAppid(String contentid)
	{
		String hql = "from ContentIos s where s.appId=?";
		List<ContentIos> list = this.baseDao.list(hql, new Object[]{contentid}, null);
		if(list!=null&&list.size()>0)
		{
			return list.get(0);
		}
		return null;	
	}

	public void deleteRationIos(int appid) {
		this.baseDao.delete(appid);
		String sql = "delete from mm_content_data t where t.contentid=?";
		this.baseDao.updateBySql(sql, new Object[]{appid});
		
	}
}
