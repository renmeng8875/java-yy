package com.richinfo.module.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.WorkflowUserDao;
import com.richinfo.module.entity.WorkflowUser;
import com.richinfo.module.service.WorkflowUserService;

@Service("WorkflowUserService")
public class WorkflowUserServiceImpl extends BaseServiceImpl<WorkflowUser, Integer> implements WorkflowUserService{

	private WorkflowUserDao workflowUserDao;

	@Autowired
    @Qualifier("WorkflowUserDao")
	public void setWorkflowUserDao(WorkflowUserDao workflowUserDao) 
	{
		this.workflowUserDao = workflowUserDao;
	}
	
	@Autowired
	@Qualifier("WorkflowUserDao")
	@Override
	public void setBaseDao(BaseDao<WorkflowUser, Integer> baseDao) {
		this.baseDao = (WorkflowUserDao)baseDao;
	}
	
	public List<WorkflowUser> queryWfuserByFlowId(int workflowId){
		List<WorkflowUser> workflowUserList=workflowUserDao.list("from WorkflowUser w where w.flowid=?", new Object[]{workflowId}, null);
		return workflowUserList;
	}
	
	public void updateWorkflowUser(int flowid, int flevel,String userid) {
		String hql = "update WorkflowUser w set w.userid=? where w.flevel=? and w.flowid=? ";
		workflowUserDao.updateByHql(hql, new Object[]{userid,flevel,flowid});
	}
	
	public int getWorkflowUserByParam(int flowid,int flevel){
		String hql = "from WorkflowUser w where w.flevel=? and w.flowid=? ";
		List<WorkflowUser> list=workflowUserDao.list(hql, new Object[]{flevel,flowid},null);
		return list!=null?list.size():0;
	}
}
