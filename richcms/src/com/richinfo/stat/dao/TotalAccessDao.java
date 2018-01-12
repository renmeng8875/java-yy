package com.richinfo.stat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.stat.entity.TotalAccess;

@Repository
public class TotalAccessDao extends BaseDaoImpl<TotalAccess, Integer>{

	public TotalAccess getTotalAccessByPk(long startDate){
		String hql="from TotalAccess t where t.statDate=?";
		Object[] args=new Object[]{startDate};
		Object obj=queryObject(hql, args, null);
		return obj==null?null:(TotalAccess)obj;
	}
	
	public Page<Map<String,Object>> getTotalAccessList(String beginTime,String endTime){
		List<Object> paramList=null;
		
		String sql="select IP,UV,PV,round(pv/decode(uv,0,1,uv),2) as PVUV,TOTALUSERNUM,TOTALDLNUM,LOGINDLNUM,NLOGINDLNUM,STATDATE from MM_TOTAL_ACCESS t where 1=1 ";
		String format="yyyy-MM-dd";
		
		paramList=new ArrayList<Object>();
		if(!StringUtils.isEmpty(beginTime)&&!StringUtils.isEmpty(endTime)){
			sql+=" and t.statdate>=? and t.statdate<=?";
			paramList.add(DateTimeUtil.converToTimestamp(beginTime, format));
			paramList.add(DateTimeUtil.converToTimestamp(endTime,format));
		}
		if(!StringUtils.isEmpty(beginTime)&&StringUtils.isEmpty(endTime)){
			sql+=" and t.statdate>=? ";
			paramList.add(DateTimeUtil.converToTimestamp(beginTime,format));
		}
		if(StringUtils.isEmpty(beginTime)&&!StringUtils.isEmpty(endTime)){
			sql+=" and t.statdate<=? ";
			paramList.add(DateTimeUtil.converToTimestamp(endTime,format));
		}
		return this.findBySql(sql,paramList.toArray(),null);
	}
}
