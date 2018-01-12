package com.richinfo.privilege.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.privilege.entity.Shortcut;

public interface ShortcutService extends BaseService<Shortcut,Integer>
{
	public void deleteShortcuts(int userId,int menuId);
	
	public void deleteShortcutsByUserId(int userId);
	
	public List<Integer> queryMyShortcutId(int userId);
}
