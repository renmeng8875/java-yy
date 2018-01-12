package com.richinfo.playersknockout.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.playersknockout.dao.PlayersKnockoutDao;
import com.richinfo.playersknockout.entity.PlayersKnockout;

@Repository("PlayersKnockoutDao")
public class PlayersKnockoutDaoImpl extends BaseDaoImpl<PlayersKnockout, Integer> implements PlayersKnockoutDao 
{

}
