package com.richinfo.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.LockOptions;
import org.hibernate.type.Type;
import org.springframework.jdbc.core.JdbcTemplate;

import com.richinfo.common.pagination.Page;

public interface BaseDao<T extends Serializable, PK extends Serializable> 
{
	public void add(T t);

	public void update(T t);

	public void delete(PK id);
	
	public void deleteObject(T model);

	public void merge(T model) ;

	public void saveOrUpdate(T model);
	
	public T load(PK id);
	
	public T loadWithLock(PK id, LockOptions option);

	public T get(PK id);
	
	public T getWithLock(PK id, LockOptions option);
	
	public List<T> list(String hql, Object[] args, Map<String, Object> alias);
	
	public <N extends Object>List<N> listBySql(String sql, Object[] args,Map<String, Object> alias, Class<?> clz, boolean hasEntity) ;
	
	public List<Object[]>  listBySql(String sql,Object[] args,Map<String,Type> scalarMap);
	
	public Object queryObject(String hql, Object[] args,Map<String, Object> alias);
	
	public int updateByHql(String hql, Object[] args);

	public int updateBySql(String sql,Object[] args);
	
	public Long getCountByHql(String hql ,Object[] args,Map<String,Object> alias);
	
	public Long getCountBySql(String sql ,Object[] args,Map<String,Object> alias);
	
	public Page<T> find(String hql, Object[] args, Map<String, Object> alias);

	public <N extends Object>Page<N> findBySql(String sql, Object[] args,Map<String, Object> alias, Class<?> clz, boolean hasEntity);
		
	public void flush();

    public void clear();
    
    public int countAll();
    
    public List<T> listAll();
	
	public boolean exists(PK id);

	public JdbcTemplate getJdbcTemplate();
	
	public void batchDeleteBySql(String sql, Object[] ids);
	
	
	
}
