package com.richinfo.playersknockout.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Table(name = "MM_GAME_PLAYERS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@SequenceGenerator(name = "GEN_MM_PK_PLAYER", sequenceName = "SEQ_MM_PK_PLAYER")
public class GamePlayer extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GEN_MM_PK_PLAYER")
    @Column(name = "PLAYERID", nullable = false)
	private Integer playerid;
	
	@Column(name="SCORE")
	private String score;
	
	@Column(name="PLAYERNAME",length=50)
	private String playername;
	
	@Column(name="PHONENUMBER",length=11)
	private String phonenumber;
	
	@Column(name="MEASUREMENT",length=50)
	private String measurement;
	
	@Column(name="CTIME",nullable = false)
	private Long ctime;
	
 
	@Column(name="PKID",nullable = false)
	private String pkid;
	
	@Column(name="PKNAME",length=50)
	private String pkname;
	
	@Column(name="PLAYERDESC",length=500)
	private String playerdesc;
	
	@Column(name="QQ",length=50)
	private String qq;
	
	@Column(name="WEIBO",length=50)
	private String weibo;
	
	@Column(name="WEIXIN",length=50)
	private String weixin;
	
	@Column(name="IMGPATH",length=200)
	private String imgpath;
	
	@Column(name="PRAISENUM")
	private Long praisenum;
	
	@Column(name="VIDEOID")
	private String videoid;
	
	@Column(name="PRIORITY")
	private int priority;
	
	@Column(name="ISGOD")
	private String isgod;
	
	@Column(name="PRIZESID")
	private Integer prizesid;
	
	@Column(name="PK")
	private String pk;
	
	@Transient
	private Integer rank;
	@Transient
	private String bscore;
	@Transient
	private String escore;
	@Transient
	private String prizesname;
	
	public Integer getPlayerid() {
		return playerid;
	}
	public void setPlayerid(Integer playerid) {
		this.playerid = playerid;
	}

	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}

	public String getPlayername() {
		return playername;
	}
	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getMeasurement() {
		return measurement;
	}
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	public Long getCtime() {
		return ctime;
	}
	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	public String getPkid() {
		return pkid;
	}
	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
    


	public String getPkname() {
		return pkname;
	}
	public void setPkname(String pkname) {
		this.pkname = pkname;
	}
	public String getPlayerdesc() {
		return playerdesc;
	}
	public void setPlayerdesc(String playerdesc) {
		this.playerdesc = playerdesc;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public Long getPraisenum() {
		return praisenum;
	}
	public void setPraisenum(Long praisenum) {
		this.praisenum = praisenum;
	}
	public String getVideoid() {
		return videoid;
	}
	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}
	
	
	public String getPrizesname() {
		return prizesname;
	}
	public void setPrizesname(String prizesname) {
		this.prizesname = prizesname;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String getIsgod() {
		return isgod;
	}
	public void setIsgod(String isgod) {
		this.isgod = isgod;
	}
	
	public Integer getPrizesid() {
		if(prizesid==null){
			return 0;
		}
		return prizesid;
	}
	public void setPrizesid(Integer prizesid) {
		if(prizesid==null){
			this.prizesid = 0;
		}else{
			this.prizesid = prizesid;
		}
	}
	
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getBscore() {
		return bscore;
	}
	public void setBscore(String bscore) {
		this.bscore = bscore;
	}
	public String getEscore() {
		return escore;
	}
	public void setEscore(String escore) {
		this.escore = escore;
	}
	
	
	
	
}
