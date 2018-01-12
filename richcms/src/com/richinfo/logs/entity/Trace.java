package com.richinfo.logs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Table(name = "MM_SYS_LOG")
public class Trace extends BaseEntity
{
private static final long serialVersionUID = -3823559605138488866L;

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "logid", nullable = false)
	private Integer logId;
	
	@Column(name="userid")
	private Integer userId;
	
	@Column(name="userName")
	private String  userName;
	
	@Column(name="signature",length=500)
	private String signature;
	
	@Column(name="params",length=2000)
	private String params;
	
	@Column(name="mem",length=1000)
	private String mem;
	
	@Column(name="loginip")
	private String loginIP;
	
	@Column(name="ctime")
	private String ctime;
	

	public String getLoginIP() {
		return loginIP==null?loginIP:CommonUtil.longToIp(Long.parseLong(loginIP));
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}

}
