package com.richinfo.datasource.service;


import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.datasource.entity.Choice;
import com.richinfo.datasource.entity.ChoiceObject;

public interface ChoiceConfigService extends BaseService<Choice, Integer> {
 
	public List<ChoiceObject> getChoiceObjectList(String code);
	
}