package com.richinfo.contentcat.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemContext;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.contentcat.entity.ContentData;
import com.richinfo.contentcat.service.ContentCatService;
import com.richinfo.contentcat.service.ContentDataService;
import com.richinfo.module.entity.Workflow;
import com.richinfo.module.entity.WorkflowUser;
import com.richinfo.module.service.WorkflowService;
import com.richinfo.module.service.WorkflowUserService;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.CategoryService;

@Controller
@RequestMapping(value = "/Content")
public class ContentDataController {
	
	private CategoryService categoryService;
	private ContentDataService contentDataService;
	private WorkflowService workflowService;
	private WorkflowUserService workflowUserService;
	private ContentCatService contentCatService;

	@Autowired
	@Qualifier("ContentCatService")
	public void setContentCatService(ContentCatService contentCatService) {
		this.contentCatService = contentCatService;
	}

	@Autowired
	@Qualifier("CategoryService")
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@Autowired
	@Qualifier("ContentDataService")
	public void setCategoryService(ContentDataService contentDataService) {
		this.contentDataService = contentDataService;
	}
	
	@Autowired
	@Qualifier("WorkflowUserService")
	public void setWorkflowUserService(WorkflowUserService workflowUserService) {
		this.workflowUserService = workflowUserService;
	}

	@Autowired
	@Qualifier("WorkflowService")
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	/**
	 * 
	 * 跳转至内容列表
	 * 
	 * @param request 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	public String listContent(HttpServletRequest request, Model model) {
		//获取栏目树
		List<Map<String,Object>> root = contentCatService.getRoot();
		if(root!=null && root.size()>0){
			for(int i=0;i<root.size();i++){
				Map<String,Object> itemMap=root.get(i);
				itemMap.put("CATNAME",StringUtils.isEmpty(ObjectUtils.toString(itemMap.get("CATNAME")))?"(暂缺)":itemMap.get("CATNAME"));
				List<Map<String,Object>>children=new ArrayList<Map<String,Object>>();
				itemMap.put("children",children);
			}
		}
		JSONArray json = JSONArray.fromObject(root);
		model.addAttribute("treeData", json.toString());
		return "content/list";
	}
	
	@RequestMapping(value = "/getChildren.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getChildren(HttpServletRequest request, Model model) {
		int pid=ServletRequestUtils.getIntParameter(request, "pid", -1);
		List<Map<String,Object>> list=null;
		Role role=(Role)request.getSession().getAttribute(Constants.CURRENT_USER_ROLE);
		int roleId=(role!=null?role.getRoleid():-1);
		if(roleId==1){
			list=contentCatService.getChildren(pid);
		}else{
			list=contentCatService.getChildrenByRole(pid,roleId);
		}
		
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> itemMap=list.get(i);
				itemMap.put("CATNAME",StringUtils.isEmpty(ObjectUtils.toString(itemMap.get("CATNAME")))?"(暂缺)":itemMap.get("CATNAME"));
				int parentId=StringUtils.isEmpty(ObjectUtils.toString(itemMap.get("CATID")))?-1:Integer.valueOf(ObjectUtils.toString(itemMap.get("CATID")));
				List<Map<String,Object>>children=contentCatService.getChildren(parentId);
				if(children!=null&&children.size()>0){
					itemMap.put("children",new ArrayList<Map<String,Object>>());
				}else{
					itemMap.put("children",null);
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	 * 跳转至内容列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listtpl.do", method = RequestMethod.GET)
	public String listTpl(HttpServletRequest request, Model model) {
		String catIdStr=ServletRequestUtils.getStringParameter(request, "catId", "");
		String csrfToken=ServletRequestUtils.getStringParameter(request, "csrfToken", "");
		String reqCatName=ServletRequestUtils.getStringParameter(request, "catName", "");
		
		//appScan
		if(!StringUtils.isNumeric(catIdStr)
				||CommonUtil.hasInValidCharacters(csrfToken,reqCatName)){
			return null;
		}
		
		int catId = ServletRequestUtils.getIntParameter(request, "catId", -1);
		Category category=categoryService.get(catId);
		String catName = category!=null?category.getName():"";
		model.addAttribute("catId", catId);
		model.addAttribute("catName",catName);
		
		Category cat=categoryService.get(catId);
		Integer workflowId=-1;
		if(cat!=null && cat.getWorkflowId()!=null){
			workflowId=cat.getWorkflowId();
		}
		workflowId=workflowId==null?-1:workflowId;
		Workflow workflow=workflowService.get(workflowId);
		if(workflow!=null){
			int flowLevel=workflow.getFlowlevel();
			model.addAttribute("workflowlv",flowLevel);
			
			List<WorkflowUser> workflowUserList=workflowUserService.queryWfuserByFlowId(workflowId);
			if(workflowUserList!=null && workflowUserList.size()>0){
				User user=(User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
				String userId=String.valueOf((user!=null?user.getUserId():0));
				StringBuffer sf=new StringBuffer();
				for(int i=0;i<workflowUserList.size();i++){
					WorkflowUser workflowUser=workflowUserList.get(i);
					String userIds=workflowUser.getUserid();
					if(!StringUtils.isEmpty(userIds)&&userIds.indexOf(userId)!=-1){
						sf.append(workflowUser.getFlevel()).append(",");
					}
				}
				model.addAttribute("userwflv",sf.toString());
			}
		}
		
		model.addAttribute("countList",contentDataService.getCountByCatId(catId));
		Role role=(Role)request.getSession().getAttribute(Constants.CURRENT_USER_ROLE);
		int roleId=(role!=null?role.getRoleid():0);
		model.addAttribute("roleId",roleId);
		model.addAttribute("frontendDomain",SystemProperties.getInstance().getProperty("contentMgt.frontendDomain"));
		
		return "content/listtpl";
	}
	
	/**
	 * 
	 * 内容列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findContentList.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findContentList(HttpServletRequest request, Model model) {
		//appScan
		String pageStr=ServletRequestUtils.getStringParameter(request, "page", "");
		String pagesizeStr=ServletRequestUtils.getStringParameter(request, "pagesize", "");
		String csrfToken=ServletRequestUtils.getStringParameter(request, "csrfToken", "");
		String sTime=ServletRequestUtils.getStringParameter(request, "sTime", "");//开始时间
		String eTime=ServletRequestUtils.getStringParameter(request, "eTime", "");//结束时间
		String txtHead=ServletRequestUtils.getStringParameter(request, "stxtHead", "");//名称
		String timeType=ServletRequestUtils.getStringParameter(request, "ttype", "");//时间类型
		String catIdStr=ServletRequestUtils.getStringParameter(request, "catId", "");//
		if(CommonUtil.hasInValidCharacters(pageStr,pagesizeStr,csrfToken,sTime,eTime,txtHead,timeType,catIdStr)){
			return null;
		}
		
		//分页
		int page= ServletRequestUtils.getIntParameter(request, "page", -1);
		int pagesize=ServletRequestUtils.getIntParameter(request,"pagesize",10);
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		//页面传入参数
		int catId = ServletRequestUtils.getIntParameter(request, "catId", -1);//栏目Id
		
		
		String format="yyyy-MM-dd HH:mm:ss";
		if(!DateTimeUtil.isValidDate(sTime, format)){
			return null;
		}
		if(!DateTimeUtil.isValidDate(eTime, format)){
			return null;
		}
		eTime=(eTime.length()<=10 && !StringUtils.isEmpty(eTime)) ? (eTime+" 23:59:59") : eTime;
		
		Long startTime=StringUtils.isEmpty(sTime) ? null : DateTimeUtil.converToTimestamp(sTime,format);
		Long endTime=StringUtils.isEmpty(sTime) ? null : DateTimeUtil.converToTimestamp(eTime,format);
		
		String statusTemp=ServletRequestUtils.getStringParameter(request, "status", "");
		if(!StringUtils.isEmpty(statusTemp)&&!StringUtils.isNumeric(statusTemp)){
			return null;
		}
		int status= ServletRequestUtils.getIntParameter(request, "status",0);//栏目Id

		ContentData c=new ContentData();
		c.setCatId(catId);
		c.setStartTime(startTime);
		c.setEndTime(endTime);
		c.setTxtHead(txtHead);
		c.setTimeType(timeType);
		c.setStatus(status);
		Page<ContentData> pages = contentDataService.find(c);
		
		Map<String,Object> resutlMap=null;
		resutlMap =new HashMap<String,Object>();
		
		resutlMap.put("Rows", pages.getItems());
		resutlMap.put("Total", pages.getTotal());
		return resutlMap;
	}
	
	
	/**
	 * 
	 * 跳转至添加/修改页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add.do",method=RequestMethod.GET)
	public String add(HttpServletRequest request, Model model) 
	{
		return "content/add";
	}
	
	/**
	 * 
	 * 跳转至添加/修改页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editpublishedcontent.do",method=RequestMethod.GET)
	public String editPublishedContent(HttpServletRequest request, Model model) 
	{
		Long id = ServletRequestUtils.getLongParameter(request, "contentDataId", -1);
		ContentData contentData =null;
		contentData = contentDataService.get(id);
		model.addAttribute("info", contentData);
		
		int catId = ServletRequestUtils.getIntParameter(request, "catId", -1);
		Category category=categoryService.get(catId);
		String catName = category!=null?category.getName():"";
		model.addAttribute("catId", catId);
		model.addAttribute("catName",catName);
		model.addAttribute("contextPath",SystemProperties.getInstance().getProperty("fileupload.contextPath"));
		
		return "content/editpublishedcontent";
	}
	
	/**
	 * 
	 * 添加/修改栏目
	 * @param request
	 * @param model
	 * @param newcategory
	 * @return
	 */
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model,@ModelAttribute Category newcategory) 
	{
		model.addAttribute("url", "/Content/listtpl.do");
		return Constants.FORWARDURL;
	}
	
	/**
	 * 
	 * 修改栏目内容
	 * @param request
	 * @param model
	 * @param newcategory
	 * @return
	 */
	@RequestMapping(value = "/updatePublishedContent.do")
	public String updatePublishedContent(HttpServletRequest request, Model model) 
	{
		Long contentDataId = ServletRequestUtils.getLongParameter(request, "contentDataId", -1);
		String contentDataIdStr = ServletRequestUtils.getStringParameter(request, "contentDataId", "");
		String txtHead=ServletRequestUtils.getStringParameter(request, "txtHead", "");
		String image=ServletRequestUtils.getStringParameter(request, "image", "");
		String logo=ServletRequestUtils.getStringParameter(request, "logo", "");
		String editorLanguage=ServletRequestUtils.getStringParameter(request, "editorLanguage", "");
		String stime = ServletRequestUtils.getStringParameter(request, "stime", "");
		String etime = ServletRequestUtils.getStringParameter(request, "etime", "");
		//appScan
		if(CommonUtil.hasInValidCharacters(contentDataIdStr,txtHead,image,logo,editorLanguage,stime,etime)){
			return null;
		}
		
		ContentData c =contentDataService.get(contentDataId);
		if(c!=null&&contentDataId>0){
			c.setTxtHead(txtHead);
			c.setPreImages(image);
			c.setPreLogo(logo);
			c.setEditorLanguage(editorLanguage);
			if(!"".equals(stime)){
				try {
					c.setStime(DateTimeUtil.getTimeStamp(DateTimeUtil.getDateFromStr(stime,"yyyy-MM-dd"))+"");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!"".equals(etime)){
				try {
					c.setEtime(DateTimeUtil.getTimeStamp(DateTimeUtil.getDateFromStr(etime,"yyyy-MM-dd"))+"");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			c.setUpdateTime(DateTimeUtil.getTimeStamp(new Date()));
			contentDataService.saveOrUpdate(c);
			model.addAttribute("message", "修改数据成功！");
		}else{
			model.addAttribute("message", "修改数据失败！");
		}
		
		int catId = ServletRequestUtils.getIntParameter(request, "catId", -1);
		Category category=categoryService.get(catId);
		String catName = category!=null?category.getName():"";
		model.addAttribute("catId", catId);
		model.addAttribute("catName",catName);

		model.addAttribute("url", "/Content/listtpl.do?catId="+catId);
		return Constants.FORWARDURL;
	}
	
	/**
	 * 
	 * 批量导入页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/multImport.do")
	public String multImport(HttpServletRequest request, Model model) 
	{
		int catId = ServletRequestUtils.getIntParameter(request, "catId", -1);
		if(catId==-1)
			return null;
		Category category=categoryService.get(catId);
		String catName = category!=null?category.getName():"";
		model.addAttribute("catId", catId);
		try
        {
			catName = java.net.URLDecoder.decode(catName,"UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
        	catName="";
        }
		model.addAttribute("catName",catName);

		return "content/multimport";
	}
	
	/**
	 * 
	 * 修改栏目内容
	 * @param request
	 * @param model
	 * @param newcontentdata
	 * @return
	 */
	@RequestMapping(value = "/multImportApp.do")
	@ResponseBody
	public Object multImportApp(HttpServletRequest request, Model model,@ModelAttribute ContentData newcontentdata) 
	{
		String ids=ServletRequestUtils.getStringParameter(request, "ids", "");
		String datatype=ServletRequestUtils.getStringParameter(request, "datatype", "");
		String catId=ServletRequestUtils.getStringParameter(request, "catId", "");
		//appScan
		if(CommonUtil.hasInValidCharacters(ids,datatype,catId)){
			return null;
		}
		
		User user=(User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("ids", ids);
		paramMap.put("datatype", datatype);
		paramMap.put("catId", catId);
		paramMap.put("nickname", (user!=null?user.getNickName():""));
		
		String target = contentDataService.getDataTypeByCat(Integer.valueOf(catId));
		String status= "";
		if(target.equals(datatype)||"other".equals(target)){
		   status=contentDataService.multImportApplication(paramMap);
		}else{
		   status = "-2";
		}Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", status);
		

		return jsonMap;
	}
	
	
	/**
	 * 
	 * 删除栏目
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Object del(HttpServletRequest request, Model model) 
	{
		String contentDataId = ServletRequestUtils.getStringParameter(request, "contentDataId","");
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(!StringUtils.isEmpty(contentDataId))
		{
			contentDataService.deleteContentData(Long.valueOf(contentDataId));
			jsonMap.put("status", "0");
		}
		
		return jsonMap;
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public Object delete(HttpServletRequest request, Model model)
	{
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			contentDataService.deleteContentData(Long.valueOf(id));
		}
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		return json;
	}
	
	@RequestMapping(value = "/batchSort.do")
	@ResponseBody
	public Object batchSort(HttpServletRequest request, Model model)
	{
		String sortStr = ServletRequestUtils.getStringParameter(request, "sortStr","");
		String[] sorts = sortStr.split(",");
		for(String sortPair :sorts)
		{
			String[] pair = sortPair.split("-");
			contentDataService.updatePriority(Long.valueOf(pair[0]), Integer.valueOf(pair[1]));
		}
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		return json;
	}
	
	@RequestMapping(value = "/batchAudit.do")
	@ResponseBody
	public Object batchAudit(HttpServletRequest request, Model model)
	{
		Map<String,String> json = new HashMap<String, String>();
		String auditStr = ServletRequestUtils.getStringParameter(request, "auditStr","");
		int workflowlv=ServletRequestUtils.getIntParameter(request, "workflowlv",-1);
		int status=ServletRequestUtils.getIntParameter(request, "status",-1);
		if(workflowlv>0){
            if(status==-1 || status>workflowlv){
            	json.put("status", "-1");
            	return json;
            }
			status=(status==workflowlv)?99:(status+1);
			
			User user=(User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
			String author=(user!=null?user.getNickName():"");
			Long currentTime=DateTimeUtil.getTimeStamp();
			
			String[] audits = auditStr.split(",");
			for(String id :audits)
			{
				contentDataService.updateStatus(Long.valueOf(id),status,author,currentTime);
			}
			json.put("status", "0");
		}else{
			json.put("status", "-1");
		}
		return json;
	}

}
