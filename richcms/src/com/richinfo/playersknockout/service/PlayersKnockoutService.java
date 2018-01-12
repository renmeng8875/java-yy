package com.richinfo.playersknockout.service;

import java.util.List;
import java.util.Map;

import com.richinfo.common.service.BaseService;
import com.richinfo.playersknockout.entity.PlayersKnockout;

public interface PlayersKnockoutService extends BaseService<PlayersKnockout,Integer>
{
	public void updateStatus(int pkid,int status);
	
	public void updatePriority(int pkid,int priority);
	
	public boolean nameExists(String name,Integer pkid);
	
	public String getAppTitle(String tableName,String contentid);
	
	public List<PlayersKnockout> listBySort();
	
	public String getImageJsonFormat(String alt,String path,String size);
	
	public Map<String,String> getImageJsonItemFormat(String alt,String path,String size);
	
	public PlayersKnockout getPkByName(String pkname);
	
	public void updateCommImage(String nextPrizeImage,String godBackground);
}
