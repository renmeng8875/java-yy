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
@Table(name = "MM_SYS_SEARCH")
public class Search extends BaseEntity {
	private static final long serialVersionUID = 6304433767085603092L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "sid", nullable = false)
	private int sid;

	@Column(name = "datasource", length = 50)
	private String datasource;

	@Column(name = "datatype", length = 50)
	private String datatype;

	@Column(name = "priority")
	private int priority;

	@Column(name = "mem", length = 200)
	private String mem;

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

}
