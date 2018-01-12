package com.richinfo.playersknockout.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.Constants;
import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.playersknockout.dao.PlayersKnockoutDao;
import com.richinfo.playersknockout.entity.PlayersKnockout;
import com.richinfo.playersknockout.service.PlayersKnockoutService;

@Service("PlayersKnockoutService")
public class PlayersKnockoutServiceImpl extends BaseServiceImpl<PlayersKnockout,Integer> implements PlayersKnockoutService
{

	private PlayersKnockoutDao playersKnockoutDao;
	
	@Autowired
    @Qualifier("PlayersKnockoutDao")
	public void setWarcraftDao(PlayersKnockoutDao playersKnockoutDao) 
	{
		this.playersKnockoutDao = playersKnockoutDao;
	}

	@Autowired
    @Qualifier("PlayersKnockoutDao")
	@Override
	public void setBaseDao(BaseDao<PlayersKnockout, Integer> baseDao) 
	{
		this.baseDao = (PlayersKnockoutDao)baseDao;
	}

	@RenmSelf(methodDesc="更改玩家争霸的显示或隐藏状态")
	public void updateStatus(int pkid,int status)
	{
		String hql = "update PlayersKnockout p set p.status=? where p.pkid=?";
		playersKnockoutDao.updateByHql(hql, new Object[]{status,pkid});
		
	}
	
	@RenmSelf(methodDesc="更改玩家争霸的排序权重值")
	public void updatePriority(int pkid,int priority)
	{
		String hql = "update PlayersKnockout p set p.priority=? where p.pkid=?";
		playersKnockoutDao.updateByHql(hql, new Object[]{priority,pkid});
	}

	public boolean nameExists(String name,Integer pkid) 
	{
		String hql = "from PlayersKnockout p where p.pk=? and p.pkid!=?";
		List<PlayersKnockout> list = playersKnockoutDao.list(hql, new Object[]{name,pkid}, null);
		if(list!=null&&list.size()>0)
		{
			return true;
		}
		return false;
	}

	public String getAppTitle(String tableName, String contentid) 
	{
		String pk = "contentid";
		if(Constants.IOSTABLENAME.equalsIgnoreCase(tableName))
		{
			pk = "appid";
		}
		String sql = "select title,"+pk+" from "+ tableName + "  where "+pk+"=?";
		Map<String,Type> scalarMap = new HashMap<String, Type>();
		scalarMap.put(pk,StandardBasicTypes.STRING);
		scalarMap.put("title", StandardBasicTypes.STRING);
		List<Object[]> list = playersKnockoutDao.listBySql(sql, new Object[]{contentid},scalarMap);
		if(list!=null&&list.size()>0)
		{
			Object[] objarr = list.get(0);
			return objarr[0].toString();
		}
		return null;
	}

	public List<PlayersKnockout> listBySort() {
		String hql = "from PlayersKnockout p order by p.priority desc,p.ctime desc";
		List<PlayersKnockout> list = playersKnockoutDao.list(hql, null, null);
		return list;
	}
	
	
	public String getImageJsonFormat(String alt,String path,String size)
	{
		if(StringUtils.isEmpty(path)){
			return "";
		}
		Map<String,Map<String,String>> map = new TreeMap<String, Map<String,String>>();
		Map<String,String>eachImage= new TreeMap<String,String>();
		eachImage.put("alt", "");
		eachImage.put("path", StringUtils.isEmpty(path)?"":path);
		eachImage.put("size", "");
		map.put("1", eachImage);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	
	
	public Map<String,String> getImageJsonItemFormat(String alt,String path,String size)
	{
		Map<String,String>eachImage= new TreeMap<String,String>();
		eachImage.put("alt", "");
		eachImage.put("path", StringUtils.isEmpty(path)?"":path);
		eachImage.put("size", "");
		return eachImage;
	}

	@Override
	public PlayersKnockout getPkByName(String pkname) 
	{
		String hql = "from PlayersKnockout p  where p.pkName=?";
		List<PlayersKnockout> list = playersKnockoutDao.list(hql, new Object[]{pkname}, null);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public void updateCommImage(String nextPrizeImage, String godBackground) 
	{
		String hql = "update PlayersKnockout p set p.nextPrizeImage=?,p.godBackground=?";
		playersKnockoutDao.updateByHql(hql, new Object[]{nextPrizeImage,godBackground});
		
		
	}

	
	

}
