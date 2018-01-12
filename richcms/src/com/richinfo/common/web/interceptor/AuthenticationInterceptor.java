package com.richinfo.common.web.interceptor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemProperties;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.UserService;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter
{
	private UserService userService;
	 
	@Autowired
	@Qualifier("UserService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	List<Menu> menuList  = null;
	
	private String loginUrl;
    
	public void setLoginUrl(String loginUrl) 
	{
        this.loginUrl = loginUrl;
    }
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
		
		String requestUrl = request.getServletPath();
		String authUrl = requestUrl.replaceAll("/", "").toLowerCase();
		
		//登录方法不拦截
		if(request.getServletPath().startsWith(loginUrl)) 
		{
			return true;
		}	
		User user = (User)request.getSession(true).getAttribute(Constants.CURRENT_USER_ACCOUNT);
		Role role = user.getRole();
		String superMangager = SystemProperties.getInstance().getProperty("authentication.superManager");
		//超级管理员无论是否配置可见菜单，都默认具有执行所有方法的权限
		
		if(superMangager.equals(role.getRoleName()))
		{
			return true;
		}
		
		//生成查询首页框架数据的权限不拦截
		String excludesUrl = SystemProperties.getInstance().getProperty("authentication.excludesUrl");
		if(excludesUrl.indexOf(authUrl)>-1)
		{
			return true;
		}
		if(menuList==null){
			 menuList = userService.findMenuList(user.getUserId());
		}
		Collection<String> menuCollection = new HashSet<String>();
		for(Menu menu:menuList)
		{
			String item = (menu.getControl()+menu.getAction()).replaceAll("/", "").toLowerCase();
			menuCollection.add(item);
		}
		//判断执行的方法是否在用户的角色所关联的权限集合中
		if(!menuCollection.contains(authUrl))
		{
			response.sendRedirect(request.getContextPath()+ "/login/inValid");
			return false;
		}
		
		return true;
	}
}
