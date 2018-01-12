package com.richinfo.datasource.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Table(name = "MM_SYS_CHOICE")
public class Choice extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private int choiceid;
	
	@Column(name="name",length=50)
	private String choicename;
	
	@Column(name="code",length=1000)
	private String choicecode;

	@Column(name="choicedesc",length=1000)
	private String choicedesc;
	
	@Column(name="viewcode",length=1000)
	private String viewcode;
	
	@Column(name="sign",length=50)
	private String sign;
	
	public Choice(){
		
	}
	 
	public int getChoiceid() {
		return choiceid;
	}



	public void setChoiceid(int choiceid) {
		this.choiceid = choiceid;
	}



	public String getChoicename() {
		return choicename;
	}



	public void setChoicename(String choicename) {
		this.choicename = choicename;
	}



	public String getChoicecode() {
		return choicecode;
	}



	public void setChoicecode(String choicecode) {
		this.choicecode = choicecode;
	}



	public String getChoicedesc() {
		return choicedesc;
	}

	public void setChoicedesc(String choicedesc) {
		this.choicedesc = choicedesc;
	}

	public String getViewcode() {
		return viewcode;
	}

	public void setViewcode(String viewcode) {
		this.viewcode = viewcode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
	 
	 
	
}
