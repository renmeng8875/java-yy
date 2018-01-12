package com.richinfo.contentcat.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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
import org.springframework.web.util.HtmlUtils;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemContext;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.contentcat.entity.SysDataCat;
import com.richinfo.contentcat.service.ContentCatService;
import com.richinfo.contentcat.service.ContentDataService;
import com.richinfo.contentcat.service.ContentTagService;
import com.richinfo.datasource.entity.FormInfo;
import com.richinfo.datasource.service.DataSourceConfigService;
import com.richinfo.module.entity.Style;
import com.richinfo.module.entity.Tpl;
import com.richinfo.module.entity.Workflow;
import com.richinfo.module.service.StyleService;
import com.richinfo.module.service.TplService;
import com.richinfo.module.service.WorkflowService;
import com.richinfo.privilege.entity.CatPrivilege;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.service.CatPrivilegeService;
import com.richinfo.privilege.service.CategoryService;

@Controller
@RequestMapping(value = "/ContentCat")

public class ContentCatController {

	private CategoryService categoryService;
	private CatPrivilegeService catPrivilegeService;
	private ContentCatService contentCatService;
	private WorkflowService workflowService;
	private StyleService styleService;
	private TplService tplService;
	private ContentTagService contentTagService;
	private DataSourceConfigService dataConfigService;
	private ContentDataService contentDataService;
	
	@Autowired
	@Qualifier("CategoryService")
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@Autowired
	@Qualifier("CatPrivilegeService")
	public void setCatPrivilegeService(CatPrivilegeService catPrivilegeService) {
		this.catPrivilegeService = catPrivilegeService;
	}
	
	@Autowired
	@Qualifier("ContentCatService")
	public void setContentCatService(ContentCatService contentCatService) {
		this.contentCatService = contentCatService;
	}

	@Autowired
	@Qualifier("WorkflowService")
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	
	@Autowired
	@Qualifier("StyleService")
	public void setStyleService(StyleService styleService) {
		this.styleService = styleService;
	}
	
	@Autowired
	@Qualifier("TplService")
	public void setTplService(TplService tplService) {
		this.tplService = tplService;
	}
	
	@Autowired
	@Qualifier("ContentTagService")
	public void setContentTagService(ContentTagService contentTagService) {
		this.contentTagService = contentTagService;
	}
	
	@Autowired
	@Qualifier("DataSourceConfigService")
	public void setDataSourceConfigService(DataSourceConfigService dataConfigService) {
		this.dataConfigService = dataConfigService;
	}
	
 
	
	@Autowired
	@Qualifier("ContentDataService")
	public ContentDataService getContentDataService() {
		return contentDataService;
	}

	public void setContentDataService(ContentDataService contentDataService) {
		this.contentDataService = contentDataService;
	}

	/**
	 * 
	 * 栏目列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	public String listCategory(HttpServletRequest request, Model model) {
		return "contentcat/list";
	}
	
	@RequestMapping(value = "/list.do", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getRoot(HttpServletRequest request, Model model) {
		List<Map<String, Object>> list = null;
		list = contentCatService.getRoot();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> itemMap = list.get(i);
				itemMap.put("CATID", ObjectUtils.toString(itemMap.get("CATID")));
				itemMap.put("CATNAME",StringUtils.isEmpty(ObjectUtils.toString(itemMap.get("CATNAME")))?"(暂缺)":itemMap.get("CATNAME"));
				int childrenCount = contentCatService.getChildrenCount(Integer.valueOf(ObjectUtils.toString(itemMap.get("CATID"))));
				if (childrenCount > 0) {
					itemMap.put("state", "closed");
					itemMap.put("children",new ArrayList<Map<String, Object>>());
				}
			}
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", list);
		resultMap.put("total", 1);
		return resultMap;
	}
	
	@RequestMapping(value = "/getChildren.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getChildren(HttpServletRequest request, Model model) {
		int pid = ServletRequestUtils.getIntParameter(request, "pid", -1);
		List<Map<String, Object>> list = null;
		Role role=(Role)request.getSession().getAttribute(Constants.CURRENT_USER_ROLE);
		int roleId=(role!=null?role.getRoleid():-1);
		if(roleId==1){
			list=contentCatService.getChildren(pid);
		}else{
			list=contentCatService.getChildrenByRole(pid,roleId);
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> itemMap = list.get(i);
				itemMap.put("CATID", ObjectUtils.toString(itemMap.get("CATID")));
				itemMap.put("CATNAME",StringUtils.isEmpty(ObjectUtils.toString(itemMap.get("CATNAME")))?"(暂缺)":itemMap.get("CATNAME"));
				int childrenCount = contentCatService.getChildrenCount(Integer.valueOf(ObjectUtils.toString(itemMap.get("CATID"))));
				if (childrenCount > 0) {
					itemMap.put("state", "closed");
					itemMap.put("children", new ArrayList<Map<String, Object>>());
				}else{
					itemMap.put("state", "open");
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	 * 栏目列表
	 * @param request
	 * @param mode
	 * @return
	 */
	@RequestMapping(value = "/sortcontentcat.do", method = RequestMethod.GET)
	public String listChildCategory(HttpServletRequest request, Model model) {
		int pid= ServletRequestUtils.getIntParameter(request, "pid",-1);
		List<Category> childCategoryList = contentCatService.getChildCategory(pid);
		//JSONArray catIdjson = JSONArray.fromObject(childCategoryList);
		model.addAttribute("childCategoryList", childCategoryList);
		return "contentcat/sortcontentcat";
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
		//栏目树
		JsonConfig config = new JsonConfig();
	 
		Category root = categoryService.getRootCategory();
		config.setExcludes(new String[]{"parent","childId","seoTitle","seoDesc","seoKeyword"});
		Object json = JSONObject.fromObject(root, config);
		System.out.println("----"+json.toString());
		model.addAttribute("treeData", json.toString());
		
		//单个栏目数据
		int pid = ServletRequestUtils.getIntParameter(request, "pid",-1);
		int id = ServletRequestUtils.getIntParameter(request, "categoryid", -1);
		Category category =null;
		if(id>=0){
			category = categoryService.get(id);
			model.addAttribute("imageJson", "".equals(category.getImage())?"":HtmlUtils.htmlUnescape(category.getImage()));
			CommonUtil.escapeHtmlForObject(category);
			model.addAttribute("info", category);
			model.addAttribute("pid", category.getParent()!=null?category.getParent().getCatId():"");
		}else{
			model.addAttribute("pid", pid);
		}
		
		//工作流
		List<Workflow> flowList = workflowService.listAll();
        model.addAttribute("flowList",flowList);
        
        //风格
        List<Style> styleList = styleService.listAll();
		model.addAttribute("styleList",styleList);
		
		//模板
		String style=(category!=null?category.getStyle():"");
		int styleId=-1;
		if(null!=style && !"".equals(style)){
			List<Style>  styleLists=styleService.listByParam("style", style, -1);
			if(styleLists!=null && styleLists.size()>0){
				styleId=styleLists.get(0).getStyleid();
			}
		}else{
			if(styleList!=null && styleList.size()>0){
				styleId=styleList.get(0).getStyleid();
			}
		}
		List<Tpl> tplList = tplService.listByStyleId(styleId);
		model.addAttribute("tplList",tplList);
		
		model.addAttribute("contextPath", SystemProperties.getInstance().getProperty("fileupload.contextPath"));
		return "contentcat/add";
	}
	
	/**
	 * 
	 * 获取风格列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/styleList.do")
	@ResponseBody
	public Object styleLists(HttpServletRequest request, Model model) {
        List<Style> styleList = styleService.listAll();
        Object json = JSONArray.fromObject(styleList);
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("styleList", json);
		
		return jsonMap;
	}
	
	/**
	 * 
	 * 获取模板列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tplList.do")
	@ResponseBody
	public Object tplLists(HttpServletRequest request, Model model) {
		int styleId = ServletRequestUtils.getIntParameter(request, "styleId",-1);
		List<Tpl> tplList = tplService.listByStyleId(styleId);
		Object json = JSONArray.fromObject(tplList);
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("tplList", json);
		
		return jsonMap;
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
		//获取参数
		String sTime = ServletRequestUtils.getStringParameter(request,"sTime","");
		String eTime = ServletRequestUtils.getStringParameter(request,"eTime","");
		String isHiddenRadio=ServletRequestUtils.getStringParameter(request,"isHiddenRadio","0");
		String workflowId=ServletRequestUtils.getStringParameter(request,"workflowId","0");
		
		//appScan 2015.8.27
		if(!DateTimeUtil.isValidDate(sTime, "yyyy-MM-dd")||!DateTimeUtil.isValidDate(eTime, "yyyy-MM-dd")){
			return null;
		}
		if(!StringUtils.isNumeric(isHiddenRadio)||!StringUtils.isNumeric(workflowId)){
			return null;
		}
		if(CommonUtil.hasInValidCharacter(newcategory)){
			return null;
		}
		
		int pId = ServletRequestUtils.getIntParameter(request, "pid",-1);
		int categoryId = ServletRequestUtils.getIntParameter(request, "catId", -1);
		Category parent=(pId==categoryId&&categoryId!=-1)?null:categoryService.get(pId);
		newcategory.setCatLevel((pId==categoryId&&categoryId!=-1)?0:contentCatService.queryCategoryLevelByCategoryId(pId)+1);
		newcategory.setParent(parent);
		newcategory.setStartTime(!StringUtils.isEmpty(sTime)?DateTimeUtil.converToTimestamp(sTime,"yyyy-MM-dd"):null);
		newcategory.setEndTime(!StringUtils.isEmpty(eTime)?DateTimeUtil.converToTimestamp(eTime,"yyyy-MM-dd"):null);
		
		if(newcategory!=null&&categoryId>=0){
			CommonUtil.unEscapeHtmlForObject(newcategory);
			newcategory.setCatId(categoryId);
			categoryService.merge(newcategory);
			model.addAttribute("message", "修改栏目成功！");
		}else{
			CommonUtil.unEscapeHtmlForObject(newcategory);
			categoryService.save(newcategory);
			
			//增加后将栏目权限授权给这个管理员
			CatPrivilege catPrivilege=new CatPrivilege();
			catPrivilege.setCatId(newcategory.getCatId());
			catPrivilege.setRoleId(((Role)request.getSession().getAttribute(Constants.CURRENT_USER_ROLE)).getRoleid());
			catPrivilegeService.save(catPrivilege);
			model.addAttribute("message", "添加栏目成功！");
		}

		model.addAttribute("url", "/ContentCat/list.do");
		return Constants.FORWARDURL;
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
		String catId = ServletRequestUtils.getStringParameter(request, "catId","");
		Category cate = categoryService.get(Integer.valueOf(catId));
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "1");
		if(cate.getChildren().size()>0){
			jsonMap.put("status", "-1");
		}else{
			if(!StringUtils.isEmpty(catId))
			{
				categoryService.delCategory(Integer.valueOf(catId));
				jsonMap.put("status", "0");
			}
		}
		return jsonMap;
	}
	
	/**
	 * 
	 * 批量隐藏/显示
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateHidden.do")
	@ResponseBody
	public Object updateHidden(HttpServletRequest request, Model model) 
	{
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		int isHidden=ServletRequestUtils.getIntParameter(request, "isHidden",-1);
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(!StringUtils.isEmpty(idstr) && isHidden!=-1)
		{
			String[] ids = idstr.split(",");
			for(String id:ids)
			{
				contentCatService.updateHidden(isHidden, Integer.valueOf(id));
			}
			jsonMap.put("status", "0");
		}else{
			jsonMap.put("status", "-1");
		}
		return jsonMap;
	}
	
	/**
	 * 
	 * 批量隐藏/显示
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updatePriority.do")
	@ResponseBody
	public Object updatePriority(HttpServletRequest request, Model model) 
	{
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(!StringUtils.isEmpty(idstr))
		{
			String[] ids = idstr.split(",");
			for(int i=0;i<ids.length;i++)
			{
				contentCatService.updatePriority(Integer.valueOf(ids[i]),i+1);
			}
			jsonMap.put("status", "0");
		}else{
			jsonMap.put("status", "-1");
		}
		return jsonMap;
	}
	
	/**
	 * 
	 * 内容标签列表
	 * @param request
	 * @param mode
	 * @return
	 */
	@RequestMapping(value = "/contenttaglist.do", method = RequestMethod.GET)
	public String contenttagList(HttpServletRequest request, Model model) {
		return "contentcat/contenttaglist";
	}
	
	/**
	 * 
	 * 内容标签列表
	 * @param request
	 * @param mode
	 * @return
	 */
	@RequestMapping(value = "/listContentTag.do", method = RequestMethod.POST)
	@ResponseBody
	public Object listContentTag(HttpServletRequest request, Model model) {
		int page= ServletRequestUtils.getIntParameter(request, "page", -1);
		int pagesize=ServletRequestUtils.getIntParameter(request,"pagesize",15);
		SystemContext.setPageSize(pagesize);
		SystemContext.setPageOffset((page-1)*pagesize);
		
		Page<SysDataCat> pages = contentTagService.listContentTag();
		Map<String,Object> resutlMap=null;
		resutlMap =new HashMap<String,Object>();
		
		List<SysDataCat> sysDataCatList=pages.getItems();
		if(sysDataCatList!=null && sysDataCatList.size()>0){
			StringBuffer sfPcatName =null;
			for(int i=0;i<sysDataCatList.size();i++){
				SysDataCat sysDataCat=sysDataCatList.get(i);
				String[] catIds=sysDataCat.getPcatId()!=null && !"".equals(sysDataCat.getPcatId())?sysDataCat.getPcatId().split(","):"".split(",");
				sfPcatName = new StringBuffer();
				for(int j=0;j<catIds.length;j++){
					if(StringUtils.isNotEmpty(catIds[j]))
					{
						sfPcatName.append(contentCatService.getCatNameByCatId(Integer.valueOf(catIds[j])));
						if(j!=catIds.length-1){
							sfPcatName.append(",");
						}
					}
				}
				sysDataCat.setPcatName(sfPcatName.toString());
			}
		}
		resutlMap.put("Rows", pages.getItems());
		resutlMap.put("Total", pages.getTotal());

		return resutlMap;
	}
	
	/**
	 * 
	 * 跳转至标签添加/修改页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addsysdatacat.do",method=RequestMethod.GET)
	public String addTag(HttpServletRequest request, Model model) 
	{
		//栏目树
		JsonConfig config = new JsonConfig();
	 
		Category root = categoryService.getRootCategory();
		config.setExcludes(new String[]{"parent","childId","seoTitle","seoDesc","seoKeyword"});
		Object json = JSONObject.fromObject(root, config);
		
		model.addAttribute("treeData", json.toString());
		//单个栏目数据
		int id = ServletRequestUtils.getIntParameter(request, "catId", -1);
		SysDataCat sysDataCat =null;
		if(id>=0){
			sysDataCat = contentTagService.get(id);
			Map<String,String> catNameMap=null;
			String[] catIds=sysDataCat.getPcatId()!=null && !"".equals(sysDataCat.getPcatId())?sysDataCat.getPcatId().split(","):null;
			catNameMap=new HashMap<String,String>();
			if(catIds!=null){
				for(int j=0;j<catIds.length;j++){
					catNameMap.put(catIds[j], contentCatService.getCatNameByCatId(Integer.valueOf(catIds[j])));
					model.addAttribute("catNameMap", catNameMap);
				}
			}

			model.addAttribute("info", sysDataCat);
		}
		
		//工作流
		List<FormInfo> dataTypeList = dataConfigService.getFormInfoListsByType("extends");
        model.addAttribute("dataTypeList",dataTypeList);
		return "contentcat/addsysdatacat";
	}
	
	@RequestMapping(value = "/checkCat.do")
	@ResponseBody
	public Object checkCat(HttpServletRequest request, Model model)
	{
		String userName = request.getParameter("param");
		String datatype = ServletRequestUtils.getStringParameter(request, "datatype", "");
		System.out.println(userName+"   "+datatype);
		Map<String,Object> json = new HashMap<String, Object>();
//		User user=this.userService.getUserByName(userName);
//		boolean flag = user!=null?true:false;
//		if(!flag){
//			json.put("status", "y");
//		}else{
//			json.put("status", "n");
//			json.put("info", "名称重复,请重新输入！");
//		}
//		
		return json;
	}
	
	/**
	 * 
	 * 添加/修改栏目
	 * @param request
	 * @param model
	 * @param newcategory
	 * @return
	 */
	@RequestMapping(value = "/editTag.do")
	public String editTag(HttpServletRequest request, Model model,@ModelAttribute SysDataCat newsysdatacat) 
	{
		//获取参数
		String contentTagIdTemp=ServletRequestUtils.getStringParameter(request, "catId", "0");
		//appScan
		if(!StringUtils.isNumeric(contentTagIdTemp)){
			return null;
		}
		if(!StringUtils.isEmpty(newsysdatacat.getJumpUrl())
				&&!CommonUtil.isValidUrl(newsysdatacat.getJumpUrl())){
			return null;
		}
		int contentTagId = ServletRequestUtils.getIntParameter(request, "catId", -1);
		
		if(newsysdatacat!=null&&contentTagId>0){
			newsysdatacat.setUpdateTime(DateTimeUtil.converToTimestamp((Calendar.getInstance()).getTime().toString(),"yyyy-MM-dd"));
			newsysdatacat.setCatId(contentTagId);
			contentTagService.merge(newsysdatacat);
			model.addAttribute("message", "修改内容标签成功！");
		}else{
			contentTagService.save(newsysdatacat);
			model.addAttribute("message", "添加内容标签成功！");
		}

		model.addAttribute("url", "/ContentCat/contenttaglist.do");
		return Constants.FORWARDURL;
	}
	
	/**
	 * 
	 * 删除栏目
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/delTag.do")
	@ResponseBody
	public Object delTag(HttpServletRequest request, Model model) 
	{
		String catId = ServletRequestUtils.getStringParameter(request, "catId","");
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(!StringUtils.isEmpty(catId))
		{
			contentTagService.delete(Integer.valueOf(catId));
			jsonMap.put("status", "0");
		}
		
		return jsonMap;
	}
	
	/**
	 * 
	 * 批量隐藏/显示
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchDelete.do")
	@ResponseBody
	public Object batchDelete(HttpServletRequest request, Model model) 
	{
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(!StringUtils.isEmpty(idstr))
		{
			String[] ids = idstr.split(",");
			for(String id:ids)
			{
				contentTagService.delete(Integer.valueOf(id));
			}
			jsonMap.put("status", "0");
		}else{
			jsonMap.put("status", "-1");
		}
		return jsonMap;
	}
	
	/**
	 * 
	 * 批量隐藏/显示
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateChildId.do")
	@ResponseBody
	public Object updateChildId(HttpServletRequest request, Model model) 
	{
		String status=contentCatService.updateChildId();
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", status);
		return jsonMap;
	}
	/**
	 * 
	 * 导出日志
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportContentCat.do")
	@ResponseBody
	public Object exportChannelContentCat(HttpServletRequest request, Model model) 
	{
		contentCatService.exportChannelContentCat();
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "success");
		return jsonMap;
	}

}
