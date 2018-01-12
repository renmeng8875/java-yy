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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Table(name = "MM_CONTENT_IOS", uniqueConstraints = { @UniqueConstraint(columnNames = {"DATAID"}) })
@SequenceGenerator(name = "MM_CONTENT_IOS_SEQ", sequenceName = "SEQ_MM_CONTENT_IOS")
public class ContentIos extends BaseEntity {
	private static final long serialVersionUID = 6783161331880788920L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="MM_CONTENT_IOS_SEQ")
	@Column(name = "DATAID", nullable = false)
	private Integer dataId;
	
	@Column(name = "TITLE",length=2000)
	private String title;
	
	@Column(name = "APPTYPE",length=200)
	private String appType;
	
	@Column(name = "contentid",length=50)
	private String appId;
	
	@Column(name = "APPSIZE",length=50)
	private String appSize;
	
	@Column(name = "VERSION",length=50)
	private String version;
	
	@Column(name = "EDITOR",length=50)
	private String editor;
	
	@Column(name = "EDITORLANGUAGE",length=1000)
	private String editorLanguage;
	
	@Column(name = "PROVIDER",length=100)
	private String provider;
	
	@Column(name = "PRICE")
	private Integer price;
	
	@Column(name = "SRCPRICE")
	private Integer srcPrice;
	
	private String introduce;
	
	@Column(name="P_INTRODUCE")
	private String preIntroduce;
	
	
	private String images;
	
	@Column(name="P_IMAGES")
	private String preImages;
	
	@Column(name = "LOGO",length=2000)
	private String logo;
	
	@Column(name = "P_LOGO",length=2000)
	private String preLogo;
	
	@Column(name = "ITUNESURL",length=500)
	private String itunesUrl;
	
	@Column(name = "CLICKINSTALL",length=1000)
	private String clickInstall;
	
	@Column(name = "LANGUAGE",length=500)
	private String language;
	
	@Column(name = "SEOKEYWORD",length=500)
	private String seoKeyWord;
	
	@Column(name = "SEODESC",length=500)
	private String seoDesc;
	
	@Column(name = "ONLINETIME")
	private Integer onLineTime;
	
	@Column(name = "UTIME")
	private Long updateTime;
	
	@Column(name = "OSVERSION",length=500)
	private String osversion;
	
	@Column(name = "PRIORITY")
	private Integer priority;
	
	@Column(name = "P_TITLE",length=2000)
	private String preTitle;
	
	private String longEditorLang;
	
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
	
	
	@Transient
	private String utimeStr;
	
	
	public String getUtimeStr() {
		this.utimeStr = DateTimeUtil.getTimeStamp(updateTime, "yyyy-MM-dd");
		return utimeStr;
	}

	public void setUtimeStr(String utimeStr) {
		this.utimeStr = utimeStr;
	}



	@Column(name = "CONTENTID",length=32)
	private String contentId;
	
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getSrcPrice() {
		return srcPrice;
	}

	public void setSrcPrice(Integer srcPrice) {
		this.srcPrice = srcPrice;
	}
	
	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="INTRODUCE", columnDefinition="NCLOB", nullable=true) 
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="P_INTRODUCE", columnDefinition="NCLOB", nullable=true) 
	public String getPreIntroduce() {
		return preIntroduce;
	}

	public void setPreIntroduce(String preIntroduce) {
		this.preIntroduce = preIntroduce;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="IMAGES", columnDefinition="NCLOB", nullable=true) 
	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="P_IMAGES", columnDefinition="NCLOB", nullable=true) 
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

	public String getItunesUrl() {
		return itunesUrl;
	}

	public void setItunesUrl(String itunesUrl) {
		this.itunesUrl = itunesUrl;
	}

	public String getClickInstall() {
		return clickInstall;
	}

	public void setClickInstall(String clickInstall) {
		this.clickInstall = clickInstall;
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

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getPreTitle() {
		return preTitle;
	}

	public void setPreTitle(String preTitle) {
		this.preTitle = preTitle;
	}

	public String getLongEditorLang() {
		return longEditorLang;
	}
	
	@Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="longEditorLang", columnDefinition="NCLOB", nullable=true)
	public void setLongEditorLang(String longEditorLang) {
		this.longEditorLang = longEditorLang;
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

	

	@Override
	public String[] getExcludesAttributes() {
		return new String[]{"logo","image","preLogo","preImages"};
	}

}
