package com.richinfo.privilege.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SYS_USER",uniqueConstraints = {@UniqueConstraint(columnNames={"username"})})
@SequenceGenerator(name = "GEN_MM_SYS_USER", sequenceName = "SEQ_MM_SYS_USER")
public class User extends BaseEntity
{

	private static final long serialVersionUID = 2779768826722211381L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GEN_MM_SYS_USER")
    @Column(name = "userid", nullable = false)
	private int userId;
	
	@Column(name="username",nullable = false,length=50)
	private String userName;
	
	@Column(name="passwd",nullable = false,length=32)
	private String passwd;
	
	@Column(name="email",nullable = false,length=50)
	private String email;
	
	@Column(name="nickname",nullable = true,length=50)
	private String nickName;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="roleid",unique=true) 
	private Role role;
	
	@Column(name="loginip",nullable = true,length=50)
	private Long loginIp;
	
	@Transient
	private String ipstr;
	
	@Column(name="lastlogin",nullable = true)
	private Long lastLogin;
	
	@Column(name="regedit",nullable = true)
	private Integer regedit;
	
	@Column(name="status",nullable = false,length=1)
	private Integer status;
	
	@Transient
	private List<Menu> menuList;
	
	@Transient
	private List<Category> catList;
	
	@Transient
	private String lastLoginStr;
 
	public String getLastLoginStr() {
		this.lastLoginStr = DateTimeUtil.getTimeStamp(lastLogin, "yyyy-MM-dd HH:mm:ss");
		return lastLoginStr;
	}

	public void setLastLoginStr(String lastLoginStr) {
		this.lastLoginStr = lastLoginStr;
	}
	
	public String getIpstr() {
		this.ipstr = CommonUtil.longToIp(this.loginIp);
		return this.ipstr;
	}

	public void setIpstr(String ipstr) {
		this.ipstr = ipstr;
	}


	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<Category> getCatList() {
		return catList;
	}

	public void setCatList(List<Category> catList) {
		this.catList = catList;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(Long loginIp) {
		this.loginIp = loginIp;
	}

	public Long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getRegedit() {
		return regedit;
	}

	public void setRegedit(Integer regedit) {
		this.regedit = regedit;
	}

	public Integer getStatus() {
		if(status==null)
		{
			return 0;
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return new String[]{"role","lastLogin","loginIp","menuList","catList","lastLoginStr"};
	}
	
	

}
