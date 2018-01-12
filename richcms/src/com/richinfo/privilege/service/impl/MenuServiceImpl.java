package com.richinfo.privilege.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.privilege.dao.MenuDao;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.service.MenuService;

@Service("MenuService")
public class MenuServiceImpl extends BaseServiceImpl<Menu, Integer> implements MenuService{
   
	private MenuDao menuDao;

	@Autowired
    @Qualifier("MenuDao")
	public void setMenuDao(MenuDao menuDao) 
	{
		this.menuDao = menuDao;
	}
	
	public void printMenu(Menu menu)
	{
		if (menu == null)
		{
			return;
		}
		int level = menu.getMenuLevel();
		if (level > 0) {
			for (int i = 0; i < level; i++) 
			{
				System.out.print("  |");
			}
			System.err.print("--");
		}
		System.err.println(menu.getMenu()+ (menu.isLeaf() ? "" : "[" + menu.getChildren().size() + "]"));
		Set<Menu> children = menu.getChildren();
		for (Iterator<Menu> iter = children.iterator(); iter.hasNext();) 
		{
			Menu child = (Menu) iter.next();
			printMenu(child);
		}
	}
	
	public List<Menu> listMyShortcut(int userId) 
	{
		String sql = "select * from mm_sys_menu_java where menuid in (select menuid from mm_sys_shortcuts where userid=?) and menulevel=3 order by orderby";
		List<Menu> shortcutList = menuDao.listBySql(sql, new Object[]{userId}, null, Menu.class, true);
		return shortcutList;
	}
	
	public List<Menu> listShortcut()
	{
		String sql = "select * from mm_sys_menu_java where menulevel=3 and action like '%.do%' order by orderby";
		List<Menu> shortcutList = menuDao.listBySql(sql, null, null, Menu.class, true);
		return shortcutList;
	}
	
	public Menu getMenuRoot()
	{
		String hql = "from Menu m where m.parent.menuId is null";
		Menu root = (Menu)menuDao.queryObject(hql, null, null);
		return root;
	}
	
	public long getMenuCount()
	{
		return menuDao.getCountByHql("from Menu ", null, null);
	}

	@Autowired
    @Qualifier("MenuDao")
	@Override
	public void setBaseDao(BaseDao<Menu, Integer> baseDao) 
	{
		this.baseDao = (MenuDao)baseDao;
		
	}

	public int qetMenuLevel(int menuId) 
	{
		Menu m = (Menu) menuDao.queryObject("select new Menu(menuLevel) from Menu where menuId=?", new Object[]{menuId}, null);
		return m.getMenuLevel();
	}

	public Menu getMenuByInfo(Menu m) 
	{
		String hql = "from Menu t where t.control=? and t.action=?";
		Menu menu = (Menu)menuDao.queryObject(hql,new Object[]{m.getControl(),m.getAction()}, null);
		return menu;
	}

	public List<Menu> getChildren(int pid,int userId) 
	{
		
		String hql = "select * from mm_sys_menu_java where pid=? and menuid in(select menuid from mm_sys_privilege  where roleid =(select roleid from mm_sys_user where userid=?)) order by orderby";
		List<Menu> menuList = menuDao.listBySql(hql, new Object[]{pid,userId}, null,Menu.class, true);
		return menuList;
	}

}
