package com.richinfo.openapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "mm_api_fun")
public class FunManage extends BaseEntity {

	/**
	 * uthor:watson.zhang
	 * this program is manage system for function
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "funid", nullable = false)
	private Integer funId;
	private String className;
	private String method;
	private long ctime;
	private String mem;
	
	public void setFunId(Integer funId)
	{
		this.funId = funId;
	}
	public Integer getFunId()
	{
		return this.funId;
	}
	
	public void setClassName(String className)
	{
		this.className = className;
	}
	public String getClassName()
	{
		return this.className;
	}	
	
	public void setMethod(String method)
	{
		this.method = method;
	}
	public String getMethod()
	{
		return this.method;
	}	
	
	public void setCtime(Date date)
	{
		this.ctime = DateTimeUtil.getTimeStamp(date);
	}
	public String getCtime()
	{
		return DateTimeUtil.getTimeStamp(this.ctime, "yyyy-MM-dd HH:mm:ss");
	}	
	
	public void setMem(String mem)
	{
		this.mem = mem;
	}
	public String getMem()
	{
		return this.mem;
	}
	
	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
