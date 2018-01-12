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
@Table(name = "MM_SYS_OR", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
@SequenceGenerator(name = "MM_SYS_OR_SEQ", sequenceName = "SEQ_MM_SYS_OR")
public class Sysor extends BaseEntity {

	private static final long serialVersionUID = 4198103322925389785L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MM_SYS_OR_SEQ")
	@Column(name = "ID", nullable = false)
	private Integer id;

	@Column(name = "userid", nullable = false)
	private int userId;

	@Column(name = "optype", length = 20)
	private String optype;

	@Column(name = "orid", length = 32)
	private String orid;

	@Column(name = "module", length = 50)
	private String module;

	@Column(name = "CTIME")
	private Long createTime;

	@Column(name = "OPTIME")
	private Long opTime;

	@Transient
	private String createTimeStr;

	@Transient
	private String opTimeStr;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOptype() {
		return optype;
	}

	public void setOptype(String optype) {
		this.optype = optype;
	}

	public String getOrid() {
		return orid;
	}

	public void setOrid(String orid) {
		this.orid = orid;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Long getOpTime() {
		return opTime;
	}

	public void setOpTime(Long opTime) {
		this.opTime = opTime;
	}

	public String getCreateTimeStr() {
		this.createTimeStr = DateTimeUtil.getTimeStamp(createTime,
				"yyyy/MM/dd HH:mm:ss");
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getOpTimeStr() {
		this.opTimeStr = DateTimeUtil.getTimeStamp(opTime,
				"yyyy/MM/dd HH:mm:ss");
		return opTimeStr;
	}

	public void setOpTimeStr(String opTimeStr) {
		this.opTimeStr = opTimeStr;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String[] getExcludesAttributes() {
		return new String[] {};
	}

}
