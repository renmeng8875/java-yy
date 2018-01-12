package com.richinfo.playersknockout.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.playersknockout.dao.VideoDao;
import com.richinfo.playersknockout.entity.Video;

@Repository("videoDao")
public class VideoDaoImpl extends BaseDaoImpl<Video, Integer> implements VideoDao{

}
