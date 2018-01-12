package com.richinfo.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionHandler implements HandlerExceptionResolver 
{

	private final static Logger log = Logger.getLogger("exceptionLog");
	
	public ModelAndView resolveException(HttpServletRequest request,HttpServletResponse response, Object obj, Exception ex) 
	{
		log.info("*********************************一次异常的分割线*****************************************");
		log.error("请求URL:"+request.getContextPath()+request.getServletPath()+" 发生异常！！",ex);
		return new ModelAndView("exception");
	}

}
