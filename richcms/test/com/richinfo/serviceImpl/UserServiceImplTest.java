package com.richinfo.serviceImpl;

import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import com.richinfo.AbstractTestCase;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.UserService;

public class UserServiceImplTest extends AbstractTestCase
{
	
	@Test
	public void testAddManager(){
		UserService userService = (UserService)this.getBean("UserService");
		User u = new User();
		u.setEmail("453791694@qq.com");
		u.setNickName("ilove138");
		u.setUserName("wendy");
		u.setPasswd("123456");
		userService.addManeger(u);
	}
	
	@Test
	public void testLogin1(){
		UserService userService = (UserService)this.getBean("UserService");
		User u = userService.login("wendy", "123456");
		Assert.assertNotNull(u);
	}
	
	
	@Test
	public void testLockUser(){
		UserService userService = (UserService)this.getBean("UserService");
		userService.lockUser("renmeng");
		User u = userService.getUserByName("renmeng");
		Assert.assertEquals(-1, u.getStatus().intValue());
	}
	
	@Test
	public void testGetUserByName(){
		UserService userService = (UserService)this.getBean("UserService");
		User u = userService.getUserByName("admin");
		Assert.assertEquals("超级系统管理员", u.getRole().getRoleName());
		
		
	}
	
	@Test
	public void testJson(){
		User u = new User();
		u.setEmail("453791694@qq.com");
		u.setNickName("ilove138");
		u.setUserName("wendy");
		u.setPasswd("123456");
		JSONObject json = JSONObject.fromObject(u);
		System.err.println(json.toString());
		
	}
	
	@Test
	public void testCountAll(){
		UserService userService = (UserService)this.getBean("UserService");
		int all = userService.countAll();
		System.err.println(all);
	}
	
	@Test
	public void testListAll(){
		UserService userService = (UserService)this.getBean("UserService");
		List<User> allUser = userService.listAll();
		for(User u:allUser)
		{
			System.err.println(u.getNickName()+"||"+u.getRole().getRoleName());
		}

	}
	
	@Test
	public void testDeluser()
	{
		UserService userService = (UserService)this.getBean("UserService");
		userService.delete(210000);
		
		
	}
	
	
	
}
