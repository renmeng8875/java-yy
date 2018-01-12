package com.richinfo.privilege.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.MenuService;
import com.richinfo.privilege.service.ShortcutService;
import com.richinfo.privilege.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class IndexController {

	private MenuService menuService;
	
	@SuppressWarnings("unused")
	private ShortcutService shortcutService;

	private UserService userService;
	 
	@Autowired
	@Qualifier("UserService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	@Qualifier("ShortcutService")
	public void setShortcutService(ShortcutService shortcutService) 
	{
		this.shortcutService = shortcutService;
	}

	@Autowired
	@Qualifier("MenuService")
	public void setMenuService(MenuService menuService) 
	{
		this.menuService = menuService;
	}

	List<Menu> menuList  = null;
	/**
	 * 登陆后 跳转至后台首页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index.do")
	public String index(HttpServletRequest request, Model model) {

		return "index/index";
	}
	
	@RequestMapping(value = "/drag.do")
	public String drag(HttpServletRequest request, Model model) 
	{
		return "frame/drag";
	}

	/**
	* 方法说明:获取一级菜单  
	* @param request
	* @param model
	* @return String
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-5-15
	 */
	@RequestMapping(value = "/top.do")
	public String top(HttpServletRequest request, Model model) 
	{
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		if(menuList==null){
			 menuList = userService.findMenuList(user.getUserId());
		}
		List<Menu> topmenu = new ArrayList<Menu>();
		for(Menu menu:menuList)
		{
			if(menu.getMenuLevel()==1)
			{
				topmenu.add(menu);
			}	
		}
		model.addAttribute("topmenulist",topmenu);
		return "frame/top";
	}

	
    
	/**
	* 方法说明:  初次进入首页时默认显示的菜单
	* @param request
	* @param model
	* @return 返回页面链接
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-5-15
	 */
	@RequestMapping(value = "/menu.do")
	public String menu(HttpServletRequest request, Model model) 
	{
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		if(menuList==null){
			 menuList = userService.findMenuList(user.getUserId());
		}
		List<Menu> leftmenu = new ArrayList<Menu>();
		for(Menu menu:menuList)
		{
			if(menu.getMenuLevel()==1)
			{
				leftmenu = menuService.getChildren(menu.getMenuId(),user.getUserId());
				break;
			}	
		}	
		model.addAttribute("leftmenulist",leftmenu);
		return "frame/menu";
	}
	
	/**
	* 方法说明:获取二级菜单数据  
	* @param request
	* @param model
	* @return json
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-5-15
	 */
	@RequestMapping(value = "/getleftmenu.do")
	@ResponseBody
	public Map<String,Object> getleftmenu(HttpServletRequest request, Model model)
	{
		Map<String,Object> json = new HashMap<String, Object>();
		int pid = ServletRequestUtils.getIntParameter(request, "pid",-1);
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		List<Menu> leftmenu = menuService.getChildren(pid,user.getUserId());
		JsonConfig config = new JsonConfig(); 
		config.setExcludes(new String[]{"parent"});  
		json.put("leftmenulist", JSONArray.fromObject(leftmenu,config));
		return json;
	}

	/**
	* 方法说明:查询我的面板中我的外捷键数据  
	* @param request
	* @param model
	* @return String
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-5-15
	 */
	@RequestMapping(value = "/main.do")
	public String main(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		User u = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		List<Menu> shortcutList = new ArrayList<Menu>(); 
		if(u!=null)
		{
			shortcutList = menuService.listMyShortcut(u.getUserId());
		}
		for(int i=shortcutList.size()-1;i>-1;i--)
		{
			Menu menu = shortcutList.get(i);
			if(menu.getAction().indexOf(".do")==-1)
			{
				shortcutList.remove(i);
			}
		}
		model.addAttribute("shortcutList", shortcutList);
		return "frame/main";
	}

}
