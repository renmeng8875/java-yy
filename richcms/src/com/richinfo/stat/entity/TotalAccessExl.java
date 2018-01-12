package com.richinfo.stat.entity;

import java.io.Serializable;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

@ExcelTarget("totalAccessExl")
public class TotalAccessExl implements Serializable{
	private static final long serialVersionUID = 1L;
	//日期
	@Excel(name = "日期")
	private String statDate;

	//IP
	@Excel(name = "IP")
	private String ip;

	//UV
	@Excel(name = "UV")
	private String uv;
	
	//PV
	@Excel(name = "PV")
	private String pv;

	//总下载用户数
	@Excel(name = "总下载用户数")
	private String totalUserNum;
	
	//总下载量
	@Excel(name = "总下载量")
	private String totalDownloadNum;

	//下载量（登录）
	@Excel(name = "登录下载量")
	private String loginDownloadNum;

	//下载量（免登陆）
	@Excel(name = "免登陆下载量")
	private String nloginDownloadNum;

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getTotalUserNum() {
		return totalUserNum;
	}

	public void setTotalUserNum(String totalUserNum) {
		this.totalUserNum = totalUserNum;
	}

	public String getTotalDownloadNum() {
		return totalDownloadNum;
	}

	public String getNloginDownloadNum() {
		return nloginDownloadNum;
	}

	public void setNloginDownloadNum(String nloginDownloadNum) {
		this.nloginDownloadNum = nloginDownloadNum;
	}

	public void setTotalDownloadNum(String totalDownloadNum) {
		this.totalDownloadNum = totalDownloadNum;
	}

	public String getLoginDownloadNum() {
		return loginDownloadNum;
	}

	public void setLoginDownloadNum(String loginDownloadNum) {
		this.loginDownloadNum = loginDownloadNum;
	}


}
