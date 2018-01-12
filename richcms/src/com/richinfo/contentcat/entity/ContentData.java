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
@Table(name = "MM_CONTENT_DATA", uniqueConstraints = { @UniqueConstraint(columnNames = { "DID" }) })
@SequenceGenerator(name = "MM_CONTENT_DATA_SEQ", sequenceName = "SEQ_MM_CT_DATA",allocationSize=1)
public class ContentData extends BaseEntity {

	private static final long serialVersionUID = 4198103322925389785L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="MM_CONTENT_DATA_SEQ")
	@Column(name = "DID", nullable = false)
	private Long contentDataId;

	@Column(name = "CATID")
	private Integer catId;

	@Column(name = "TITLE",length=2000)
	private String txtHead;

	@Column(name = "LOGO",length=2000)
	private String logo;

	@Column(name = "IMAGES",length=2000)
	private String image;

	@Column(name = "DATATYPE",length=50)
	private String dataType;

	@Column(name = "DATAID")
	private Integer dataId;

	@Column(name = "CONTENTID",length=50)
	private String contentId;

	@Column(name = "AUTHOR",length=50)
	private String author;

	@Column(name = "EDITORLANGUAGE",length=2000)
	private String editorLanguage;

	@Column(name = "CTIME")
	private Long createTime;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Column(name = "UTIME")
	private Long updateTime;
	
	@Column(name = "STIME")
	private String stime;
	
	@Column(name = "ETIME")
	private String etime;
	
	@Transient
	private Long startTime;
	
	@Transient
	private Long endTime;

	@Transient
	private String timeType;
	
	@Transient
	private String createTimeStr;
	
	@Transient
	private String updateTimeStr;
	
	
	@Column(name = "P_LOGO",length=2000)
	private String preLogo;
	
	@Column(name="P_IMAGES",length=2000)
	private String preImages;
	
	public String getPreLogo() {
		return preLogo;
	}

	public void setPreLogo(String preLogo) {
		this.preLogo = preLogo;
	}

	public String getPreImages() {
		return preImages;
	}

	public void setPreImages(String preImages) {
		this.preImages = preImages;
	}

	
	public String getStime() {
		if(stime==null){
			return "";
		}
		return DateTimeUtil.getTimeStamp(Long.parseLong(stime.toString()), "yyyy-MM-dd");
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		if(etime==null){
			return "";
		}
		return DateTimeUtil.getTimeStamp(Long.parseLong(etime.toString()), "yyyy-MM-dd");
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getCreateTimeStr() {
		this.createTimeStr = DateTimeUtil.getTimeStamp(createTime, "yyyy/MM/dd HH:mm:ss");
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getUpdateTimeStr() {
		this.updateTimeStr = DateTimeUtil.getTimeStamp(updateTime, "yyyy/MM/dd HH:mm:ss");
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public Long getContentDataId() {
		return contentDataId;
	}

	public void setContentDataId(Long contentDataId) {
		this.contentDataId = contentDataId;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getTxtHead() {
		return txtHead;
	}

	public void setTxtHead(String txtHead) {
		this.txtHead = txtHead;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEditorLanguage() {
		return editorLanguage;
	}

	public void setEditorLanguage(String editorLanguage) {
		this.editorLanguage = editorLanguage;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
