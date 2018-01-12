package com.richinfo.privilege.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SYS_CONFIG")
@SequenceGenerator(name = "GEN_MM_SYS_CONFIG", sequenceName = "SEQ_MM_SYS_CONFIG")
public class SysConfig extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GEN_MM_SYS_CONFIG")
    @Column(name = "SID", nullable = false)
	private int sid;
	
	@Column(name="SYSTYPE",length=50)
	private String systype;
	
	@Column(name="SYSKEY",length=50)
	private String syskey;
	
	@Column(name="SYSVALUE",length=1000)
	private String sysvalue;
	
	@Column(name="MEM",length=200)
	private String mem;
     
	@Column(name="CTIME",nullable = true)
	private Long ctime;
	
	@Transient
	private String ctimeStr;

	public int getSid() {
		return sid;
	}

	public String getCtimeStr() {
		this.ctimeStr = DateTimeUtil.getTimeStamp(ctime, "yyyy-MM-dd",false);
		return ctimeStr;
	}

	public void setCtimeStr(String ctimeStr) {
		this.ctimeStr = ctimeStr;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSystype() {
		return systype;
	}

	public void setSystype(String systype) {
		this.systype = systype;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

	public String getSysvalue() {
		return sysvalue;
	}

	public void setSysvalue(String sysvalue) {
		this.sysvalue = sysvalue;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
	
	
	
	
	
	
	
	
}
