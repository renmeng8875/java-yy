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
@Table(name = "MM_SYS_STYLE")
public class Style extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
 
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "styleid", nullable = false)
	private int styleid;
	
	@Column(name="style",length=50)
	private String style;
	
	@Column(name="stylename",length=50)
	private String stylename;
	
	@Column(name="author",length=50)
	private String author;
	
	@Column(name="version",length=50)
	private String version;
     
	@Column(name="path",length=300)
	private String path;

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStyleid() {
		return styleid;
	}

	public void setStyleid(int styleid) {
		this.styleid = styleid;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStylename() {
		return stylename;
	}

	public void setStylename(String stylename) {
		this.stylename = stylename;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

 
	 
   
 
	 
	 
	
}
