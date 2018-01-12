package com.richinfo.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.privilege.dao.ShortcutDao;
import com.richinfo.privilege.entity.Shortcut;
import com.richinfo.privilege.service.ShortcutService;

@Service("ShortcutService")
public class ShortcutServiceImpl extends BaseServiceImpl<Shortcut, Integer> implements ShortcutService 
{
	
	private ShortcutDao shortcutDao;
	
	@Autowired
    @Qualifier("ShortcutDao")
	public void setShortcutDao(ShortcutDao shortcutDao) 
	{
		this.shortcutDao = shortcutDao;
	}

	@Autowired
    @Qualifier("ShortcutDao")
	@Override
	public void setBaseDao(BaseDao<Shortcut, Integer> baseDao) 
	{
		
		this.baseDao = (ShortcutDao)baseDao;
	}

	public void deleteShortcuts(int userId, int menuId) 
	{
		String hql = "delete from Shortcut s where s.userId=? and s.menuId=?";
		shortcutDao.updateByHql(hql, new Object[]{userId,menuId});
		
	}

	public List<Integer> queryMyShortcutId(int userId) 
	{
		String hql = "select new Shortcut(menuId) from Shortcut c where c.userId=?";
		List<Shortcut> shortCutList = shortcutDao.list(hql, new Object[]{userId}, null);
		List<Integer> idList = new ArrayList<Integer>();
		for(Shortcut s:shortCutList)
		{
			idList.add(s.getMenuId());
		}
		return idList;
	}

	public void deleteShortcutsByUserId(int userId) 
	{
		String hql = "delete from Shortcut c where c.userId=?";
		shortcutDao.updateByHql(hql, new Object[]{userId});
		
	}

}
