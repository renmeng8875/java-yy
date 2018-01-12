package com.richinfo.datasource.service.impl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.datasource.dao.ChoiceConfigDao;
import com.richinfo.datasource.dao.ContentChoiceDao;
import com.richinfo.datasource.dao.DataSourceDao;
import com.richinfo.datasource.dao.FormFieldDao;
import com.richinfo.datasource.dao.FormInfoDao;
import com.richinfo.datasource.entity.Choice;
import com.richinfo.datasource.entity.ContentChoice;
import com.richinfo.datasource.entity.DataSource;
import com.richinfo.datasource.entity.FieldValue;
import com.richinfo.datasource.entity.FormField;
import com.richinfo.datasource.entity.FormInfo;
import com.richinfo.datasource.entity.TableFieldValue;
import com.richinfo.datasource.service.DataSourceService;

@Service("DataSourceService")
public class DataSourceServiceImpl extends BaseServiceImpl<DataSource, Integer>
		implements DataSourceService {


	private DataSourceDao dataSourceDao;
   
	private FormInfoDao formInfoDao;
	
	private FormFieldDao formFieldDao;
	
	private ContentChoiceDao contentChoiceDao;
	
	private ChoiceConfigDao choiceDao; 
	
	private final static Logger log = Logger.getLogger("accessLog");
	
	@Autowired
	@Qualifier("DataSourceDao")
	public void setDataSourceDao(DataSourceDao dataSourceDao) {
		this.dataSourceDao = dataSourceDao;
	}
	
	@Autowired
	@Qualifier("FormInfoDao")
	public void setFormInfoDao(FormInfoDao formInfoDao) {
		this.formInfoDao = formInfoDao;
	}
	 
	@Autowired
	@Qualifier("FormFieldDao")
	public void setFormFieldDao(FormFieldDao formFieldDao) {
		this.formFieldDao = formFieldDao;
	}
	
	@Autowired
	@Qualifier("ContentChoiceDao")
	public void setContentChoiceDao(ContentChoiceDao contentChoiceDao) {
		this.contentChoiceDao = contentChoiceDao;
	}
	
	@Autowired
	@Qualifier("ChoiceConfigDao")
	public void setChoiceConfigDao(ChoiceConfigDao choiceDao) {
		this.choiceDao = choiceDao;
	}

	@Autowired
	@Qualifier("DataSourceDao")
	@Override
	public void setBaseDao(BaseDao<DataSource, Integer> baseDao) {
		this.baseDao = (DataSourceDao) baseDao;
	}



	public FormInfo getFormByName(String formname) {
		// TODO Auto-generated method stub
		FormInfo form = (FormInfo) formInfoDao.queryObject("from FormInfo where formname=?", new Object[]{formname}, null);
		return form;
	}

	public List<FormField> getFormFieldByFormid(String formid,String type) {
		// TODO Auto-generated method stub
		List<FormField> fieldlist = null;
		String status = "2";
		if("list".equals(type)){
			status = "1";
		}else if("info".equals(type)){
			status = "2";
		}else{
			status = "3";
		}
		fieldlist = formFieldDao.list("from FormField where formid=? and status like '%"+status+"%' order by fieldorder", new Object[]{formid}, null);
		return fieldlist;
	}

	public List<TableFieldValue> getFormValueForLists(String formname,List<FormField> fieldlist) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		for(FormField field : fieldlist){
			sb.append(field.getFieldname()+", ");
		}
		sb.append(" onlinetime,utime,dataid from "+formname);
		
	    List<?> rows = dataSourceDao.getJdbcTemplate().queryForList(sb.toString());
	    
	    Iterator<?> it = rows.iterator();  
	    List<TableFieldValue> list = new ArrayList<TableFieldValue>();
	    while(it.hasNext()) {  
	        Map<?,?> rowmap = (Map<?,?>) it.next();
	        TableFieldValue values = new TableFieldValue();
	        List<FieldValue> fields = new ArrayList<FieldValue>();
            for(FormField field : fieldlist){
            	FieldValue f = new FieldValue(); 
            	try {
					BeanUtils.copyProperties(f, field);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	f.setFieldvalue(CommonUtil.null2String(rowmap.get(field.getFieldname())));
            	fields.add(f);
            	f = null;
            }
            values.setCtime(rowmap.get("onlinetime").toString());
            values.setUtime(rowmap.get("utime").toString());
            values.setDataid(rowmap.get("dataid").toString());
            values.setFieldlist(fields);
            list.add(values);
	    } 
		return list;
	}

	public List<TableFieldValue> getFormValueForPageList(String formname,List<FormField> fieldlist,Map<String,Object> search,int pageNo,int pageSize) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		StringBuffer where = new StringBuffer();
		sb.append("select ");
		for(FormField field : fieldlist){
			sb.append(field.getFieldname()+", ");
		}
		sb.append(" onlinetime,utime,dataid from "+formname + " where 1=1 ");
		String key = "";
		String datatype = formname.substring(Constants.FORMPREFIX.length());
	    for (Entry<String, Object> entry: search.entrySet()) {
            key = entry.getKey();
            if(key.startsWith("t_")||"onlinetime".equals(key)||"utime".equals(key)){
            	@SuppressWarnings("unchecked")
				Map<String,String> time = (Map<String, String>) entry.getValue();
            	if(key.indexOf("t_")!=-1){
            		key = key.substring(2);
            	}
            	where.append(" and ("+key + " >" + time.get("stime") +" and "+key+"<"+time.get("etime")+")" );
            }else if("contentid".equals(key)){ 
            	if(!"".equals(entry.getValue())){
            		String[] ids = entry.getValue().toString().split(",");
            		StringBuffer s = new StringBuffer();
            		for(String id :ids){
            			s.append("'"+id+"',");
            		}
            		where.append(" and "+key + " in ("+s.toString().substring(0,s.toString().length()-1)+")");
            	}
            }else if(key.indexOf("price")!=-1){
            	switch(Integer.valueOf(entry.getValue().toString())){
            	  case 0:  break;
            	  case 1:  if("ios".equals(datatype)){where.append("and srcprice>price and price = 0");}break;
            	  case 2:  if("ios".equals(datatype)){where.append("and srcprice>price and price > 0");}break;
            	  case 3:  where.append("and price > 0");break;
            	  case 4:  where.append("and price = 0");break;
            	}
            }else if(key.indexOf("ismmtoevent")!=-1){
            	switch(Integer.valueOf(entry.getValue().toString())){
	          	  case 0:  where.append("and ismmtoevent is null");break;
	          	  case 1:  where.append("and ismmtoevent is not null");break;
	          	}
            }else{
            	if(entry.getValue()!=""){
            		where.append(" and "+key +" like '%"+entry.getValue()+"%'");
            	}
            }
        }
		String sqlCountRows = "select count(*) from "+formname + " where 1=1 " + where.toString();
		String sqlFetchRows = sb.toString() + where.toString() + " order by onlinetime desc";
	    List<?> rows = dataSourceDao.fetchPage(sqlCountRows, sqlFetchRows, null, pageNo, pageSize);
	    Iterator<?> it = rows.iterator();  
	    List<TableFieldValue> list = new ArrayList<TableFieldValue>();
	    while(it.hasNext()) {  
	        Map<?,?> rowmap = (Map<?,?>) it.next();
	        TableFieldValue values = new TableFieldValue();
	        List<FieldValue> fields = new ArrayList<FieldValue>();
            for(FormField field : fieldlist){
            	FieldValue f = new FieldValue(); 
            	try {
					BeanUtils.copyProperties(f, field);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	f.setFieldvalue(CommonUtil.null2String(rowmap.get(field.getFieldname())));
            	fields.add(f);
            	f = null;
            }
            values.setCtime(CommonUtil.null2String(rowmap.get("onlinetime")));
            values.setUtime(CommonUtil.null2String(rowmap.get("utime")));
            values.setDataid(CommonUtil.null2String(rowmap.get("dataid")));
            values.setFieldlist(fields);
            list.add(values);
	    } 
		return list;
	}
	public TableFieldValue getFormValueForInfo(String formname, List<FormField> fieldlist, String dataid,String editor) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for(FormField field: fieldlist){
			sql.append(field.getFieldname()+ ",");
		}
		sql.append(" dataid,onlinetime,utime from "+ formname);
		sql.append(" where dataid= ? ");
		List<?> rows = dataSourceDao.getJdbcTemplate().queryForList(sql.toString(),new Object[]{dataid});
	    Iterator<?> it = rows.iterator();  
	    List<TableFieldValue> list = new ArrayList<TableFieldValue>();
	    while(it.hasNext()) {  
	        Map<?,?> rowmap = (Map<?,?>) it.next();
	        TableFieldValue values = new TableFieldValue();
	        List<FieldValue> fields = new ArrayList<FieldValue>();
            for(FormField field : fieldlist){
            	FieldValue f = new FieldValue(); 
            	try {
					BeanUtils.copyProperties(f, field);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	f.setFieldvalue(CommonUtil.null2String(rowmap.get(field.getFieldname())));
            	if(f.getChoiceid()>0){
            		f.setFieldvalue(getChoices(CommonUtil.null2String(rowmap.get("contentid")),f.getChoiceid(),f.getFieldid()));
            	}
            	if("editor".equals(field.getFieldname())&&"".equals(f.getFieldvalue())){
            		f.setFieldvalue(editor);
            	}
            	fields.add(f);
            	f = null;
            }
            values.setDataid(CommonUtil.null2String(rowmap.get("dataid")));
            values.setCtime(CommonUtil.null2String(rowmap.get("onlinetime")));
            values.setUtime(CommonUtil.null2String(rowmap.get("utime")));
            values.setFieldlist(fields);
            list.add(values);
	    }
	   if(list.size()>0){
		   return list.get(0);
	   }else{
		   TableFieldValue values = new TableFieldValue();
	       List<FieldValue> fields = new ArrayList<FieldValue>();
           for(FormField field : fieldlist){
	           FieldValue f = new FieldValue(); 
	           try {
					BeanUtils.copyProperties(f, field);
			   } catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   } catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   }
	           if("contentid".equals(field.getFieldname()))
	        	   f.setFieldvalue(this.createContentid());
	           else
	        	   f.setFieldvalue("");
	           
	           if("editor".equals(field.getFieldname())&&"".equals(f.getFieldvalue())){
           		   f.setFieldvalue(editor);
           	   }
	           if("priority".equals(field.getFieldname())&&"".equals(f.getFieldvalue())){
	        	   f.setFieldvalue("99");
	           }
	           fields.add(f);
	           f = null;
           }
           values.setCtime(DateTimeUtil.getTimeStamp()+"");
           values.setUtime(DateTimeUtil.getTimeStamp()+"");
           values.setFieldlist(fields);
           list.add(values);
           return list.get(0);
	   }
	}
    
	private String getChoices(String contentid,int choiceid,int fieldid){
		List<ContentChoice> clist = (List<ContentChoice>) contentChoiceDao.list("from ContentChoice where contentid=? and choiceid=? and fieldid=?", new Object[]{contentid,choiceid,fieldid}, null);
	    Choice choice = choiceDao.get(Integer.valueOf(choiceid));
	    String choiceids ="";
	    String choicenames = "";
	    if(clist.size()>0){
	    	for(ContentChoice c : clist){
	    		String s = (String) this.dataSourceDao.getJdbcTemplate().queryForObject( choice.getViewcode(), new Object[] {c.getTagid()}, java.lang.String.class);
	    		choiceids += ","+c.getTagid();
	    		choicenames += ","+s;
	    	}
	    	choicenames = choicenames==""?"":choicenames.substring(1);
	    	choiceids=choiceids==""?"":choiceids.substring(1);
	    	return choicenames+";"+choiceids+"&"+choiceid+","+fieldid;
	    }else{
	    	return "";
	    }
	}
	public String createContentid(){
		Long  contentid = this.dataSourceDao.getJdbcTemplate().queryForLong("select SEQ_MM_DATASOURCE_NEW.NEXTVAL from dual ");
		return contentid+"";
	}
	@SuppressWarnings("unchecked")
	@RenmSelf(methodDesc="保存数据源应用信息")
	public boolean saveForm(Map<String, String> map) {
		// TODO Auto-generated method stub
		String tablename = "";
		StringBuffer fields = new StringBuffer();
		StringBuffer ikey = new StringBuffer();
		StringBuffer ivalue = new StringBuffer();
		List<String> values = new ArrayList<String>();
		String contentid = "";
		
		Map<String,Object> ccmap = new HashMap<String,Object>();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry<String, String> entry = it.next();
			if("formid".equals(entry.getKey())||"dataid".equals(entry.getKey())){
				if("formid".equals(entry.getKey())){
					tablename = this.formInfoDao.get(Integer.valueOf(entry.getValue())).getFormname();
				}
			}else if(entry.getKey().startsWith("t_")){
				fields.append(entry.getKey().substring(2) +" = ?, ");
				ikey.append(entry.getKey().substring(2) + ", ");
				try {
					Date t = DateTimeUtil.getDateFromStr(entry.getValue(), "yyyy-MM-dd HH:mm:ss");
					values.add(DateTimeUtil.getTimeStamp(t)+"");
					ivalue.append(DateTimeUtil.getTimeStamp(t)+", ");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(entry.getKey().endsWith("_choice")){
				String tags = entry.getValue().split("&")[0];
				if("".equals(tags)){
					continue;
				}
				fields.append(entry.getKey().substring(0,entry.getKey().lastIndexOf("_choice")) +" = ?, ");
				ikey.append(entry.getKey().substring(0,entry.getKey().lastIndexOf("_choice"))+ ", ");
				String paramids = entry.getValue().split("&")[1];
				String choiceid = paramids.split(",")[0];
				String fieldid =paramids.split(",")[1];
				values.add(""+tags+"");
				ivalue.append("'"+tags+ "', ");
				List<ContentChoice> contentChoice = new ArrayList<ContentChoice>();
				for(String tag: tags.split(",")){
					ContentChoice cc = new ContentChoice();
					cc.setChoiceid(Integer.valueOf(choiceid));
					cc.setTagid(Integer.valueOf(tag));
					cc.setFieldid(Integer.valueOf(fieldid));
					contentChoice.add(cc);
				}
				ccmap.put(paramids,contentChoice);
			}else{
				fields.append(entry.getKey() +" = ?, ");
				values.add(entry.getValue());
				ikey.append(entry.getKey() +", ");
				ivalue.append("'"+entry.getValue()+ "', ");
			}
		}
		if("".equals(map.get("dataid"))){
			contentid = createContentid();
		}else{
			contentid = this.getContentid(map.get("dataid"), tablename);
		}
		//更新选择项中间表
		String datatype =  tablename.substring(Constants.FORMPREFIX.length(),tablename.length());
		Set<?> set = ccmap.entrySet();         
		Iterator<?> i = set.iterator();         
		while(i.hasNext()){      
		     Map.Entry<String, Object> entry=(Map.Entry<String, Object>)i.next();    
		     String[] ids = entry.getKey().toString().split(",");
		     System.out.println(ids[0]+"   "+ids[1]);
		     contentChoiceDao.updateByHql("delete from ContentChoice where contentid=? and choiceid=? and fieldid=?", new Object[]{contentid,Integer.valueOf(ids[0]),Integer.valueOf(ids[1])});
		     List<ContentChoice> cclist = (List<ContentChoice>) entry.getValue();
		     for(ContentChoice c: cclist){
		    	 c.setDatatype(datatype);
		    	 c.setContentid(contentid);
		    	 contentChoiceDao.saveOrUpdate(c);
		     }
		}    
		fields.append(" onlinetime = ? , utime=? ");
        values.add(DateTimeUtil.getTimeStamp()+"");
        values.add(DateTimeUtil.getTimeStamp()+"");
        //保存应用数据
        String sql ="";
        if("".equals(map.get("dataid"))){
        	sql = "insert into "+tablename+"(dataid,contentid,"+ikey.toString() + " onlinetime,utime) values(SEQ_MM_CT_ANDROID.NEXTVAL,'"+contentid+"',"+ivalue.toString()+""+DateTimeUtil.getTimeStamp()+","+DateTimeUtil.getTimeStamp()+")";
        	this.dataSourceDao.getJdbcTemplate().update(sql);
        }else{
        	sql = "update "+tablename + " set "+fields.toString() + " where dataid = ?";
        	values.add(map.get("dataid"));
        	Object[] args = values.toArray();
        	this.dataSourceDao.getJdbcTemplate().update(sql, args);
        }
        System.out.println(sql);
		return true;
	}
    private String getContentid(String dataid,String tablename){
    	String contentid = (String) this.dataSourceDao.getJdbcTemplate().queryForObject( "select contentid from "+tablename +" where dataid=?", new Object[] {dataid}, java.lang.String.class);
        return contentid;
    }
	public int getTotalRows(String tablename,Map<String,Object> search) {
		// TODO Auto-generated method stub
		StringBuffer where = new StringBuffer();
		String key = "";
		String datatype = tablename.substring(Constants.FORMPREFIX.length());
		 for (Entry<String, Object> entry: search.entrySet()) {
	            System.out.println(entry.getKey() + "==="+ entry.getValue());
	            key = entry.getKey();
	            if(key.startsWith("t_")||"onlinetime".equals(key)||"utime".equals(key)){
	            	@SuppressWarnings("unchecked")
					Map<String,String> time = (Map<String, String>) entry.getValue();
	            	if(key.indexOf("t_")!=-1){
	            		key = key.substring(2);
	            	}
	            	where.append(" and ("+key + " >" + time.get("stime") +" and "+key+"<"+time.get("etime")+")" );
	            }else if("contentid".equals(key)){ 
	            	if(!"".equals(entry.getValue())){
	            		String[] ids = entry.getValue().toString().split(",");
	            		StringBuffer s = new StringBuffer();
	            		for(String id :ids){
	            			s.append("'"+id+"',");
	            		}
	            		where.append(" and "+key + " in ("+s.toString().substring(0,s.toString().length()-1)+")");
	            	}
	            }else if(key.indexOf("price")!=-1){
	            	switch(Integer.valueOf(entry.getValue().toString())){
	            	  case 0:  break;
	            	  case 1:  if("ios".equals(datatype)){where.append("and srcprice>price and price = 0");}break;
	            	  case 2:  if("ios".equals(datatype)){where.append("and srcprice>price and price > 0");}break;
	            	  case 3:  where.append("and price > 0");break;
	            	  case 4:  where.append("and price = 0");break;
	            	}
	            }else if(key.indexOf("ismmtoevent")!=-1){
	            	switch(Integer.valueOf(entry.getValue().toString())){
	            	  case 0:  where.append("and ismmtoevent is null");break;
	            	  case 1:  where.append("and ismmtoevent is not null");break;
	            	 
	            	}
	            }else{
	            	if(entry.getValue()!=""){
	            		where.append(" and "+key +" like '%"+entry.getValue()+"%'");
	            	}
	            }
	        }
		int a= this.dataSourceDao.getJdbcTemplate().queryForInt("select count(*) from "+tablename + " where 1=1 " + where.toString() );  
		return a;
	}
	@RenmSelf(methodDesc="根据id删除数据源应用信息")
	public boolean deleteInfoById(String tablename, String datatype, int dataid) {
		// TODO Auto-generated method stub
		try{
			String sql = "SELECT contentid FROM "+tablename+" WHERE dataid = ?";
			String contentid = "";
			try {  
				 contentid = (String)this.dataSourceDao.getJdbcTemplate().queryForObject(sql,new Object[] {dataid}, String.class);  
		    } catch (Exception e) {  
		         contentid = "";  
		         e.printStackTrace();  
		    }
			sql = "delete from mm_user_like t where t.contentid = ?";
			this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{contentid});
			sql = "delete from mm_user_share t where t.contentid = ?";
			this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{contentid});
			sql = "delete from mm_user_favorite t where t.contentid = ?";
			this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{contentid});
			sql = "delete from mm_content_data t where t.contentid = ?";
			this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{contentid});
			if("ios".equals(datatype)){
				sql = "delete from "+tablename+" i where i.dataid=?";
				this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{dataid});
				String path =  SystemProperties.getInstance().getProperty("fileupload.savePath") +datatype+"/"+CommonUtil.hashNumber(Long.valueOf(contentid));
				File file = new File(path);  
			    // 判断目录或文件是否存在  
			    if (!file.exists()) {  // 不存在返回 false  
			       // return false;  
			    } else {  
			        boolean bln = deleteDirectory(path);  
			        log.error("deleteDirectory status "+bln+" filepath="+path);
			    }  
			}
			if("android".equals(datatype)){
				sql = "delete from mm_content_android_ctb t where t.contentid = ?" ;
				this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{contentid});
				sql = "delete from mm_content_android_device t where t.contentid=?";
				this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{contentid});
				sql = "delete from "+tablename+" i where i.dataid=?";
				this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{dataid});
			}else{
				sql = "delete from "+tablename+" i where i.dataid=?";
				this.dataSourceDao.getJdbcTemplate().update(sql, new Object[]{dataid});
			}
			
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public boolean deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    boolean flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}  
	
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public boolean deleteFile(String sPath) {  
	   boolean flag = false;  
	   File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}  
 
	public List<DataSource> getPublishList(String datatype, String ids) {
		// TODO Auto-generated method stub
		String tablename = Constants.FORMPREFIX + datatype  ;
		String sql = "select dataid,contentid,title ,p_title from "+tablename ;
		if(StringUtils.isNumeric(ids)){
			sql += " where dataid ="+ids;
		}else{
			sql += " where dataid in("+ids+")";
		}
		List<?> list = this.dataSourceDao.getJdbcTemplate().queryForList(sql);
	    Iterator<?> it = list.iterator();  
	    List<DataSource> dlist = new ArrayList<DataSource>();
	    while(it.hasNext()) {  
	        Map<?,?> rowmap = (Map<?,?>) it.next();
	        DataSource ds = new DataSource();
	        ds.setDataid(CommonUtil.null2String(rowmap.get("dataid")));
	        ds.setContentid(rowmap.get("contentid").toString());
	        ds.setTitle(CommonUtil.null2String(rowmap.get("title")));
	        ds.setP_title(CommonUtil.null2String(rowmap.get("p_title")));
	        dlist.add(ds);
	    }
		return dlist;
	}

	public boolean checkDataIsExit(String datatype,String catid, String contentid) {
		// TODO Auto-generated method stub
		String sql ="select count(*) from mm_content_data t where t.catid = ? and t.contentid = ? and t.datatype=?";
		int count = this.dataSourceDao.getJdbcTemplate().queryForInt(sql, new Object[]{catid,contentid,datatype});
		if(count>0){
			return true;
		}
		return false;
	}
	@RenmSelf(methodDesc="推送数据源应用信息至栏目")
	public void publicData(String datatype, String catid, String contentid,String title,String author) {
		// TODO Auto-generated method stub
		FormInfo form = (FormInfo) this.formInfoDao.queryObject("from FormInfo where formname=?", new Object[]{Constants.CONTENTDATA}, null);
		String sql = "from FormField t where t.ispush=1 and t.formid = ?";
		List<FormField> fieldlist = this.formFieldDao.list(sql, new Object[]{form.getFormid()+""}, null);
		long status = dataSourceDao.getJdbcTemplate().queryForLong("select WORKFLOWID from mm_content_cat t where t.catid="+catid);
		status = status==0?99:1;
		StringBuffer sb = new StringBuffer();
		String tablename = Constants.FORMPREFIX + datatype;
		sb.append("select ");
		for(FormField field : fieldlist){
			sb.append(field.getFieldname() +", ");
		}
		sb.append("dataid from "+tablename +" where contentid=?");
		List<?> rows = dataSourceDao.getJdbcTemplate().queryForList(sb.toString(),new Object[]{contentid});
		Iterator<?> it = rows.iterator();  
	    while(it.hasNext()) {  
	        Map<?,?> rowmap = (Map<?,?>) it.next();
	        if(this.checkDataIsExit(datatype, catid, contentid)){
				//修改
				sb = new StringBuffer();
				sb.append("update mm_content_data t set ");
			    for(FormField f : fieldlist){
		        	if(!"".equals(title)&&"title".equals(f.getFieldname())){
		        		sb.append(" t.title = '"+title+"',");
		        	}else{
		        		if("number".equals(f.getFieldtype())){
		        			sb.append(f.getFieldname() + "= " + CommonUtil.null2String(rowmap.get(f.getFieldname()))+", ");
		        		}else{
		        			sb.append(f.getFieldname() + "= '" + CommonUtil.null2String(rowmap.get(f.getFieldname()))+"', ");
		        		}
		        	}
		        }
			    sb.append(" t.utime = "+DateTimeUtil.getTimeStamp() + ", t.author='"+author+"' ,t.status="+status);
			    sb.append(" where t.contentid=? and t.catid=? and datatype=?");
			    this.dataSourceDao.getJdbcTemplate().update(sb.toString(),new Object[]{contentid,catid,datatype});
			}else{
				//新增
				sb = new StringBuffer();
				sb.append("insert into mm_content_data ( did,catid,datatype,status,author,dataid,");
				StringBuffer key = new StringBuffer();
				StringBuffer value = new StringBuffer();
			    for(FormField f : fieldlist){
		        	key.append(f.getFieldname() + ",");
		        	if("number".equals(f.getFieldtype())){
		        		value.append( CommonUtil.null2String(rowmap.get(f.getFieldname())) + ",");
		        	}else{
		        		value.append( "'"+CommonUtil.null2String(rowmap.get(f.getFieldname())) + "',");
		        	}
		        }
			    key.append(" ctime,utime)");
			    value.append( DateTimeUtil.getTimeStamp()+","+DateTimeUtil.getTimeStamp());
			    sb.append(key.toString());
			    sb.append("values( seq_mm_ct_data.nextval,"+catid + ",'"+datatype+"',");
			    sb.append(status + ", ");
			    sb.append("'"+author+"', ");
			    sb.append(CommonUtil.null2String(rowmap.get("dataid"))+",");
			    sb.append( value.toString() + ")");
			    System.out.println(sb.toString());
			    this.dataSourceDao.getJdbcTemplate().update(sb.toString());
			}
	    }
		
	}
 
}
