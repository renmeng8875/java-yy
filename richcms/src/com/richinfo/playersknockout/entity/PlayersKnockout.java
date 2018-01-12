package com.richinfo.playersknockout.entity;

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
@Table(name = "MM_PLAYERS_KNOCKOUT")
public class PlayersKnockout extends BaseEntity
{
	private static final long serialVersionUID = -8779392940766155232L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PKID", nullable = false)
	private Integer pkid;
	
	@Column(length=200)
	private String pkName;
	
	@Column(length=50)
	private String pk;
	
	private Long startTime;
	
	@Transient
	private String startTimeStr;
	
	@Transient
	private boolean vaild;
	
	private Long endTime;
	
	@Transient
	private String endTimeStr;
	
	private Long ctime;
	
	@Transient
	private String ctimeStr;
	
	private Integer status;
	
	@Column(length=200)
	private String pkdesc;
	

	@Column(length=200)
	private String logo;
	
	@Column(length=2000)
	private String images;
	
	@Column(name="style",length=50)
	private String style;
	
	@Column(name="tplpath",length=200)
	private String tplpath;
	
	@Column(length=32)
	private String contentid;
	
	@Column(length=32)
	private String nextContentid;
	
	@Column(length=500)
	private String jpImage;
	
	private String evaluation;
	
	private String article;
	
	private Integer articleId;
	
	private Long articleCtime;
	
	@Transient
	private String articleCtimeStr;
	
	@Column(length=1)
	private Integer appType;
	
	@Column(length=1)
	private Integer nextAppType;
	
	

	private String playerList;
	
	@Column(length=4)
	private Integer priority;
	
	@Column(length=2000)
	private String code;
	
	@Column(length=50)
	private String seoTitle;
	
	@Column(length=50)
	private String seoKeyword;
	
	@Column(length=200)
	private String seoDesc;
	
	private Integer isForward;
	
	@Column(length=500)
	private String forwardUrl;

	private String nextName;
	
	private String appName;
	
	private String nextAppName;
	
	private String introduce;
	
	private String nextTipcontent;
	
	private String joinway;
	
	private String nextPrizeImage;
	
	private String godBackground;
	
	private String activitiesLogo;
	
	public String getActivitiesLogo() 
	{
		return activitiesLogo;
	}

	public void setActivitiesLogo(String activitiesLogo) {
		this.activitiesLogo = activitiesLogo;
	}

	public String getNextPrizeImage() 
	{
		return nextPrizeImage;
	}

	public void setNextPrizeImage(String nextPrizeImage) 
	{
		this.nextPrizeImage = nextPrizeImage;
	}

	public String getGodBackground() {
		return godBackground;
	}

	public void setGodBackground(String godBackground) {
		this.godBackground = godBackground;
	}

	public String getNextTipcontent() {
		return nextTipcontent;
	}

	public void setNextTipcontent(String nextTipcontent) {
		this.nextTipcontent = nextTipcontent;
	}

	public String getJoinway() {
		return joinway;
	}

	public void setJoinway(String joinway) {
		this.joinway = joinway;
	}

	@Column(length=2000)
	private String gameImages;
	
	public String getGameImages() {
		return gameImages;
	}

	public void setGameImages(String gameImages) {
		this.gameImages = gameImages;
	}

	public boolean isVaild() 
	{
		this.vaild = true;
		long currentTime = DateTimeUtil.getTimeStamp();
		if(endTime==null||startTime==null)
		{
			return true;
		}
		if(currentTime>endTime)
		{
			return false;
		}
		return vaild;
	}

	public void setVaild(boolean vaild) 
	{
		this.vaild = vaild;
	}

	public String getStartTimeStr() {
		this.startTimeStr = DateTimeUtil.getTimeStamp(startTime, "yyyy-MM-dd");
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	
	public String getEndTimeStr() {
		this.endTimeStr = DateTimeUtil.getTimeStamp(endTime, "yyyy-MM-dd");
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getCtimeStr() {
		this.ctimeStr = DateTimeUtil.getTimeStamp(ctime, "yyyy-MM-dd");
		return ctimeStr;
	}

	public void setCtimeStr(String ctimeStr) {
		this.ctimeStr = ctimeStr;
	}
	
	
	public String getArticleCtimeStr() {
		this.articleCtimeStr = DateTimeUtil.getTimeStamp(articleCtime, "yyyy-MM-dd");
		return articleCtimeStr;
	}

	public void setArticleCtimeStr(String articleCtimeStr) {
		this.articleCtimeStr = articleCtimeStr;
	}

	public String getIntroduce() {
		return introduce;
	}
	
	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="introduce", columnDefinition="CLOB", nullable=true)
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getNextAppName() {
		return nextAppName;
	}

	public void setNextAppName(String nextAppName) {
		this.nextAppName = nextAppName;
	}

	public String getNextName() {
		return nextName;
	}

	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}


	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getCtime() {
		return ctime;
	}


	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}


	public String getImages() {
		return images;
	}


	public void setImages(String images) {
		this.images = images;
	}

	

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTplpath() {
		return tplpath;
	}

	public void setTplpath(String tplpath) {
		this.tplpath = tplpath;
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getNextContentid() {
		return nextContentid;
	}

	public void setNextContentid(String nextContentid) {
		this.nextContentid = nextContentid;
	}

	public String getJpImage() {
		return jpImage;
	}


	public void setJpImage(String jpImage) {
		this.jpImage = jpImage;
	}

	public String getEvaluation() {
		return evaluation;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="evaluation", columnDefinition="CLOB", nullable=true) 
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	
	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(Integer pkid) {
		this.pkid = pkid;
	}
	public String getPkdesc() {
		return pkdesc;
	}

	public void setPkdesc(String pkdesc) {
		this.pkdesc = pkdesc;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
	
	public String getArticle() {
		return article;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="article", columnDefinition="CLOB", nullable=true) 
	public void setArticle(String article) {
		this.article = article;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Long getArticleCtime() {
		return articleCtime;
	}

	public void setArticleCtime(Long articleCtime) {
		this.articleCtime = articleCtime;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getPlayerList() {
		return playerList;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="playerList", columnDefinition="CLOB", nullable=true) 
	public void setPlayerList(String playerList) {
		this.playerList = playerList;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
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

	public Integer getIsForward() {
		return isForward;
	}

	public void setIsForward(Integer isForward) {
		this.isForward = isForward;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	
	public Integer getNextAppType() {
		return nextAppType;
	}

	public void setNextAppType(Integer nextAppType) {
		this.nextAppType = nextAppType;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return new String[]{"pkdesc","evaluation","article","playerList","introduce"};
	}

}
