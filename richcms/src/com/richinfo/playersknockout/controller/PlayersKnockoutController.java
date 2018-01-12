package com.richinfo.playersknockout.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.playersknockout.entity.PlayersKnockout;
import com.richinfo.playersknockout.service.PlayersKnockoutService;

@Controller
@RequestMapping(value = "/pk")
public class PlayersKnockoutController 
{
	
	private PlayersKnockoutService playersKnockoutService;
	
	private EhCacheCacheManager ehCacheManager;
	
	private Cache cache;
	
	@Autowired
	@Qualifier("ehcacheManager")
	public void setEhCacheManager(EhCacheCacheManager ehCacheManager) {
		this.ehCacheManager = ehCacheManager;
		CacheManager cacheM= (CacheManager) this.ehCacheManager.getCacheManager();
		cache = cacheM.getCache("jsonCache");
		
	}

	@Autowired
	@Qualifier("PlayersKnockoutService")
	public void setWarcraftService(PlayersKnockoutService warcraftService) 
	{
		this.playersKnockoutService = warcraftService;
	}

	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) 
	{
		Element ele = cache.get("/pk/lists.do");
		if(ele==null)
		{
			List<PlayersKnockout> warcraftList = playersKnockoutService.listBySort();
			JsonConfig config = new JsonConfig();
			config.setExcludes(new String[]{"playerList","article"});
			Object json = JSONArray.fromObject(warcraftList,config);
			ele = new Element("/pk/lists.do", json);
			cache.put(ele);
		}
        model.addAttribute("warcraftList", ele.getObjectValue());
        return "playersknockout/lists";
	}
	
	@RequestMapping(value = "/add.do")
	public String add(HttpServletRequest request, Model model)
	{
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid",-1);
		if(pkid!=-1)
		{
			PlayersKnockout pk = playersKnockoutService.get(pkid);
			
			pk.setPkName(HtmlUtils.htmlEscape(pk.getPkName()));
			pk.setPk(HtmlUtils.htmlEscape(pk.getPk()));
			pk.setPkdesc(HtmlUtils.htmlEscape(pk.getPkdesc()));
			pk.setSeoTitle(HtmlUtils.htmlEscape(pk.getSeoTitle()));
			pk.setSeoKeyword(HtmlUtils.htmlEscape(pk.getSeoKeyword()));
			pk.setSeoDesc(HtmlUtils.htmlEscape(pk.getSeoDesc()));
			model.addAttribute("pk", pk);
		}
		model.addAttribute("storageUrl", SystemProperties.getInstance().getProperty("fileupload.contextPath"));
		return "playersknockout/add";
	}
	
	@RequestMapping(value = "/addpk.do")
	public String addpk(HttpServletRequest request, Model model,@ModelAttribute PlayersKnockout pk)
	{
		String startTimeStr = ServletRequestUtils.getStringParameter(request, "startTimeStr","1970-01-01");
		long startTime = DateTimeUtil.converToTimestamp(startTimeStr, "yyyy-MM-dd");
		pk.setStartTime(startTime);
		String endTimeStr = ServletRequestUtils.getStringParameter(request, "endTimeStr","1970-01-01");
		endTimeStr=(endTimeStr.length()<=10)?(endTimeStr+" 23:59:59"):endTimeStr;
		if (!DateTimeUtil.isValidDate(startTimeStr, "yyyy-MM-dd")
				|| !DateTimeUtil.isValidDate(endTimeStr, "yyyy-MM-dd HH:mm:ss")) {
			return null;
		}
		
		long endTime = DateTimeUtil.converToTimestamp(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		pk.setEndTime(endTime);
		
		String images = ServletRequestUtils.getStringParameter(request, "jsonimg","");
		pk.setImages(playersKnockoutService.getImageJsonFormat("",images,""));
		String logo = ServletRequestUtils.getStringParameter(request, "logo","");
		String activitiesLogo = ServletRequestUtils.getStringParameter(request, "activitiesLogo","");
		
		//appScan验证
		if(!StringUtils.isEmpty(pk.getForwardUrl())
				&&!CommonUtil.isValidUrl(pk.getForwardUrl())){
			return null;
		}
		
		pk.setPkName(HtmlUtils.htmlUnescape(pk.getPkName()));
		pk.setPk(HtmlUtils.htmlUnescape(pk.getPk()));
		pk.setPkdesc(HtmlUtils.htmlUnescape(pk.getPkdesc()));
		pk.setSeoTitle(HtmlUtils.htmlUnescape(pk.getSeoTitle()));
		pk.setSeoKeyword(HtmlUtils.htmlUnescape(pk.getSeoKeyword()));
		pk.setSeoDesc(HtmlUtils.htmlUnescape(pk.getSeoDesc()));
		if(pk.getPkid()==null)
		{
			pk.setCtime(DateTimeUtil.getTimeStamp());
			pk.setLogo(playersKnockoutService.getImageJsonFormat("",logo,""));
			pk.setPriority(99);
			pk.setActivitiesLogo(activitiesLogo);
			playersKnockoutService.save(pk);
			model.addAttribute("message", "添加玩家争霸栏目成功！");
		}
		else
		{
			PlayersKnockout dbpk = playersKnockoutService.get(pk.getPkid());
			dbpk.setLogo(playersKnockoutService.getImageJsonFormat("",pk.getLogo(),""));
			dbpk.setActivitiesLogo(activitiesLogo);
			dbpk.setImages(pk.getImages());
			dbpk.setStatus(pk.getStatus());
			dbpk.setTplpath(pk.getTplpath());
			dbpk.setStyle(pk.getStyle());
			dbpk.setSeoDesc(pk.getSeoDesc());
			dbpk.setSeoTitle(pk.getSeoTitle());
			dbpk.setSeoKeyword(pk.getSeoKeyword());
			dbpk.setIsForward(pk.getIsForward());
			dbpk.setForwardUrl(pk.getForwardUrl());
			dbpk.setPk(pk.getPk());
			dbpk.setPkName(pk.getPkName());
			dbpk.setStartTime(pk.getStartTime());
			dbpk.setEndTime(pk.getEndTime());
			dbpk.setPkdesc(pk.getPkdesc());
			playersKnockoutService.update(dbpk);
			model.addAttribute("message", "修改玩家争霸栏目成功！");
		}
		cache.remove("/pk/lists.do");
		model.addAttribute("url", "/pk/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model)
	{
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid",-1);
		String methodType = request.getMethod();
		if("GET".equals(methodType)){
			PlayersKnockout pk = playersKnockoutService.get(pkid);
			if(pk!=null)
			{
				pk.setPkName(HtmlUtils.htmlEscape(pk.getPkName()));
				pk.setPk(HtmlUtils.htmlEscape(pk.getPk()));
				pk.setPkdesc(HtmlUtils.htmlEscape(pk.getPkdesc()));
				pk.setSeoTitle(HtmlUtils.htmlEscape(pk.getSeoTitle()));
				pk.setSeoKeyword(HtmlUtils.htmlEscape(pk.getSeoKeyword()));
				pk.setSeoDesc(HtmlUtils.htmlEscape(pk.getSeoDesc()));
				model.addAttribute("pk", pk);
				model.addAttribute("storageUrl", SystemProperties.getInstance().getProperty("fileupload.contextPath"));
				return "playersknockout/edit";
			}
		}
		return "playersknockout/edit";
	}
	
	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String pk = request.getParameter("param");
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid", -1);
		Map<String,Object> json = new HashMap<String, Object>();
		boolean flag = playersKnockoutService.nameExists(pk,pkid);
		if(!flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "名称重复,请重新输入！");
		}
		
		return json;
	}
	
	@RequestMapping(value = "/contentEdit.do")
	public String contentEdit(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid",-1);
		PlayersKnockout pk = playersKnockoutService.get(pkid);
		if("GET".equals(methodType)){
			if(pk!=null)
			{
				model.addAttribute("pk", pk);
				model.addAttribute("storageUrl", SystemProperties.getInstance().getProperty("fileupload.contextPath"));
				return "playersknockout/content";
			}
		}
		int appType = ServletRequestUtils.getIntParameter(request, "appType",0);
		int nextAppType = ServletRequestUtils.getIntParameter(request, "nextAppType",0);
		String introduce = ServletRequestUtils.getStringParameter(request, "introduce","");
		String evaluation = ServletRequestUtils.getStringParameter(request, "evaluation","");
		String nextTipcontent = ServletRequestUtils.getStringParameter(request, "nextTipcontent","");
		String joinway = ServletRequestUtils.getStringParameter(request, "joinway","");
		String contentid  = ServletRequestUtils.getStringParameter(request, "contentid","");
		String nextContentid  = ServletRequestUtils.getStringParameter(request, "nextContentid","");
		String nextName = ServletRequestUtils.getStringParameter(request, "nextName","");
		String code = ServletRequestUtils.getStringParameter(request, "code","");
		String[] images = ServletRequestUtils.getStringParameters(request, "jsonimg");
		Map<String,Map<String,String>> map = new TreeMap<String, Map<String,String>>();
		for(int i=0;i<images.length;i++)
		{
			String[] ss = images[i].split(",");
			map.put(ss[1],playersKnockoutService.getImageJsonItemFormat("", ss[0], ""));
			if(i>5)break;
		}
		
		JSONObject json = JSONObject.fromObject(map);
		pk.setGameImages(json.toString());
		pk.setAppType(appType);
		pk.setCode(code);
		pk.setEvaluation(evaluation);
		pk.setContentid(contentid);
		pk.setNextContentid(nextContentid);
		pk.setNextName(nextName);
		pk.setIntroduce(introduce);
		pk.setNextAppType(nextAppType);
		pk.setNextTipcontent(nextTipcontent);
		pk.setJoinway(joinway);
		playersKnockoutService.update(pk);
		model.addAttribute("message", "编辑内容成功！");
		model.addAttribute("url","/pk/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/playersListEdit.do")
	public String playerListEdit(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid",-1);
		if("GET".equals(methodType)){
			PlayersKnockout pk = playersKnockoutService.get(pkid);
			if(pk!=null)
			{
				model.addAttribute("pk", pk);
				return "playersknockout/playersList";
			}
		}
		
		return "playersknockout/playersList";
	}
	
	@RequestMapping(value = "/prizeEdit.do")
	public String prizeEdit(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid",-1);
		PlayersKnockout pk = playersKnockoutService.get(pkid);
		if("GET".equals(methodType)){
			if(pk!=null)
			{
				model.addAttribute("pk", pk);
				model.addAttribute("storageUrl", SystemProperties.getInstance().getProperty("fileupload.contextPath"));
				return "playersknockout/prize";
			}
		}
		String jpImage = ServletRequestUtils.getStringParameter(request, "jpImage","");
		pk.setJpImage(playersKnockoutService.getImageJsonFormat("",jpImage,""));
		String playersList = ServletRequestUtils.getStringParameter(request, "playerList","");
		pk.setPlayerList(playersList);
		playersKnockoutService.update(pk);
		model.addAttribute("message", "编辑中奖名单成功！");
		model.addAttribute("url","/pk/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public Object delete(HttpServletRequest request, Model model)
	{
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			playersKnockoutService.delete(Integer.valueOf(id));
		}
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		cache.remove("/pk/lists.do");
		return json;
	}
	
	@RequestMapping(value = "/batchSort.do")
	@ResponseBody
	public Object batchSort(HttpServletRequest request, Model model)
	{
		String sortStr = ServletRequestUtils.getStringParameter(request, "sortStr","");
		String[] sorts = sortStr.split(",");
		for(String sortPair :sorts)
		{
			String[] pair = sortPair.split("-");
			playersKnockoutService.updatePriority(Integer.valueOf(pair[0]), Integer.valueOf(pair[1]));
		}
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		cache.remove("/pk/lists.do");
		return json;
	}
	
	@RequestMapping(value = "/changeStatus.do")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, Model model)
	{
		int status = ServletRequestUtils.getIntParameter(request, "status",-1);
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid",-1);
		playersKnockoutService.updateStatus(pkid, status);
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		cache.remove("/pk/lists.do");
		return json;
	}
	
	
	@RequestMapping(value = "/getAppName.do")
	@ResponseBody
	public Object getAppName(HttpServletRequest request, Model model)
	{
		Map<String,String> json = new HashMap<String, String>();
		String contentid = ServletRequestUtils.getStringParameter(request,"contentid", "");
		if(StringUtils.isEmpty(contentid))
		{
			json.put("status", "n");
			json.put("info", "应用ID不能为空！");
			return json;
		}
		int apptype = ServletRequestUtils.getIntParameter(request, "appType", 0);
		String tableName = Constants.ANDROIDTABLENAME;
		if(apptype ==1)
		{
			tableName = Constants.IOSTABLENAME;
		}
		String title = playersKnockoutService.getAppTitle(tableName, contentid);
		if(StringUtils.isEmpty(title))
		{
			json.put("status", "n");
			json.put("info", "无此应用，请检查输入的ID！");
		}
		else
		{
			json.put("status", "y");
			json.put("info", title);
		}
		return json;
	}
	
	@RequestMapping(value = "/updateImage.do")
	public String updateCommImage(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		if("GET".equals(methodType))
		{
			List<PlayersKnockout> warcraftList = playersKnockoutService.listBySort();
			if(warcraftList!=null&&warcraftList.size()>0)
			{
				PlayersKnockout war = warcraftList.get(0);
				model.addAttribute("war", war);
			}
			return "playersknockout/updateImage";
		}
		String godBackground = ServletRequestUtils.getStringParameter(request, "godBackground","");
		String nextPrizeImage = ServletRequestUtils.getStringParameter(request, "nextPrizeImage","");
		playersKnockoutService.updateCommImage(nextPrizeImage, godBackground);
		model.addAttribute("url", "/pk/lists.do");
		model.addAttribute("message", "修改成功！");
		return Constants.FORWARDURL;
	}
}
