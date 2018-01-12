package com.richinfo.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.encryptUtil.MD5Coder;
import com.richinfo.privilege.dao.CategoryDao;
import com.richinfo.privilege.dao.MenuDao;
import com.richinfo.privilege.dao.UserDao;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.UserService;
@Service("UserService")
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService {

	private UserDao userDao;
	
	private CategoryDao catDao;

	private MenuDao menuDao;
	
	@Autowired
    @Qualifier("CategoryDao")
	public void setCatDao(CategoryDao catDao) {
		this.catDao = catDao;
	}

	@Autowired
    @Qualifier("UserDao")
	public void setUserDao(UserDao userDao) 
	{
		this.userDao = userDao;
	}
	
	@Autowired
    @Qualifier("MenuDao")
	public void setUserDao(MenuDao menuDao) 
	{
		this.menuDao = menuDao;
	}
	
	@Autowired
    @Qualifier("UserDao")
	@Override
	public void setBaseDao(BaseDao<User, Integer> baseDao) {
		
		this.baseDao = (UserDao)baseDao;
	}

	/**
	 * 添加管理员
	 */
	@RenmSelf(methodDesc="添加一个管理员")
	public User addManeger(User user) 
	{
		String pwd = user.getPasswd();
		try 
		{
			String dbPwd = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(pwd));
			user.setPasswd(dbPwd);
		} 
		catch (Exception e) 
		{
			
		}
		userDao.add(user);
		return user;
	}

	
	public Menu findShortcuts(User u) 
	{
		Menu menu = new Menu();
		return menu;
	}

	public void lockUser(String  userName) 
	{
		User u = getUserByName(userName);
		u.setStatus(-1);
		userDao.update(u);
		
	}


	public void lockUser(int  userid) 
	{
		User u = get(userid);
		u.setStatus(-1);
		userDao.update(u);
		
	}
	/**
	 * 清除session
	 */
	public void logout(User  user) 
	{
		
		
	}
	
	@RenmSelf(methodDesc="用户登录校验方法")
	public User login(String userName,String pwd) 
	{
		User dbUser = (User)userDao.queryObject("from User u where u.userName=?", new Object[]{userName}, null);
		if(dbUser!=null){
			try {
				String encytpwd = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(pwd));
				if(encytpwd.equals(dbUser.getPasswd()))
				{
					return dbUser;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}


	@RenmSelf(methodDesc="修改用户密码")
	public boolean updateUserPwd(User user) 
	{
		String encytpwd ="";
		try {
			encytpwd = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(user.getPasswd()));
			
		} catch (Exception e) {
			encytpwd = user.getPasswd();
		}
		user.setPasswd(encytpwd);
		userDao.update(user);
		return true;
	}

	


	public void unlockUser(int  userId) 
	{
		User u = get(userId);
		u.setStatus(0);
		userDao.update(u);	
		
	}

	

	public boolean isExist(String userName) {
		List<User> list = userDao.list("from User u where u.userName=?", new Object[] {userName}, null);
		if(list!=null&&list.size()>0){
			return true;
		}
		return false;
	}

	public User getUserByName(String userName){
		User u = (User)userDao.queryObject("from User u where u.userName=?", new Object[] {userName}, null);
		return u;
	}

	public List<Category> findCatList(int userId) {
		String sql = "select * from mm_content_cat where catid in (select catid from mm_cat_privilege where roleid = (select roleid from mm_sys_user where userid = ?))";
		List<Category> list = catDao.listBySql(sql, new Object[]{userId}, null, Category.class, true);
		return list;
	}

	public List<Menu> findMenuList(int userId) {
		String sql = "select * from mm_sys_menu_java where menuid in(select menuid from mm_sys_privilege  where roleid =(select roleid from mm_sys_user where userid=?)) and ishidden=0 order by orderby";
		List<Menu> list =  menuDao.listBySql(sql, new Object[]{userId}, null, Menu.class, true);
		return list;
	}

	public List<User> findUserByRole(int roleid)
	{
		String hql = "from User u where u.role.roleid=?";
		List<User> list = userDao.list(hql, new Object[]{roleid}, null);
		return list;
	}
	
	
}
