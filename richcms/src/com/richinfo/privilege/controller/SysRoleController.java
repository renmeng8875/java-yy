package com.richinfo.privilege.controller;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.privilege.entity.CatPrivilege;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.entity.Privilege;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.CatPrivilegeService;
import com.richinfo.privilege.service.CategoryService;
import com.richinfo.privilege.service.MenuService;
import com.richinfo.privilege.service.PrivilegeService;
import com.richinfo.privilege.service.RoleService;
import com.richinfo.privilege.service.UserService;

@Controller
@RequestMapping(value = "/SysRole")
public class SysRoleController {

	private RoleService roleService;
	
	private UserService userService;
	
	private MenuService menuService;
	
	private CategoryService categoryService;
	
	private PrivilegeService privilegeService;
	
	private CatPrivilegeService catPrivilegeService;
	
	@Autowired
	@Qualifier("CatPrivilegeService")
	public void setCatPrivilegeService(CatPrivilegeService catPrivilegeService) {
		this.catPrivilegeService = catPrivilegeService;
	}

	@Autowired
	@Qualifier("PrivilegeService")
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

	@Autowired
	@Qualifier("CategoryService")
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Autowired
	@Qualifier("MenuService")
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@Autowired
	@Qualifier("RoleService")
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Autowired
	@Qualifier("UserService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<Role> roleList = roleService.listAll();
		model.addAttribute("roleList", roleList);
		return "sysrole/lists";
	}
 
	
	@RequestMapping(value = "/addrole.do")
	public String addRole(HttpServletRequest request, Model model) {
		List<Role> roleList = roleService.listAll();
		model.addAttribute("roleList", roleList);
		String method=request.getMethod();
		if ("GET".equals(method)) {
			return "sysrole/add";
		}
		String roleName=ServletRequestUtils.getStringParameter(request, "rolename", "");
		String mem=ServletRequestUtils.getStringParameter(request, "mem", "");
		//appScan
		if(CommonUtil.hasInValidCharacters(roleName,mem)){
			return null;
		}
		
		Role role=new Role();
		role.setCtime(DateTimeUtil.getTimeStamp());
		role.setRoleName(roleName);
		role.setMem(mem);
		roleService.save(role);
		model.addAttribute("message", "添加成功！");

		model.addAttribute("url", "/SysRole/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/grantsys.do",method=RequestMethod.GET)
	public String listMenus(HttpServletRequest request, Model model) {
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid",-1);
		Role role = roleService.get(roleid);
		model.addAttribute("role", role);
		List<Integer> menuIdlist = privilegeService.getPrivilegeByRoleid(roleid);
		JSONArray menuIdjson = JSONArray.fromObject(menuIdlist);
		model.addAttribute("menuidArray", menuIdjson.toString());
		model.addAttribute("roleid", roleid);
		Menu root = menuService.getMenuRoot();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"parent"}); 
		Object json = JSONObject.fromObject(root,config);
		model.addAttribute("treeData",json.toString());
		return "sysrole/grantsys";
	}
	
	
	
	
	@RequestMapping(value = "/grantsys.do",method=RequestMethod.POST,params="idstr")
	@ResponseBody
	public Map<String ,String> grantSys(HttpServletRequest request, Model model) {
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid",-1);
		privilegeService.deletePrivilegeByRoleid(roleid);
		String  idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		String[] ids = idstr.split(",");
		for(String menuid:ids)
		{
			if(!StringUtils.isEmpty(menuid))
			{
				Privilege p = new Privilege();
				p.setRoleId(roleid);
				p.setMenuId(Integer.valueOf(menuid));
				privilegeService.save(p);
			}
		}
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value = "/grantcat.do",method=RequestMethod.GET)
	public String listCats(HttpServletRequest request, Model model) 
	{
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid",-1);
		Role role = roleService.get(roleid);
		model.addAttribute("role", role);
		List<Integer> catidList = catPrivilegeService.getCatPrivilegeByRoleid(roleid);
		JSONArray catIdjson = JSONArray.fromObject(catidList);
		model.addAttribute("catIdArray", catIdjson);
		Category cate = categoryService.getRootCategory();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"parent","childId","seoTitle","seoDesc","seoKeyword"});
		Object json = JSONObject.fromObject(cate,config);
		model.addAttribute("treeData",json.toString());
		return "sysrole/grantcat";
	}
	
	@RequestMapping(value = "/grantcat.do",method=RequestMethod.POST,params="idstr")
	@ResponseBody
	public Map<String ,String> grantCat(HttpServletRequest request, Model model) 
	{
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid",-1);
		catPrivilegeService.deleteCatPrivilegeByRoleid(roleid);
		String  idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		String[] ids = idstr.split(",");
		for(String catid:ids)
		{
			if(!StringUtils.isEmpty(catid))
			{
				CatPrivilege c = new CatPrivilege();
				c.setRoleId(roleid);
				c.setCatId(Integer.valueOf(catid));
				catPrivilegeService.save(c);
			}
		}
		
		Map<String,String> jsonMap = new HashMap<String, String>();
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value = "/membermanage.do")
	public String memberManage(HttpServletRequest request, Model model) 
	{
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid",-1);
		List<User> userList = roleService.findUserByRoleid(roleid);
		model.addAttribute("userList", userList);
		return "sysrole/membermanage";
	}
	
	@RequestMapping(value = "/editrole.do")
	public String editRole(HttpServletRequest request, Model model) 
	{
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid",-1);
		String roleName=ServletRequestUtils.getStringParameter(request, "rolename", "");
		String mem=ServletRequestUtils.getStringParameter(request, "mem", "");
		String method=request.getMethod();
		Role role = roleService.get(roleid);
		if ("GET".equals(method)) {
			model.addAttribute("role", role);
			return "sysrole/edit";
		}
		if(role!=null){
			role.setCtime(DateTimeUtil.getTimeStamp());
			role.setRoleName(roleName);
			role.setMem(mem);
			roleService.update(role);
			model.addAttribute("message", "修改成功！");
		}else{
			model.addAttribute("message", "修改失败,当前角色不存在！");
		}
		model.addAttribute("url", "/SysRole/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/deleterole.do")
	@ResponseBody
	public Object deletetRole(HttpServletRequest request, Model model) 
	{
		//appScan
		String roleidStr=ServletRequestUtils.getStringParameter(request, "roleid", "");
		if(CommonUtil.hasInValidCharacters(roleidStr)){
			return null;
		}
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid",-1);
		Map<String,String> jsonMap = new HashMap<String, String>();
		//超级管理员不能删除,系统默认roleid=1为超级管理员
		if(roleid==1){
			jsonMap.put("status", "2");
			return jsonMap;
		}
		List<User> list = userService.findUserByRole(roleid);
		StringBuffer sb = new StringBuffer();
		if(list!=null&&list.size()>0){
			for(User user :list)
			{
				sb.append(user.getNickName()+" ");
			}
			jsonMap.put("status", "1");
			jsonMap.put("nickNames", sb.toString());
			return jsonMap;
		}
		roleService.delete(roleid);
		privilegeService.deletePrivilegeByRoleid(roleid);
		catPrivilegeService.deleteCatPrivilegeByRoleid(roleid);
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String roleName = request.getParameter("param");
		String roleidTemp=ServletRequestUtils.getStringParameter(request, "roleid","");
		if(!StringUtils.isEmpty(roleidTemp)&&!StringUtils.isNumeric(roleidTemp)){
			return null;
		}
		
		int roleid = ServletRequestUtils.getIntParameter(request, "roleid", -1);
		Map<String,Object> json = new HashMap<String, Object>();
		boolean flag = roleService.nameExists(roleName,roleid);
		if(!flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "名称重复,请重新输入！");
		}
		
		return json;
	}

}
