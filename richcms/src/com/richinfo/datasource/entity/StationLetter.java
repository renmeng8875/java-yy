package com.richinfo.datasource.entity;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.privilege.entity.User;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SYS_STATION_LETTER")
@SequenceGenerator(name = "MM_STATION_LETTER_SEQ", sequenceName = "SEQ_MM_SYS_LETTER",allocationSize=1)
public class StationLetter extends BaseEntity
{

	private static final long serialVersionUID = -6770612345551878164L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MM_STATION_LETTER_SEQ")
    @Column(name = "letterid", nullable = false)
	private int letterid;
	
	@Column(name="letter",length=50)
	private String letter;

	@Column(name="content")
	private String content;

	@Column(name="publisher",length=50)
	private String publisher;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="publisherid",unique=true) 
	private User user;
	
	
	@Transient
	private String userNickName;
	
	@Column(name="publishtime",nullable = false)
	private Long publishtime;
	
	// 转化得到的时间字符串
	@Transient
	private String publishTimeStr;

	@Column(name="viewtimes",nullable = false)
	private Long viewtimes;
	
	@Column(name="priority",nullable = false,columnDefinition="default 99")
	private Integer priority;
	
	@Column(name="recivers",length=100)
	private String recivers;
	
	// 转化得到的接收者字符串（recivers=1表示全站用户）
	@Transient
	private String reciversStr;
	
	@Column(name="announcetype",length=50)
	private Integer announcetype;
	
	// 转化得到的公告类型字符串
	@Transient
	private String announceTypeStr;

	@Column(name="top", nullable = false)
	private Integer top;
	
	public int getLetterid() {
		return letterid;
	}

	public void setLetterid(int letterid) {
		this.letterid = letterid;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Long getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(Long publishtime) {
		this.publishtime = publishtime;
	}
	
	public String getUserNickName() {
		return user.getNickName();
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getPublishTimeStr() {
		this.publishTimeStr = DateTimeUtil.getTimeStamp(publishtime, "yyyy-MM-dd HH:mm:ss");
		return publishTimeStr;
	}

	public void setPublishTimeStr(String publishTimeStr) {
		this.publishTimeStr = publishTimeStr;
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

	public String getRecivers() {
		return recivers;
	}

	public void setRecivers(String recivers) {
		this.recivers = recivers;
	}

	public String getReciversStr() {
		return recivers != null & recivers.equals("1") ? "全站用户" : recivers;
	}

	public void setReciversStr(String reciversStr) {
		this.reciversStr = reciversStr;
	}

	public Integer getAnnouncetype() {
		return announcetype;
	}

	public void setAnnouncetype(Integer announcetype) {
		this.announcetype = announcetype;
	}
	
	public String getAnnounceTypeStr() {
		return announcetype != 0 && announcetype == 1 ? "站内信" : "其他";
	}

	public void setAnnounceTypeStr(String announceTypeStr) {
		this.announceTypeStr = announceTypeStr;
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

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	
	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
	 
	 
	
}
