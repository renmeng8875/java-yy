package com.richinfo.contentcat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Table(name = "MM_CONTENT_ANDROID", uniqueConstraints = { @UniqueConstraint(columnNames = {"DATAID", "CONTENTID" }) })
@SequenceGenerator(name = "MM_CONTENT_ANDROID_SEQ", sequenceName = "SEQ_MM_CT_ANDROID")
public class ContentAndroid extends BaseEntity {

	private static final long serialVersionUID = 997072929706481367L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="MM_CONTENT_ANDROID_SEQ")
	@Column(name = "DATAID", nullable = false)
	private Integer dataId;

	@Column(name = "TITLE",length=500)
	private String title;

	@Column(name = "APPTYPE",length=50)
	private String appType;

	@Column(name = "APPNAME",length=50)
	private String appName;

	@Column(name = "CONTENTID",length=50)
	private String contentId;

	@Column(name = "APPSIZE",length=50)
	private String appSize;

	@Column(name = "VERSION",length=500)
	private String version;

	@Column(name = "HAVENET",length=50)
	private String haveNet;

	@Column(name = "EDITOR",length=50)
	private String editor;

	@Column(name = "EDITORLANGUAGE",length=4000)
	private String editorLanguage;

	@Column(name = "PRICE")
	private Integer price;

	@Column(name = "DISCOUNT")
	private Integer discount;

	@Column(name = "STARTTIME")
	private Integer startTime;

	@Column(name = "ENDTIME")
	private Integer endTime;

	private String introduce;

	@Column(name="P_INTRODUCE")
	private String preIntroduce;

	private String images;

	@Column(name="P_IMAGES")
	private String preImages;

	@Column(name = "LOGO")
	private String logo;

	@Column(name = "P_LOGO",length=500)
	private String preLogo;

	@Column(name = "SPNAME",length=500)
	private String spname;

	@Column(name = "ICPCODE",length=20)
	private String icpCode;

	@Column(name = "ICPSERVID",length=30)
	private String icpServid;

	@Column(name = "LANGUAGE",length=1)
	private String language;

	@Column(name = "SEOKEYWORD",length=50)
	private String seoKeyWord;

	@Column(name = "SEODESC",length=200)
	private String seoDesc;

	@Column(name = "ONLINETIME")
	private Integer onLineTime;

	@Column(name = "PRODUCTID",length=30)
	private String productId;

	@Column(name = "UTIME")
	private Integer updateTime;

	@Column(name = "CHARGETIME")
	private Integer chargeTime;

	@Column(name = "OSVERSION",length=50)
	private String osversion;

	@Column(name = "COMPANYID",length=20)
	private String companyId;

	@Column(name = "RESOURCEID",length=30)
	private String resourceId;

	@Column(name = "SUBTYPE",length=2)
	private String subType;

	@Column(name = "LONGEDITORLANG",length=4000)
	private String longEditorLang;

	@Column(name = "ISCDN",length=1)
	private Integer iscdn;
	
	@Column(name = "SFSTATUS",length=1)
	private String sfstatus;

	@Column(name = "CDNURL",length=1024)
	private String cdnUrl;

	@Column(name = "DEVICEID",length=1000)
	private String deviceId;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Column(name = "SCORE")
	private Integer score;

	@Column(name = "SCORETIMES")
	private Integer scoreTimes;

	@Column(name = "DTIMES")
	private Integer downTimes;

	@Column(name = "FAVORITE")
	private Integer favorite;

	@Column(name = "LIKES")
	private Integer likes;

	@Column(name = "SHARES")
	private Integer shares;

	@Column(name = "COMMENTS")
	private Integer comments;

	@Column(name = "INFOURL")
	private String infoUrl;

	@Column(name = "USERID")
	private Integer userId;

	@Column(name = "CONTESTCODE",length=20)
	private String contestCode;

	@Column(name = "APPCATENAME",length=50)
	private String appCateName;

	@Column(name = "OSVERSON")
	private String osverson;

	@Column(name = "REFNODEID",length=30)
	private String refnodeid;

	@Column(name = "COLLEGEID",length=10)
	private String collegeId;

	@Column(name = "COLLEGE",length=256)
	private String college;

	@Column(name = "DAYORDERTIMES")
	private Integer dayOrderTimes;

	@Column(name = "WEEKORDERTIMES")
	private Integer weekOrderTimes;

	@Column(name = "MONTHORDERTIMES")
	private Integer monthOrderTimes;

	@Column(name = "ORDERTIMES")
	private Integer orderTimes;

	@Column(name = "FLAG")
	private Integer flag;
	
	@Column(name = "P_TITLE")
	private String ptitle;

	public String getSfstatus() {
		return sfstatus;
	}

	public void setSfstatus(String sfstatus) {
		this.sfstatus = sfstatus;
	}

	public String getPtitle() {
		return ptitle;
	}

	public void setPtitle(String ptitle) {
		this.ptitle = ptitle;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHaveNet() {
		return haveNet;
	}

	public void setHaveNet(String haveNet) {
		this.haveNet = haveNet;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditorLanguage() {
		return editorLanguage;
	}

	public void setEditorLanguage(String editorLanguage) {
		this.editorLanguage = editorLanguage;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	
	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="INTRODUCE", columnDefinition="CLOB", nullable=true) 
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="P_INTRODUCE", columnDefinition="CLOB", nullable=true) 
	public String getPreIntroduce() {
		return preIntroduce;
	}

	public void setPreIntroduce(String preIntroduce) {
		this.preIntroduce = preIntroduce;
	}

	
	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="IMAGES", columnDefinition="CLOB", nullable=true)
	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="P_IMAGES", columnDefinition="CLOB", nullable=true)
	public String getPreImages() {
		return preImages;
	}

	public void setPreImages(String preImages) {
		this.preImages = preImages;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPreLogo() {
		return preLogo;
	}

	public void setPreLogo(String preLogo) {
		this.preLogo = preLogo;
	}

	public String getSpname() {
		return spname;
	}

	public void setSpname(String spname) {
		this.spname = spname;
	}

	public String getIcpCode() {
		return icpCode;
	}

	public void setIcpCode(String icpCode) {
		this.icpCode = icpCode;
	}

	public String getIcpServid() {
		return icpServid;
	}

	public void setIcpServid(String icpServid) {
		this.icpServid = icpServid;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSeoKeyWord() {
		return seoKeyWord;
	}

	public void setSeoKeyWord(String seoKeyWord) {
		this.seoKeyWord = seoKeyWord;
	}

	public String getSeoDesc() {
		return seoDesc;
	}

	public void setSeoDesc(String seoDesc) {
		this.seoDesc = seoDesc;
	}

	public Integer getOnLineTime() {
		return onLineTime;
	}

	public void setOnLineTime(Integer onLineTime) {
		this.onLineTime = onLineTime;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Integer chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getLongEditorLang() {
		return longEditorLang;
	}

	public void setLongEditorLang(String longEditorLang) {
		this.longEditorLang = longEditorLang;
	}

	public Integer getIscdn() {
		return iscdn;
	}

	public void setIscdn(Integer iscdn) {
		this.iscdn = iscdn;
	}

	public String getCdnUrl() {
		return cdnUrl;
	}

	public void setCdnUrl(String cdnUrl) {
		this.cdnUrl = cdnUrl;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getScoreTimes() {
		return scoreTimes;
	}

	public void setScoreTimes(Integer scoreTimes) {
		this.scoreTimes = scoreTimes;
	}

	public Integer getDownTimes() {
		return downTimes;
	}

	public void setDownTimes(Integer downTimes) {
		this.downTimes = downTimes;
	}

	public Integer getFavorite() {
		return favorite;
	}

	public void setFavorite(Integer favorite) {
		this.favorite = favorite;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getShares() {
		return shares;
	}

	public void setShares(Integer shares) {
		this.shares = shares;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public String getInfoUrl() {
		return infoUrl;
	}

	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContestCode() {
		return contestCode;
	}

	public void setContestCode(String contestCode) {
		this.contestCode = contestCode;
	}

	public String getAppCateName() {
		return appCateName;
	}

	public void setAppCateName(String appCateName) {
		this.appCateName = appCateName;
	}

	public String getOsverson() {
		return osverson;
	}

	public void setOsverson(String osverson) {
		this.osverson = osverson;
	}

	public String getRefnodeid() {
		return refnodeid;
	}

	public void setRefnodeid(String refnodeid) {
		this.refnodeid = refnodeid;
	}

	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public Integer getDayOrderTimes() {
		return dayOrderTimes;
	}

	public void setDayOrderTimes(Integer dayOrderTimes) {
		this.dayOrderTimes = dayOrderTimes;
	}

	public Integer getWeekOrderTimes() {
		return weekOrderTimes;
	}

	public void setWeekOrderTimes(Integer weekOrderTimes) {
		this.weekOrderTimes = weekOrderTimes;
	}

	public Integer getMonthOrderTimes() {
		return monthOrderTimes;
	}

	public void setMonthOrderTimes(Integer monthOrderTimes) {
		this.monthOrderTimes = monthOrderTimes;
	}

	public Integer getOrderTimes() {
		return orderTimes;
	}

	public void setOrderTimes(Integer orderTimes) {
		this.orderTimes = orderTimes;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String[] getExcludesAttributes() {
		return new String[]{"logo","image","preLogo","preImages"};
	}

}
