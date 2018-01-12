package com.richinfo.contentcat.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.contentcat.dao.SysorDao;
import com.richinfo.contentcat.entity.Sysor;
import com.richinfo.contentcat.service.SysorService;

@Service("SysorService")
public class SysorServiceImpl extends BaseServiceImpl<Sysor, Integer> implements
		SysorService {

	private SysorDao sysorDao;

	@Autowired
	@Qualifier("SysorDao")
	public void setSysorDao(SysorDao sysorDao) {
		this.sysorDao = sysorDao;
	}

	@Autowired
	@Qualifier("SysorDao")
	@Override
	public void setBaseDao(BaseDao<Sysor, Integer> baseDao) {
		this.baseDao = (SysorDao) baseDao;
	}

	@Override
	public long getTodayDelNum(int userid) {
		// TODO Auto-generated method stub
        String hql = "select count(s.id) from Sysor s where s.userId = ? and (s.opTime > ? and s.opTime <= ?) ";
        long count=  sysorDao.getCountByHql(hql, new Object[]{userid ,getTimesmorning() ,getTimesnight()}, null);
        return  count;
	}
 

	//
	//获得当天0点时间 
	public static Long getTimesmorning(){ 
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return  (cal.getTimeInMillis()/1000); 
	} 
	//获得当天24点时间 
	public static Long getTimesnight(){ 
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 24); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return  (cal.getTimeInMillis()/1000); 
	} 

	public static void main(String[] args) {
		System.out.println(getTimesmorning() + "  " + getTimesnight());
	}

}
