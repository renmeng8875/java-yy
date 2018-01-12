package com.richinfo.privilege.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SITE")
@SequenceGenerator(name = "GEN_MM_SITE", sequenceName = "SEQ_MM_SITE")
public class Site extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GEN_MM_SITE")
    @Column(name = "SID", nullable = false)
	private int sid;
	
	@Column(name="SITE",length=50)
	private String site;
	
	@Column(name="DOMAIN",length=50)
	private String domain;
	
	@Column(name="HTMLDIRECTORY",length=200)
	private String htmldirectory;
	
	@Column(name="STYLEDIRECTORY",length=200)
	private String styledirectory;
     
	@Column(name="CURRENTSTYLE",nullable = true)
	private Long currentstyle;

	@Column(name="SEOTITLE",length=50)
	private String seotitle;
	
	@Column(name="SEOKEYWORD",length=200)
	private String seokeyword;
	
	 
	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getHtmldirectory() {
		return htmldirectory;
	}

	public void setHtmldirectory(String htmldirectory) {
		this.htmldirectory = htmldirectory;
	}

	public String getStyledirectory() {
		return styledirectory;
	}

	public void setStyledirectory(String styledirectory) {
		this.styledirectory = styledirectory;
	}

	public Long getCurrentstyle() {
		return currentstyle;
	}

	public void setCurrentstyle(Long currentstyle) {
		this.currentstyle = currentstyle;
	}

	public String getSeotitle() {
		return seotitle;
	}

	public void setSeotitle(String seotitle) {
		this.seotitle = seotitle;
	}

	public String getSeokeyword() {
		return seokeyword;
	}

	public void setSeokeyword(String seokeyword) {
		this.seokeyword = seokeyword;
	}

	@Override
	public String[] getExcludesAttributes() {
		return null;
	}
	
	
}
