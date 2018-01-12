package com.richinfo.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.openapi.dao.SysPrivilegeOfRoleDao;
import com.richinfo.openapi.entity.SysPrivilegeOfRole;
import com.richinfo.openapi.service.SysPrivilegeOfRoleService;

@Service("SysPrivilegeOfRoleService")
public class SysPrivilegeOfRoleServiceImpl extends BaseServiceImpl<SysPrivilegeOfRole, Integer>
		implements SysPrivilegeOfRoleService {


	@Autowired
	@Qualifier("SysPrivilegeOfRoleDao")
	private SysPrivilegeOfRoleDao sysPrivilegeOfRoleDao;
	
	@Autowired
	@Qualifier("SysPrivilegeOfRoleDao")
	@Override
	public void setBaseDao(BaseDao<SysPrivilegeOfRole, Integer> baseDao) {
		// TODO Auto-generated method stub
		this.baseDao = (SysPrivilegeOfRoleDao) baseDao;
	}

	public List<SysPrivilegeOfRole> listByRoleId(Integer roleId) {
		// TODO Auto-generated method stub
		String hql = "from SysPrivilegeOfRole s where s.roleId=?";
		return sysPrivilegeOfRoleDao.list(hql, new Object[]{roleId}, null);
	}

	public Object queryObject(String hql, Object[] args) {
		return sysPrivilegeOfRoleDao.queryObject(hql, args, null);
	}

	public int deleteByFunId(Integer funId) {
		String hql = "delete SysPrivilegeOfRole s where s.funId=?";
		return sysPrivilegeOfRoleDao.updateByHql(hql,new Object[]{funId});
	}
	
	public boolean canDeleteFun(Integer funId)
	{
		String hql = "from SysPrivilegeOfRole s where s.funId=?";
		List<SysPrivilegeOfRole> list = sysPrivilegeOfRoleDao.list(hql, new Object[]{funId}, null);
		if(list!=null&&list.size()>0)
		{
			return false;
		}
		return true;
	}
	
	public int deleteByRoleId(Integer roleId) {
		String hql = "delete SysPrivilegeOfRole s where roleId=?";
		return sysPrivilegeOfRoleDao.updateByHql(hql,new Object[]{roleId});
	}
}
