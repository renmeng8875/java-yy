package com.richinfo.datasource.entity;

import java.util.List;

import com.richinfo.common.utils.dateUtil.DateTimeUtil;

public class TableFieldValue {
	
	private String ctime;
	
	private String utime;
	
	private String dataid;
	
	private List<FieldValue> fieldlist;

	public String getCtime() {
		this.ctime = DateTimeUtil.getTimeStamp(Long.valueOf(ctime), "yyyy-MM-dd HH:mm:ss");
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getUtime() {
		this.utime = DateTimeUtil.getTimeStamp(Long.valueOf(utime), "yyyy-MM-dd HH:mm:ss");
		return utime;
	}

	public void setUtime(String utime) {
		this.utime = utime;
	}

	public List<FieldValue> getFieldlist() {
		return fieldlist;
	}

	public void setFieldlist(List<FieldValue> fieldlist) {
		this.fieldlist = fieldlist;
	}

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

 
	
}

