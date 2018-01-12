package com.richinfo.stat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.richinfo.common.dao.impl.BaseDaoImpl;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.stat.entity.CategoryAccess;
import com.richinfo.stat.entity.ChannelAccess;

@Repository
public class ChannelAccessDao extends BaseDaoImpl<ChannelAccess, Integer> {

	public ChannelAccess getChannelAccessByPk(String channelNum,long startDate){
		String hql="from ChannelAccess t where t.channelNum=? and t.statDate=?";
		Object[] args=new Object[]{channelNum,startDate};
		Object obj=queryObject(hql, args, null);
		return obj==null?null:(ChannelAccess)obj;
	}
	
	public Page<Map<String,Object>> getChannelAccessList(String beginTime,String endTime){
		List<Object> paramList=null;
		
		String sql="select sum(IP) as ip,sum(UV) as UV,sum(PV) as PV,round(sum(pv) /sum(decode(uv, 0, 1,uv)), 0) as PVUV,sum(t.dlnum) as dlnum,t.STATDATE from MM_CHANNEL_ACCESS t where 1 = 1 ";
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
		sql+=" group by t.statdate order by t.statdate";
		return this.findBySql(sql,paramList.toArray(),null);
	}
}
