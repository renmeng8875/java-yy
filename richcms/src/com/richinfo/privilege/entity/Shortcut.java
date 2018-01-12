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
@Table(name = "mm_sys_shortcuts",uniqueConstraints = {@UniqueConstraint(columnNames={"USERID","MENUID"})})
@SequenceGenerator(name = "gen_mm_sys_shortcuts", sequenceName = "SEQ_MM_SYS_SHORTCUTS")
public class Shortcut extends BaseEntity
{
	private static final long serialVersionUID = -1077639085745000288L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="gen_mm_sys_shortcuts")
    @Column(name = "scid", nullable = false)
	private int scid;
	
	@Column(name="userid")
	private int userId;
	
	@Column(name="menuid")
	private int menuId;
	
	@Column(name="orderby",length=3)
	private int orderby;
	
	@Column(name="topid")
	private int topId;

	public Shortcut(){}
	
	public Shortcut(int menuid)
	{
		this.menuId = menuid;
	}
	
	public int getScid() {
		return scid;
	}

	public void setScid(int scid) {
		this.scid = scid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

	public int getTopId() {
		return topId;
	}

	public void setTopId(int topId) {
		this.topId = topId;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
	
	
	
	
	
	
	

}
