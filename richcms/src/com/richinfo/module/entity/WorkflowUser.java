package com.richinfo.module.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SYS_WORKFLOW_USER")
public class WorkflowUser extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
 
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "auditid", nullable = false)
	private int auditid;
	
	@Column(name = "flowid", nullable = false)
	private int flowid;
	 
	@Column(name = "userid")
	private String userid;
	
	@Column(name="mem",length=200)
	private String mem;
	
	@Column(name="flevel", nullable = false)
	private int flevel;
	
	@Column(name="ctime")
	private Long ctime;
	
	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getAuditid() {
		return auditid;
	}

	public void setAuditid(int auditid) {
		this.auditid = auditid;
	}

	public int getFlowid() {
		return flowid;
	}

	public void setFlowid(int flowid) {
		this.flowid = flowid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

	public int getFlevel() {
		return flevel;
	}

	public void setFlevel(int flevel) {
		this.flevel = flevel;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}
	
	
}
