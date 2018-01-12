package com.richinfo.playersknockout.service;




import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.playersknockout.entity.GamePrizes;

public interface GamePrizesService extends BaseService<GamePrizes,Integer>{
 
	public List<GamePrizes> findPrizesList(int pkid);
	
}
