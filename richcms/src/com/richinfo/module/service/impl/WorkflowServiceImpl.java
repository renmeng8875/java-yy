package com.richinfo.module.service.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.module.dao.WorkflowDao;
import com.richinfo.module.dao.WorkflowUserDao;
import com.richinfo.module.entity.Workflow;
import com.richinfo.module.entity.WorkflowUser;
import com.richinfo.module.service.WorkflowService;

@Service("WorkflowService")
public class WorkflowServiceImpl extends BaseServiceImpl<Workflow, Integer> implements WorkflowService{

	private WorkflowDao workflowDao;

	private WorkflowUserDao workflowUserDao;
	
	@Autowired
    @Qualifier("WorkflowDao")
	public void setWorkflowDao(WorkflowDao workflowDao) 
	{
		this.workflowDao = workflowDao;
	}
	
	@Autowired
    @Qualifier("WorkflowUserDao")
	public void setWorkflowUserDao(WorkflowUserDao workflowUserDao) 
	{
		this.workflowUserDao = workflowUserDao;
	}

	@Autowired
    @Qualifier("WorkflowDao")
	@Override
	public void setBaseDao(BaseDao<Workflow, Integer> baseDao) 
	{
		this.baseDao = (WorkflowDao)baseDao;
	}
   
	public boolean deleteWorkflow(int flowid) {
		List<WorkflowUser> list = workflowUserDao.list("from WorkflowUser where flowid=?", new Object[]{flowid}, null);
		for(WorkflowUser workflowUser : list){
			workflowUserDao.deleteObject(workflowUser);
		}
		workflowDao.delete(flowid);
		return true;
	}

	public boolean nameExists(String name,Integer id) 
	{
		String hql = "from Workflow w where w.workflow=? and w.flowid!=?";
		List<Workflow> list = workflowDao.list(hql, new Object[]{name,id}, null);
		if(list!=null&&list.size()>0)
		{
			return true;
		}
		return false;
	}
 

}
