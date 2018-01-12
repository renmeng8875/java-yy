package com.richinfo.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.privilege.dao.CatPrivilegeDao;
import com.richinfo.privilege.dao.PrivilegeDao;
import com.richinfo.privilege.dao.RoleDao;
import com.richinfo.privilege.entity.CatPrivilege;
import com.richinfo.privilege.entity.Privilege;
import com.richinfo.privilege.entity.Role;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.RoleService;

@Service("RoleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements RoleService
{

	private RoleDao roleDao;
	
	private CatPrivilegeDao catPrivilegeDao;
	
	private PrivilegeDao privilegeDao;
	
	public PrivilegeDao getPrivilegeDao() {
		return privilegeDao;
	}
	
	@Autowired
    @Qualifier("PrivilegeDao")
	public void setPrivilegeDao(PrivilegeDao privilegeDao) {
		this.privilegeDao = privilegeDao;
	}

	public CatPrivilegeDao getCatPrivilegeDao() {
		return catPrivilegeDao;
	}
	
	@Autowired
    @Qualifier("CatPrivilegeDao")
	public void setCatPrivilegeDao(CatPrivilegeDao catPrivilegeDao) {
		this.catPrivilegeDao = catPrivilegeDao;
	}

	
	@Autowired
    @Qualifier("RoleDao")
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	


	public List<String> findRoleCat(int roleid) {
		List<String> catItems = roleDao.listBySql("select m.catid from mm_cat_privilege m where m.roleid=?", new Object[]{roleid}, null, null, false);
		return catItems;
	}

	public List<String> findRoleMenu(int roleid) {
		List<String> rolePrivileges = roleDao.listBySql("select m.menuid from mm_sys_privilege m where m.roleid=?", new Object[]{roleid}, null, null, false);
		return rolePrivileges;
	}

	public List<User> findUserByRoleid(int roleid) {
		List<User> allManager = roleDao.listBySql("select * from mm_sys_user u where u.roleid = ?", new Object[]{roleid}, null, User.class, true);
		return allManager;
	}

	public boolean updateRoleCat(List<CatPrivilege> adds,List<Integer> delids) 
	{
	    for(CatPrivilege catp : adds)
	    {
	    	catPrivilegeDao.add(catp);
	    }
	    for(int delid : delids)
	    {
	    	catPrivilegeDao.delete(delid);
	    }
		return true;
	}

	

	public boolean updateRoleMenu(List<Privilege> adds,List<Integer> delids) 
	{
	    for(Privilege p : adds){
	    	privilegeDao.add(p);
	    }
	    for(int delid : delids){
	    	privilegeDao.delete(delid);
	    }
		return true;
	}

	@Autowired
    @Qualifier("RoleDao")
	@Override
	public void setBaseDao(BaseDao<Role, Integer> baseDao) 
	{
		this.baseDao = (RoleDao)baseDao;
		
	}
	
	/**
	 * 检查角色名称是否存在
	 */
	public boolean nameExists(String roleName,Integer roleid) 
	{
		String hql = "from Role r where r.roleName=? and r.roleid!=?";
		List<Role> list = roleDao.list(hql, new Object[]{roleName,roleid}, null);
		if(list!=null&&list.size()>0)
		{
			return true;
		}
		return false;
	}
 
}
