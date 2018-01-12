package com.richinfo.privilege.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.CatPrivilege;
import com.richinfo.privilege.entity.Privilege;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.entity.User;

/**
 * 角色管理
 * @author Administrator
 */
public interface RoleService extends BaseService<Role, Integer>{
	/**
	 * 角色成员列表
	 */
	public List<User> findUserByRoleid(int roleid);
  
	/**
	 * 角色系统权限列表
	 */
	public List<String> findRoleMenu(int roleid);
	
	/**
	 * 修改角色系统权限
	 * addids 新增的功能id  
	 * delids 取消的功能id
	 */
	public boolean updateRoleMenu(List<Privilege> adds,List<Integer> delids);
	/**
	 * 角色栏目权限列表
	 */
	public List<String> findRoleCat(int roleid);
	/**
	 * 修改角色栏目权限
	 */
	public boolean updateRoleCat(List<CatPrivilege> adds,List<Integer> delids);
	
	/**
	 * 检查角色名称是否存在
	 */
	public boolean nameExists(String roleName,Integer roleid);
	
}
