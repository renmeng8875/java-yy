package com.richinfo.datasource.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.Constants;
import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.datasource.dao.DataSourceDao;
import com.richinfo.datasource.dao.FormFieldDao;
import com.richinfo.datasource.dao.FormInfoDao;
import com.richinfo.datasource.entity.DataSource;
import com.richinfo.datasource.entity.FormField;
import com.richinfo.datasource.entity.FormInfo;
import com.richinfo.datasource.service.DataSourceConfigService;
import com.richinfo.privilege.dao.MenuDao;
import com.richinfo.privilege.entity.Menu;

@Service("DataSourceConfigService")
public class DataSourceConfigServiceImpl extends BaseServiceImpl<DataSource, Integer>
		implements DataSourceConfigService {

	private DataSourceDao dataSourceDao;

	private FormFieldDao formFieldDao;
	
	private FormInfoDao formInfoDao;
	
	 private MenuDao menuDao;

	@Autowired
	@Qualifier("DataSourceDao")
	public void setDataSourceDao(DataSourceDao dataSourceDao) {
		this.dataSourceDao = dataSourceDao;
	}

	@Autowired
	@Qualifier("FormFieldDao")
	public void setFormFieldDao(FormFieldDao formFieldDao) {
		this.formFieldDao = formFieldDao;
	}
	
	@Autowired
	@Qualifier("FormInfoDao")
	public void setFormInfoDao(FormInfoDao formInfoDao) {
		this.formInfoDao = formInfoDao;
	}
	@Autowired
    @Qualifier("MenuDao")
	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Autowired
	@Qualifier("DataSourceDao")
	@Override
	public void setBaseDao(BaseDao<DataSource, Integer> baseDao) {
		this.baseDao = (DataSourceDao) baseDao;
	}
 
	/**
	 * 添加表单字段
	 */
	@RenmSelf(methodDesc="添加表单字段")
	public boolean addTableField(FormField formfield) {
		// TODO Auto-generated method stub
		try{
			FormInfo form = this.getFormInfoById(Integer.valueOf(formfield.getFormid()));
			if("base".equals(form.getFormtype())&&"1".equals(formfield.getIspush())){
				List<FormInfo> formlists = this.getFormInfoLists();
				for(FormInfo f: formlists){
					this.saveOrUpdateFiled(f,formfield);
				}
			}else{
				this.saveOrUpdateFiled(form,formfield);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void saveOrUpdateFiled(FormInfo form ,FormField formfield){
    	String sql = "";
    	FormField field = this.checkfieldIsExit(form,formfield);
		if(field==null){
			field = this.createBean(field, formfield, form);
		    boolean bln = this.checkColumnIsExit(form.getFormname(), formfield.getFieldname());
		    if(bln){//存在
		    	if(!checkfieldIsUse(form,formfield)){//字段未使用，修改字段
		    		sql = "alter table "+form.getFormname()+" modify "+formfield.getFieldname()+" "+getFieldType(formfield)+"";
		    		dataSourceDao.updateBySql(sql, null);
		    	}
		    }else{
		    	sql = "alter table "+form.getFormname()+" add "+field.getFieldname()+" "+getFieldType(field);
				dataSourceDao.updateBySql(sql, null);
		    }
			formFieldDao.add(field);
		}else{
			if(!checkfieldIsUse(form,formfield)){//字段未使用，修改字段
				sql = "alter table "+form.getFormname()+" modify "+formfield.getFieldname()+" "+getFieldType(formfield)+"";
				dataSourceDao.updateBySql(sql, null);
			} 
			formfield.setFieldid(field.getFieldid());
			field = this.createBean(field, formfield, form);
			formFieldDao.update(field);
		}
    }
	/**
	 * 
	 * @param newbean
	 * @param tarbean
	 * @return
	 */
	private FormField createBean(FormField newbean,FormField tarbean,FormInfo form){
		if(newbean==null){
			newbean = new FormField();
		}
		try {
			BeanUtils.copyProperties(newbean, tarbean);
			if("extends".equals(form.getFormtype())){
				//newbean.setIspush("0");
	    	}
			newbean.setFormid(form.getFormid()+"");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return newbean;
	}
	/**
	 */
	private String getFieldType(FormField formfield){
		String fieldtype = "";
		if("number".equals(formfield.getFieldtype())){
			if("".equals(formfield.getFieldattr()))
				fieldtype = "number";
			else
				fieldtype = formfield.getFieldtype()+"("+formfield.getFieldattr()+")";
		}else if("clob".equals(formfield.getFieldtype())||"nclob".equals(formfield.getFieldtype())){
			    fieldtype = formfield.getFieldtype();
		}else{
			if("".equals(formfield.getFieldattr()))
				fieldtype = formfield.getFieldtype()+"(256)";
			else
				fieldtype = formfield.getFieldtype()+"("+formfield.getFieldattr()+")";
		}
		return fieldtype;
	}

	public FormField checkfieldIsExit(FormInfo form,FormField field) {
		// TODO Auto-generated method stub
		FormField f = (FormField) this.formFieldDao.queryObject("from FormField where formid=? and fieldname = ?",new Object[]{form.getFormid()+"",field.getFieldname()}, null);
		return f;
	}

	public boolean checkfieldIsUse(FormInfo form,FormField formfield) {
		// TODO Auto-generated method stub
		int i = formFieldDao.getJdbcTemplate().queryForInt("select count(*) from "+form.getFormname() +" where "+formfield.getFieldname() +" is not null");
		if(i>0){
			return true;
		}
		return false;
	}
	
	public List<FormField> getFieldListByTableId(String tableid) {
		// TODO Auto-generated method stub
		List<FormField> fieldlist = formFieldDao.list("from FormField where formid=? order by fieldorder", new Object[]{tableid}, null);
		return fieldlist;
	}

	public List<FormInfo> getFormInfoLists() {
		// TODO Auto-generated method stub
		List<FormInfo> formlist = formInfoDao.list("from FormInfo", null, null);
		return formlist;
	}

	public List<FormInfo> getFormInfoListsByType(String formtype) {
		// TODO Auto-generated method stub
		List<FormInfo> formlist = formInfoDao.list("from FormInfo where formtype=?", new Object[]{formtype}, null);
		return formlist;
	}
	
	public FormInfo getFormInfoById(int formid) {
		// TODO Auto-generated method stub
		return formInfoDao.get(formid);
	}
	@RenmSelf(methodDesc="添加数据源")
	public boolean saveOrUpdateFormInfo(FormInfo formInfo) {
		// TODO Auto-generated method stub
		if(!checkformIsExit(formInfo.getFormname().toUpperCase())){
		 	List<FormField> fieldlist = this.getContentDataFiled();
		 	StringBuffer sb = new StringBuffer();
		 	if(fieldlist!=null){
		 		for(FormField field : fieldlist){
		 			sb.append( field.getFieldname() + " "+getFieldType(field) +",");
		 		}
		 	}
			String sql = "create table "+formInfo.getFormname()+"(dataid number,"+sb.toString()+" onlinetime number,utime number)";
			formInfoDao.updateBySql(sql, null);//创建表
			formInfoDao.saveOrUpdate(formInfo);
			if("extends".equals(formInfo.getFormtype())){
				this.createDataSourceMenu(formInfo);
			}
		}else{
			formInfoDao.saveOrUpdate(formInfo);
			if("extends".equals(formInfo.getFormtype())){
				this.createDataSourceMenu(formInfo);
			}
			return false;
		}
		return true;
		 
	}
	@RenmSelf(methodDesc="添加数据源对应的栏目")
	public boolean createDataSourceMenu(FormInfo formInfo){
		Menu m = new Menu();
		m.setAction(Constants.DATASOURCEMENU_ACTION);
		m.setControl(Constants.DATASOURCEMENU_CONTROL);
		Menu pmenu = this.getMenuByInfo(m,"");
		m = null;
		m = new Menu();
		m.setParent(pmenu);
		m.setAction("lists.do?module="+formInfo.getDatatype());
		m.setControl("datasource");
		m.setModule("Admin");
		m.setMenu(formInfo.getFormdesc());
		m.setIsHidden(0);
		m.setOrderby(99);
		m.setMenuLevel(3);
		Menu menu = this.getMenuByInfo(m,"c");
		if(menu==null)
		     menuDao.add(m);
		 else
			 m.setMenuId(menu.getMenuId());
			 menuDao.merge(m);
		return true;
	}
	
	public Menu getMenuByInfo(Menu m , String type){
		String hql = "from Menu t where t.control=? and t.action=?";
		Menu menu = null;
		if("c".equals(type)){
			hql = "from Menu t where t.control=? and t.action=? and t.parent.menuId=?";
			menu = (Menu)menuDao.queryObject(hql,new Object[]{m.getControl(),m.getAction(),m.getParent().getMenuId()}, null);
		}else{
			menu = (Menu)menuDao.queryObject(hql,new Object[]{m.getControl(),m.getAction()}, null);
		}
		return menu;
	}
	public boolean checkformIsExit(String formname) {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			count = formInfoDao.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM USER_OBJECTS WHERE  OBJECT_NAME = ?", new Object[]{formname});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	    if(count>0){
	    	return true;
	    }
		return false;
	}

	private boolean checkColumnIsExit(String tablename,String column){
		String sql = " SELECT COUNT(*) FROM cols WHERE table_name=UPPER('"+tablename+"') AND column_name=UPPER('"+column+"')";
		int count = formInfoDao.getJdbcTemplate().queryForInt(sql);
		if(count > 0){
			return true;
		}
		return false;
	}
	public FormField getFormFieldById(int fieldid) {
		// TODO Auto-generated method stub
		FormField field = (FormField) formFieldDao.queryObject("from FormField where fieldid=?", new Object[]{fieldid}, null);
		return field;
	}
	@RenmSelf(methodDesc="删掉指定id表单字段")
	public Map<String,String> deleteField(int fieldid) {
		// TODO Auto-generated method stub
		Map<String,String> json = new HashMap<String,String>();
		FormField field = this.getFormFieldById(fieldid);
		FormInfo form = this.getFormInfoById(Integer.valueOf(field.getFormid()));
		int count = this.getCountByFieldName(field.getFieldname(),form.getFormname());
		if(count>0){//字段已使用
			json.put("status", "-1");
			json.put("msg", "字段已使用，不可删除");
			return json;
		}
		if("1".equals(field.getIspush())&&"extends".equals(form.getFormtype())){//推送字段不能删除
			json.put("status", "-2");
			json.put("msg", "关联字段，不可删除");
			return json;
		}
		try{
			if("1".equals(field.getIspush())&&"base".equals(form.getFormtype())){
				List<FormInfo> formlist = this.getFormInfoLists();
				for(FormInfo table : formlist ){
					FormField f =  this.checkfieldIsExit(table, field);
					this.deleteFiled(table, f);
				}
			}else{
				this.deleteFiled(form, field);
			}
			json.put("status", "0");
		    json.put("msg", "字段删除成功");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", "-3");
			json.put("msg", "系统异常，请重试");
		}
		return json;
	}

	private void deleteFiled(FormInfo form , FormField field){
		String sql = "alter table "+form.getFormname() +" drop column "+field.getFieldname();
		this.formInfoDao.getJdbcTemplate().execute(sql);
		this.formFieldDao.delete(field.getFieldid());
		 
	}
	@RenmSelf(methodDesc="删除数据源")
	public boolean deleteForm(int formid) {
		// TODO Auto-generated method stub
		FormInfo form = this.getFormInfoById(formid);
		int count = this.getCountByFormName(form.getFormname());
		if(count>0){
			return false;
		}
		try{
			String sql = "drop table "+form.getFormname();
			this.formInfoDao.getJdbcTemplate().execute(sql);
			this.formInfoDao.delete(formid);
			this.formFieldDao.updateBySql("delete mm_sys_formfield where formid=?", new Object[]{formid});
			Menu m = new Menu();
			m.setAction(Constants.DATASOURCEMENU_ACTION);
			m.setControl(Constants.DATASOURCEMENU_CONTROL);
			Menu pmenu = this.getMenuByInfo(m,"");
			this.menuDao.updateBySql("delete Menu t where t.action=? and t.control=? and t.parent.menuId=?", new Object[]{"lists.do?module="+form.getDatatype(),"datasource",pmenu.getMenuId()});
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public int getCountByFormName(String formname) {
		// TODO Auto-generated method stub
		String sql = "select count(1) from "+formname ;
		int a = formInfoDao.getJdbcTemplate().queryForInt(sql);
		return a;
	}

	public int getCountByFieldName(String fieldname,String formname) {
		// TODO Auto-generated method stub
		String sql = "select count("+fieldname+") from "+formname ;
		int a = formInfoDao.getJdbcTemplate().queryForInt(sql);
		return a;
	}
	
	public List<FormField> getContentDataFiled() {
		// TODO Auto-generated method stub
		FormInfo form = (FormInfo) this.formInfoDao.queryObject("from FormInfo where formname=?", new Object[]{Constants.CONTENTDATA}, null);
		if(form==null){
			return null;
		}
		List<FormField> fieldlist = this.formFieldDao.list("from FormField where formid = ? and ispush=1", new Object[]{form.getFormid()+""}, null);
		return fieldlist;
	}

	public List<FormInfo> getExtendsFormLists(){
		// TODO Auto-generated method stub
		List<FormInfo> formlists = this.formInfoDao.list("from FormInfo where formtype=?", new Object[]{"extends"} , null);
		return formlists;
	}

	public FormField getFormFieldByName(String fieldname,String formid) {
		// TODO Auto-generated method stub
		FormField f = (FormField)this.formFieldDao.queryObject("from FormField where fieldname=? and formid = ?", new Object[]{fieldname,formid}, null);
		return f;
	}

	
	
	

}
