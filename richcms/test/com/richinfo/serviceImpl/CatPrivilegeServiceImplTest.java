package com.richinfo.serviceImpl;

import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.junit.Test;

import com.richinfo.AbstractTestCase;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.service.CategoryService;

public class CatPrivilegeServiceImplTest extends AbstractTestCase
{
	final String[] includeAttributes = new String[]{"pid","children","catId","cat"}; ;
	@Test
	public void testRootCat(){
		CategoryService categoryService = (CategoryService)this.getBean("CategoryService");
		Category root = categoryService.getRootCategory();
		JsonConfig config = new JsonConfig();
		
		config.setJsonPropertyFilter(new PropertyFilter() 
		{
			public boolean apply(Object bean, String name, Object value)
			{
				for(int i=0;i<includeAttributes.length;i++)
				{
					if(includeAttributes[i].equals(name)){
						return false;
					}
				}
				return true;
			}
		});
		Object json = JSONObject.fromObject(root,config);
		System.err.println(json.toString());
	}
	
	@Test
	public void testListAllcat(){
		CategoryService categoryService = (CategoryService)this.getBean("CategoryService");
		List<Category> list = categoryService.listAll();
		for(Category c :list)
		{
			System.err.println(c.getPid()+"|"+c.getCat()+"|"+c.getCatId());
		}
		
	}
}
