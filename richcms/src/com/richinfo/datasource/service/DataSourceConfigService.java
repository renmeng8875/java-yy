package com.richinfo.datasource.service;

import java.util.List;
import java.util.Map;

import com.richinfo.common.service.BaseService;
import com.richinfo.datasource.entity.DataSource;
import com.richinfo.datasource.entity.FormField;
import com.richinfo.datasource.entity.FormInfo;

public interface DataSourceConfigService extends BaseService<DataSource, Integer> {
	/**表单字段操作***/
	public List<FormField> getFieldListByTableId(String tableid);
	public FormField getFormFieldById(int fieldid);
	public FormField checkfieldIsExit(FormInfo form,FormField formfield);
	public boolean checkfieldIsUse(FormInfo form,FormField formfield);
	public boolean addTableField(FormField formfield);
	public Map<String,String> deleteField(int fieldid);
	public FormField getFormFieldByName(String fieldname,String formid);
	
	/**表单操作service*/
	public List<FormInfo>  getFormInfoLists();
	public List<FormInfo>  getFormInfoListsByType(String formtype);
	public FormInfo getFormInfoById(int formid);
	public boolean saveOrUpdateFormInfo(FormInfo formInfo);
	public boolean checkformIsExit(String formname);
    public boolean deleteForm(int formid);
    public int getCountByFormName(String formname);
    public int getCountByFieldName(String fieldname,String formname);
    public List<FormField> getContentDataFiled();
    public List<FormInfo> getExtendsFormLists();
}
