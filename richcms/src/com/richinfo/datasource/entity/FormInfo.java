package com.richinfo.datasource.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Table(name = "MM_SYS_FORMINFO")
public class FormInfo extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private int formid;
	
	@Column(name="datatype",length=50)
	private String datatype;

	@Column(name="formname",length=50)
	private String formname;
     
	@Column(name="formdesc",length=200)
	private String formdesc;

	@Column(name="formtype",length=200)
	private String formtype;
	
	@Column(name="formorder", length=5)
	private String formorder = "99";
 
	@Column(name="ctime",nullable = false)
	private Long ctime;
	
	@Column(name="template",length=200)
	private String template;
	
  
	public FormInfo(){
		
	}
	public FormInfo(int formid){
		this.formid = formid;
	}
	
	public int getFormid() {
		return formid;
	}

	public void setFormid(int formid) {
		this.formid = formid;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}
	public String getFormdesc() {
		return formdesc;
	}

	public void setFormdesc(String formdesc) {
		this.formdesc = formdesc;
	}

	public String getFormorder() {
		return formorder;
	}

	public void setFormorder(String formorder) {
		this.formorder = formorder;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}
    
	public String getFormtype() {
		return formtype;
	}

	public void setFormtype(String formtype) {
		this.formtype = formtype;
	}
   
	public String getTemplate() {
		if(template!=null){
			template = "'" +template +",";
		}
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
	 
	 
	
}
