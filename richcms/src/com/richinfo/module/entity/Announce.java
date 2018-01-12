package com.richinfo.module.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SYS_ANNOUNCE")
public class Announce extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "announceid", nullable = false)
	private int announceid;
	
	@Column(name="announce",length=50)
	private String announce;
	
	@Column(name="starttime",nullable = false)
	private Long starttime;
	
	@Transient
	private String startTimeStr;
	
	public String getStartTimeStr() {
		this.startTimeStr = DateTimeUtil.getTimeStamp(starttime, "yyyy-MM-dd HH:mm");
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	@Column(name="endtime",nullable = false)
	private Long endtime;
	
	@Transient
	private String endTimeStr;
	
	public String getEndTimeStr() {
		this.endTimeStr = DateTimeUtil.getTimeStamp(endtime, "yyyy-MM-dd HH:mm");
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	@Column(name="content")
	private String content;
     
	@Column(name="control")
	private String control;

	@Column(name="action",length=50)
	private String action;
	
	@Column(name="status",nullable = false)
	private int status;
	
	@Column(name="publisher",length=50)
	private String publisher;
	
	@Column(name="publishtime",nullable = false)
	private Long publishtime;
	
	@Transient
	private String publishTimeStr;
	
	public String getPublishTimeStr() {
		this.publishTimeStr = DateTimeUtil.getTimeStamp(publishtime, "yyyy-MM-dd HH:mm:ss");
		return publishTimeStr;
	}

	public void setPublishTimeStr(String publishTimeStr) {
		this.publishTimeStr = publishTimeStr;
	}

	@Column(name="viewtimes",nullable = false)
	private Long viewtimes;
	
	@Column(name="priority",nullable = false,columnDefinition="default 99")
	private Integer priority;
	

	public int getAnnounceid() {
		return announceid;
	}

	public void setAnnounceid(int announceid) {
		this.announceid = announceid;
	}

	public String getAnnounce() {
		return announce;
	}

	public void setAnnounce(String announce) {
		this.announce = announce;
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

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="content", columnDefinition="CLOB", nullable=true) 
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Long getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(Long publishtime) {
		this.publishtime = publishtime;
	}

	public Long getViewtimes() {
		return viewtimes;
	}

	public void setViewtimes(Long viewtimes) {
		this.viewtimes = viewtimes;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
	 
	 
	
}
