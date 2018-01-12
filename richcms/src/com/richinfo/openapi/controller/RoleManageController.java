package com.richinfo.openapi.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.openapi.entity.CatPrivilegeOfRole;
import com.richinfo.openapi.entity.FunManage;
import com.richinfo.openapi.entity.AppRole;
import com.richinfo.openapi.entity.SysPrivilegeOfRole;
import com.richinfo.openapi.service.ApplicationService;
import com.richinfo.openapi.service.CatPrivilegeOfRoleService;
import com.richinfo.openapi.service.FunManageService;
import com.richinfo.openapi.service.RoleManageService;
import com.richinfo.openapi.service.SysPrivilegeOfRoleService;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.service.CategoryService;

@Controller
@RequestMapping("/rolemanage")
public class RoleManageController {
	
	@Autowired
	@Qualifier("RoleManageService")
	private RoleManageService roleManageService;
	
	@Autowired
	@Qualifier("SysPrivilegeOfRoleService")
	private SysPrivilegeOfRoleService sysPrivilegeOfRoleService;
	
	@Autowired
	@Qualifier("FunManageService")
	private FunManageService funManageService;
	
	@Autowired
	@Qualifier("CatPrivilegeOfRoleService")
	private CatPrivilegeOfRoleService catPrivilegeOfRoleService;
	
	@Autowired
	@Qualifier("ApplicationService")
	private ApplicationService applicationService;
	
	@RequestMapping("/list.do")
	public String lists(HttpServletRequest request,Model model)
	{
		List<AppRole> roleList = roleManageService.listAll();
		if(roleList!=null && roleList.size()>0){
			for(AppRole appRole:roleList){
				CommonUtil.escapeHtmlForObject(appRole);
			}
		}
		model.addAttribute("roleManageList", JSONArray.fromObject(roleList));
		return "openapi/rolemanage/lists";
	}
	
	@Autowired
	@Qualifier("CategoryService")
	private CategoryService categoryService;
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,@ModelAttribute AppRole roleManage,Model model)
	{
		String method = request.getMethod();
		if("GET".equals(method))
		{
			CommonUtil.escapeHtmlForObject(roleManage);
			model.addAttribute("opertype", "add");
			return "openapi/rolemanage/add";
		}
		//appScan
		if(CommonUtil.hasInValidCharacter(roleManage)){
			return null;
		}
		
		CommonUtil.unEscapeHtmlForObject(roleManage);
		roleManageService.save(roleManage);
		model.addAttribute("opertype", "add");
		model.addAttribute("message", "添加成功！");
		model.addAttribute("url","/rolemanage/list.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping("/modify.do")
	public String modify(HttpServletRequest request,@ModelAttribute AppRole roleManage,Model model)
	{
		String method = request.getMethod();
		if("GET".equals(method))
		{
			CommonUtil.escapeHtmlForObject(roleManage);
			model.addAttribute("opertype", "modify");
			AppRole appRole=roleManageService.get(roleManage.getRoleId());
			model.addAttribute("appRole",appRole);
			return "openapi/rolemanage/add";
		}
		String roleName=ServletRequestUtils.getStringParameter(request, "roleName", "");
		String roleMem=ServletRequestUtils.getStringParameter(request, "roleMem", "");
		
		roleManage.setRoleName(roleName);
		roleManage.setRoleMem(roleMem);
		CommonUtil.unEscapeHtmlForObject(roleManage);
		
		roleManageService.update(roleManage);
		model.addAttribute("message", "修改成功！");
		model.addAttribute("url","/rolemanage/list.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(HttpServletRequest request)
	{
		Map<String,String> jsonMap = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		String[] ids = idstr.split(",");
		StringBuffer sf=new StringBuffer();
		for(String id:ids)
		{
			boolean isFlag=applicationService.isRoleInApp(Integer.valueOf(id));
			if(isFlag)
			{
				sf.append(id).append(",");
			}
		}
		if("".equals(sf.toString())){
			for(String id:ids)
			{
				roleManageService.delete(Integer.valueOf(id));
				sysPrivilegeOfRoleService.deleteByRoleId(Integer.valueOf(id));
			}
			jsonMap.put("status", "0");
		}else{
			jsonMap.put("status", "-1");
			jsonMap.put("idstr", (sf.toString()).substring(0, (sf.toString()).lastIndexOf(",")));
		}
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping("/funmanage/list.do")
	public String funList(HttpServletRequest request,Model model)
	{
		Integer roleId = Integer.valueOf(request.getParameter("roleId")); 
		List<SysPrivilegeOfRole> sysPrivilegeOfRoleList = sysPrivilegeOfRoleService.listByRoleId(roleId);
		model.addAttribute("roleId", roleId);
		model.addAttribute("sysPrivilegeOfRoleList", JSONArray.fromObject(sysPrivilegeOfRoleList));
		return "openapi/rolemanage/funlists";
	}
	
	@RequestMapping("/funmanage/add.do")
	@ResponseBody
	public Object funAdd(HttpServletRequest request,Model model)
	{
		Map<String,String> jsonMap = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		Integer roleId = Integer.valueOf(request.getParameter("roleId"));
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			Integer funId = Integer.valueOf(id);
			FunManage funManage = funManageService.get(funId);
			SysPrivilegeOfRole sysPrivilegeOfRole = null;
			String hql = "from SysPrivilegeOfRole s where s.roleId=? and s.funId=?";
			sysPrivilegeOfRole = (SysPrivilegeOfRole)sysPrivilegeOfRoleService.queryObject(hql,  new Object[]{roleId,funId});
			if(sysPrivilegeOfRole == null)
			{
				sysPrivilegeOfRole = new SysPrivilegeOfRole();
			}
			sysPrivilegeOfRole.setFunId(funId);
			sysPrivilegeOfRole.setClassName(funManage.getClassName());
			sysPrivilegeOfRole.setMethod(funManage.getMethod());
			sysPrivilegeOfRole.setRoleId(roleId);
			sysPrivilegeOfRoleService.save(sysPrivilegeOfRole);
		}
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping("/funmanage/delete.do")
	@ResponseBody
	public Object funDelete(HttpServletRequest request,Model model)
	{
		Map<String,String> jsonMap = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			sysPrivilegeOfRoleService.delete(Integer.valueOf(id));
		}
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value = "/grantcat.do",method=RequestMethod.GET)
	public String listCats(HttpServletRequest request, Model model) 
	{
		int roleid = ServletRequestUtils.getIntParameter(request, "roleId",-1);
		AppRole roleManage = roleManageService.get(roleid);
		model.addAttribute("roleManage", roleManage);
		List<Integer> catidList = catPrivilegeOfRoleService.getCatPrivilegeByRoleid(roleid);
		JSONArray catIdjson = JSONArray.fromObject(catidList);
		model.addAttribute("catIdArray", catIdjson);
		Category cate = categoryService.getRootCategory();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"parent","childId","seoTitle","seoDesc","seoKeyword"});
		Object json = JSONObject.fromObject(cate,config);
		model.addAttribute("treeData",json.toString());
		return "openapi/rolemanage/grantcat";
	}
	
	@RequestMapping(value = "/grantcat.do",method=RequestMethod.POST,params="idstr")
	@ResponseBody
	public Map<String ,String> grantCat(HttpServletRequest request, Model model) 
	{
		//appScan
		String roleidStr = ServletRequestUtils.getStringParameter(request, "roleId","");
		if(!StringUtils.isNumeric(roleidStr)){
			return null;
		}
		
		int roleid = ServletRequestUtils.getIntParameter(request, "roleId",-1);
		catPrivilegeOfRoleService.deleteCatPrivilegeByRoleid(roleid);
		String  idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		String[] ids = idstr.split(",");
		for(String catid:ids)
		{
			if(!StringUtils.isEmpty(catid))
			{
				CatPrivilegeOfRole c = new CatPrivilegeOfRole();
				c.setRoleId(roleid);
				c.setCateId(Integer.valueOf(catid));
				catPrivilegeOfRoleService.save(c);
			}
		}
		
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
}
