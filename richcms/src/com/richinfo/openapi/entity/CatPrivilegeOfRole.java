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
@Table(name="mm_api_cat_privilege")
public class CatPrivilegeOfRole extends BaseEntity {

	/**
	 * author:watson.zhang
	 * this program is cat privilege entity for open api
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "pId",nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer pId;
	private Integer roleId;
	private Integer cateId;
	
	

	public Integer getpId() {
		return pId;
	}
	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	public Integer getCateId() {
		return cateId;
	}
	public void setCateId(Integer cateId) {
		this.cateId = cateId;
	}

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
