package com.richinfo.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.richinfo.AbstractTestCase;
import com.richinfo.privilege.entity.Menu;
import com.richinfo.privilege.service.MenuService;

public class MenuServiceImplTest extends AbstractTestCase{
     
	@Test
	public void testBuildTree(){
		MenuService menuService = (MenuService)this.getBean("MenuService");
		Menu root = menuService.get(174);
		menuService.printMenu(root);
		
		
	}
	
	
	@Test
	public void testGetMenuRoot(){
		MenuService menuService = (MenuService)this.getBean("MenuService");
		Menu root = menuService.getMenuRoot();
		menuService.printMenu(root);
	}
	
	
	@Test
	public void testConvertJsonTree(){
		MenuService menuService = (MenuService)this.getBean("MenuService");
		Menu root = menuService.getMenuRoot();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"parentMenu"}); 
		JSONObject json = JSONObject.fromObject(root,config);
		System.err.println(json.toString());
		
	}
	
	@Test
	public void testBackup() throws Exception
	{
		MenuService menuService = (MenuService)this.getBean("MenuService");
		List<Menu> menuList = menuService.listAll();
		String name = Menu.class.getName();
		System.err.println(Menu.class.getCanonicalName());
		System.err.println(Menu.class.getSimpleName());
		OutputStream out = new FileOutputStream(new File("e:/"+name));
		IOUtils.writeLines(menuList, null, out);
		out.close();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRestore() throws Exception
	{
		InputStream in = new FileInputStream(new File("e:/backup.txt"));
		List<String> readLines = IOUtils.readLines(in);
		for(String line:readLines)
		{
			System.err.println(line);
		}
		in.close();
	}
	
	
	
	
}
