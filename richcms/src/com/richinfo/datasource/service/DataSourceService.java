package com.richinfo.datasource.service;

import java.util.List;
import java.util.Map;

import com.richinfo.common.service.BaseService;
import com.richinfo.datasource.entity.DataSource;
import com.richinfo.datasource.entity.FormField;
import com.richinfo.datasource.entity.FormInfo;
import com.richinfo.datasource.entity.TableFieldValue;

public interface DataSourceService extends BaseService<DataSource, Integer> {
	
	public FormInfo getFormByName(String formname);
	public List<FormField> getFormFieldByFormid(String formid,String type);
	
	public List<TableFieldValue> getFormValueForLists(String formname,List<FormField> fieldlist);
	public TableFieldValue getFormValueForInfo(String formname,List<FormField> fieldlist,String dataid,String editor);

    public boolean saveForm(Map<String,String> map);
	public List<TableFieldValue> getFormValueForPageList(String iostablename,List<FormField> fieldlist, Map<String,Object> search, int i, int j);
	public int getTotalRows(String tablename,Map<String,Object> search);
	
	public boolean deleteInfoById(String tablename,String datatype, int dataid);
	
	public List<DataSource> getPublishList(String datatype,String ids);
	public boolean checkDataIsExit(String datatype,String catid ,String contentid);
	public void publicData(String datatype,String catid,String contentid,String title,String author);
}