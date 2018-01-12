package com.richinfo.privilege.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.entity.User;

public interface UserService extends BaseService<User, Integer>{
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public User login(String userName,String pwd);
	/**
	 * 退出登录
	 * @return
	 */
	public void logout(User user);
	
	/**
	 * 添加管理员
	 */
	public User addManeger(User user);
	
	/**
	 * 修改用户密码
	 */
	public boolean updateUserPwd(User user);
	
	/**
	 * 锁定用户
	 */
	public void lockUser(String  userName);
	
	/**
	 * 锁定用户
	 */
	public void lockUser(int  userId);
	
	/** 
	 * 解锁
	 */
	public void unlockUser(int  userId);
	
	/**
	 * 我的快捷操作
	 */
	public Menu findShortcuts(User u);
	
	
	public boolean isExist(String userName);
	
	public User getUserByName(String userName);
	
	public List<Menu> findMenuList(int userId);
	
	public List<Category> findCatList(int UserId);
	
	public List<User> findUserByRole(int roleid);
	
}
