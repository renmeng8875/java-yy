package com.richinfo.privilege.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.richinfo.common.SystemProperties;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.privilege.dao.SysConfigDao;
import com.richinfo.privilege.entity.SysConfig;
import com.richinfo.privilege.service.SysConfigService;

@Service("SysConfigService")
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig, Integer> implements SysConfigService{

	private SysConfigDao sysConfigDao;

	
	@Autowired
    @Qualifier("SysConfigDao")
	public void setSysConfigDao(SysConfigDao sysConfigDao) 
	{
		this.sysConfigDao = sysConfigDao;
	}

	@Autowired
    @Qualifier("SysConfigDao")
	@Override
	public void setBaseDao(BaseDao<SysConfig, Integer> baseDao) 
	{
		this.baseDao = (SysConfigDao)baseDao;
	}

	public SysConfig getValue(String type,String key) {
		SysConfig config = (SysConfig) sysConfigDao.queryObject("from SysConfig where systype=? and syskey=?", new Object[]{type,key}, null);
		return config;
	}

	public String getSysParamValue(String type,String key,String jsonKey){
		SysConfig sysConfig=getValue(type, key);
		JSONObject jsonConfig=null;
		String paramValue=null;
		try{
			if(sysConfig!=null){
				if(!StringUtils.isEmpty(jsonKey)){
					String strHtmlUnescape=StringUtils.strip(HtmlUtils.htmlUnescape(sysConfig.getSysvalue()), "\"");
					jsonConfig = JSONObject.fromObject(strHtmlUnescape);
				}else{
					paramValue=sysConfig.getSysvalue();
				}
				paramValue=ObjectUtils.toString(jsonConfig.get(jsonKey));
			}
		}catch(Exception e){
		}
		if(StringUtils.isEmpty(paramValue)){
			paramValue=SystemProperties.getInstance().getProperty("fileupload."+key);
		}
		return !StringUtils.isEmpty(paramValue)?paramValue:"";
	}
	
	public boolean nameExists(String name,Integer pkid) 
	{
		String hql = "from SysConfig p where p.syskey=? and p.sid!=?";
		List<SysConfig> list = sysConfigDao.list(hql, new Object[]{name,pkid}, null);
		if(list!=null&&list.size()>0)
		{
			return true;
		}
		return false;
	}
	
}
