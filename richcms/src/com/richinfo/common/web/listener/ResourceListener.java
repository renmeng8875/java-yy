package com.richinfo.common.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class ResourceListener implements ServletContextListener
{
	private final static Logger log = Logger.getLogger(ResourceListener.class);

	public void contextDestroyed(ServletContextEvent event) 
	{
		log.info("web application resource listener destroyed...");
		
	}

	public void contextInitialized(ServletContextEvent event) 
	{
		log.info("web application config parameters initialized...");
		ServletContext context = event.getServletContext();
		String version = context.getInitParameter("vesion");
		String ctx = context.getInitParameter("context");
		context.setAttribute("ctx", ctx);
		context.setAttribute("version", version);
		
	}

}
