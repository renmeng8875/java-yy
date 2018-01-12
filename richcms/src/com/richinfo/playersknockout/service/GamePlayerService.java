package com.richinfo.playersknockout.service;



import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.playersknockout.entity.GamePlayer;

public interface GamePlayerService extends BaseService<GamePlayer,Integer>{

	public List<GamePlayer> listPlayerByObj(GamePlayer player);
	
	public GamePlayer exists(GamePlayer player);
	
	public GamePlayer checkIsExists(GamePlayer player);
	
	public GamePlayer findGamePlayer(int playerid);
	
	
}
