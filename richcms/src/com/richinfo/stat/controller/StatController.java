package com.richinfo.stat.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemContext;
import com.richinfo.common.utils.BrowserUtils;
import com.richinfo.privilege.entity.User;
import com.richinfo.stat.service.CategoryAccessService;
import com.richinfo.stat.service.ChannelAccessService;
import com.richinfo.stat.service.TotalAccessService;

@Controller
@RequestMapping(value = "/Stataccess")
public class StatController {
	
	private CategoryAccessService categoryAccessService;
	private ChannelAccessService channelAccessService;
	private TotalAccessService totalAccessService;
	@Autowired
	@Qualifier("categoryAccessService")
	public void setCategoryAccessService(CategoryAccessService categoryAccessService) {
		this.categoryAccessService = categoryAccessService;
	}
	@Autowired
	@Qualifier("channelAccessService")
	public void setChannelAccessService(ChannelAccessService channelAccessService) {
		this.channelAccessService = channelAccessService;
	}
	@Autowired
	@Qualifier("totalAccessService")
	public void setTotalAccessService(TotalAccessService totalAccessService) {
		this.totalAccessService = totalAccessService;
	}
	
	@RequestMapping(value = "/importPage.do", method = RequestMethod.GET)
	public String importPage(HttpServletRequest request, Model model) {
		return "stataccess/importexcel";
	}
	
	@RequestMapping(value = "/importExcel.do")
	public void importExcel(HttpServletRequest request,HttpServletResponse response, Model model){
		Map<String,Object> jsonMap=new HashMap<String,Object>();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			for(String key:fileMap.keySet()){
				 MultipartFile file = fileMap.get(key);
				 String fileName = file.getOriginalFilename();
				 if(fileName.indexOf("channel")!=-1){
					 jsonMap=channelAccessService.importExcel(file.getInputStream());
				 }else if(fileName.indexOf("catstatreport_daily")!=-1){
					 jsonMap=categoryAccessService.importExcel(file.getInputStream());
				 }else if(fileName.indexOf("3wdaily")!=-1){
					 jsonMap=totalAccessService.importExcel(file.getInputStream());
				 }else{
					 jsonMap.put("status", "1");
					 jsonMap.put("msg", "文件名称没有按模板名称定义,请下载模板!!");
				 }
			}
		} catch (IOException e) {
			e.printStackTrace();
			jsonMap.put("status", "1");
			jsonMap.put("msg", e.getMessage());
		}
		JSONObject json = JSONObject.fromObject(jsonMap);
		try {
			Writer out = response.getWriter();
			out.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	* @desc: 圈子下载访问数据 
	* @param request
	* @param model
	* @return 
	* @author wuzy
	* @date 2015-11-12
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model){
		int page= ServletRequestUtils.getIntParameter(request, "pageno", 1);
		int pagesize=ServletRequestUtils.getIntParameter(request,"pagesize",10);
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		String ctone=ServletRequestUtils.getStringParameter(request,"ctone","downldNum");
		String cttwo=ServletRequestUtils.getStringParameter(request,"cttwo","daysum");
		
		String beginTime = ServletRequestUtils.getStringParameter(request,"beginTime","");
		String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
		HttpSession session = SystemContext.getSessionContext();
		User user = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("user", user);
		paramMap.put("ctone", ctone);
		paramMap.put("cttwo", cttwo);
		paramMap.put("page", page);
		paramMap.put("webinf", request.getServletContext().getRealPath("WEB-INF"));
		
		Map resultMap=categoryAccessService.getCategoryAccessData(paramMap);
		model.addAttribute("count",(Integer.valueOf(ObjectUtils.toString(resultMap.get("count")))+pagesize-1)/pagesize);
	    model.addAttribute("currentpage",page);
	    model.addAttribute("dataimg",resultMap.get("dataimg"));
	    model.addAttribute("list",resultMap.get("list"));
	    model.addAttribute("beginTime",beginTime);
	    model.addAttribute("endTime",endTime);
	    model.addAttribute("ctone",ctone);
	    model.addAttribute("cttwo",cttwo);
		return "stataccess/datastatic";
	}
	
	/**
	* @desc:下载统计
	* @param request
	* @param model
	* @return 
	* @author wuzy
	* @date 2015-11-12
	 */
	@RequestMapping(value = "/datatLists.do")
	public String datatLists(HttpServletRequest request, Model model){
		int page= ServletRequestUtils.getIntParameter(request, "pageno", 1);
		int pagesize=ServletRequestUtils.getIntParameter(request,"pagesize",10);
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		String ctone=ServletRequestUtils.getStringParameter(request,"ctone","taccessNum");
		
		String beginTime = ServletRequestUtils.getStringParameter(request,"beginTime","");
		String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
		HttpSession session = SystemContext.getSessionContext();
		User user = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("user", user);
		paramMap.put("ctone", ctone);
		paramMap.put("page", page);
		paramMap.put("webinf", request.getServletContext().getRealPath("WEB-INF"));
		
		Map resultMap=totalAccessService.getTotalAccessData(paramMap);
		model.addAttribute("count",(Integer.valueOf(ObjectUtils.toString(resultMap.get("count")))+pagesize-1)/pagesize);
	    model.addAttribute("currentpage",page);
	    model.addAttribute("dataimg",resultMap.get("dataimg"));
	    model.addAttribute("list",resultMap.get("list"));
	    model.addAttribute("beginTime",beginTime);
	    model.addAttribute("endTime",endTime);
	    model.addAttribute("ctone",ctone);
		return "stataccess/downldstatic";
	}
	/**
	 * @desc:下载统计
	 * @param request
	 * @param model
	 * @return 
	 * @author wuzy
	 * @date 2015-11-12
	 */
	@RequestMapping(value = "/channelLists.do")
	public String channelLists(HttpServletRequest request, Model model){
		int page= ServletRequestUtils.getIntParameter(request, "pageno", 1);
		int pagesize=ServletRequestUtils.getIntParameter(request,"pagesize",10);
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		String beginTime = ServletRequestUtils.getStringParameter(request,"beginTime","");
		String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
		HttpSession session = SystemContext.getSessionContext();
		User user = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("user", user);
		paramMap.put("page", page);
		paramMap.put("webinf", request.getServletContext().getRealPath("WEB-INF"));
		
		Map resultMap=channelAccessService.getChannelAccessData(paramMap);
		model.addAttribute("count",(Integer.valueOf(ObjectUtils.toString(resultMap.get("count")))+pagesize-1)/pagesize);
		model.addAttribute("currentpage",page);
		model.addAttribute("dataimg",resultMap.get("dataimg"));
		model.addAttribute("list",resultMap.get("list"));
		model.addAttribute("beginTime",beginTime);
		model.addAttribute("endTime",endTime);
		return "stataccess/channelstatic";
	}
	
	@RequestMapping(value = "/exportExcel.do")
	@ResponseBody
	public void exportExcel(HttpServletRequest request,HttpServletResponse response, Model model){
		int page= ServletRequestUtils.getIntParameter(request, "pageno", 1);
		int pagesize=10000;//导出最大值为10000
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		
		String ctone=ServletRequestUtils.getStringParameter(request,"co","downldNum");
		String cttwo=ServletRequestUtils.getStringParameter(request,"ct","daysum");
		
		String beginTime = ServletRequestUtils.getStringParameter(request,"bt","");
		String endTime = ServletRequestUtils.getStringParameter(request,"et","");
		HttpSession session = SystemContext.getSessionContext();
		User user = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("user", user);
		paramMap.put("ctone", ctone);
		paramMap.put("cttwo", cttwo);
		
		
		Workbook wb=null;
		OutputStream os=null;
		try {
			wb=categoryAccessService.getWorkbook(paramMap);
			String codedFileName = ObjectUtils.toString(paramMap.get("codedFileName"));
			
			response.setContentType("application/vnd.ms-excel");
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition","attachment;filename="+ java.net.URLEncoder.encode(codedFileName,"UTF-8") + ".xlsx");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),"ISO8859-1");
				response.setHeader("content-disposition","attachment;filename=" + newtitle + ".xlsx");
			}
			os=response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(wb!=null){
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@RequestMapping(value = "/exportTotExcel.do")
	@ResponseBody
	public void exportTotExcel(HttpServletRequest request,HttpServletResponse response, Model model){
		int page= ServletRequestUtils.getIntParameter(request, "pageno", 1);
		int pagesize=10000;//导出最大值为10000
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		String beginTime = ServletRequestUtils.getStringParameter(request,"bt","");
		String endTime = ServletRequestUtils.getStringParameter(request,"et","");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		
		Workbook wb=null;
		OutputStream os=null;
		try {
			wb=this.totalAccessService.getWorkbook(paramMap);
			String codedFileName = "下载数据统计";
			
			response.setContentType("application/vnd.ms-excel");
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition","attachment;filename="+ java.net.URLEncoder.encode(codedFileName,"UTF-8") + ".xlsx");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),"ISO8859-1");
				response.setHeader("content-disposition","attachment;filename=" + newtitle + ".xlsx");
			}
			os=response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(wb!=null){
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@RequestMapping(value = "/exportChanelExcel.do")
	@ResponseBody
	public void exportChanelExcel(HttpServletRequest request,HttpServletResponse response, Model model){
		int page= ServletRequestUtils.getIntParameter(request, "pageno", 1);
		int pagesize=10000;//导出最大值为10000
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		String ctone=ServletRequestUtils.getStringParameter(request,"ctone","downldNum");
		String cttwo=ServletRequestUtils.getStringParameter(request,"ct","daysum");
		
		String beginTime = ServletRequestUtils.getStringParameter(request,"bt","");
		String endTime = ServletRequestUtils.getStringParameter(request,"et","");
		HttpSession session = SystemContext.getSessionContext();
		User user = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("user", user);
		paramMap.put("ctone", ctone);
		paramMap.put("cttwo", cttwo);
		
		
		Workbook wb=null;
		OutputStream os=null;
		try {
			wb=this.channelAccessService.getWorkbook(paramMap);
			String codedFileName = "渠道访问数据统计";
			
			response.setContentType("application/vnd.ms-excel");
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition","attachment;filename="+ java.net.URLEncoder.encode(codedFileName,"UTF-8") + ".xlsx");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),"ISO8859-1");
				response.setHeader("content-disposition","attachment;filename=" + newtitle + ".xlsx");
			}
			os=response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(wb!=null){
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@RequestMapping(value = "/downloadModel.do")
	@ResponseBody
	public void downloadModel(HttpServletRequest request,HttpServletResponse response, Model model){
		String ctone=ServletRequestUtils.getStringParameter(request,"ct","");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("ct", ctone);
		
		Workbook wb=null;
		OutputStream os=null;
		try {
			wb=this.channelAccessService.getDownloadTemplate(paramMap);
			String codedFileName = ObjectUtils.toString(paramMap.get("codedFileName"));
			
			response.setContentType("application/vnd.ms-excel");
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition","attachment;filename="+ java.net.URLEncoder.encode(codedFileName,"UTF-8") + ".xlsx");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),"ISO8859-1");
				response.setHeader("content-disposition","attachment;filename=" + newtitle + ".xlsx");
			}
			os=response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(wb!=null){
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
