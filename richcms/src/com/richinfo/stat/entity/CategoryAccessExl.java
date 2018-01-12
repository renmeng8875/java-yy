package com.richinfo.stat.entity;

import java.io.Serializable;

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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import com.richinfo.common.entity.BaseEntity;

@ExcelTarget("categoryAccessExl")
public class CategoryAccessExl implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日期
	 */
	@Excel(name = "统计时间", orderNum = "1")
	private String statDate;
	
	/**
	 * 一级栏目
	 */
	@Excel(name = "一级栏目", orderNum = "2")
	private String levelone;

	/**
	 * 二级栏目
	 */
	@Excel(name = "二级栏目", orderNum = "3")
	private String leveltwo;

	/**
	 * 三级栏目
	 */
	@Excel(name = "三级栏目", orderNum = "4")
	private String levelthree;

	/**
	 * 总访问次数
	 */
	@Excel(name = "总访问次数", orderNum = "5")
	private String totalAccessNum;

	/**
	 * 总IP
	 */
	@Excel(name = "总IP", orderNum = "6")
	private String totalIp;

	/**
	 * 总PV
	 */
	@Excel(name = "总PV", orderNum = "7")
	private String totalPv;

	/**
	 * 总UV
	 */
	@Excel(name = "总UV", orderNum = "8")
	private String uv;

	/**
	 * PV/IP比
	 */
	@Excel(name = "PV/IP比", orderNum = "9")
	private String pvIp;

	/**
	 * 跳出次数
	 */
	@Excel(name = "跳出次数", orderNum = "10")
	private String jumpNum;

	/**
	 * 跳出率
	 */
	@Excel(name = "跳出率", orderNum = "11")
	private String jumpRate;

	/**
	 * 尝试订购总次数
	 */
	@Excel(name = "尝试订购总次数", orderNum = "12")
	private String orderTotalNum;

	/**
	 * 尝试订购总用户数
	 */
	@Excel(name = "尝试订购总用户数", orderNum = "13")
	private String orderTotalUserNum;

	/**
	 * 登录下载次数
	 */
	@Excel(name = "登录下载次数", orderNum = "14")
	private String loginDownloadNum;

	/**
	 * 登录下载用户数
	 */
	@Excel(name = "登录下载用户数", orderNum = "15")
	private String loginDownloadUserNum;

	/**
	 * 免登录下载次数
	 */
	@Excel(name = "免登录下载次数", orderNum = "16")
	private String nloginDownloadNum;

	/**
	 * 免登录下载用户数
	 */
	@Excel(name = "免登录下载用户数", orderNum = "17")
	private String nloginDownloadUserNum;

	/**
	 * 免费下载次数
	 */
	@Excel(name = "免费下载次数", orderNum = "18")
	private String freeDownloadNum;

	/**
	 * 免费下载用户数
	 */
	@Excel(name = "免费下载用户数", orderNum = "19")
	private String freeDownloadUserNum;

	/**
	 * 付费下载次数
	 */
	@Excel(name = "付费下载次数", orderNum = "20")
	private String payDownloadNum;

	/**
	 * 付费下载用户数
	 */
	@Excel(name = "付费下载用户数", orderNum = "21")
	private String payDownloadUserNum;

	/**
	 * 下载总次数
	 */
	@Excel(name = "下载总次数", orderNum = "22")
	private String totalDownloadNum;

	/**
	 * 下载用户总数
	 */
	@Excel(name = "下载用户总数", orderNum = "23")
	private String totalDownloadUserNum;

	

	public String getLevelone() {
		return levelone;
	}

	public void setLevelone(String levelone) {
		this.levelone = levelone;
	}

	public String getLeveltwo() {
		return leveltwo;
	}

	public void setLeveltwo(String leveltwo) {
		this.leveltwo = leveltwo;
	}

	public String getLevelthree() {
		return levelthree;
	}

	public void setLevelthree(String levelthree) {
		this.levelthree = levelthree;
	}

	public String getTotalAccessNum() {
		return totalAccessNum;
	}

	public void setTotalAccessNum(String totalAccessNum) {
		this.totalAccessNum = totalAccessNum;
	}

	public String getTotalIp() {
		return totalIp;
	}

	public void setTotalIp(String totalIp) {
		this.totalIp = totalIp;
	}

	public String getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(String totalPv) {
		this.totalPv = totalPv;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public String getPvIp() {
		return pvIp;
	}

	public void setPvIp(String pvIp) {
		this.pvIp = pvIp;
	}

	public String getJumpNum() {
		return jumpNum;
	}

	public void setJumpNum(String jumpNum) {
		this.jumpNum = jumpNum;
	}

	public String getJumpRate() {
		return jumpRate;
	}

	public void setJumpRate(String jumpRate) {
		this.jumpRate = jumpRate;
	}

	public String getOrderTotalNum() {
		return orderTotalNum;
	}

	public void setOrderTotalNum(String orderTotalNum) {
		this.orderTotalNum = orderTotalNum;
	}

	public String getOrderTotalUserNum() {
		return orderTotalUserNum;
	}

	public void setOrderTotalUserNum(String orderTotalUserNum) {
		this.orderTotalUserNum = orderTotalUserNum;
	}

	public String getLoginDownloadNum() {
		return loginDownloadNum;
	}

	public void setLoginDownloadNum(String loginDownloadNum) {
		this.loginDownloadNum = loginDownloadNum;
	}

	public String getLoginDownloadUserNum() {
		return loginDownloadUserNum;
	}

	public void setLoginDownloadUserNum(String loginDownloadUserNum) {
		this.loginDownloadUserNum = loginDownloadUserNum;
	}

	public String getNloginDownloadNum() {
		return nloginDownloadNum;
	}

	public void setNloginDownloadNum(String nloginDownloadNum) {
		this.nloginDownloadNum = nloginDownloadNum;
	}

	public String getNloginDownloadUserNum() {
		return nloginDownloadUserNum;
	}

	public void setNloginDownloadUserNum(String nloginDownloadUserNum) {
		this.nloginDownloadUserNum = nloginDownloadUserNum;
	}

	public String getFreeDownloadNum() {
		return freeDownloadNum;
	}

	public void setFreeDownloadNum(String freeDownloadNum) {
		this.freeDownloadNum = freeDownloadNum;
	}

	public String getFreeDownloadUserNum() {
		return freeDownloadUserNum;
	}

	public void setFreeDownloadUserNum(String freeDownloadUserNum) {
		this.freeDownloadUserNum = freeDownloadUserNum;
	}

	public String getPayDownloadNum() {
		return payDownloadNum;
	}

	public void setPayDownloadNum(String payDownloadNum) {
		this.payDownloadNum = payDownloadNum;
	}

	public String getPayDownloadUserNum() {
		return payDownloadUserNum;
	}

	public void setPayDownloadUserNum(String payDownloadUserNum) {
		this.payDownloadUserNum = payDownloadUserNum;
	}

	public String getTotalDownloadNum() {
		return totalDownloadNum;
	}

	public void setTotalDownloadNum(String totalDownloadNum) {
		this.totalDownloadNum = totalDownloadNum;
	}

	public String getTotalDownloadUserNum() {
		return totalDownloadUserNum;
	}

	public void setTotalDownloadUserNum(String totalDownloadUserNum) {
		this.totalDownloadUserNum = totalDownloadUserNum;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
	
	@Override
	public String toString() {
		return "CategoryAccessExl [freeDownloadNum=" + freeDownloadNum
				+ ", freeDownloadUserNum=" + freeDownloadUserNum + ", jumpNum="
				+ jumpNum + ", jumpRate=" + jumpRate + ", levelone=" + levelone
				+ ", levelthree=" + levelthree + ", leveltwo=" + leveltwo
				+ ", loginDownloadNum=" + loginDownloadNum
				+ ", loginDownloadUserNum=" + loginDownloadUserNum
				+ ", nloginDownloadNum=" + nloginDownloadNum
				+ ", nloginDownloadUserNum=" + nloginDownloadUserNum
				+ ", orderTotalNum=" + orderTotalNum + ", orderTotalUserNum="
				+ orderTotalUserNum + ", payDownloadNum=" + payDownloadNum
				+ ", payDownloadUserNum=" + payDownloadUserNum + ", pvIp="
				+ pvIp + ", statDate=" + statDate + ", totalAccessNum="
				+ totalAccessNum + ", totalDownloadNum=" + totalDownloadNum
				+ ", totalDownloadUserNum=" + totalDownloadUserNum
				+ ", totalIp=" + totalIp + ", totalPv=" + totalPv + ", uv="
				+ uv + "]";
	}

}
