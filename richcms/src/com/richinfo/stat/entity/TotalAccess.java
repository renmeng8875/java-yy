package com.richinfo.stat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Table(name = "MM_TOTAL_ACCESS", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "first")
@SequenceGenerator(name = "MM_TOTAL_ACCESS_SEQ", sequenceName = "SEQ_MM_CT_TOTACC",allocationSize=1)
public class TotalAccess extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getExcludesAttributes() {
		return null;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MM_TOTAL_ACCESS_SEQ")
	@Column(name = "ID", nullable = false)
	private Integer id;

	//日期
	@Column(name = "STATDATE")
	private long statDate;

	//IP
	@Column(name = "IP")
	private float ip;

	//UV
	@Column(name = "UV")
	private float uv;

	//PV
	@Column(name = "PV")
	private float pv;

	//总下载用户数
	@Column(name = "TOTALUSERNUM")
	private float totalUserNum;
	
	//总下载量
	@Column(name = "TOTALDLNUM")
	private float totalDownloadNum;

	//下载量（登录）
	@Column(name = "LOGINDLNUM")
	private float loginDownloadNum;

	//下载量（免登陆）
	@Column(name = "NLOGINDLNUM")
	private float nLoginDownloadNum;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getStatDate() {
		return statDate;
	}

	public void setStatDate(long statDate) {
		this.statDate = statDate;
	}

	public float getIp() {
		return ip;
	}

	public void setIp(float ip) {
		this.ip = ip;
	}

	public float getUv() {
		return uv;
	}

	public void setUv(float uv) {
		this.uv = uv;
	}

	public float getPv() {
		return pv;
	}

	public void setPv(float pv) {
		this.pv = pv;
	}

	public float getTotalUserNum() {
		return totalUserNum;
	}

	public void setTotalUserNum(float totalUserNum) {
		this.totalUserNum = totalUserNum;
	}

	public float getTotalDownloadNum() {
		return totalDownloadNum;
	}

	public void setTotalDownloadNum(float totalDownloadNum) {
		this.totalDownloadNum = totalDownloadNum;
	}

	public float getLoginDownloadNum() {
		return loginDownloadNum;
	}

	public void setLoginDownloadNum(float loginDownloadNum) {
		this.loginDownloadNum = loginDownloadNum;
	}

	public float getnLoginDownloadNum() {
		return nLoginDownloadNum;
	}

	public void setnLoginDownloadNum(float nLoginDownloadNum) {
		this.nLoginDownloadNum = nLoginDownloadNum;
	}

	public void copy(TotalAccessExl c){
		if(c!=null){
			this.statDate=DateTimeUtil.converToTimestamp(c.getStatDate(), "yyyy-MM-dd");
			this.ip=c.getIp()==null?0:Float.valueOf(c.getIp());
			this.uv=c.getUv()==null?0:Float.valueOf(c.getUv());
			this.pv=c.getPv()==null?0:Float.valueOf(c.getPv());
			this.totalUserNum=c.getTotalUserNum()==null?0:Float.valueOf(c.getTotalUserNum());
			this.totalDownloadNum=c.getTotalDownloadNum()==null?0:Float.valueOf(c.getTotalDownloadNum());
			this.loginDownloadNum=c.getLoginDownloadNum()==null?0:Float.valueOf(c.getLoginDownloadNum());
			this.nLoginDownloadNum=c.getNloginDownloadNum()==null?0:Float.valueOf(c.getNloginDownloadNum());
		}
	}
	
}
