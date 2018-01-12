package com.richinfo.privilege.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;

@Entity
@Table(name = "MM_CONTENT_CAT",uniqueConstraints = {@UniqueConstraint(columnNames={"cat","pid"})})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@SequenceGenerator(name = "MM_CONTENT_CAT_SEQ", sequenceName = "SEQ_MM_CT_CAT")
public class Category extends BaseEntity
{
	private static final long serialVersionUID = 6304433767085603092L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="MM_CONTENT_CAT_SEQ")
    @Column(name = "CATID", nullable = false)
	private Integer catId;
	
	@Column(name="cat",length=50)
	private String cat;
	
	@Column(name="catname",length=50)
	private String name;
	
	@Column(name="image",length=1000)
	private String image;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="pid") 
	private Category parent ;
	
	@Transient
	private String startTimeStr;
	
	@Transient
	private String endTimeStr;
	
	
	public Category() {
	}
	
	public Category(Integer catLevel) {
		this.catLevel = catLevel;
	}

	@Transient
	private String pid;
	
	public String getPid() 
	{
		if(this.parent!=null){
			return this.parent.getCatId()+"";
		}
		this.pid="";
		return pid;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="parent")
	@OrderBy("priority")
	private Set<Category> children = new HashSet<Category>();
	
	

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	public boolean isLeaf(){
		return children.size()==0;
	}
	
	@Column(name="catLevel",length=1)
	private Integer catLevel;
	
	@Column(name="workflowId")
	private Integer workflowId;
	
	@Column(name="childId",length=4000)
	private String childId;
	
	@Column(name="seoTitle",length=50)
	private String seoTitle;
	
	@Column(name="seoDesc",length=200)
	private String seoDesc;
	
	@Column(name="seoKeyword",length=50)
	private String seoKeyword;
	
	@Column(name="priority")
	private Integer priority;
	
	@Column(name="mem",length=500)
	private String mem;
	
	@Column(name="userid")
	private Integer userId;
	
	@Column(name="style",length=50)
	private String style;
	
	@Column(name="tplpath",length=200)
	private String tplpath;
	
	@Column(name="jumpurl",length=300)
	private String jumpurl;
	
	@Column(name="ishidden")
	private Integer isHidden;
	
	@Column(name="sTime")
	private Long startTime;
	
	@Column(name="eTime")
	private Long endTime;
	

	@Column(name="catDesc",length=150)
	private String catDesc;

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	

	public Integer getCatLevel() {
		return catLevel;
	}

	public void setCatLevel(Integer catLevel) {
		this.catLevel = catLevel;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDesc() {
		return seoDesc;
	}

	public void setSeoDesc(String seoDesc) {
		this.seoDesc = seoDesc;
	}

	public String getSeoKeyword() {
		return seoKeyword;
	}

	public void setSeoKeyword(String seoKeyword) {
		this.seoKeyword = seoKeyword;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTplpath() {
		return tplpath;
	}

	public void setTplpath(String tplpath) {
		this.tplpath = tplpath;
	}

	public String getJumpurl() {
		return jumpurl;
	}

	public void setJumpurl(String jumpurl) {
		this.jumpurl = jumpurl;
	}

	public Integer getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Integer isHidden) {
		this.isHidden = isHidden;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

	@Override
	public String[] getExcludesAttributes() {
		
		return new String[]{"parent","children"};
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	public String getStartTimeStr() {
		this.startTimeStr = DateTimeUtil.getTimeStamp(startTime, "yyyy-MM-dd",false);
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	
	public String getEndTimeStr() {
		this.endTimeStr = DateTimeUtil.getTimeStamp(endTime, "yyyy-MM-dd",false);
		return endTimeStr;
	}
	
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	
	
}
