package com.richinfo.playersknockout.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.playersknockout.dao.VideoDao;
import com.richinfo.playersknockout.entity.Video;
import com.richinfo.playersknockout.service.VideoService;

@Service("videoService")
public class VideoServiceImpl extends BaseServiceImpl<Video,Integer> implements VideoService{

	@Autowired
    @Qualifier("videoDao")
	@Override
	public void setBaseDao(BaseDao<Video, Integer> baseDao) 
	{
		this.baseDao = (VideoDao)baseDao;
		
	}

}
