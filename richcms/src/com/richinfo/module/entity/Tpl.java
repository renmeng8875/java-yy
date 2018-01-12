package com.richinfo.module.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@Table(name = "MM_SYS_TPL")
public class Tpl extends BaseEntity {

	private static final long serialVersionUID = -39857904948861603L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tid", nullable = false)
	private int tId;

	@Column(name = "styleid")
	private int styleId;

	@Column(name = "typeen", length = 300)
	private String tyPeen;

	@Column(name = "typename", length = 300)
	private String typeName;

	@Column(name = "listpath", length = 300)
	private String listPath;

	@Column(name = "infopath", length = 300)
	private String infoPath;

	@Column(name = "indexpath", length = 300)
	private String indexPath;
	
	@Transient
	private String style;
	@Transient
	private String stylename;
	
	@Transient
	private List<Template> template;
	

	public int gettId() {
		return tId;
	}

	public void settId(int tId) {
		this.tId = tId;
	}

	public int getStyleId() {
		return styleId;
	}

	public void setStyleId(int styleId) {
		this.styleId = styleId;
	}

	public String getTyPeen() {
		return tyPeen;
	}

	public void setTyPeen(String tyPeen) {
		this.tyPeen = tyPeen;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getListPath() {
		return listPath;
	}

	public void setListPath(String listPath) {
		this.listPath = listPath;
	}

	public String getInfoPath() {
		return infoPath;
	}

	public void setInfoPath(String infoPath) {
		this.infoPath = infoPath;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStylename() {
		return stylename;
	}

	public void setStylename(String stylename) {
		this.stylename = stylename;
	}

 

	public List<Template> getTemplate() {
		return template;
	}

	public void setTemplate(List<Template> template) {
		this.template = template;
	}

	@Override
	public String[] getExcludesAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
 
