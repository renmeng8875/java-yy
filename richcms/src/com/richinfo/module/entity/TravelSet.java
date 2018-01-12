package com.richinfo.module.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_TRAVEL_SET",uniqueConstraints = {@UniqueConstraint(columnNames={"travelname","tagtype"})})
public  class TravelSet extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TRAVELID", nullable = false)
	private int travelid;
	
	@Column(name="TRAVELNAME",length=50)
	private String travelname;
	
	@Column(name="TAGTYPE",length=50)
	private String tagtype;
	
	@Column(name="CATNAME",length=50)
	private String catname;
	
	@Column(name="CAT",length=20)
	private String cat;
     
	@Column(name="CATID",nullable = true)
	private int catid;

	@Column(name="IMG",length=200)
	private String img;
	
	@Column(name="CTIME",nullable = false)
	private Long ctime;
	
	@Transient
	private String ctimeStr;
	
	
	public String getCtimeStr() {
		this.ctimeStr = DateTimeUtil.getTimeStamp(ctime, "yyyy-MM-dd HH:mm");
		return ctimeStr;
	}

	public void setCtimeStr(String ctimeStr) {
		this.ctimeStr = ctimeStr;
	}

	@Column(name="TRAVEL",length=50)
	private String travel;

	public int getTravelid() {
		return travelid;
	}

	public void setTravelid(int travelid) {
		this.travelid = travelid;
	}

	public String getTravelname() {
		return travelname;
	}

	public void setTravelname(String travelname) {
		this.travelname = travelname;
	}

	public String getTagtype() {
		return tagtype;
	}

	public void setTagtype(String tagtype) {
		this.tagtype = tagtype;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public int getCatid() {
		return catid;
	}

	public void setCatid(int catid) {
		this.catid = catid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	public String getTravel() {
		return travel;
	}

	public void setTravel(String travel) {
		this.travel = travel;
	}

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
 
	
	
	
}
