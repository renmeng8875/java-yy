package com.richinfo.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.richinfo.common.SystemContext;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.pagination.Page;

public class BaseDaoImpl<T extends Serializable, PK extends Serializable>  implements BaseDao<T, PK> {
	
	@Autowired
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private final Class<T> entityClass;
	
	private final String HQL_LIST_ALL;
	
	private final String HQL_COUNT_ALL;
	
	private String pkName = null;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl(){
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Field[] fields = this.entityClass.getDeclaredFields();
        for(Field f : fields) 
        {
            if(f.isAnnotationPresent(Id.class)) {
                this.pkName = f.getName();
            }
        }
        
        HQL_COUNT_ALL = " select count(*) from " + this.entityClass.getSimpleName();
        String subfix = "";
        if(StringUtils.isNotEmpty(pkName))
        {
        	subfix = " order by " + pkName + " desc";
        }
        
        HQL_LIST_ALL = "from " + this.entityClass.getSimpleName() + subfix;
	}
	
	
	public JdbcTemplate getJdbcTemplate()
	{
		return this.jdbcTemplate;
	}
	
	
	protected Session getSession() 
	{
		return sessionFactory.getCurrentSession();
	}

	public void add(T t) 
	{
		getSession().save(t);
		flush();
	}

	
	public void update(T t) 
	{
		getSession().update(t);
		flush();
	}

	public void delete(PK id) 
	{
		getSession().delete(this.load(id));
		flush();
	}

	public void deleteObject(T model) {
        getSession().delete(model);
        flush();
    }
	
	public void merge(T model) {
        getSession().merge(model);
        flush();
    }

	
	public void saveOrUpdate(T model) {
        getSession().saveOrUpdate(model);
        flush();
    }
	
	@SuppressWarnings("unchecked")
	public T load(PK id) 
	{
		return (T)getSession().load(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
	public T loadWithLock(PK id, LockOptions option)
	{
		return (T)getSession().load(entityClass, id,option);
	}

	@SuppressWarnings("unchecked")
	public T get(PK id){
		return (T)getSession().get(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
	public T getWithLock(PK id, LockOptions option){
		return (T)getSession().get(entityClass, id,option);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> list(String hql, Object[] args, Map<String, Object> alias) 
	{
		hql = initSort(hql);
		Query query = getSession().createQuery(hql);
		query.setCacheable(true);
		setAliasParameter(query, alias);
		setParameter(query, args);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public <N extends Object>List<N> listBySql(String sql, Object[] args,Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		sql = initSort(sql);
		SQLQuery sq = getSession().createSQLQuery(sql);
		sq.setCacheable(true);
		setAliasParameter(sq, alias);
		setParameter(sq, args);
		if(hasEntity) {
			sq.addEntity(clz);
		} else 
			sq.setResultTransformer(Transformers.aliasToBean(clz));
		List<N> resultList = sq.list();
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]>  listBySql(String sql,Object[] args,Map<String,Type> scalarMap)
	{
		sql = initSort(sql);
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setCacheable(true);
		if(scalarMap!=null&&scalarMap.size()>0)
		{
			for(String key:scalarMap.keySet())
			{
				query.addScalar(key,scalarMap.get(key));
			}
		}
		setParameter(query, args);
		List<Object[]> list = query.list();
		return list;
	}
	
	public Object queryObject(String hql, Object[] args,Map<String, Object> alias) 
	{
		Query query = getSession().createQuery(hql);
		query.setCacheable(true);
		setAliasParameter(query, alias);
		setParameter(query, args);
		return query.uniqueResult();
	}

	
	public int updateByHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		setParameter(query, args);
		Object result = query.executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
	}


	public int updateBySql(String sql,Object[] args)
	{
		Query query = getSession().createSQLQuery(sql);
		setParameter(query, args);
		Object result = query.executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
	}
	
	public Long getCountByHql(String hql ,Object[] args,Map<String,Object> alias)
	{
		long result =0;
		Query cquery = getSession().createQuery(hql);
		setAliasParameter(cquery, alias);
		setParameter(cquery, args);
		result = (Long)cquery.uniqueResult();
		return result;
	}
	
	public Long getCountBySql(String sql ,Object[] args,Map<String,Object> alias)
	{
		long result =0;
		Query cquery = getSession().createSQLQuery(sql);
		setAliasParameter(cquery, alias);
		setParameter(cquery, args);
		result = (Long)cquery.uniqueResult();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Page<T> find(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql);
		String cq = getCountHql(hql,true);
		Query cquery = getSession().createQuery(cq);
		Query query = getSession().createQuery(hql);
		//设置别名参数
		setAliasParameter(query, alias);
		setAliasParameter(cquery, alias);
		//设置参数
		setParameter(query, args);
		setParameter(cquery, args);
		Page<T> pages = new Page<T>();
		setPagers(query,pages);
		List<T> datas = query.list();
		pages.setItems(datas);
		long total = (Long)cquery.uniqueResult();
		pages.setTotal(total);
		return pages;
	}



	
	@SuppressWarnings("unchecked")
	public <N extends Object>Page<N> findBySql(String sql, Object[] args,Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		sql = initSort(sql);
		String cq = getCountHql(sql,false);
		SQLQuery sq = getSession().createSQLQuery(sql);
		SQLQuery cquery = getSession().createSQLQuery(cq);
		setAliasParameter(sq, alias);
		setAliasParameter(cquery, alias);
		setParameter(sq, args);
		setParameter(cquery, args);
		Page<N> pages = new Page<N>();
		setPagers(sq, pages);
		if(hasEntity) {
			sq.addEntity(clz);
		} else {
			sq.setResultTransformer(Transformers.aliasToBean(clz));
		}
		List<N> datas = sq.list();
		pages.setItems(datas);
		long total = ((BigInteger)cquery.uniqueResult()).longValue();
		pages.setTotal(total);
		return pages;
	}
	
	@SuppressWarnings("unchecked")
	public Page<Map<String,Object>> findBySql(String sql, Object[] args,Map<String, Object> alias) {
		sql = initSort(sql);
		String cq = getCountHql(sql,false);
		SQLQuery sq = getSession().createSQLQuery(sql);
		SQLQuery cquery = getSession().createSQLQuery(cq);
		setAliasParameter(sq, alias);
		setAliasParameter(cquery, alias);
		setParameter(sq, args);
		setParameter(cquery, args);
		Page<Map<String,Object>> pages = new Page<Map<String,Object>>();
		setPagers(sq, pages);
		sq.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List<Map<String,Object>> datas = sq.list();
		pages.setItems(datas);
		BigDecimal b=null;
		if(sql!=null&&sql.toLowerCase().indexOf("group")!=-1){
			pages.setTotal(cquery.list().size());
		}else{
			b=(BigDecimal)cquery.uniqueResult();
			pages.setTotal(b.longValue());
		}
		return pages;
	}
	

	public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }
	
	
	private String initSort(String hql) {
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order)) 
		{
			hql+=" order by "+sort;
			if(!"desc".equals(order))
			{	
				hql+=" asc";
			}	
			else
			{	
				hql+=" desc";
			}	
		}
		return hql;
	}
	
	@SuppressWarnings("unchecked")
	private void setAliasParameter(Query query,Map<String,Object> alias) {
		if(alias!=null) {
			Set<String> keys = alias.keySet();
			for(String key:keys) {
				Object val = alias.get(key);
				if(val instanceof Collection) {
					//查询条件是列表
					query.setParameterList(key, (Collection)val);
				} else {
					query.setParameter(key, val);
				}
			}
		}
	}
	
	private void setParameter(Query query,Object[] args) {
		if(args!=null&&args.length>0) {
			int index = 0;
			for(Object arg:args) {
				query.setParameter(index++, arg);
			}
		}
	}
	
	/**
	* <p><b>说明</b>:设置分页参数信息   </p>  
	* @param query
	* @param pages =>
	* @return void
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-2-21
	 */
	private void setPagers(Query query,Page<?> pages) {
		Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageOffset==null||pageOffset<0) pageOffset = 0;
		if(pageSize==null||pageSize<0) pageSize = 15;
		pages.setOffset(pageOffset);
		pages.setSize(pageSize);
		query.setFirstResult(pageOffset).setMaxResults(pageSize);
	}
	

	
	private String getCountHql(String hql,boolean isHql) 
	{
		String e = hql.substring(hql.indexOf("from"));
		String c = "select count(*) "+e;
		if(isHql)
			c = c.replaceAll("fetch", "");
		return c;
	}

	public int countAll() {
        Query query = getSession().createQuery(HQL_COUNT_ALL);
        Long total = (Long)query.uniqueResult();
        return total.intValue();
    }


    public List<T> listAll() 
    {
       return this.list(HQL_LIST_ALL, null, null);
    }

    
    public boolean exists(PK id)
    {
    	return get(id) != null;
    }

    /**
     * 通过id批量删除
     * @param sql = delete from tableName where coloumnName in
     * @param args 需要被删除的id数组
     * @author songtinglong
     * 
     */
	public void batchDeleteBySql(String sql, Object[] args) {
		if (sql == null || args.length <= 0){
			return;
		}
		StringBuffer sqlBuf = new StringBuffer(sql + "(");
		for (Object id : args){
			sqlBuf.append(id + ",");
		}
		sqlBuf.deleteCharAt(sqlBuf.length() - 1);
		sqlBuf.append(")");
		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
		query.executeUpdate();
	}
}  
