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
@Table(name = "mm_cat_privilege", uniqueConstraints = {@UniqueConstraint(columnNames={"roleid", "catid"})})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@SequenceGenerator(name = "gen_mm_cat_privilege", sequenceName = "SEQ_MM_CAT_PRIV")
public class CatPrivilege extends BaseEntity {

	private static final long serialVersionUID = -3231294547565687617L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="gen_mm_cat_privilege")
    @Column(name = "pid", nullable = false)
	private int pid;
	
	@Column(name="roleid",nullable = false)
	private int roleId;
	
	@Column(name="catid",nullable = false)
	private int catId;
	
	public CatPrivilege(){}
	
	public CatPrivilege(int catid)
	{
		this.catId = catid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	@Override
	public String[] getExcludesAttributes() {
		
		return null;
	}
	

}
