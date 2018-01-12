package com.richinfo.playersknockout.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.playersknockout.dao.GamePlayerDao;
import com.richinfo.playersknockout.entity.GamePlayer;

@Repository("GamePlayerDao")
public class GamePlayerDaoImpl extends BaseDaoImpl<GamePlayer, Integer> implements GamePlayerDao {

 

}
