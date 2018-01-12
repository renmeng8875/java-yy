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
@Table(name = "MM_SYS_FRIENDLINK")
public class FriendLink extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
 
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "linkid", nullable = false)
	private int linkid;
	
	@Column(name="site",length=50)
	private String site;
	
	@Column(name="linkurl",length=200)
	private String linkurl;
	
	@Column(name="logo",length=200)
	private String logo;
	
	@Column(name="contact",length=50)
	private String contact;
     
	@Column(name="mem",length=500)
	private String mem;
	
	@Column(name="priority")
	private int priority;

	public int getLinkid() {
		return linkid;
	}

	public void setLinkid(int linkid) {
		this.linkid = linkid;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	 
 
	 
   
 
	 
	 
	
}
