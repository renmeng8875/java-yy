package com.richinfo.module.service;


import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.TravelSet;

public interface TravelSetService extends BaseService<TravelSet,Integer>
{
	
	public boolean checkTravelset(String travelsetName);

}
