package com.richinfo.privilege.controller;


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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.privilege.entity.SysConfig;
import com.richinfo.privilege.service.SysConfigService;

@Controller
@RequestMapping(value = "/Sysconfig")
public class SysConfigController {

	private SysConfigService sysConfigService;
	

	@Autowired
	@Qualifier("SysConfigService")
	public void setSysConfigService(SysConfigService SysConfigService) {
		this.sysConfigService = SysConfigService;
	}

	/**
	 * 登陆后 跳转至后台首页
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	
	public String lists(HttpServletRequest request, Model model) {
		List<SysConfig> configlist = sysConfigService.listAll();
        model.addAttribute("configlist", JSONArray.fromObject(configlist));
		return "sysconfig/lists";
		
	}
 

	/**
	 * add edit
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add.do")
    public String add(HttpServletRequest request, Model model,@ModelAttribute SysConfig config) {
		String method = request.getMethod();
		//to 新增 修改
		if("GET".equals(method)){
			int sid = ServletRequestUtils.getIntParameter(request, "sid", -1);
			if(sid>0){//edit
				SysConfig vo = sysConfigService.get(sid);
				model.addAttribute("data", vo);
			}
			return "sysconfig/add";
		}
		String sysvalue=ServletRequestUtils.getStringParameter(request, "sysvalue", "");
		config.setSysvalue(HtmlUtils.htmlEscape(sysvalue));
		//保存
		if(config.getSid()>0){
			config.setCtime(DateTimeUtil.getTimeStamp());
			sysConfigService.update(config);
			model.addAttribute("message", "修改配置成功");
			model.addAttribute("url","/Sysconfig/lists.do");
		}else{
			sysConfigService.save(config);
			model.addAttribute("message", "配置成功");
			model.addAttribute("url","/Sysconfig/lists.do");
		}
		return Constants.FORWARDURL;
    }
	
	
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Map<String,String> del(HttpServletRequest request, Model model){
		Map<String,String> json = new HashMap<String,String>();
		String idstr =request.getParameter("idstr");
		String[] ids = idstr.split(",");
		for(String id:ids){
			sysConfigService.delete(Integer.valueOf(id));
		}
		json.put("status", "0");
		return json;
	}
	
	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String name = request.getParameter("param");
		int pk = ServletRequestUtils.getIntParameter(request, "pk", -1);
		Map<String,Object> json = new HashMap<String, Object>();
		boolean flag = sysConfigService.nameExists(name,pk);
		if(!flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "名称重复,请重新输入！");
		}
		
		return json;
	}
}
