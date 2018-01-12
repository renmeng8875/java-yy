package com.richinfo.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.richinfo.common.SystemContext;
import com.richinfo.common.TokenMananger;
import com.richinfo.common.utils.CommonUtil;

public class MontiorInterceptor extends HandlerInterceptorAdapter 
{

    private NamedThreadLocal<Long>  startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");
    
    private final static Logger log = Logger.getLogger("montiorLog");
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	//动态获取用户请求的方法名和类名
    	/*HandlerMethod hm = (HandlerMethod)handler;
    	String requestMethod = hm.getBean().getClass().getName()+"."+hm.getMethod().getName();
    	System.err.println(requestMethod);*/
    	
    	
    	long beginTime = System.currentTimeMillis();
        startTimeThreadLocal.set(beginTime);
        HttpSession session = request.getSession(true);
        SystemContext.setSessionContext(session);
        String method = request.getMethod();
        if("GET".equals(method))
        {
        	request.setAttribute(TokenMananger.CSRF_PARAM_NAME, TokenMananger.getTokenFromSession(session));
        }
        
        //所有页面参数加token也不能解决安全问题，先注释后续版本解决
        if("POST".equals(method))
        {
        	
        	String clientToken = request.getParameter(TokenMananger.CSRF_PARAM_NAME);
        	String serverToken = (String)session.getAttribute(TokenMananger.CSRF_TOKEN_SESSION_ATTR_NAME);
        	if((StringUtils.isNotEmpty(clientToken)&&!clientToken.equals(serverToken))||StringUtils.isEmpty(clientToken))
        	{
        		log.warn("发现非法请求,url:"+request.getRequestURI()+",ip:"+CommonUtil.getClientIp(request));
        		return false;
        	}
        		
        	
        }
        
        return true;
    }
    
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        long beginTime = startTimeThreadLocal.get();
        long consumeTime = endTime - beginTime;
        if(consumeTime > 500) 
        {
            log.warn("警告:本页面请求处理时间过长,url:"+request.getRequestURI()+"|useTime:"+ consumeTime+"ms!");
        }
        
    }
    
}
