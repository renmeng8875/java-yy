package com.richinfo.module.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "first")
@Table(name = "MM_SP_PUBLISH")
public class SpPublish extends BaseEntity {

	private static final long serialVersionUID = 6318021818380172366L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PUBID", nullable = false)
	private Integer pubId;
	
	@Column(name = "COMPANYID")
	private Integer companyId;
	
	@Column(name = "BLOCKTYPE",length=50)
	private String blockType;
	
	@Column(name = "BASEINFO",length=4000)
	private String baseInfo;
	
	@Column(name = "CONTENT",length=4000)
	private String content;
	
	@Column(name = "UTIME")
	private Integer uptime;
	
	@Column(name = "REMARK",length=4000)
	private String remark;

	public Integer getPubId() {
		return pubId;
	}

	public void setPubId(Integer pubId) {
		this.pubId = pubId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getBlockType() {
		return blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public String getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(String baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getUptime() {
		return uptime;
	}

	public void setUptime(Integer uptime) {
		this.uptime = uptime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String[] getExcludesAttributes() {
		return null;
	}

}
