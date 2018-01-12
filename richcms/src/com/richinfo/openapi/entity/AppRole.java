package com.richinfo.openapi.entity;

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
@Table(name = "mm_api_role")
public class AppRole extends BaseEntity{

	/**
	 * uthor:watson.zhang
	 * this program is manage system for function
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "roleid", nullable = false)
	private Integer roleId;
	private String roleName;
	@Column(name = "mem", nullable = true)
	private String roleMem;
	
	public void setRoleId(Integer roleId)
	{
		this.roleId = roleId;
	}
	public Integer getRoleId()
	{
		return this.roleId;
	}
	
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
	public String getRoleName()
	{
		return this.roleName;
	}
	
	public void setRoleMem(String roleMem)
	{
		this.roleMem = roleMem;
	}
	public String getRoleMem()
	{
		return this.roleMem;
	}
	
	
	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
