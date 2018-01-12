package com.richinfo.stat.entity;

import java.io.Serializable;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

@ExcelTarget("channelAccessExl")
public class ChannelAccessExl implements Serializable{
	private static final long serialVersionUID = 1L;

	//渠道商名称
	@Excel(name = "渠道商名称",orderNum = "1")
	private String channleName;

	//运营位置
	@Excel(name = "运营位置",orderNum = "2")
	private String location;

	//推广类型
	@Excel(name = "推广类型",orderNum = "3")
	private String type;

	//渠道号
	@Excel(name = "渠道号",orderNum = "4")
	private String channelNum;

	//IP
	@Excel(name = "IP",orderNum = "5")
	private String ip;

	//PV
	@Excel(name = "PV",orderNum = "6")
	private String pv;

	//UV
	@Excel(name = "UV",orderNum = "7")
	private String uv;

	//访问数
	@Excel(name = "访问数",orderNum = "8")
	private String accessNum;

	//下载用户数
	@Excel(name = "下载用户数",orderNum = "9")
	private String downloadUserNum;

	//下载数
	@Excel(name = "下载数",orderNum = "10")
	private String downloadNum;

	//日期
	@Excel(name = "日期",orderNum = "11")
	private String statDate;

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public String getAccessNum() {
		return accessNum;
	}

	public void setAccessNum(String accessNum) {
		this.accessNum = accessNum;
	}

	public String getDownloadUserNum() {
		return downloadUserNum;
	}

	public void setDownloadUserNum(String downloadUserNum) {
		this.downloadUserNum = downloadUserNum;
	}

	public String getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	@Override
	public String toString() {
		return "ChannelAccessExl [accessNum=" + accessNum + ", channelNum="
				+ channelNum + ", channleName=" + channleName
				+ ", downloadNum=" + downloadNum + ", downloadUserNum="
				+ downloadUserNum + ", ip=" + ip + ", location=" + location
				+ ", pv=" + pv + ", statDate=" + statDate + ", type=" + type
				+ ", uv=" + uv + "]";
	}
	
}
