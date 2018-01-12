package com.richinfo.privilege.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "mm_sys_role",uniqueConstraints = {@UniqueConstraint(columnNames={"roleName"})})
@SequenceGenerator(name = "gen_mm_sys_role", sequenceName = "SEQ_MM_SYS_RULE")
public class Role extends BaseEntity{

	private static final long serialVersionUID = -1950437207866458251L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="gen_mm_sys_role")
    @Column(name = "roleid", nullable = false)
	private int roleid;
	
	@Column(name="rolename",nullable = false,length=50)
	private String roleName;
	
	@Column(name="mem",nullable = true,length=200)
	private String mem;
	
	@Column(name="priority",nullable = true,length=1)
	private Integer priority;
	
	@Column(name="ctime",nullable = true)
	private Long ctime;

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}

}
