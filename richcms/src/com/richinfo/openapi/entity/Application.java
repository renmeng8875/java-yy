package com.richinfo.openapi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_API_APP")
public class Application extends BaseEntity
{

	private static final long serialVersionUID = 1599578350355497530L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "appid", nullable = false)
	private Integer appId;
	
	/**
	 * 开发者ID
	 */
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="apperid") 
	private Account account;
	
	private String appName;
	
	private String appKey;
	
	private Integer appStatus;
	
	private Long times;
	
	private Long utime;
	
	@Transient
	private String utimeStr;

	private Long ctime;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="roleid") 
	private AppRole role;

	public AppRole getRole()
	{
		return role;
	}

	public void setRole(AppRole role) 
	{
		this.role = role;
	}



	public Integer getAppId() {
		return appId;
	}



	public void setAppId(Integer appId) {
		this.appId = appId;
	}



	public String getAppName() {
		return appName;
	}



	public void setAppName(String appName) {
		this.appName = appName;
	}



	public String getAppKey() {
		return appKey;
	}



	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}



	public Integer getAppStatus() {
		return appStatus;
	}



	public void setAppStatus(Integer appStatus) {
		this.appStatus = appStatus;
	}



	public Long getTimes() {
		return times;
	}



	public void setTimes(Long times) {
		this.times = times;
	}



	public Long getUtime() {
		return utime;
	}



	public void setUtime(Long utime) {
		this.utime = utime;
	}



	public Long getCtime() {
		return ctime;
	}



	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}


	public String getUtimeStr() {
		this.utimeStr = DateTimeUtil.getTimeStamp(ctime, "yyyy-MM-dd HH:mm:ss");
		return utimeStr;
	}

	public void setUtimeStr(String utimeStr) {
		this.utimeStr = utimeStr;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}

}
