package com.richinfo.openapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

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
import com.richinfo.common.utils.encryptUtil.MD5Coder;
import com.richinfo.openapi.entity.Account;
import com.richinfo.openapi.entity.AppRole;
import com.richinfo.openapi.entity.Application;
import com.richinfo.openapi.service.ApplicationService;
import com.richinfo.openapi.service.DeveloperManageService;
import com.richinfo.openapi.service.RoleManageService;

@Controller
@RequestMapping(value = "/application")
public class ApplicationController 
{
	@Autowired
	@Qualifier("ApplicationService")
	private ApplicationService  applicationService;
	
	@Autowired
	@Qualifier("DeveloperManageService")
	private DeveloperManageService developerManageService;
	
	@Autowired
	@Qualifier("RoleManageService")
	private RoleManageService  roleManageService;
	
	@RequestMapping(value = "/list.do")
	public String list(HttpServletRequest request, Model model)
	{
		List<Application> appList = applicationService.listAll();
		Object json = JSONArray.fromObject(appList);
		model.addAttribute("appList", json);
		return "openapi/application/list";
	} 
	
	@RequestMapping(value = "/changeStatus.do")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, Model model)
	{
		
		int status = ServletRequestUtils.getIntParameter(request, "appStatus",-1);
		int appId = ServletRequestUtils.getIntParameter(request, "appId",-1);
		Application app = applicationService.get(appId);
		app.setAppStatus(status);
		applicationService.update(app);
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		return json;
	}
	
	@RequestMapping(value = "/getAllocation.do")
	public String getAllocation(HttpServletRequest request, Model model)
	{
		int appId = ServletRequestUtils.getIntParameter(request, "appId", -1);
		Application app = applicationService.get(appId);
		List<AppRole> roleList = roleManageService.listAll();
		model.addAttribute("roleList", roleList);
		model.addAttribute("app", app);
		return "openapi/application/allocationRole";
	}
	
	@RequestMapping(value = "/allocationRole.do")
	@ResponseBody
	public Object allocationRole(HttpServletRequest request, Model model){
		int appId = ServletRequestUtils.getIntParameter(request, "appId", -1);
		Application app = applicationService.get(appId);
		int roleid = ServletRequestUtils.getIntParameter(request, "roleId", -1);
		String appkey = ServletRequestUtils.getStringParameter(request, "appKey", "");
		AppRole role = roleManageService.get(roleid);
		app.setRole(role);
		app.setAppKey(appkey);
		applicationService.update(app);
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "y");
		json.put("info", "分配角色成功！");
		return json;
	}
	
	@RequestMapping(value = "/createAppkey.do")
	@ResponseBody
	public Object createAppkey(HttpServletRequest request, Model model)
	{
		int appId = ServletRequestUtils.getIntParameter(request, "appId", -1);
		Application app = applicationService.get(appId);
		String encodestr = app.getAccount().getApperId()+app.getAppName();
		String appKey = "";
		try {
			appKey = MD5Coder.encodeMD5Hex(encodestr);
		} catch (Exception e) {
		}
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		json.put("appKey", appKey);
		return json;
	}
	
	@RequestMapping(value = "/deleteApplication.do")
	@ResponseBody
	public Object deleteApplication(HttpServletRequest request, Model model)
	{
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr", "");
		String[] ids = idstr.split(",");
		for(String appid:ids)
		{
			applicationService.delete(Integer.valueOf(appid));
		}
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		return json;
	}
	
	@RequestMapping(value = "/addApp.do")
	public String addApplication(HttpServletRequest request, Model model)
	{
		String method = request.getMethod();
		if("GET".equals(method))
		{
			List<Account> apperList = developerManageService.listAll();
			model.addAttribute("apperList", apperList);
			List<AppRole> roleList = roleManageService.listAll();
			model.addAttribute("roleList", roleList);
			return "openapi/application/add";
		}
		String appName = ServletRequestUtils.getStringParameter(request, "appName", "");
		long times = ServletRequestUtils.getLongParameter(request, "times", 0);
		int roleId = ServletRequestUtils.getIntParameter(request, "roleId", 0);
		int apperId = ServletRequestUtils.getIntParameter(request, "apperId", 0);
		
		if(!CommonUtil.isValidCharacter(appName)){
			return null;
		}
		
		Application app = new Application();
		app.setAppName(appName);
		app.setRole(roleManageService.get(roleId));
		app.setAccount(developerManageService.get(apperId));
		app.setTimes(times);
		app.setCtime(DateTimeUtil.getTimeStamp());
		app.setAppStatus(0);
		applicationService.save(app);
		model.addAttribute("url", "/application/list.do");
		model.addAttribute("message", "添加应用成功！");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/editApp.do")
	public String editApplication(HttpServletRequest request, Model model)
	{
		String method = request.getMethod();
		if("GET".equals(method))
		{
			int appId = ServletRequestUtils.getIntParameter(request, "appId", -1);
			Application app = applicationService.get(appId);
			List<Account> apperList = developerManageService.listAll();
			model.addAttribute("apperList", apperList);
			List<AppRole> roleList = roleManageService.listAll();
			model.addAttribute("roleList", roleList);
			model.addAttribute("app", app);
			return "openapi/application/edit";
		}
		String appName = ServletRequestUtils.getStringParameter(request, "appName", "");
		int appId = ServletRequestUtils.getIntParameter(request, "appId", 0);
		long times = ServletRequestUtils.getLongParameter(request, "times", 0);
		int roleId = ServletRequestUtils.getIntParameter(request, "roleId", 0);
		int apperId = ServletRequestUtils.getIntParameter(request, "apperId", 0);
		Application app = applicationService.get(appId);
		app.setAppName(appName);
		app.setRole(roleManageService.get(roleId));
		app.setAccount(developerManageService.get(apperId));
		app.setTimes(times);
		app.setUtime(DateTimeUtil.getTimeStamp());
		applicationService.update(app);
		model.addAttribute("url", "/application/list.do");
		model.addAttribute("message", "修改应用成功！");
		return Constants.FORWARDURL;
	}
	
	
}
