package com.richinfo.openapi.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.encryptUtil.MD5Coder;
import com.richinfo.common.utils.encryptUtil.RSAUtil;
import com.richinfo.openapi.entity.Account;
import com.richinfo.openapi.service.ApplicationService;
import com.richinfo.openapi.service.DeveloperManageService;

@Controller
@RequestMapping(value = "/developermanage")
public class DeveloperManageController {
	
	@Autowired
	@Qualifier("DeveloperManageService")
	private DeveloperManageService developerManageService;
	
	@Autowired
	@Qualifier("ApplicationService")
	private ApplicationService applicationService;

	
	@RequestMapping(value = "/list.do")
	public String developerList(HttpServletRequest request, Model model)
	{
		List<Account> developerManagerlist = developerManageService.listAll();
        model.addAttribute("developerManagerlist", JSONArray.fromObject(developerManagerlist));
		return "openapi/developermanage/lists";
	} 
	
	@RequestMapping(value = "/deleteapper.do")
	@ResponseBody
	public Object deleteApper(HttpServletRequest request)
	{
		Map<String,String> jsonMap = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		String[] ids = idstr.split(",");
		StringBuffer sf=new StringBuffer();
		for(String id:ids)
		{
			boolean isFlag=applicationService.isApperInApp(Integer.valueOf(id));
			if(isFlag)
			{
				sf.append(id).append(",");
			}
		}
		
		if("".equals(sf.toString())){
			for(String id:ids)
			{
				developerManageService.delete(Integer.valueOf(id));
			}
			jsonMap.put("status", "0");
		}else{
			jsonMap.put("status", "-1");
			jsonMap.put("idstr", (sf.toString()).substring(0, (sf.toString()).lastIndexOf(",")));
		}
		return jsonMap;
	}
	
	@RequestMapping(value = "/addapper.do")
	public String addApper(HttpServletRequest request,Model model,@ModelAttribute Account developerManage)
	{
		String method = request.getMethod();
		if("GET".equals(method)){
			//处理appScan脚本编制
			CommonUtil.escapeHtmlForObject(developerManage);
			model.addAttribute("opertype", "addapper");
			return "openapi/developermanage/add";
		}
		developerManage.setStatus(0);
		developerManage.setCtime(new Date());
		String passwd = developerManage.getPasswd();
		passwd = RSAUtil.restoreText(passwd);
		try {
			passwd = MD5Coder.encodeMD5Hex(passwd);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "添加失败！");
			model.addAttribute("url","/developermanage/list.do");
			return Constants.FORWARDURL;
		}
		CommonUtil.unEscapeHtmlForObject(developerManage);
		developerManage.setPasswd(passwd);
		developerManageService.save(developerManage);
		model.addAttribute("message", "添加成功！");
		model.addAttribute("url","/developermanage/list.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/editApper.do")
	public String editApper(HttpServletRequest request,Model model,@ModelAttribute Account developerManage){
		String method = request.getMethod();
		if("GET".equals(method))
		{	
			int apperId = ServletRequestUtils.getIntParameter(request, "apperId",-1);
			Account account = developerManageService.get(apperId);
			model.addAttribute("opertype", "editApper");
			model.addAttribute("account",account);
			return "openapi/developermanage/add";
		}
		Account old = developerManageService.get(developerManage.getApperId());
		old.setApper(developerManage.getApper());
		old.setUname(developerManage.getUname());
		old.setEmail(developerManage.getEmail());
		old.setTel(developerManage.getTel());
		String passwd = developerManage.getPasswd();
		passwd = RSAUtil.restoreText(passwd);
		try {
			passwd = MD5Coder.encodeMD5Hex(passwd);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失败！");
			model.addAttribute("url", "/developermanage/list.do");
			return Constants.FORWARDURL;
		}
		CommonUtil.unEscapeHtmlForObject(developerManage);
		old.setPasswd(passwd);
		developerManageService.merge(old);
		model.addAttribute("message", "修改成功！");
		model.addAttribute("url","/developermanage/list.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/updateStatus.do")
	public String updateStatus(HttpServletRequest request,Model model)
	{
		int apperId = Integer.valueOf(ServletRequestUtils.getStringParameter(request,"apperid",""));
		int status = Integer.valueOf(ServletRequestUtils.getStringParameter(request,"status",""));
		Account developerManage = developerManageService.get(apperId);
		developerManage.setStatus(status);
		developerManage.setCtime(new Date());
		developerManageService.update(developerManage);
		model.addAttribute("message", "修改状态成功！");
		model.addAttribute("url","/developermanage/list.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkApperName(HttpServletRequest request,Model model)
	{
		String apperName = ServletRequestUtils.getStringParameter(request, "param", "");
		Map<String,Object> json = new HashMap<String, Object>();
		boolean flag = developerManageService.checkApperExist(apperName);
		if(!flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "开发者名称已经存在请重新输入！");
		}
		return json;
	}
}
