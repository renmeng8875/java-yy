package com.richinfo.playersknockout.service.impl;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.playersknockout.dao.GamePlayerDao;
import com.richinfo.playersknockout.entity.GamePlayer;
import com.richinfo.playersknockout.service.GamePlayerService;

@Service("GamePlayerService")
public class GamePlayerServiceImpl extends BaseServiceImpl<GamePlayer, Integer> implements GamePlayerService{

	private GamePlayerDao gamePlayerDao;

	@Autowired
    @Qualifier("GamePlayerDao")
	public void setStyleDao(GamePlayerDao gamePlayerDao) 
	{
		this.gamePlayerDao = gamePlayerDao;
	}

	@Autowired
    @Qualifier("GamePlayerDao")
	@Override
	public void setBaseDao(BaseDao<GamePlayer, Integer> baseDao) 
	{
		this.baseDao = (GamePlayerDao)baseDao;
	}

	public List<GamePlayer> listPlayerByObj(GamePlayer player) {
		// TODO Auto-generated method stub
		List<GamePlayer> list = null;
		  
		StringBuffer hql = new StringBuffer();
		StringBuffer args = new StringBuffer();
		hql.append("from GamePlayer where 1=1 ");
		if(player!=null){
			if(player.getPkid()!=null&&!"0".equals(player.getPkid())){
				hql.append(" and  pkid=?");
				args.append(player.getPkid()+",");
			}
			if(!StringUtils.isEmpty(player.getPhonenumber())&&!"手机号".equals(player.getPhonenumber())){
				hql.append(" and  phonenumber like  '%'||?||'%'");
				args.append(player.getPhonenumber()+",");
			}
			if(!StringUtils.isEmpty(player.getPlayername())&&!"用户名".equals(player.getPlayername())){
				hql.append(" and  playername like  '%'||?||'%'");
				args.append(player.getPlayername()+",");

			}
			if(!StringUtils.isEmpty(player.getBscore())&&!"分数范围".equals(player.getBscore())){
				hql.append(" and  score >=?");
				args.append(player.getBscore()+",");
			}
			if(!StringUtils.isEmpty(player.getEscore())&&!"分数范围".equals(player.getEscore())){
				hql.append(" and  score <=?");
				args.append(player.getEscore()+",");
			}
			if("1".equals(player.getIsgod())){
				hql.append(" and  isgod = ? ");
				args.append(player.getIsgod()+",");
			}
			if(player.getPkname()!=null&&!StringUtils.isEmpty(player.getEscore())){
				hql.append(" and  pkname like  '%'||?||'%'");
				args.append(player.getPkname()+",");
			}
			hql.append(" order by score desc ,playerid");
		}else{
			hql.append(" order by score desc ,playerid");
		}
		Object[] params = "".equals(args.toString())?null: args.toString().split(",");
		list = gamePlayerDao.list(hql.toString(), params, null);
		return list;
	}

	public GamePlayer exists(GamePlayer player) {
		// TODO Auto-generated method stub
		GamePlayer g = (GamePlayer)gamePlayerDao.queryObject("from GamePlayer where playername=? and pkid=?", new Object[]{player.getPlayername(),player.getPkid()}, null);
		return g;
	}

	public GamePlayer checkIsExists(GamePlayer player) {
		// TODO Auto-generated method stub
		GamePlayer g = (GamePlayer)gamePlayerDao.queryObject("from GamePlayer t where t.pkid = ? and t.playername = ? and t.playerid != ?", new Object[]{player.getPkid(),player.getPlayername(),player.getPlayerid()}, null);
		return g;
	}
  

	public GamePlayer findGamePlayer(int playerid){
		// TODO Auto-generated method stub
		GamePlayer g = (GamePlayer)gamePlayerDao.queryObject("from GamePlayer where playerid=?", new Object[]{playerid}, null);
		return g;
	}
 

}
