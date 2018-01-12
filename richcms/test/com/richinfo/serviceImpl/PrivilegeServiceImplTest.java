package com.richinfo.serviceImpl;

import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;

import com.richinfo.AbstractTestCase;
import com.richinfo.privilege.service.PrivilegeService;

public class PrivilegeServiceImplTest extends AbstractTestCase
{
	
	@Test
	public void testGetPrivilegeByRoleid(){
		PrivilegeService service = (PrivilegeService)this.getBean("PrivilegeService");
		List<Integer> list = service.getPrivilegeByRoleid(144);
		JSONArray json = JSONArray.fromObject(list);
		System.err.println(json.toString());
		
	}
}
