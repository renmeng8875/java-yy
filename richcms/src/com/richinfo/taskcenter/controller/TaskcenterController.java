package com.richinfo.taskcenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.SystemProperties;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.contentcat.entity.ContentIos;
import com.richinfo.contentcat.service.ContentIosService;

@Controller
@RequestMapping(value = "/taskcenter")
public class TaskcenterController 
{
	private ContentIosService contentIosService;
	
	private final String[] includeAttributes = new String[]{"dataId","title","price","appId","appSize","logo","utimeStr","version"}; ;
	
	@Autowired
	@Qualifier("ContentIosService")
	public void setContentIosService(ContentIosService contentIosService) {
		this.contentIosService = contentIosService;
	}

	@RequestMapping(value = "/fetchios.do",method=RequestMethod.GET)
	public String displayFetchIos(HttpServletRequest request, Model model)
	{
		return "taskcenter/iosfetch";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list.do",method=RequestMethod.POST)
	public Object list(HttpServletRequest request, Model model)
	{
		String appidstr = ServletRequestUtils.getStringParameter(request, "appids", "");
		String[] appids = appidstr.split(",");
		String prefix = SystemProperties.getInstance().getProperty("taskcenter.iosFetchPrefix");
		List<ContentIos> list = new ArrayList<ContentIos>();
		for(String appid:appids)
		{
			ContentIos ios =contentIosService.getContentIosByAppid(appid);
			if(ios!=null){
				String jsonLogoStr = ios.getLogo();
				JSONParser jp = new JSONParser();
				try {
					org.json.simple.JSONObject jb = (org.json.simple.JSONObject) jp.parse(jsonLogoStr);
					Map map = (HashMap)jb.get("1");
					String path = (String)map.get("path");
					ios.setLogo(prefix+path);
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
			}else{
				ios = new ContentIos();
				ios.setAppId(appid);
			}
			list.add(ios);
			
		}
		JsonConfig config = new JsonConfig();
		config.setJsonPropertyFilter(new PropertyFilter() 
		{
			public boolean apply(Object bean, String name, Object value)
			{
				for(int i=0;i<includeAttributes.length;i++)
				{
					if(includeAttributes[i].equals(name)){
						return false;
					}
				}
				return true;
			}
		});
		Object json = JSONArray.fromObject(list,config);
		model.addAttribute("iosList", json);
		return "taskcenter/list";
	
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/fetch.do",method=RequestMethod.POST)
	@ResponseBody
	public Object fetch(HttpServletRequest request, Model model){
		String appids = ServletRequestUtils.getStringParameter(request, "appids","");
		String prefix = SystemProperties.getInstance().getProperty("taskcenter.requestUrlPrefix");
		String subfix = SystemProperties.getInstance().getProperty("taskcenter.requestUrlParam");
		subfix = subfix.replace("{#}", appids);
		subfix = CommonUtil.urlComponentEncode(subfix);
		CommonUtil.triggerTaskcenter(prefix+subfix);
		Map<String,String> jsonMap = new HashMap<String,String>();
		jsonMap.put("status", "0");
		jsonMap.put("info", "任务已启动！");
		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return jsonMap;
	}
	
	
	@RequestMapping(value = "/cancle.do",method=RequestMethod.POST)
	@ResponseBody
	public Object cancle(HttpServletRequest request, Model model)
	{
		int appid = ServletRequestUtils.getIntParameter(request, "appid",0);
		ContentIos ios = contentIosService.get(appid);
		if(ios!=null)
		{
			contentIosService.deleteRationIos(appid);
		}
		Map<String,String> jsonMap = new HashMap<String,String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
}
