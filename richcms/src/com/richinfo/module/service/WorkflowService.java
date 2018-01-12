package com.richinfo.module.service;



import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.Workflow;

public interface WorkflowService extends BaseService<Workflow,Integer>{

	public boolean deleteWorkflow(int flowid);
	
	public boolean nameExists(String name,Integer id);
}
