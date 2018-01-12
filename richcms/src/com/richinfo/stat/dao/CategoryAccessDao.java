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

@Repository
public class CategoryAccessDao extends BaseDaoImpl<CategoryAccess, Integer> {
	
	public CategoryAccess getCategoryAccessByPk(String levelOne,String levelTwo,String levelThree,long startDate){
		String hql="from CategoryAccess t where t.levelone=? and t.leveltwo=? and t.levelthree=? and t.statDate=?";
		Object[] args=new Object[]{levelOne, levelTwo, levelThree,startDate};
		Object obj=queryObject(hql, args, null);
		return obj==null?null:(CategoryAccess)obj;
	}
	
	public Page<Map<String,Object>> getCategoryAccessList(String beginTime,String endTime,String groupBy){
		List<Object> paramList=null;
		String sql="select ";
		
		//分类
		if("classisum".equals(groupBy)){
			sql+="t.levelthree,";
		}else{
			//按日
			sql+="t.statdate,";
		}
		sql=sql+"sum(t.totalaccessnum) dtassnum,sum(t.totalip) dtip,sum(t.totalpv) as dtpv," +
		"sum(t.totaluv) as dtuv,round((sum(t.totalpv)/sum(t.totalip)),2)*100 as dtpvip,sum(t.logindlnum) as dlogindlnum," +
		"sum(t.logindlusernum) as dlogindlusernum,sum(t.nlogindlnum) as dnlogindlnum,sum(t.nlogindlusernum) dnlogdlusernum," +
		"sum(t.totaldlnum) as dtotaldlnum,sum(t.totaldlusernum) as dtdlusernum " +
		"from  mm_category_access t where 1=1 ";
		
		paramList=new ArrayList<Object>();
		String format="yyyy-MM-dd";
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
		//分类
		if("classisum".equals(groupBy)){
			sql+=" group by t.levelthree order by t.levelthree";
		}else{
			//按日
			sql+=" group by t.statdate order by t.statdate";
		}
		return this.findBySql(sql,paramList.toArray(),null);
	}
	
	public Page<Map<String,Object>> getCategoryAccessList(String beginTime,String endTime){
		String sql="select t.statdate,t.leveltwo,t.levelthree,t.totalaccessnum,t.totalip,t.totalpv," +
		"t.totaluv,t.pvip,t.logindlnum," +
		"t.logindlusernum,t.nlogindlnum,t.nlogindlusernum," +
		"t.totaldlnum,t.totaldlusernum " +
		" from mm_category_access t where 1=1 ";
		
		List<Object> paramList=new ArrayList<Object>();
		String format="yyyy-MM-dd";
		
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
		sql+=" order by t.statDate desc,t.leveltwo";
		return this.findBySql(sql,paramList.toArray(),null);
	}
}
