package com.richinfo.openapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_API_ACCOUNT",uniqueConstraints = {@UniqueConstraint(columnNames={"apper"})})
public class Account extends BaseEntity {

	/**
	 * author:watson.zhang
	 * this program is manage system for open api
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "apperid", nullable = false)
	private Integer apperId;
	private String apper;
	private String passwd;
	private String tel;
	private String email;
	private String uname;
	private Integer status;
	private long ctime;
	
	public void setApperId(Integer apperId)
	{
		this.apperId = apperId;
	}
	public Integer getApperId()
	{
		return this.apperId;
	}
	
	public void setApper(String apper)
	{
		this.apper = apper;
	}
	public String getApper()
	{
		return this.apper;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}
	public String getTel()
	{
		return this.tel;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getEmail()
	{
		return this.email;
	}

	public void setUname(String uname)
	{
		this.uname = uname;
	}
	public String getUname()
	{
		return this.uname;
	}
	
	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}
	public String getPasswd()
	{
		return this.passwd;
	}
	
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getStatus()
	{
		return this.status;
	}
	
	public void setCtime(Date date)
	{
		this.ctime = DateTimeUtil.getTimeStamp(date);
	}
	public String getCtime()
	{
		return DateTimeUtil.getTimeStamp(this.ctime, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
