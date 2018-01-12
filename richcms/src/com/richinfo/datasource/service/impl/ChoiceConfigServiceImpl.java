package com.richinfo.datasource.service.impl;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.datasource.dao.ChoiceConfigDao;
import com.richinfo.datasource.entity.Choice;
import com.richinfo.datasource.entity.ChoiceObject;
import com.richinfo.datasource.service.ChoiceConfigService;

@Service("ChoiceConfigService")
public class ChoiceConfigServiceImpl extends BaseServiceImpl<Choice, Integer>
		implements ChoiceConfigService {

	private ChoiceConfigDao choiceConfigDao;
 
	@Autowired
	@Qualifier("ChoiceConfigDao")
	public void setChoiceConfigDao(ChoiceConfigDao choiceConfigDao) {
		this.choiceConfigDao = choiceConfigDao;
	}
	@Autowired
	@Qualifier("ChoiceConfigDao")
	@Override
	public void setBaseDao(BaseDao<Choice, Integer> baseDao) {
		this.baseDao = (ChoiceConfigDao) baseDao;
	}
	public List<ChoiceObject> getChoiceObjectList(String code) {
		// TODO Auto-generated method stub
		List<?> choicelist = choiceConfigDao.getJdbcTemplate().queryForList(code);
	    Iterator<?> it = choicelist.iterator();  
	    List<ChoiceObject> list = new ArrayList<ChoiceObject>();
	    while(it.hasNext()) {  
	        Map<?,?> rowmap = (Map<?,?>) it.next();
	        ChoiceObject choice = new ChoiceObject();
	        choice.setChoiceid(CommonUtil.null2String(rowmap.get("CHOICEID")));
	        choice.setChoicename(CommonUtil.null2String(rowmap.get("CHOICENAME")));
	        list.add(choice);
	    }
		return list;
	}
 


 
}
