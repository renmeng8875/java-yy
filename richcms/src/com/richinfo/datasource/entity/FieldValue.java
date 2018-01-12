package com.richinfo.datasource.entity;

import org.apache.commons.lang.StringUtils;

import com.richinfo.common.utils.dateUtil.DateTimeUtil;

public class FieldValue{
	private int fieldid;
	
	private String formid;
	
	private int htmltype;
	private String fieldtype;
	
	private String fieldname;
	
	private String labelname;
	
	private String fieldattr; 
	
	private String fieldcheck;
	
	private String status;
	
	private int choiceid;
	
	private String fieldvalue;
	
	private int isedit;
	
	private String checktips;
	
	private String isrequired;

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
		if(this.getHtmltype()==6){
			if(!"".equals(fieldvalue)&&StringUtils.isNumeric(fieldvalue)){
				this.fieldvalue = DateTimeUtil.getTimeStamp(Long.valueOf(fieldvalue), "yyyy-MM-dd HH:mm:ss");
			}else{
				this.fieldvalue = "";//DateTimeUtil.getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
			}
		}
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

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
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

	public String getChecktips() {
		return checktips;
	}

	public void setChecktips(String checktips) {
		this.checktips = checktips;
	}

	public void setFieldcheck(String fieldcheck) {
		this.fieldcheck = fieldcheck;
	}

	public String getIsrequired() {
		return isrequired;
	}

	public void setIsrequired(String isrequired) {
		this.isrequired = isrequired;
	}
	
	
    	
}
