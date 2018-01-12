package com.richinfo.privilege.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.Menu;

public interface MenuService extends BaseService<Menu,Integer>
{

	public void printMenu(Menu menu);

	public List<Menu> listMyShortcut(int userId);
	
	public List<Menu> listShortcut();
	
	public Menu getMenuRoot();
	
	public int qetMenuLevel(int menuId);
	
	public Menu getMenuByInfo(Menu m);
	
	public List<Menu> getChildren(int pid,int userId);
}
