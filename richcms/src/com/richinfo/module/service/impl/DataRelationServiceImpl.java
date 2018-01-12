package com.richinfo.module.service.impl;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.DataRelationDao;
import com.richinfo.module.dao.StyleDao;
import com.richinfo.module.dao.TplDao;
import com.richinfo.module.entity.DataRelation;
import com.richinfo.module.entity.Style;
import com.richinfo.module.entity.Template;
import com.richinfo.module.entity.Tpl;
import com.richinfo.module.service.DataRelationService;

@Service("DataRelationService")
public class DataRelationServiceImpl extends BaseServiceImpl<DataRelation, Integer> implements DataRelationService{

	@SuppressWarnings("unused")
	private DataRelationDao dataRelationDao;
	
	private TplDao tplDao;
	
	private StyleDao styleDao;

	
	@Autowired
    @Qualifier("DataRelationDao")
	public void setDataRelationDao(DataRelationDao dataRelationDao) 
	{
		this.dataRelationDao = dataRelationDao;
	}

	@Autowired
    @Qualifier("TplDao")
	public void setTplDao(TplDao tplDao) 
	{
		this.tplDao = tplDao;
	}
	
	@Autowired
    @Qualifier("StyleDao")
	public void setStyleDao(StyleDao styleDao) 
	{
		this.styleDao = styleDao;
	}

	@Autowired
    @Qualifier("DataRelationDao")
	@Override
	public void setBaseDao(BaseDao<DataRelation, Integer> baseDao) 
	{
		this.baseDao = (DataRelationDao)baseDao;
	}

	public List<Tpl> getTplList(String typeen) {
		// TODO Auto-generated method stub
		List<Tpl> list = this.tplDao.list("from Tpl where typeen=?", new Object[]{typeen}, null);
		return list;
	}

	public Style getStyleByid(int styleid) {
		// TODO Auto-generated method stub
		return this.styleDao.get(styleid);
	}

	public List<Tpl> decodeInfoPath(String pathfield, String typeen) {
		// TODO Auto-generated method stub
		List<Tpl> list = new ArrayList<Tpl>();
		List<Tpl> tpllist = this.getTplList(typeen);
		for(Tpl tpl : tpllist){
			Style style = this.getStyleByid(tpl.getStyleId());
			JSONObject json = JSONObject.fromObject(tpl.getInfoPath());
			Iterator<?> iter = json.entrySet().iterator();
			List<Template> template = new ArrayList<Template>();
		    while(iter.hasNext()){
		    	@SuppressWarnings("unchecked")
				Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next(); 
		    	System.out.println(entry.getKey()+ "  "+ entry.getValue());
		    	Template t = new Template();
		    	t.setName(entry.getKey());
		    	t.setPath(entry.getValue());
		    	template.add(t);
		    }
		    if(style!=null){
				tpl.setStyle(style.getStyle());
			    tpl.setStylename(style.getStylename());
		    }
            tpl.setTemplate(template);
		    list.add(tpl);
		}
		return list;
		
	}
	
	
	public static void main(String[] args) {
		String str = "{'软件列表页':'default/Content/lists.html','专题':'default/Content/topic_template.html'}";
		JSONObject json = JSONObject.fromObject(str);
		Iterator<?> iter = json.entrySet().iterator();
	    while(iter.hasNext()){
	    	 @SuppressWarnings("unchecked")
			Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next(); 
	    	 System.out.println(entry.getKey()+ "  "+ entry.getValue());
	    }
		
	}

   

 

}
