package com.richinfo.module.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.ContentSpDao;
import com.richinfo.module.entity.ContentSp;
import com.richinfo.module.service.ContentSpService;

@Service("ContentSpService")
public class ContentSpServiceImpl extends BaseServiceImpl<ContentSp, Integer> implements ContentSpService{

	private ContentSpDao contentSpDao;
	
	@Autowired
    @Qualifier("ContentSpDao")
	public void setContentSpDao(ContentSpDao contentSpDao) {
		this.contentSpDao = contentSpDao;
	}

	@Autowired
    @Qualifier("ContentSpDao")
	@Override
	public void setBaseDao(BaseDao<ContentSp, Integer> baseDao) 
	{
		this.baseDao = (ContentSpDao)baseDao; 
	}

	public void updatePriority(int companyId, int priority) 
	{
		String hql="update ContentSp a set a.priority=? where a.companyId=?";
		contentSpDao.updateByHql(hql, new Object[]{priority,companyId});
	}


	public Page<ContentSp> find(ContentSp c){
		List<Object> paramList=null;
		String hql = "from ContentSp c Where 1=1 ";
		
		paramList=new ArrayList<Object>();
		
		if(!StringUtils.isEmpty(c.getTitle())){
			hql+=" and c.title like ?";
			paramList.add('%'+c.getTitle()+'%');
		}
		if(null!=c.getCompanyId()){
			hql+=" and c.companyId = ?";
			paramList.add(c.getCompanyId());
		}
		if(c.getPublishBeginTime()!=null&&c.getPublishEndTime()!=null){
				hql+=" and c.pubTime>=? and c.pubTime<=?";
				paramList.add(c.getPublishBeginTime());
				paramList.add(c.getPublishEndTime());
		}
		if(c.getPublishBeginTime()!=null&&c.getPublishEndTime()==null){
				hql+=" and c.pubTime>=? ";
				paramList.add(c.getPublishBeginTime());
		}
		if(c.getPublishBeginTime()==null&&c.getPublishEndTime()!=null){
				hql+=" and c.pubTime<=? ";
				paramList.add(c.getPublishEndTime());
		}
		
		if(c.getSubmitBeginTime()!=null&&c.getSubmitEndTime()!=null){
				hql+=" and c.submitTime>=? and c.submitTime<=?";
				paramList.add(c.getSubmitBeginTime());
				paramList.add(c.getSubmitEndTime());
		}
		if(c.getSubmitBeginTime()!=null&&c.getSubmitEndTime()==null){
				hql+=" and c.submitTime>=? ";
				paramList.add(c.getSubmitBeginTime());
		}
		if(c.getSubmitBeginTime()==null&&c.getSubmitEndTime()!=null){
				hql+=" and c.submitTime<=? ";
				paramList.add(c.getSubmitEndTime());
		}
		
		hql+=" order by c.priority";
		Page<ContentSp> page= contentSpDao.find(hql, paramList.toArray(), null);
		
		paramList=null;
		return page;
	}
	
	public  byte[] hexStr2ByteArr(String strIn){
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];

		// 将原始字节数组转换为8位
		byte[] arrBTmp=strIn.getBytes();
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
        return arrB;
    }
	
	public  byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}


}
