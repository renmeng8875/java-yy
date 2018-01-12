package com.richinfo.module.entity;


import java.sql.Clob;

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
@Table(name = "MM_CONTENT_AD")
public class ContentAd extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dataid", nullable = false)
	private int dataid;
	
	@Column(name="title",length=50)
	private String title;
	
	@Column(name="keyword",length=50)
	private String keyword;
	
	@Column(name="content")
	private Clob content;
	
	@Column(name="author",length=50)
	private String author;
     
	@Column(name="images",length=500)
	private String images;

 
	
	@Column(name="infourl",length=200)
	private String infourl;
	
	@Column(name="starttime",nullable = false)
	private Long starttime;

	@Column(name="endtime",nullable = false)
	private Long endtime;
	
	@Column(name="linkurl",length=200)
	private String linkurl;
	
	@Column(name="utime",nullable = false)
	private Long utime;
	
	@Column(name="price",length=500)
	private String price;
	
	@Column(name="logo",length=500)
	private String logo;
	
	@Column(name="p_logo",length=500)
	private String p_logo;
	
	@Column(name="editorlanguage",length=500)
	private String editorlanguage;
	
	@Column(name="priority",nullable = false,columnDefinition="default 99")
	private Long priority;
	
	@Column(name="imgtype",nullable = false)
	private int imgtype;

	public int getDataid() {
		return dataid;
	}

	public void setDataid(int dataid) {
		this.dataid = dataid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Clob getContent() {
		return content;
	}

	public void setContent(Clob content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
 

	public String getInfourl() {
		return infourl;
	}

	public void setInfourl(String infourl) {
		this.infourl = infourl;
	}

	public Long getStarttime() {
		return starttime;
	}

	public void setStarttime(Long starttime) {
		this.starttime = starttime;
	}

	public Long getEndtime() {
		return endtime;
	}

	public void setEndtime(Long endtime) {
		this.endtime = endtime;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public Long getUtime() {
		return utime;
	}

	public void setUtime(Long utime) {
		this.utime = utime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getP_logo() {
		return p_logo;
	}

	public void setP_logo(String p_logo) {
		this.p_logo = p_logo;
	}

	public String getEditorlanguage() {
		return editorlanguage;
	}

	public void setEditorlanguage(String editorlanguage) {
		this.editorlanguage = editorlanguage;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public int getImgtype() {
		return imgtype;
	}

	public void setImgtype(int imgtype) {
		this.imgtype = imgtype;
	}

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
