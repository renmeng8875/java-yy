package com.richinfo.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.contentcat.dao.ContentDataDao;
import com.richinfo.privilege.dao.CategoryDao;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.service.CategoryService;

@Service("CategoryService")
public class CategoryServiceImpl extends BaseServiceImpl<Category,Integer> implements CategoryService{

	private CategoryDao categoryDao;
	private ContentDataDao contentDataDao;
	
	@Autowired
    @Qualifier("CategoryDao")
	public void setCategoryDao(CategoryDao categoryDao) 
	{
		this.categoryDao = categoryDao;
	}
	
	@Autowired
    @Qualifier("ContentDataDao")
	public void setContentDataDao(ContentDataDao contentDataDao) 
	{
		this.contentDataDao = contentDataDao;
	}


	@Autowired
    @Qualifier("CategoryDao")
	@Override
	public void setBaseDao(BaseDao<Category, Integer> baseDao) 
	{
		this.baseDao = (CategoryDao)baseDao;
	}



	public Category getRootCategory() 
	{
		String hql = "from Category c where c.parent.catId is null and c.catLevel<=2";
		return (Category)categoryDao.queryObject(hql, null, null);
	}
	
	public void delCategory(int catId){
		categoryDao.delete(catId);
		contentDataDao.updateBySql("delete from MM_CONTENT_DATA c WHERE c.catId=?", new Object[]{catId});
	}
}
