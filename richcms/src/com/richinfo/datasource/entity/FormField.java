package com.richinfo.datasource.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Table(name = "MM_SYS_FORMFIELD")
public class FormField extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private int fieldid;
	
	@Column(name="formid",length=50)
	private String formid;

	@Column(name="fieldtype",length=50)
	private String fieldtype;
	
	@Column(name="htmltype",length=50)
	private int htmltype;
     
	@Column(name="fieldname",length=200)
	private String fieldname;

	@Column(name="labelname",length=200)
	private String labelname;
	
	@Column(name="fieldattr",length=200)
	private String fieldattr; 
	
	@Column(name="status")
	private String status;
 
	@Column(name="ctime",nullable = false)
	private Long ctime;
	
	@Column(name="ispush")
	private String ispush ;
	
	@Column(name="fieldorder")
	private int fieldorder ;
	
	@Column(name="isedit")
	private int isedit ;
	
	@Column(name="CHOICEID")
	private int choiceid ;
	
	@Column(name="fieldcheck")
	private String fieldcheck;
	
	@Column(name="checktips")
	private String checktips;
	
	@Column(name="isrequired")
	private String isrequired;
	
    @Transient
	private String fieldvalue;
	
	public int getFieldid() {
		return fieldid;
	}

	public void setFieldid(int fieldid) {
		this.fieldid = fieldid;
	}

	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getLabelname() {
		return labelname;
	}

	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}
	public String getFieldattr() {
		return fieldattr;
	}

	public void setFieldattr(String fieldattr) {
		this.fieldattr = fieldattr;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getFieldvalue() {
		return fieldvalue;
	}

	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}

	public int getHtmltype() {
		return htmltype;
	}

	public void setHtmltype(int htmltype) {
		this.htmltype = htmltype;
	}

	
	public String getIspush() {
		return ispush;
	}

	public void setIspush(String ispush) {
		this.ispush = ispush;
	}

	public int getFieldorder() {
		return fieldorder;
	}

	public void setFieldorder(int fieldorder) {
		this.fieldorder = fieldorder;
	}

	public int getIsedit() {
		return isedit;
	}

	public void setIsedit(int isedit) {
		this.isedit = isedit;
	}
	

	public int getChoiceid() {
		return choiceid;
	}

	public void setChoiceid(int choiceid) {
		this.choiceid = choiceid;
	}

	public String getFieldcheck() {
		return fieldcheck;
	}

	public void setFieldcheck(String fieldcheck) {
		this.fieldcheck = fieldcheck;
	}
    
	public String getChecktips() {
		return checktips;
	}

	public void setChecktips(String checktips) {
		this.checktips = checktips;
	}

	public String getIsrequired() {
		return isrequired;
	}

	public void setIsrequired(String isrequired) {
		this.isrequired = isrequired;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
 
}

