package com.richinfo.privilege.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.entity.Shortcut;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.MenuService;
import com.richinfo.privilege.service.ShortcutService;

@Controller
@RequestMapping(value = "/menu")
public class MenuController 
{
	
	private MenuService menuService;
	
	private ShortcutService shortcutService;
	
	@Autowired
	@Qualifier("ShortcutService")
	public void setShortcutService(ShortcutService shortcutService) {
		this.shortcutService = shortcutService;
	}


	@Autowired
	@Qualifier("MenuService")
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
	
	
	@RequestMapping(value = "/addShortcut.do",method=RequestMethod.GET)
	public String getShortcutList(HttpServletRequest request, Model model) 
	{
		List<Menu> menuList = menuService.listShortcut();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"parent","children"});
		JSONArray json = JSONArray.fromObject(menuList,config);
		model.addAttribute("gridData",json.toString());
		HttpSession session = request.getSession();
		User u = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		List<Integer> idList = shortcutService.queryMyShortcutId(u.getUserId());
		JSONArray menuIdjson = JSONArray.fromObject(idList);
		model.addAttribute("menuIdArray", menuIdjson);
		return "shortcut/add";
	}
	
	@RequestMapping(value = "/addShortcut.do",method=RequestMethod.POST,params="idstr")
	@ResponseBody
	public Object addShortcut(HttpServletRequest request, Model model) 
	{
		String  idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		HttpSession session = request.getSession();
		User u = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		shortcutService.deleteShortcutsByUserId(u.getUserId());
		if(!StringUtils.isEmpty(idstr))
		{
			String[] ids = idstr.split(","); 
			for(String menuId:ids)
			{
				Shortcut sc = new Shortcut();
				sc.setMenuId(Integer.valueOf(menuId));
				sc.setUserId(u.getUserId());
				shortcutService.save(sc);
			}
		}
		
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value = "/listShortcut.do")
	public String listShortcut(HttpServletRequest request, Model model) 
	{
		HttpSession session = request.getSession();
		User u = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		List<Menu> shortcuList = new ArrayList<Menu>(); 
		if(u!=null){
			shortcuList = menuService.listMyShortcut(u.getUserId());
		}
		model.addAttribute("shortcutList", shortcuList);
		return "shortcut/list";
	}
	
	
	@RequestMapping(value = "/deleteshortcut.do")
	@ResponseBody
	public Object deleteShortcut(HttpServletRequest request, Model model) 
	{
		String ids = ServletRequestUtils.getStringParameter(request, "ids","");
		String[] idList = ids.split(",");
		HttpSession session = request.getSession();
		User u = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		int userId = u.getUserId();
		for(String menuid:idList)
		{
			shortcutService.deleteShortcuts(userId, Integer.valueOf(menuid));
		}
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	
	
}
