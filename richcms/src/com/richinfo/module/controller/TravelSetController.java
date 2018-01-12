package com.richinfo.module.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.module.entity.TravelSet;
import com.richinfo.module.service.TravelSetService;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.service.CategoryService;

@Controller
@RequestMapping(value = "/TravelSet")
public class TravelSetController {

	private TravelSetService travelSetService;
	
	private CategoryService categoryService;
	
	private final String[] includeAttributes = new String[]{"children","catId","catName","catLevel"}; ;
	
	@Autowired
	@Qualifier("CategoryService")
	public void setCategoryService(CategoryService categoryService) 
	{
		this.categoryService = categoryService;
	}

	@Autowired
	@Qualifier("TravelSetService")
	public void setTravelSetService(TravelSetService travelSetService) {
		this.travelSetService = travelSetService;
	}


	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<TravelSet> travelsetlist = travelSetService.listAll();
        model.addAttribute("travelsetlist", JSONArray.fromObject(travelsetlist));
		return "travelset/lists";
	}

	@RequestMapping(value = "/deletetravelset.do")
	@ResponseBody
	public Object deleteTravelset(HttpServletRequest request, Model model)
	{
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			travelSetService.delete(Integer.valueOf(id));
		}
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	} 

	@RequestMapping(value = "/addtravelset.do")
	public String addTravelset(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		if("GET".equals(methodType))
		{
			Category root = categoryService.getRootCategory();
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
			Object json = JSONObject.fromObject(root,config);
			model.addAttribute("treeData",json.toString());
			return "travelset/add";
		}
		String[] images = ServletRequestUtils.getStringParameters(request, "jsonimg");
		Map<String,String> map = new HashMap<String, String>();
		for(int i=0;i<images.length;i++)
		{
			map.put(i+1+"", images[i]);
		}
		
		JSONObject json = JSONObject.fromObject(map);
		String travelname = ServletRequestUtils.getStringParameter(request,"travelname","");
		String travel = ServletRequestUtils.getStringParameter(request,"travel","");
		String tagtype = ServletRequestUtils.getStringParameter(request,"tagtype","");
		int contentcatid = ServletRequestUtils.getIntParameter(request,"contentcat",-1);
		//针对appScan扫描
		if(!CommonUtil.isValidCharacter(travelname)
				||!CommonUtil.isValidCharacter(travel)
				||!CommonUtil.isValidCharacter(tagtype)
				||contentcatid==-1){
			return null;
		}
		
		Category cate = categoryService.get(contentcatid);
		TravelSet ts = new TravelSet();
		ts.setCatid(contentcatid);
		ts.setTagtype(tagtype);
		ts.setTravel(travel);
		ts.setTravelname(travelname);
		ts.setImg(json.toString());
		ts.setCat(cate.getCat());
		ts.setCatname(cate.getName());
		ts.setCtime(DateTimeUtil.getTimeStamp());
		travelSetService.save(ts);
		model.addAttribute("message", "添加MM之旅成功！");
		model.addAttribute("url", "/TravelSet/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/edittravelset.do")
	public String editTravelset(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		int id = ServletRequestUtils.getIntParameter(request,"travelid",-1);
		TravelSet ts = travelSetService.get(id);
		if("GET".equals(methodType))
		{
			model.addAttribute("travelset", ts);
			Category root = categoryService.getRootCategory();
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
			Object json = JSONObject.fromObject(root,config);
			model.addAttribute("treeData",json.toString());
			return "travelset/edit";
		}
		String[] images = ServletRequestUtils.getStringParameters(request, "jsonimg");
		Map<String,String> map = new HashMap<String, String>();
		for(int i=0;i<images.length;i++)
		{
			map.put(i+1+"", images[i]);
		}
		
		JSONObject json = JSONObject.fromObject(map);
		String travelname = ServletRequestUtils.getStringParameter(request,"travelname","");
		String travel = ServletRequestUtils.getStringParameter(request,"travel","");
		String tagtype = ServletRequestUtils.getStringParameter(request,"tagtype","");
		int contentcatid = ServletRequestUtils.getIntParameter(request,"contentcat",-1);
		Category cate = categoryService.get(contentcatid);
		ts.setCatid(contentcatid);
		ts.setTagtype(tagtype);
		ts.setTravel(travel);
		ts.setTravelname(travelname);
		ts.setImg(json.toString());
		ts.setCat(cate.getCat());
		ts.setCatname(cate.getName());
		ts.setCtime(DateTimeUtil.getTimeStamp());
		travelSetService.save(ts);
		model.addAttribute("message", "修改MM之旅成功！");
		model.addAttribute("url", "/TravelSet/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model) {
		Map<String,String> json = new HashMap<String,String>();
		String name = ServletRequestUtils.getStringParameter(request,"param", "");
		boolean flag = travelSetService.checkTravelset(name);
		
		if(flag)
		{
			json.put("status", "n");
			json.put("info", "MM之旅中文标识重复请重新输入！");
		}else{
			json.put("status", "y");
		}	
		return json;
	}
	

}
