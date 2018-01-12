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

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Table(name = "MM_CHANNEL_ACCESS", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "first")
@SequenceGenerator(name = "MM_CHANNEL_ACCESS_SEQ", sequenceName = "SEQ_MM_CT_CHANACC",allocationSize=1)
public class ChannelAccess extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getExcludesAttributes() {
		return null;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MM_CHANNEL_ACCESS_SEQ")
	@Column(name = "ID", nullable = false)
	private Integer id;

	//渠道商名称
	@Column(name = "CHANNELNAME", length = 200)
	private String channleName;

	
	//运营位置
	@Column(name = "LOCATION", length = 200)
	private String location;

	// 推广类型
	@Column(name = "TYPE", length = 200)
	private String type;

	//渠道号
	@Column(name = "CHANNELNUM", length = 200)
	private String channelNum;

	//IP
	@Column(name = "IP")
	private float ip;

	//PV
	@Column(name = "PV")
	private float pv;

	//UV
	@Column(name = "UV")
	private float uv;

	//访问数
	@Column(name = "ACCESSNUM")
	private float accessNum;

	//下载用户数
	@Column(name = "DLUSERNUM")
	private float downloadUserNum;

	//下载数
	@Column(name = "DLNUM")
	private float downloadNum;

	//日期
	@Column(name = "STATDATE")
	private long statDate;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChannleName() {
		return channleName;
	}

	public void setChannleName(String channleName) {
		this.channleName = channleName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}

	public float getIp() {
		return ip;
	}

	public void setIp(float ip) {
		this.ip = ip;
	}

	public float getPv() {
		return pv;
	}

	public void setPv(float pv) {
		this.pv = pv;
	}

	public float getUv() {
		return uv;
	}

	public void setUv(float uv) {
		this.uv = uv;
	}

	public float getAccessNum() {
		return accessNum;
	}

	public void setAccessNum(float accessNum) {
		this.accessNum = accessNum;
	}

	public float getDownloadUserNum() {
		return downloadUserNum;
	}

	public void setDownloadUserNum(float downloadUserNum) {
		this.downloadUserNum = downloadUserNum;
	}

	public float getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(float downloadNum) {
		this.downloadNum = downloadNum;
	}

	public long getStatDate() {
		return statDate;
	}

	public void setStatDate(long statDate) {
		this.statDate = statDate;
	}

	public void copy(ChannelAccessExl c){
		if(c!=null){
			this.channleName=c.getChannleName();
			this.location=c.getLocation();
			this.type=c.getType();
			this.channelNum=c.getChannelNum();
			this.ip=c.getIp()==null?0:Float.valueOf(c.getIp());
			this.pv=c.getPv()==null?0:Float.valueOf(c.getPv());
			this.uv=c.getUv()==null?0:Float.valueOf(c.getUv());
			this.accessNum=c.getAccessNum()==null?0:Float.valueOf(c.getAccessNum());
			this.downloadUserNum=c.getDownloadUserNum()==null?0:Float.valueOf(c.getDownloadUserNum());
			this.downloadNum=c.getDownloadNum()==null?0:Float.valueOf(c.getDownloadNum());
			this.statDate=DateTimeUtil.converToTimestamp(c.getStatDate(),"yyyy-MM-dd");
		}
	}

}
