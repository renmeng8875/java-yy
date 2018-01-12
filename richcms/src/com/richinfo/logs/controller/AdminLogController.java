package com.richinfo.logs.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.SystemContext;
import com.richinfo.common.pagination.Page;
import com.richinfo.contentcat.entity.ContentData;
import com.richinfo.logs.service.AdminLogService;
import com.richinfo.logs.entity.Trace;

 

@Controller
@RequestMapping(value = "/adminLog")
public class AdminLogController {

	private AdminLogService adminLogService;

	@Autowired
	@Qualifier("AdminLogService")
	public void setAdminLogService(AdminLogService adminLogService) {
		this.adminLogService = adminLogService;
	}

	
	/**
	 * 登陆后 跳转至后台首页
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		return "/adminLog/list";
	}
	
	@RequestMapping(value = "/listsJson.do", method = RequestMethod.POST)
	@ResponseBody
	public Object listsJson(HttpServletRequest request, Model model) {
		//分页
		int page= ServletRequestUtils.getIntParameter(request, "page", -1);
		int pagesize=ServletRequestUtils.getIntParameter(request,"pagesize",10);
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		Trace trace = new Trace();
	    Page<Trace> pages = adminLogService.find(trace);
		
		Map<String,Object> resutlMap=null;
		resutlMap =new HashMap<String,Object>();
		
		resutlMap.put("Rows", pages.getItems());
		resutlMap.put("Total", pages.getTotal());
		return resutlMap;
	}
	
    @RequestMapping(value = "/update.do", method = RequestMethod.GET)
	public String update(HttpServletRequest request, Model model){
    	int logId = ServletRequestUtils.getIntParameter(request, "logId", -1);
    	Trace trace = adminLogService.get(logId);
    	model.addAttribute("model", trace);
		return "/adminLog/edit";
	}
    
    /**
     * 删除和批量删除日志条目
     * @param request
     * @param model
     * @return
     * @author songtonglong
     */
    @RequestMapping(value = "/batchDelete.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> batchDelete(HttpServletRequest request, Model model) {
    	String idstr = ServletRequestUtils.getStringParameter(request, "idstr", "");
    	String[] ids = idstr.split(",");
    	adminLogService.batchDelete(ids);
    	Map<String, String> json = new HashMap<String, String>();
    	json.put("status", "0");
    	return json;
    }
}
