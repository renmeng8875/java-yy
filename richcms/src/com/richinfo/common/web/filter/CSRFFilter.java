package com.richinfo.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CSRFFilter implements Filter{

	public void destroy() {
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,FilterChain filterchain) throws IOException, ServletException {
		 HttpServletRequest request = (HttpServletRequest) servletRequest;
	     HttpServletResponse response = (HttpServletResponse) servletResponse; 
		 String clientSessionId = servletRequest.getParameter("ssid");
	     String serverSessionId = request.getSession().getId();
         if (serverSessionId.equals(clientSessionId)) {
            filterchain.doFilter(request, response);
         } else {
            response.sendRedirect(request.getContextPath() + "/login/auth.do");
         }
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		
		
	}

}
