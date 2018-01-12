package com.richinfo.common.service;

import java.util.List;

public interface BaseService <T extends java.io.Serializable, PK extends java.io.Serializable>
{
	public T save(T model);

    public void saveOrUpdate(T model);
    
    public void update(T model);
    
    public void merge(T model);

    public void delete(PK id);

    public void deleteObject(T model);

    public T get(PK id);
    
    public int countAll();
    
    public List<T> listAll();
    
    public boolean exists(PK id);
    
	
	
	
	
	
	
	
	
	
	
	
}
