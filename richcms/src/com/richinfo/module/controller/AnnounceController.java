package com.richinfo.module.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.module.entity.Announce;
import com.richinfo.module.service.AnnounceService;

@Controller
@RequestMapping(value = "/Announce")
public class AnnounceController {

	private AnnounceService announceService;
	
	private static final String TIMEFORMAT = "yyyy-MM-dd HH:mm";

	@Autowired
	@Qualifier("AnnounceService")
	public void setAnnounceService(AnnounceService announceService) {
		this.announceService = announceService;
	}

	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) 
	{
		List<Announce> announcelist = announceService.listAll();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"content"});
        model.addAttribute("announcelist", JSONArray.fromObject(announcelist,config));
		return "announce/lists";
	}

	@RequestMapping(value="/deleteAnnounce.do")
	@ResponseBody
	public Object deleteAnnounce(HttpServletRequest request, Model model)
	{
		Map<String,String> jsonMap = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			announceService.delete(Integer.valueOf(id));
		}
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value="/addAnnounce.do",method=RequestMethod.GET)
	public String getAddAnnounce(HttpServletRequest request, Model model)
	{
		return "announce/add";
	}
	
	@RequestMapping(value="/addAnnounce.do",method=RequestMethod.POST)
	public String addAnnounce(HttpServletRequest request, Model model)
	{
		Announce announce = new Announce();
		String title = ServletRequestUtils.getStringParameter(request,"announce","");
		String starttime = ServletRequestUtils.getStringParameter(request,"starttime","");
		String endtime = ServletRequestUtils.getStringParameter(request,"endtime","");
		String content = ServletRequestUtils.getStringParameter(request,"content","");
		announce.setAnnounce(title);
		announce.setContent(content);
		announce.setStarttime(DateTimeUtil.converToTimestamp(starttime, TIMEFORMAT));
		announce.setEndtime(DateTimeUtil.converToTimestamp(endtime, TIMEFORMAT));
		announceService.save(announce);
		model.addAttribute("message", "添加公告成功！");
		model.addAttribute("url","/Announce/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value="/editAnnounce.do",method=RequestMethod.GET)
	public String getEditAnnounce(HttpServletRequest request, Model model)
	{
		int announceid = ServletRequestUtils.getIntParameter(request,"announceid",-1);
		Announce announce = announceService.get(announceid);
		model.addAttribute("announce", announce);
		return "announce/edit";
	}
	
	@RequestMapping(value="/editAnnounce.do",method=RequestMethod.POST)
	public String editAnnounce(HttpServletRequest request, Model model)
	{
		String title = ServletRequestUtils.getStringParameter(request,"announce","");
		String starttime = ServletRequestUtils.getStringParameter(request,"starttime","");
		String endtime = ServletRequestUtils.getStringParameter(request,"endtime","");
		String content = ServletRequestUtils.getStringParameter(request,"content","");
		int announceid = ServletRequestUtils.getIntParameter(request,"announceid",-1);
		Announce announce = new Announce();
		announce.setAnnounceid(announceid);
		announce.setAnnounce(title);
		announce.setContent(content);
		announce.setStarttime(DateTimeUtil.converToTimestamp(starttime, TIMEFORMAT));
		announce.setEndtime(DateTimeUtil.converToTimestamp(endtime, TIMEFORMAT));
		announceService.update(announce);
		model.addAttribute("message", "修改公告成功！");
		model.addAttribute("url","/Announce/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value="/viewAnnounce.do",method=RequestMethod.GET)
	public String getViewAnnounce(HttpServletRequest request, Model model)
	{
		int announceid = ServletRequestUtils.getIntParameter(request,"announceid",-1);
		Announce announce = announceService.get(announceid);
		model.addAttribute("announce", announce);
		return "announce/view";
	}
	
	@RequestMapping(value="/batchsort.do",method=RequestMethod.POST)
	@ResponseBody
	public Object batchSort(HttpServletRequest request, Model model)
	{
		String sortStr = ServletRequestUtils.getStringParameter(request,"sortStr","");
		String[] sorts = sortStr.split(",");
		for(String sort:sorts)
		{
			String[] pair = sort.split("-");
			announceService.updatePriority(Integer.valueOf(pair[0]), Integer.valueOf(pair[1]));
		}
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value="/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String announce = ServletRequestUtils.getStringParameter(request,"param","");
		List<Announce> list = announceService.listByParam(announce);
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(list!=null&&list.size()>0)
		{
			jsonMap.put("info","公告标题重复！");
			jsonMap.put("status", "n");
			return jsonMap;
		}	
		jsonMap.put("status", "y");
		return jsonMap;
	}

}
