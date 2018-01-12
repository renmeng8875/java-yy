package com.richinfo.contentcat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Table(name = "MM_SYS_DATA_CAT",uniqueConstraints = {@UniqueConstraint(columnNames={"dataType","appType"})})
@SequenceGenerator(name = "SYS_DATA_CAT_SEQ", sequenceName = "SEQ_MM_SYS_DATA_CAT")
public class SysDataCat extends BaseEntity {

	private static final long serialVersionUID = -305896631129573080L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SYS_DATA_CAT_SEQ")
	@Column(name="CATID",nullable=false)
	private Integer catId;
	
	@Column(name="DATATYPE",nullable=false,length=50)
	private String dataType;
	
	@Column(name="APPTYPE",nullable=false,length=500)
	private String appType;
	
	@Column(name="JUMPURL",length=300)
	private String jumpUrl;
	
	@Column(name="UTIME")
	private Long updateTime;
	
	@Column(name="PCATID",length=100)
	private String pcatId;
	
	@Column(name="priority")
	private int priority ;
	
	@Transient
	private String pcatName;
	
	
	@Transient
	private String updateTimeStr;
	
	public Integer getCatId() {
		return catId;
	}

	public String getUpdateTimeStr() {
		this.updateTimeStr = DateTimeUtil.getTimeStamp(updateTime, "yyyy-MM-dd");
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getPcatId() {
		return pcatId;
	}

	public void setPcatId(String pcatId) {
		this.pcatId = pcatId;
	}
	
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String[] getExcludesAttributes() {
		return null;
	}
	
	public String getPcatName() {
		return pcatName;
	}

	public void setPcatName(String pcatName) {
		this.pcatName = pcatName;
	}
	
}
