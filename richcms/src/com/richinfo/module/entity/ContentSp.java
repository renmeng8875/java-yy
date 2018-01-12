package com.richinfo.module.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_CONTENT_SP")
public class ContentSp extends BaseEntity
{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "companyid", nullable = false)
	private Integer companyId;
	
	@Column(name="spName",length=300)
	private String spName;
	
	@Column(name="title",length=300)
	private String title;
	
	@Column(name="logs",length=4000)
	private String logs;
	
	@Column(name="image",length=4000)
	private String image;
	
	@Column(name="introduce",length=4000)
	private String introduce;
	
	@Column(name="seoKeyword",length=4000)
	private String seoKeyword;
	
	@Column(name="seoDesc",length=4000)
	private String seoDesc;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="reason",length=4000)
	private String reason;
	
	@Column(name="tplId")
	private Integer tplId;
	
	@Column(name="confPath",length=200)
	private String confPath;
	
	@Column(name="preconfPath",length=200)
	private String preconfPath;
	
	@Column(name="pubTime")
	private Long pubTime;
	
	@Transient
	private String pubTimeStr;
	
	private Long submitTime;
	
	@Transient
	private String submitTimeStr;
	
	@Transient
	private Long publishBeginTime;
	
	@Transient
	private Long publishEndTime;
	
	@Transient
	private Long submitBeginTime;
	
	@Transient
	private Long submitEndTime;
	
	@Transient
	private Integer have;
	
	@Transient
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getHave() {
		return have;
	}

	public void setHave(Integer have) {
		this.have = have;
	}

	public Long getPublishBeginTime() {
		return publishBeginTime;
	}

	public void setPublishBeginTime(Long publishBeginTime) {
		this.publishBeginTime = publishBeginTime;
	}

	public Long getPublishEndTime() {
		return publishEndTime;
	}

	public void setPublishEndTime(Long publishEndTime) {
		this.publishEndTime = publishEndTime;
	}

	public Long getSubmitBeginTime() {
		return submitBeginTime;
	}

	public void setSubmitBeginTime(Long submitBeginTime) {
		this.submitBeginTime = submitBeginTime;
	}

	public Long getSubmitEndTime() {
		return submitEndTime;
	}

	public void setSubmitEndTime(Long submitEndTime) {
		this.submitEndTime = submitEndTime;
	}

	public String getPubTimeStr() {
		this.pubTimeStr = DateTimeUtil.getTimeStamp(pubTime, "yyyy-MM-dd",false);
		return pubTimeStr;
	}

	public void setPubTimeStr(String pubTimeStr) {
		this.pubTimeStr = pubTimeStr;
	}

	public String getSubmitTimeStr() {
		this.submitTimeStr = DateTimeUtil.getTimeStamp(submitTime, "yyyy-MM-dd",false);
		return submitTimeStr;
	}

	public void setSubmitTimeStr(String submitTimeStr) {
		this.submitTimeStr = submitTimeStr;
	}

	private Integer priority;
	
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	

	public String getSeoKeyword() {
		return seoKeyword;
	}

	public void setSeoKeyword(String seoKeyword) {
		this.seoKeyword = seoKeyword;
	}

	public String getSeoDesc() {
		return seoDesc;
	}

	public void setSeoDesc(String seoDesc) {
		this.seoDesc = seoDesc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getTplId() {
		return tplId;
	}

	public void setTplId(Integer tplId) {
		this.tplId = tplId;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public String getPreconfPath() {
		return preconfPath;
	}

	public void setPreconfPath(String preconfPath) {
		this.preconfPath = preconfPath;
	}

	public Long getPubTime() {
		return pubTime;
	}

	public void setPubTime(Long pubTime) {
		this.pubTime = pubTime;
	}

	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}

}
