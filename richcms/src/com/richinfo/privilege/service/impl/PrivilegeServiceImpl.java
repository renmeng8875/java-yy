package com.richinfo.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.privilege.dao.PrivilegeDao;
import com.richinfo.privilege.entity.Privilege;
import com.richinfo.privilege.service.PrivilegeService;

@Service("PrivilegeService")
public class PrivilegeServiceImpl extends BaseServiceImpl<Privilege, Integer> implements PrivilegeService{

	private PrivilegeDao privilegeDao;

	
	@Autowired
    @Qualifier("PrivilegeDao")
	public void setPrivilegeDao(PrivilegeDao privilegeDao) 
	{
		this.privilegeDao = privilegeDao;
	}

	@Autowired
    @Qualifier("PrivilegeDao")
	@Override
	public void setBaseDao(BaseDao<Privilege, Integer> baseDao) 
	{
		this.baseDao = (PrivilegeDao)baseDao;
	}

	public List<Integer> getPrivilegeByRoleid(Integer roleid) 
	{
		String hql = "select new Privilege(menuId)  from Privilege p where p.roleId=?";
		List<Privilege> list = privilegeDao.list(hql, new Object[]{roleid}, null);
		List<Integer> menuIds = new ArrayList<Integer>();
		for(Privilege p:list)
		{
			menuIds.add(p.getMenuId());
		}
		return menuIds;
	}
	
	@RenmSelf(methodDesc="修改角色权限-根据角色id清空角色权限")
	public void deletePrivilegeByRoleid(Integer roleid) 
	{
		String hql = "delete from Privilege p where p.roleId=?";
		privilegeDao.updateByHql(hql, new Object[]{roleid});
	}

	
	@RenmSelf(methodDesc="删除当前菜单-删除菜单下的权限信息")
 	public boolean deletePrivilegeByMenuid(int menuId) {
		// TODO Auto-generated method stub
		String hql = "delete Privilege p where p.menuId=?";
		privilegeDao.updateByHql(hql, new Object[]{menuId}); 
		return true;
	}
	

}
