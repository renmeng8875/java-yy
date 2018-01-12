package com.richinfo.common;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class TokenMananger 
{

	public static final String CSRF_PARAM_NAME = "csrfToken";
	
	public static final String CSRF_TOKEN_SESSION_ATTR_NAME = TokenMananger.class.getName()+".tokenval"; 
	
	private TokenMananger(){}
	
	public static String getTokenFromSession(HttpSession session)
	{
		String token = null;
		synchronized (session) 
		{
			token = (String)session.getAttribute(CSRF_TOKEN_SESSION_ATTR_NAME);
			if(token==null)
			{
				token = UUID.randomUUID().toString();
				session.setAttribute(CSRF_TOKEN_SESSION_ATTR_NAME, token);
			}	
		}
		return token;
	}
	
	public static String getTokenFromRequest(HttpServletRequest request)
	{
		return request.getParameter(CSRF_PARAM_NAME);
	}
	
}
