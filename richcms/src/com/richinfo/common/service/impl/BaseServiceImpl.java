package com.richinfo.common.service.impl;

import java.util.List;

import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.BaseService;

public abstract class BaseServiceImpl <T extends java.io.Serializable, PK extends java.io.Serializable> implements BaseService<T, PK>  
{
	protected BaseDao<T, PK> baseDao;
    
    public abstract void setBaseDao(BaseDao<T, PK> baseDao);
    

    @RenmSelf(methodDesc="保存实体")
    public T save(T model) 
    {
        baseDao.add(model);
        return model;
    }
    
    @RenmSelf(methodDesc="比较并更新实体")
    public void merge(T model) 
    {
        baseDao.merge(model);
    }

    @RenmSelf(methodDesc="保存或更新实体")
    public void saveOrUpdate(T model) 
    {
        baseDao.saveOrUpdate(model);
    }

    @RenmSelf(methodDesc="更新实体")
    public void update(T model) {
        baseDao.update(model);
    }
    
    @RenmSelf(methodDesc="根椐主键删除实体")
    public void delete(PK id) 
    {
        baseDao.delete(id);
    }

    @RenmSelf(methodDesc="根椐实体模型删除实体")
    public void deleteObject(T model) 
    {
        baseDao.deleteObject(model);
    }

    public T get(PK id) 
    {
        return baseDao.get(id);
    }

    public int countAll() 
    {
        return baseDao.countAll();
    }

    public List<T> listAll()
    {
        return baseDao.listAll();
    }
   
    public boolean exists(PK id)
    {
    	return baseDao.exists(id);
    }
    
    
}
