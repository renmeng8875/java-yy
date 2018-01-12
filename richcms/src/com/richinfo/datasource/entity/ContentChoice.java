package com.richinfo.datasource.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Table(name = "MM_CONTENT_CHOICE")
public class ContentChoice extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private int id;
	
	@Column(name="datatype",length=50)
	private String datatype;
	
	@Column(name="tagid")
	private int tagid;

	@Column(name="contentid",length=50)
	private String contentid;
	
	@Column(name="choiceid")
	private int choiceid;
	
	@Column(name="fieldid")
	private int fieldid;
	
	public ContentChoice(){
		
	}
	 
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public int getTagid() {
		return tagid;
	}

	public void setTagid(int tagid) {
		this.tagid = tagid;
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public int getChoiceid() {
		return choiceid;
	}

	public void setChoiceid(int choiceid) {
		this.choiceid = choiceid;
	}

	public int getFieldid() {
		return fieldid;
	}

	public void setFieldid(int fieldid) {
		this.fieldid = fieldid;
	}

	@Override
	public String[] getExcludesAttributes() 
	{
		return null;
	}
	 
	 
	
}
