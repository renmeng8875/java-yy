package com.richinfo.datasource.dao.impl;

import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.datasource.dao.StationLetterDao;
import com.richinfo.datasource.entity.StationLetter;

@Repository("StationLetterDao")
public class StationLetterDaoImpl extends BaseDaoImpl<StationLetter, Integer> implements StationLetterDao {

}
