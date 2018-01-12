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

import com.richinfo.openapi.entity.FunManage;
import com.richinfo.openapi.service.FunManageService;
import com.richinfo.openapi.service.SysPrivilegeOfRoleService;

@Controller
@RequestMapping(value = "/funmanage")
public class FunManageController {

	@Autowired
	@Qualifier(value = "FunManageService")
	private FunManageService funManageService;
	
	@Autowired
	@Qualifier("SysPrivilegeOfRoleService")
	private SysPrivilegeOfRoleService sysPrivilegeOfRoleService;
	
	@RequestMapping("/list.do")
	public String lists(HttpServletRequest request,Model model)
	{
		List<FunManage> funManageList = funManageService.listAll();
		String operType = request.getParameter("opertype");
		if("chose".equals(operType))
		{
			model.addAttribute("operType", "选择");
			model.addAttribute("click", "choseFuns");
			model.addAttribute("icon", "ok");
		}else{
			model.addAttribute("operType", "删除");
			model.addAttribute("click", "deleteFuns");
			model.addAttribute("icon", "delete");
		}
		model.addAttribute("funManageList", JSONArray.fromObject(funManageList));
		return "openapi/funmanage/lists";
	}
	
	@RequestMapping(value = "/deletefun.do")
	@ResponseBody
	public Object deleteFun(HttpServletRequest request)
	{
		Map<String,String> jsonMap = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			if(sysPrivilegeOfRoleService.canDeleteFun(Integer.valueOf(id)))
			{
				funManageService.delete(Integer.valueOf(id));
				sysPrivilegeOfRoleService.deleteByFunId(Integer.valueOf(id));
			}
			else{
				jsonMap.put("status", "1");
				return jsonMap;
			}
			
		}
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
}
