package com.richinfo.privilege.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.service.MenuService;
import com.richinfo.privilege.service.PrivilegeService;

@Controller
@RequestMapping(value = "/Extendsetting")
public class ExtendSettingController {

	private MenuService menuService;
	
	private PrivilegeService privilegeService;

	@Autowired
	@Qualifier("MenuService")
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@Autowired
	@Qualifier("PrivilegeService")
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}
	/**
	 * 登陆后 跳转至后台首页
	 * 
	 * @param request
	 * @param model 
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		Menu root = menuService.getMenuRoot();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "parent" });
		Object json = JSONObject.fromObject(root, config);
		model.addAttribute("treeData", json.toString());
		return "extendsetting/lists";
	}
   /**
    * 添加子菜单
    * @param request
    * @param model
    * @return
    */
	@RequestMapping(value = "/add.do")
	public String add(HttpServletRequest request, Model model){
		String pidStr = ServletRequestUtils.getStringParameter(request, "pid","");
		String idSrtr = ServletRequestUtils.getStringParameter(request, "menuid","");
		if(!StringUtils.isNumeric(pidStr)||!StringUtils.isNumeric(idSrtr)){
			return null;
		}
		
		Menu root = menuService.getMenuRoot();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "parent" });
		Object json = JSONObject.fromObject(root, config);
		model.addAttribute("treeData", json.toString());
		int pid = ServletRequestUtils.getIntParameter(request, "pid",-1);
		int id = ServletRequestUtils.getIntParameter(request, "menuid", -1);
		if(id>-1){
			Menu menu = menuService.get(id);
			model.addAttribute("info", menu);
			model.addAttribute("pid", menu.getParent()!=null?menu.getParent().getMenuId():pid);
		}else{
			  model.addAttribute("pid", pid);
		}
		return "extendsetting/add";
 
		//save
	}
    
	/**
	 * 
	 * @param request
	 * @param model
	 * @param newmenu
	 * @return
	 */
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model,@ModelAttribute Menu newmenu){
		//appScan
		String pidStr = ServletRequestUtils.getStringParameter(request, "pid","");
		String menuidStr = ServletRequestUtils.getStringParameter(request, "menuId", "");
		if(!StringUtils.isNumeric(pidStr)
				||!StringUtils.isNumeric(menuidStr)
				||CommonUtil.hasInValidCharacter(newmenu)){
			return null;
		}
		
		int pid = ServletRequestUtils.getIntParameter(request, "pid",-1);
		int menuid = ServletRequestUtils.getIntParameter(request, "menuId", -1);
		Menu parent =(pid==menuid&&menuid!=-1)?null:menuService.get(pid);
		newmenu.setMenuLevel((pid==menuid&&menuid!=-1)?0:(menuService.qetMenuLevel(pid)+1));
		newmenu.setModule("Admin");
		newmenu.setParent(parent);
		
		if(newmenu!=null&&menuid>=0){
			newmenu.setMenuId(menuid);
			menuService.merge(newmenu);
			model.addAttribute("message", "修改菜单成功！");
		}else{
			menuService.save(newmenu);
			model.addAttribute("message", "添加子菜单成功！");
		}
		model.addAttribute("url","/Extendsetting/lists.do");
		return Constants.FORWARDURL;
	}
   /**
    * 
    * 删除当前菜单
    * @param request
    * @param model
    * @return
    */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Map<String,String> del(HttpServletRequest request, Model model){
		 Map<String,String> json = new HashMap<String, String>();
		 int menuid = ServletRequestUtils.getIntParameter(request, "menuid", -1);
		 if(menuid>0){
			 Menu menu = menuService.get(menuid);
			 if(menu.getChildren().size()>0){
				 json.put("status", "2");
				 json.put("msg", "不能删除,该菜单下还存在子菜单");
			 }else{
				 if(privilegeService.deletePrivilegeByMenuid(menuid)){
					 menuService.delete(menuid);//删除模块
					 json.put("status", "0");
					 json.put("msg", "删除成功");
				 }else{
					 json.put("status", "3");
					 json.put("msg", "删除失败");
				 }
			 }
		 }else{
			 json.put("1", "数据不存在");
		 }
		return json;
	}
}
