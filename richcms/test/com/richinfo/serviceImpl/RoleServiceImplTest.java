package com.richinfo.serviceImpl;

import java.util.List;

import org.junit.Test;

import com.richinfo.AbstractTestCase;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.RoleService;

public class RoleServiceImplTest extends AbstractTestCase{

	@Test
	public void testAddRole(){
		RoleService roleService = (RoleService)this.getBean("RoleService");
		Role role = new Role();
		role.setCtime(1234567890);
		role.setMem("测试");
		role.setPriority(0);
		role.setRoleName("test");
		role.setRoleid(100);
		roleService.save(role);
	}
	@Test
	public void testFindAllRole(){
		RoleService roleService = (RoleService)this.getBean("RoleService");
		List<Role> rolelist = roleService.listAll();
		System.out.println(rolelist.size());
	}
	
	@Test
	public void testUpdateRoleInfo(){
		RoleService roleService = (RoleService)this.getBean("RoleService");
		Role role = roleService.get(4);
		role.setMem("test");
		role.setRoleName("testtest");
		roleService.update(role);
	}
	
	@Test
	public void testDeleteRole(){
		RoleService roleService = (RoleService)this.getBean("RoleService");
		  roleService.delete(4);
		 
	}
	
	@Test
	public void testFindUserByRoleid(){
		RoleService roleService = (RoleService)this.getBean("RoleService");
		List<User> roleuser = roleService.findUserByRoleid(8);
		System.out.println(roleuser.size());
	}
	
	@Test
	public void testFindRoleMenu(){
		RoleService roleService = (RoleService)this.getBean("RoleService");
		List<String> rolemenu = roleService.findRoleMenu(8);
		for(String menuid : rolemenu){
			System.out.println(menuid);
		}
	}
	
	
	@Test
	public void testFindRoleCat(){
		RoleService roleService = (RoleService)this.getBean("RoleService");
		List<String> rolecat = roleService.findRoleCat(8);
		for(String catid : rolecat){
			System.out.println(catid);
		}
	}
	
	
}
