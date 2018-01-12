package com.richinfo.common.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

import com.richinfo.common.utils.AntiXSSUtil;

public class XssRequestWrapper extends HttpServletRequestWrapper
{
	
	
	public XssRequestWrapper(HttpServletRequest request) 
	{
		super(request);
	}

	@Override
    public String getParameter(String name)
    {
        String value = super.getParameter(name);
        if(StringUtils.isNotEmpty(value))
        {
        	value = AntiXSSUtil.antiXSSNEW(value);
        }
        
        return value;
    }
}
