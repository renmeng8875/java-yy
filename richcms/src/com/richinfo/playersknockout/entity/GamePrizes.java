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
@Table(name = "MM_GAME_PRIZES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@SequenceGenerator(name = "GEN_MM_PK_PRIZES", sequenceName = "SEQ_MM_PK_PRIZES")
public class GamePrizes extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GEN_MM_PK_PRIZES")
    @Column(name = "PRIZESID", nullable = false)
	private Integer prizesid;
	 
	@Column(name="PRIZESNAME",length=50)
	private String prizesname;
	
	@Column(name="PRIZESIMG",length=500)
	private String prizesimg;
	
	@Column(name="CTIME",nullable = false)
	private Long ctime;
	
	@Column(name="PRIORITY",length=50)
	private String priority;
	
	@Column(name="PKID",length=50)
	private Integer pkid;
	
	@Transient
	private String pkname;
	
	 
	public Integer getPrizesid() {
		return prizesid;
	}

	public void setPrizesid(Integer prizesid) {
		this.prizesid = prizesid;
	}

	public String getPrizesname() {
		return prizesname;
	}

	public void setPrizesname(String prizesname) {
		this.prizesname = prizesname;
	}

	public String getPrizesimg() {
		return prizesimg;
	}

	public void setPrizesimg(String prizesimg) {
		this.prizesimg = prizesimg;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(Integer pkid) {
		this.pkid = pkid;
	}

	public String getPkname() {
		return pkname;
	}

	public void setPkname(String pkname) {
		this.pkname = pkname;
	}

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
