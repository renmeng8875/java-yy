package com.richinfo.module.service;



import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.DataRelation;
import com.richinfo.module.entity.Style;
import com.richinfo.module.entity.Tpl;

public interface DataRelationService extends BaseService<DataRelation,Integer>{

	public List<Tpl> getTplList(String typeen);
	
	public Style getStyleByid(int styleid);
	
	public List<Tpl> decodeInfoPath(String pathfield, String typeen);
}
