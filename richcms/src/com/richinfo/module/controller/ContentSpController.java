package com.richinfo.module.controller;


import java.net.URLEncoder;
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
import com.richinfo.common.SystemContext;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.common.utils.encryptUtil.DESCoder;
import com.richinfo.module.entity.ContentSp;
import com.richinfo.module.service.ContentSpService;
import com.richinfo.module.service.SpPublishService;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.service.SysConfigService;

@Controller
@RequestMapping(value = "/ContentSp")
public class ContentSpController {
	
	private ContentSpService contentSpService;
	private SpPublishService spPublishService;
	private SysConfigService sysConfigService;

	@Autowired
	@Qualifier("SysConfigService")
	public void setSysConfigService(SysConfigService sysConfigService) {
		this.sysConfigService = sysConfigService;
	}

	@Autowired
	@Qualifier("ContentSpService")
	public void setContentSpService(ContentSpService contentSpService) {
		this.contentSpService = contentSpService;
	}
	
	@Autowired
	@Qualifier("SpPublishService")
	public void setSpPublishService(SpPublishService spPublishService) {
		this.spPublishService = spPublishService;
	}
	
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		model.addAttribute("frontendDomain",SystemProperties.getInstance().getProperty("contentMgt.frontendDomain"));
		Role role=(Role)request.getSession().getAttribute(Constants.CURRENT_USER_ROLE);
		model.addAttribute("roleId", role!=null?role.getRoleid():"");
		return "contentsp/lists";
	}
	
	@RequestMapping(value = "/findLists.do")
	@ResponseBody
	public Object findLists(HttpServletRequest request, Model model) {
		//分页
		int page= ServletRequestUtils.getIntParameter(request, "page", -1);
		int pagesize=ServletRequestUtils.getIntParameter(request,"pagesize",10);
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		//列表
		String stype=ServletRequestUtils.getStringParameter(request, "stype", "");
		String txthead=ServletRequestUtils.getStringParameter(request, "txthead", "");
		
		String ttype=ServletRequestUtils.getStringParameter(request, "ttype", "");
		String stime=ServletRequestUtils.getStringParameter(request, "stime", "");
		String etime=ServletRequestUtils.getStringParameter(request, "etime", "");
		etime=(etime.length()<=10&&!StringUtils.isEmpty(etime))?(etime+" 23:59:59"):etime;
		Long startTime=StringUtils.isEmpty(stime)?null:DateTimeUtil.converToTimestamp(stime,"yyyy-MM-dd");
		Long endTime=StringUtils.isEmpty(etime)?null:DateTimeUtil.converToTimestamp(etime,"yyyy-MM-dd HH:mm:ss");
		
        ContentSp c=new ContentSp();
        if("title".equals(stype)){
        	c.setTitle(txthead);
        }else{
        	if(!StringUtils.isEmpty(txthead)){
        		if(StringUtils.isNumeric(txthead)){
        			c.setCompanyId(Integer.valueOf(txthead));
        		}else{
        			c.setCompanyId(-1);
        		}
        	}
        }
        if("pubtime".equals(ttype)){
        	c.setPublishBeginTime(startTime);
        	c.setPublishEndTime(endTime);
        }else{
        	c.setSubmitBeginTime(startTime);
        	c.setSubmitEndTime(endTime);
        }
        
		Page<ContentSp> pages = contentSpService.find(c);
		List<ContentSp> list=pages.getItems();
		if(list!=null && list.size()>0){
			String desKey=sysConfigService.getSysParamValue("system", "ppddescode", "value");
			String coreStr=null;
			for(int i=0;i<list.size();i++){
				ContentSp cp=list.get(i);
				String decryptStr=cp.getCompanyId()+"|"+DateTimeUtil.getTimeStamp()+"|"+request.getRemoteAddr()+"|0|1";
				try {
					coreStr=DESCoder.encrypt(decryptStr,desKey);
					coreStr=URLEncoder.encode(coreStr,"UTF-8");
					cp.setCode(coreStr);
				} catch (Exception e) {
					cp.setCode("");
				}
				cp.setHave(spPublishService.getSpPublishCountByCompanyId(cp.getCompanyId()));
			}
		}
		
		Map<String,Object> resutlMap=null;
		resutlMap =new HashMap<String,Object>();
		resutlMap.put("Rows", pages.getItems());
		resutlMap.put("Total", pages.getTotal());
		return resutlMap;
	}

	@RequestMapping(value="/batchsort.do",method=RequestMethod.POST)
	@ResponseBody
	public Object batchSort(HttpServletRequest request, Model model)
	{
		String sortStr = ServletRequestUtils.getStringParameter(request,"sortStr","");
		String[] sorts = sortStr.split(",");
		for(String sort:sorts)
		{
			String[] pair = sort.split("-");
			contentSpService.updatePriority(Integer.valueOf(pair[0]), Integer.valueOf(pair[1]));
		}
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value="/doAudit.do",method=RequestMethod.POST)
	@ResponseBody
	public Object doAudit(HttpServletRequest request, Model model)
	{
		String stu = ServletRequestUtils.getStringParameter(request,"stu","");
		String msg = ServletRequestUtils.getStringParameter(request,"msg","");
		
		int id=ServletRequestUtils.getIntParameter(request, "id", -1);
		ContentSp contentSp=contentSpService.get(id);
		 //通过审核'
		if("99".equals(stu)){
            //处理发布表
            //取出基本信息
			if(contentSp!=null){
				contentSp.setStatus(99);
				contentSp.setPubTime(DateTimeUtil.getTimeStamp());
				contentSpService.merge(contentSp);
			}
        }else if("2".equals(stu)){
        	//不通过审核
        	if(contentSp!=null){
				contentSp.setStatus(2);
				contentSp.setReason(msg);
				contentSpService.merge(contentSp);
			}
        }
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value = "/audit.do")
	public String auditPage(HttpServletRequest request, Model model) {
		String id=ServletRequestUtils.getStringParameter(request, "id", "");
		String status=ServletRequestUtils.getStringParameter(request, "status", "");
		model.addAttribute("frontendDomain",SystemProperties.getInstance().getProperty("contentMgt.frontendDomain"));
		Role role=(Role)request.getSession().getAttribute(Constants.CURRENT_USER_ROLE);
		model.addAttribute("roleId", role!=null?role.getRoleid():"");
		model.addAttribute("id", id);
		model.addAttribute("status", status);
		return "contentsp/audit";
	}

}
