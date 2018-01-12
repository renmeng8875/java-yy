package com.richinfo.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.privilege.dao.CatPrivilegeDao;
import com.richinfo.privilege.entity.CatPrivilege;
import com.richinfo.privilege.service.CatPrivilegeService;

@Service("CatPrivilegeService")
public class CatPrivilegeServiceImpl extends BaseServiceImpl<CatPrivilege, Integer> implements CatPrivilegeService{

	private CatPrivilegeDao catPrivilegeDao;
	
	@Autowired
    @Qualifier("CatPrivilegeDao")
	public void setCatPrivilegeDao(CatPrivilegeDao catPrivilegeDao) 
	{
		this.catPrivilegeDao = catPrivilegeDao;
	}

	@Autowired
    @Qualifier("CatPrivilegeDao")
	@Override
	public void setBaseDao(BaseDao<CatPrivilege, Integer> baseDao) 
	{
		this.baseDao = (CatPrivilegeDao)baseDao;
		
	}

	public List<Integer> getCatPrivilegeByRoleid(int roleid) 
	{
		String hql = "select new CatPrivilege(catId)  from CatPrivilege p where p.roleId=?";
		List<CatPrivilege> list =  catPrivilegeDao.list(hql, new Object[]{roleid}, null);
		List<Integer> catIds = new ArrayList<Integer>();
		for(CatPrivilege p:list)
		{
			catIds.add(p.getCatId());
		}
		return catIds;
	}

	public void deleteCatPrivilegeByRoleid(int roleid) 
	{
		String hql = "delete from CatPrivilege c where c.roleId=?";
		catPrivilegeDao.updateByHql(hql, new Object[]{roleid});
		
	}

}
