package com.richinfo.playersknockout.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.playersknockout.dao.GamePrizesDao;
import com.richinfo.playersknockout.entity.GamePrizes;
import com.richinfo.playersknockout.service.GamePrizesService;

@Service("GamePrizesService")
public class GamePrizesServiceImpl extends BaseServiceImpl<GamePrizes, Integer> implements GamePrizesService{

	private GamePrizesDao gamePrizesDao;

	@Autowired
    @Qualifier("GamePrizesDao")
	public void setStyleDao(GamePrizesDao gamePrizesDao) 
	{
		this.gamePrizesDao = gamePrizesDao;
	}

	@Autowired
    @Qualifier("GamePrizesDao")
	@Override
	public void setBaseDao(BaseDao<GamePrizes, Integer> baseDao) 
	{
		this.baseDao = (GamePrizesDao)baseDao;
	}

	@Override
	public List<GamePrizes> findPrizesList(int pkid) {
		// TODO Auto-generated method stub
		 
		String hql = "from GamePrizes t where t.pkid=? order by t.priority desc";
		List<GamePrizes> prizesList = gamePrizesDao.list(hql, new Object[]{pkid}, null);
		
		return prizesList;
	}

	 

}
