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
@Table(name = "mm_sys_privilege",uniqueConstraints = {@UniqueConstraint(columnNames={"roleid", "menuid"})})
@SequenceGenerator(name = "gen_mm_sys_privilege", sequenceName = "SEQ_MM_SYS_PRIV")
public class Privilege extends BaseEntity{

	
	private static final long serialVersionUID = 4961425196639446861L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="gen_mm_sys_privilege")
    @Column(name = "pid", nullable = false)
	private int pid;
	
	@Column(name = "roleid", nullable = false)
	private int roleId;
	
	@Column(name = "menuid", nullable = false)
	private int menuId;
	
	@Column(name = "module", length=50)
	private String module;
	
	@Column(name = "control",length=50)
	private String control;
	
	@Column(name = "action",length=50)
	private String action;

	public Privilege(){
		
	}
	
	public Privilege(int menuid){
		this.menuId = menuid;
	}
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}

}
