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
@Table(name = "mm_api_sys_privilege")
public class SysPrivilegeOfRole extends BaseEntity {

	/**
	 *  uthor:watson.zhang
	 * this program is role`s funcation
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "pId",nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer pId;
	private Integer funId;
	private Integer roleId;
	private String className;
	private String method;
	
	public Integer getpId() {
		return pId;
	}
	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public Integer getFunId() {
		return funId;
	}
	public void setFunId(Integer funId) {
		this.funId = funId;
	}

	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
