package com.richinfo.module.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SYS_DATA_RELATION")
public class DataRelation extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
 
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "srcid", nullable = false)
	private int srcid;
	
	@Column(name="source",length=50)
	private String source;
	
	@Column(name="datatype",length=50)
	private String datatype;
	
	@Column(name="ext",length=50)
	private String ext;
	
	@Column(name="template",length=500)
	private String template;
     
	 

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}



	public int getSrcid() {
		return srcid;
	}



	public void setSrcid(int srcid) {
		this.srcid = srcid;
	}



	public String getSource() {
		return source;
	}



	public void setSource(String source) {
		this.source = source;
	}



	public String getDatatype() {
		return datatype;
	}



	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}



	public String getExt() {
		return ext;
	}



	public void setExt(String ext) {
		this.ext = ext;
	}



	public String getTemplate() {
		template = "'"+template+"'";
		return template;
	}



	public void setTemplate(String template) {
		
		this.template = template;
	}

	
	 
	 
	
}
