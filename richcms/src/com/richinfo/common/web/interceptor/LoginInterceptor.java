package com.richinfo.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.richinfo.common.Constants;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    
    private String loginUrl;
    
    private String indexUrl; 
  

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	HttpSession session = request.getSession(true);
    	if(session.isNew()){
    		response.sendRedirect(request.getContextPath() + loginUrl);
            return false;
    	}
       
    	
    	//1、请求到登录页面且没有登录则通过
        if(request.getServletPath().startsWith(loginUrl)) 
        {
        	 //2.如果请求到登录页面但用户已登录则跳转到首页，不需要再次登录
        	 if(request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT) != null) 
        	 {
        		 response.sendRedirect(request.getContextPath() + indexUrl);
        		 return true;
             }
            return true;
        }
        
        //3.如果用户已经登录 放行  
        if(session.getAttribute(Constants.CURRENT_USER_ACCOUNT) != null) {
            return true;
        }
        
        //4.非法请求重定向到登录页面
        response.sendRedirect(request.getContextPath() + loginUrl);
        return false;
    }

}
