package com.richinfo.playersknockout.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first") 
@Table(name = "MM_PK_VIDEO")
@SequenceGenerator(name = "GEN_MM_PK_VIDEO", sequenceName = "SEQ_MM_PK_VIDEO")
public class Video extends BaseEntity
{

	private static final long serialVersionUID = -7592136497064508896L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GEN_MM_PK_VIDEO")
    @Column(name = "videoid", nullable = false)
	private Integer videoId;
	
	private String videoUrl;
	
	private String uploader;
	
	private String lastModifier;
	
	private String vname;
	
	private String introduce;
	
	private String capture;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="utime")
	private Date utime;
	
	private Integer sortNum;


	public Integer getSortNum() {
		return sortNum;
	}


	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}


	public Date getUtime() {
		return utime;
	}


	public void setUtime(Date utime) {
		this.utime = utime;
	}


	public String getCapture() 
	{
		return capture;
	}


	public void setCapture(String capture) {
		this.capture = capture;
	}


	public String getVname() {
		return vname;
	}


	public void setVname(String vname) {
		this.vname = vname;
	}


	public String getIntroduce() {
		return introduce;
	}


	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}


	public String getLastModifier() {
		return lastModifier;
	}


	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Integer getVideoId() {
		return videoId;
	}


	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}


	public String getVideoUrl() {
		return videoUrl;
	}




	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}




	public String getUploader() {
		return uploader;
	}




	public void setUploader(String uploader) {
		this.uploader = uploader;
	}




	


	@Override
	public String[] getExcludesAttributes() 
	{
		return new String[]{"utime"};
	}

}
