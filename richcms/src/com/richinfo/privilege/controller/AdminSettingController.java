package com.richinfo.privilege.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.richinfo.common.utils.encryptUtil.RSAUtil;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.RoleService;
import com.richinfo.privilege.service.UserService;

@Controller
@RequestMapping(value = "/Adminsetting")
public class AdminSettingController {

	private UserService userService;
	
	private RoleService roleService;
	
	@Autowired
	@Qualifier("UserService")
	public void setMenuService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	@Qualifier("RoleService")
	public void setMenuService(RoleService roleService) {
		this.roleService = roleService;
	}

	
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<User> ulist = userService.listAll();
		model.addAttribute("ulist", ulist);
		return "adminsetting/lists";
	}

	@RequestMapping(value = "/changerole.do")
	public String changeRole(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		int userid = ServletRequestUtils.getIntParameter(request, "userId",-1);
		User user = userService.get(userid);
		
		if("GET".equals(methodType))
		{
			List<Role> roleList = roleService.listAll();
			model.addAttribute("user", user);
			model.addAttribute("roleList", roleList);
			return "adminsetting/edit";
		}
		 
		int roleid = ServletRequestUtils.getIntParameter(request, "role",-1);
		Role role = roleService.get(roleid);
		user.setRole(role);
		userService.update(user);
		model.addAttribute("message", "修改角色成功！");
		model.addAttribute("url","/Adminsetting/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/changePwd.do")
	public String changePassword(HttpServletRequest request, Model model)
	{
		int userId = ServletRequestUtils.getIntParameter(request, "userId",0);
		String methodType = request.getMethod();
		User user = userService.get(userId);
		if("GET".equals(methodType))
		{
			model.addAttribute("user", user);
			return "adminsetting/resetpwd";
		}
		String pwd = ServletRequestUtils.getStringParameter(request, "passwd","");
		pwd = RSAUtil.restoreText(pwd);
		String pwdl = ServletRequestUtils.getStringParameter(request, "passwd1","");
		if(pwd.equals(pwdl))
		{
			
		}
		user.setPasswd(pwd);
		userService.updateUserPwd(user);
		model.addAttribute("message", "修改密码成功");
		model.addAttribute("url","/Adminsetting/lists.do");
		return Constants.FORWARDURL;
	}
	
	
	@RequestMapping(value = "/lock.do")
	public String lockUser(HttpServletRequest request, Model model)
	{
		int userId = ServletRequestUtils.getIntParameter(request, "userId",-1);
		userService.lockUser(userId);
		model.addAttribute("message", "锁定用户成功！");
		model.addAttribute("url", "/Adminsetting/lists.do");
		return Constants.FORWARDURL;
	} 
	
	@RequestMapping(value = "/unlock.do")
	public String unLockUser(HttpServletRequest request, Model model)
	{
		int userId = ServletRequestUtils.getIntParameter(request, "userId",-1);
		userService.unlockUser(userId);
		model.addAttribute("message", "解锁用户成功！");
		model.addAttribute("url", "/Adminsetting/lists.do");
		return Constants.FORWARDURL;
	} 
	
	@RequestMapping(value = "/deluser.do",method=RequestMethod.POST)
	public String deleteUser(HttpServletRequest request, Model model)
	{
		int userId = ServletRequestUtils.getIntParameter(request, "userId",-1);
		userService.delete(userId);
		model.addAttribute("message", "删除用户成功！");
		model.addAttribute("url", "/Adminsetting/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/deluser.do",method=RequestMethod.GET)
	@ResponseBody
	public Object deleteUserFromMem(HttpServletRequest request, Model model)
	{
		int userId = ServletRequestUtils.getIntParameter(request, "userId",-1);
		userService.delete(userId);
		Map<String,Object> json = new HashMap<String, Object>();
		json.put("status", "0");
		return json;
	}
	

	@RequestMapping(value = "/adduser.do")
	public String addUser(HttpServletRequest request, Model model)
	{
		String methodType = request.getMethod();
		List<Role> roleList = roleService.listAll();
		model.addAttribute("roleList", roleList);
		if("GET".equals(methodType))
		{
			return "adminsetting/add";
		}
		else
		{
			String username = ServletRequestUtils.getStringParameter(request, "username","");
			String pwd = ServletRequestUtils.getStringParameter(request, "passwd","");
			pwd = RSAUtil.restoreText(pwd);
			String pwdl = ServletRequestUtils.getStringParameter(request, "passwd1","");
			if(pwd.equals(pwdl))
			{
				
			}
			String realname = ServletRequestUtils.getStringParameter(request, "realname","");
			String roleTemp=ServletRequestUtils.getStringParameter(request, "role", "");
			if(StringUtils.isEmpty(roleTemp)&&!StringUtils.isNumeric(roleTemp)){
				return null;
			}
			int roleid = ServletRequestUtils.getIntParameter(request, "role",0);
			User user = new User();
			user.setUserName(username);
			user.setPasswd(pwd);
			user.setNickName(realname);
			Role role = roleService.get(roleid);
			user.setRole(role);
			userService.addManeger(user);
			model.addAttribute("message", "添加管理员成功！");
			model.addAttribute("url", "/Adminsetting/lists.do");
		}
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String userName = request.getParameter("param");
		Map<String,Object> json = new HashMap<String, Object>();
		User user=this.userService.getUserByName(userName);
		boolean flag = user!=null?true:false;
		if(!flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "名称重复,请重新输入！");
		}
		
		return json;
	}
	
	
}
