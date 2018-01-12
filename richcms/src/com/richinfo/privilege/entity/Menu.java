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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;



import com.richinfo.common.entity.BaseEntity;

@Entity
@Table(name = "mm_sys_menu_java",uniqueConstraints = {@UniqueConstraint(columnNames={"pid", "control", "module", "action"})})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="first")
@SequenceGenerator(name = "gen_mm_sys_menu_java", sequenceName = "SEQ_MM_SYS_MENU")
public class Menu extends BaseEntity{

	private static final long serialVersionUID = 48320902213233046L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="gen_mm_sys_menu_java")
    @Column(name = "menuid", nullable = false)
	private Integer menuId;
	
	@Column(name = "orderby", nullable = true,length=3,columnDefinition="default 99")
	private Integer orderby;
	
	@Column(name = "menu", nullable = true,length=50)
	private String menu;
	
	
	/**
	 * 0为显示，1为隐藏
	 */
	@Column(name = "ishidden", nullable = true,length=1)
	private Integer isHidden;
	
	@Column(name = "menulevel", nullable = true,length=2)
	private Integer menuLevel = 1;
	
	@Column(name = "ext", nullable = true,length=2)
	private String ext;
	
	@Column(name = "module", nullable = false,length=50)
	private String module;
	
	@Column(name = "control", nullable = false,length=50)
	private String control;
	
	@Column(name = "action", nullable = false,length=50)
	private String action;
	

	@ManyToOne(fetch=FetchType.LAZY)   
	@JoinColumn(name="pid")  
	private Menu parent;
	
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE}, fetch=FetchType.LAZY, mappedBy="parent")
	@OrderBy("orderby")
	private Set<Menu> children = new HashSet<Menu>();
	
	public Set<Menu> getChildren() {
		return children;
	}

	public void setChildren(Set<Menu> children) {
		this.children = children;
	}
    public Menu(){
		
	}
	public Menu(int menuLevel){
		this.menuLevel = menuLevel;
	}
	public boolean isLeaf(){
		return children.size()==0;
	}

	

	

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}


	public int getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(int isHidden) {
		this.isHidden = isHidden;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String[] getExcludesAttributes() {
		return new String[]{"parent","children"};
	}

	@Override
	public String toString() {
		StringBuffer  sb = new StringBuffer();
		sb.append("menuId="+this.getMenuId()).append("|orderby="+this.getOrderby())
		.append("|menu="+this.getMenu()).append("|isHidden="+this.getIsHidden())
		.append("|menuLevel="+this.getMenuLevel()).append("|ext="+this.getExt())
		.append("|module="+this.getModule()).append("|control="+this.getControl())
		.append("|action="+this.getAction()).append("|pid=");
		if(this.getParent()!=null)
		{
			sb.append(this.getParent().getMenuId());
		}
		return sb.toString();
	}
	
	

	

}
