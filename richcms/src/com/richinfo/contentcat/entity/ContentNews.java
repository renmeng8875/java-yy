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
@Table(name = "MM_CONTENT_NEWS", uniqueConstraints = { @UniqueConstraint(columnNames = {"DATAID"}) })
@SequenceGenerator(name = "MM_CONTENT_NEWS_SEQ", sequenceName = "SEQ_MM_CT_NEW")
public class ContentNews extends BaseEntity {
	private static final long serialVersionUID = 6783161331880788920L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="MM_CONTENT_NEWS_SEQ")
	@Column(name = "DATAID", nullable = false)
	private Integer dataId;
	
	@Column(name = "TITLE",length=2000)
	private String title;
	
	@Column(name = "appid",length=50)
	private String appId;
	
	@Column(name = "EDITOR",length=50)
	private String editor;
	
	@Column(name = "EDITORLANGUAGE",length=1000)
	private String editorLanguage;
	
	@Column(name = "AUTHOR",length=100)
	private String author;
	
	@Column(name="INTRODUCE")
	private String introduce;
	
	@Column(name="IMAGES")
	private String images;
	
	@Column(name="P_IMAGES")
	private String preImages;
	
	@Column(name = "INFOURL",length=500)
	private String infourl;
	
	@Column(name = "SEOKEYWORD",length=500)
	private String seoKeyWord;
	
	@Column(name = "SEODESC",length=500)
	private String seoDesc;
	
	@Column(name = "ONLINETIME")
	private Integer onLineTime;
	
	@Column(name = "UTIME")
	private Long updateTime;
 
	@Column(name = "P_TITLE",length=2000)
	private String preTitle;
	
	@Column(name = "LOGO",length=2000)
	private String logo;
	
	@Column(name = "P_LOGO",length=2000)
	private String preLogo;
	
	@Column(name = "PRIORITY")
	private Integer priority;
	
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
 
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getPreTitle() {
		return preTitle;
	}
	public void setPreTitle(String preTitle) {
		this.preTitle = preTitle;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getInfourl() {
		return infourl;
	}

	public void setInfourl(String infourl) {
		this.infourl = infourl;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public String[] getExcludesAttributes() {
		return new String[]{"logo","image","preLogo","preImages"};
	}

}
