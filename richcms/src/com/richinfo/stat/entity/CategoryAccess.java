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
@Table(name = "MM_CATEGORY_ACCESS", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "first")
@SequenceGenerator(name = "MM_CATEGORY_ACCESS_SEQ", sequenceName = "SEQ_MM_CT_CATACC",allocationSize=1)
public class CategoryAccess extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getExcludesAttributes() {
		return null;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MM_CATEGORY_ACCESS_SEQ")
	@Column(name = "ID", nullable = false)
	private Integer id;

	/**
	 * 一级栏目
	 */
	@Column(name = "LEVELONE", length = 200)
	private String levelone;

	/**
	 * 二级栏目
	 */
	@Column(name = "LEVELTWO", length = 200)
	private String leveltwo;

	/**
	 * 三级栏目
	 */
	@Column(name = "LEVELTHREE", length = 200)
	private String levelthree;

	/**
	 * 总访问次数
	 */
	@Column(name = "TOTALACCESSNUM")
	private float totalAccessNum;

	/**
	 * 总IP
	 */
	@Column(name = "TOTALIP")
	private float totalIp;

	/**
	 * 总PV
	 */
	@Column(name = "TOTALPV")
	private float totalPv;

	/**
	 * 总UV
	 */
	@Column(name = "TOTALUV")
	private float uv;

	/**
	 * PV/IP比
	 */
	@Column(name = "PVIP")
	private float pvIp;

	/**
	 * 跳出次数
	 */
	@Column(name = "JUMPNUM")
	private float jumpNum;

	/**
	 * 跳出率
	 */
	@Column(name = "JUMPRATE")
	private float jumpRate;

	/**
	 * 尝试订购总次数
	 */
	@Column(name = "ORDERTNUM")
	private float orderTotalNum;

	/**
	 * 尝试订购总用户数
	 */
	@Column(name = "ORDERTUSERNUM")
	private float orderTotalUserNum;

	/**
	 * 登录下载次数
	 */
	@Column(name = "LOGINDLNUM")
	private float loginDownloadNum;

	/**
	 * 登录下载用户数
	 */
	@Column(name = "LOGINDLUSERNUM")
	private float loginDownloadUserNum;

	/**
	 * 免登录下载次数
	 */
	@Column(name = "NLOGINDLNUM")
	private float nloginDownloadNum;

	/**
	 * 免登录下载用户数
	 */
	@Column(name = "NLOGINDLUSERNUM")
	private float nloginDownloadUserNum;

	/**
	 * 免费下载次数
	 */
	@Column(name = "FREEDLNUM")
	private float freeDownloadNum;

	/**
	 * 免费下载用户数
	 */
	@Column(name = "FREEDLUSERNUM")
	private float freeDownloadUserNum;

	/**
	 * 付费下载次数
	 */
	@Column(name = "PAYDLNUM")
	private float payDownloadNum;

	/**
	 * 付费下载用户数
	 */
	@Column(name = "PAYDLUSERNUM")
	private float payDownloadUserNum;

	/**
	 * 下载总次数
	 */
	@Column(name = "TOTALDLNUM")
	private float totalDownloadNum;

	/**
	 * 下载用户总数
	 */
	@Column(name = "TOTALDLUSERNUM")
	private float totalDownloadUserNum;

	/**
	 * 日期
	 */
	@Column(name = "STATDATE")
	private long statDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public float getTotalAccessNum() {
		return totalAccessNum;
	}

	public void setTotalAccessNum(float totalAccessNum) {
		this.totalAccessNum = totalAccessNum;
	}

	public float getTotalIp() {
		return totalIp;
	}

	public void setTotalIp(float totalIp) {
		this.totalIp = totalIp;
	}

	public float getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(float totalPv) {
		this.totalPv = totalPv;
	}

	public float getUv() {
		return uv;
	}

	public void setUv(float uv) {
		this.uv = uv;
	}

	public float getPvIp() {
		return pvIp;
	}

	public void setPvIp(float pvIp) {
		this.pvIp = pvIp;
	}

	public float getJumpNum() {
		return jumpNum;
	}

	public void setJumpNum(float jumpNum) {
		this.jumpNum = jumpNum;
	}

	public float getJumpRate() {
		return jumpRate;
	}

	public void setJumpRate(float jumpRate) {
		this.jumpRate = jumpRate;
	}

	public float getOrderTotalNum() {
		return orderTotalNum;
	}

	public void setOrderTotalNum(float orderTotalNum) {
		this.orderTotalNum = orderTotalNum;
	}

	public float getOrderTotalUserNum() {
		return orderTotalUserNum;
	}

	public void setOrderTotalUserNum(float orderTotalUserNum) {
		this.orderTotalUserNum = orderTotalUserNum;
	}

	public float getLoginDownloadNum() {
		return loginDownloadNum;
	}

	public void setLoginDownloadNum(float loginDownloadNum) {
		this.loginDownloadNum = loginDownloadNum;
	}

	public float getLoginDownloadUserNum() {
		return loginDownloadUserNum;
	}

	public void setLoginDownloadUserNum(float loginDownloadUserNum) {
		this.loginDownloadUserNum = loginDownloadUserNum;
	}

	public float getNloginDownloadNum() {
		return nloginDownloadNum;
	}

	public void setNloginDownloadNum(float nloginDownloadNum) {
		this.nloginDownloadNum = nloginDownloadNum;
	}

	public float getNloginDownloadUserNum() {
		return nloginDownloadUserNum;
	}

	public void setNloginDownloadUserNum(float nloginDownloadUserNum) {
		this.nloginDownloadUserNum = nloginDownloadUserNum;
	}

	public float getFreeDownloadNum() {
		return freeDownloadNum;
	}

	public void setFreeDownloadNum(float freeDownloadNum) {
		this.freeDownloadNum = freeDownloadNum;
	}

	public float getFreeDownloadUserNum() {
		return freeDownloadUserNum;
	}

	public void setFreeDownloadUserNum(float freeDownloadUserNum) {
		this.freeDownloadUserNum = freeDownloadUserNum;
	}

	public float getPayDownloadNum() {
		return payDownloadNum;
	}

	public void setPayDownloadNum(float payDownloadNum) {
		this.payDownloadNum = payDownloadNum;
	}

	public float getPayDownloadUserNum() {
		return payDownloadUserNum;
	}

	public void setPayDownloadUserNum(float payDownloadUserNum) {
		this.payDownloadUserNum = payDownloadUserNum;
	}

	public float getTotalDownloadNum() {
		return totalDownloadNum;
	}

	public void setTotalDownloadNum(float totalDownloadNum) {
		this.totalDownloadNum = totalDownloadNum;
	}

	public float getTotalDownloadUserNum() {
		return totalDownloadUserNum;
	}

	public void setTotalDownloadUserNum(float totalDownloadUserNum) {
		this.totalDownloadUserNum = totalDownloadUserNum;
	}

	public long getStatDate() {
		return statDate;
	}

	public void setStatDate(long statDate) {
		this.statDate = statDate;
	}
	
	public void copy(CategoryAccessExl c){
		if(c!=null){
			this.levelone=c.getLevelone();
			this.leveltwo=c.getLeveltwo();
			this.levelthree=c.getLevelthree();
			this.totalAccessNum=c.getTotalAccessNum()==null?0:Float.valueOf(c.getTotalAccessNum());
			this.totalIp=c.getTotalIp()==null?0:Float.valueOf(c.getTotalIp());
			this.totalPv=c.getTotalPv()==null?0:Float.valueOf(c.getTotalPv());
			this.uv=c.getUv()==null?0:Float.valueOf(c.getUv());
			this.pvIp=c.getPvIp()==null?0:Float.valueOf(c.getPvIp());
			this.jumpNum=c.getJumpNum()==null?0:Float.valueOf(c.getJumpNum());
			if(c.getJumpRate()==null){
				this.jumpRate=0;
			}else{
				String tempJumpRate=c.getJumpRate().replace("%", "");
				this.jumpRate=Float.valueOf(tempJumpRate);
			}
			this.orderTotalNum=c.getOrderTotalNum()==null?0:Float.valueOf(c.getOrderTotalNum());
			this.orderTotalUserNum=c.getOrderTotalUserNum()==null?0:Float.valueOf(c.getOrderTotalUserNum());
			this.loginDownloadNum=c.getLoginDownloadNum()==null?0:Float.valueOf(c.getLoginDownloadNum());
			this.loginDownloadUserNum=c.getLoginDownloadUserNum()==null?0:Float.valueOf(c.getLoginDownloadUserNum());
			this.nloginDownloadNum=c.getNloginDownloadNum()==null?0:Float.valueOf(c.getNloginDownloadNum());
			this.nloginDownloadUserNum=c.getNloginDownloadUserNum()==null?0:Float.valueOf(c.getNloginDownloadUserNum());
			this.freeDownloadNum=c.getFreeDownloadNum()==null?0:Float.valueOf(c.getFreeDownloadNum());
			this.freeDownloadUserNum=c.getFreeDownloadUserNum()==null?0:Float.valueOf(c.getFreeDownloadNum());
			this.payDownloadNum=c.getPayDownloadNum()==null?0:Float.valueOf(c.getPayDownloadNum());
			this.payDownloadUserNum=c.getFreeDownloadUserNum()==null?0:Float.valueOf(c.getFreeDownloadUserNum());
			this.totalDownloadNum=c.getTotalDownloadNum()==null?0:Float.valueOf(c.getTotalDownloadNum());
			this.totalDownloadUserNum=c.getTotalDownloadUserNum()==null?0:Float.valueOf(c.getTotalDownloadUserNum());
			this.statDate=DateTimeUtil.converToTimestamp(c.getStatDate(), "yyyy-MM-dd");
		}
	}
}
