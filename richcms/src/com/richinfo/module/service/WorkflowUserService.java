package com.richinfo.module.service;

import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.WorkflowUser;

public interface WorkflowUserService extends BaseService<WorkflowUser,Integer>{

	
	public List<WorkflowUser> queryWfuserByFlowId(int workflowId);
	
	public void updateWorkflowUser(int flowid, int flevel,String userid);
	
	public int getWorkflowUserByParam(int flowid,int flevel);
}
