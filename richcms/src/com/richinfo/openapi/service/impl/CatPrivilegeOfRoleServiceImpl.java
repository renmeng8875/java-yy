package com.richinfo.openapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.openapi.dao.CatPrivilegeOfRoleDao;
import com.richinfo.openapi.entity.CatPrivilegeOfRole;
import com.richinfo.openapi.service.CatPrivilegeOfRoleService;

@Service("CatPrivilegeOfRoleService")
public class CatPrivilegeOfRoleServiceImpl extends BaseServiceImpl<CatPrivilegeOfRole, Integer>
		implements CatPrivilegeOfRoleService {

	@Autowired
	@Qualifier("CatPrivilegeOfRoleDao")
	private CatPrivilegeOfRoleDao catPrivilegeOfRoleDao;
	
	@Autowired
	@Qualifier("CatPrivilegeOfRoleDao")
	@Override
	public void setBaseDao(BaseDao<CatPrivilegeOfRole, Integer> baseDao) {
		// TODO Auto-generated method stub
		this.baseDao = (CatPrivilegeOfRoleDao)baseDao;
	}

	public List<Integer> getCatPrivilegeByRoleid(int roleid) 
	{
		String hql = "from CatPrivilegeOfRole p where p.roleId=?";
		List<CatPrivilegeOfRole> list =  catPrivilegeOfRoleDao.list(hql, new Object[]{roleid}, null);
		List<Integer> catIds = new ArrayList<Integer>();
		for(CatPrivilegeOfRole p:list)
		{
			catIds.add(p.getCateId());
		}
		return catIds;
	}

	public void deleteCatPrivilegeByRoleid(int roleid) 
	{
		String hql = "delete from CatPrivilege c where c.roleId=?";
		catPrivilegeOfRoleDao.updateByHql(hql, new Object[]{roleid});
		
	}
}
