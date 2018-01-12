package com.richinfo.datasource.entity;

import com.richinfo.common.entity.BaseEntity;


public class DataSource extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dataid;
	
	private String contentid;
	
	private String title;
	
	private String p_title;
	
	

	public String getDataid() {
		return dataid;
	}



	public void setDataid(String dataid) {
		this.dataid = dataid;
	}



	public String getContentid() {
		return contentid;
	}



	public void setContentid(String contentid) {
		this.contentid = contentid;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getP_title() {
		return p_title;
	}



	public void setP_title(String p_title) {
		this.p_title = p_title;
	}



	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
