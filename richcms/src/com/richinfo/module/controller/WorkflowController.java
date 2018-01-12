package com.richinfo.module.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.AntiXSSUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.module.entity.Workflow;
import com.richinfo.module.entity.WorkflowUser;
import com.richinfo.module.service.WorkflowService;
import com.richinfo.module.service.WorkflowUserService;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.UserService;

@Controller
@RequestMapping(value = "/Workflow")
public class WorkflowController {

	private WorkflowService workflowService;
	
	private WorkflowUserService workflowUserService;
	 
	private UserService userService;
	
	@Autowired
	@Qualifier("UserService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	@Qualifier("WorkflowService")
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	@Autowired
	@Qualifier("WorkflowUserService")
	public void setWorkflowUserService(WorkflowUserService workflowUserService) {
		this.workflowUserService = workflowUserService;
	}
	 
	/**
	 * 登陆后 跳转至后台首页
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<Workflow> flowList = workflowService.listAll();
        model.addAttribute("flowList", JSONArray.fromObject(flowList));
		return "workflow/lists";
	}
	/**
	 * add edit
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	public String add(HttpServletRequest request, Model model,@ModelAttribute Workflow workflow) {
		String method = request.getMethod();
		int flowid = ServletRequestUtils.getIntParameter(request, "flowid", -1);
		if("GET".equals(method)){
			if(flowid>0){
				Workflow info = workflowService.get(flowid);
				model.addAttribute("id", flowid);
				model.addAttribute("info", info);
			}
			return "workflow/add";
		}
		workflow.setMem(AntiXSSUtil.antiXSSNEW(workflow.getMem()));
		if(flowid>0){
			workflowService.update(workflow);
			model.addAttribute("message", "修改流程成功！");
		}else{
			workflowService.save(workflow);
			model.addAttribute("message", "添加流程成功！");
		}
		
        model.addAttribute("url", "/Workflow/lists.do");
        return Constants.FORWARDURL;
	}
	/**
	 * s删除工作流
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Map<String,String> del(HttpServletRequest request, Model model){
		Map<String,String> json = new HashMap<String, String>();
		int flowid = ServletRequestUtils.getIntParameter(request, "flowid", -1);
		if(flowid>0){
			
			if(workflowService.deleteWorkflow(flowid)){
				json.put("status", "1");
				json.put("msg", "删除成功");
			}else{
				json.put("status", "0");
				json.put("msg", "删除失败");
			}
			
		}else{
			json.put("status", "-1");
			json.put("msg", "数据不存在");
		}
		return json;
	}
	
	@RequestMapping(value = "/audiflowuser.do")
	public String openFlowUser(HttpServletRequest request, Model model)
	{
		int flowid = ServletRequestUtils.getIntParameter(request, "flowid", -1);
		int flowlevel = ServletRequestUtils.getIntParameter(request, "flowlevel", -1);
		
		String method = request.getMethod();
		if("GET".equals(method))
		{
			List<WorkflowUser> wfuserList = workflowUserService.queryWfuserByFlowId(flowid);
			List<User> userList = userService.listAll();
			model.addAttribute("userList", userList);
			model.addAttribute("wfuserList",wfuserList);
			model.addAttribute("flowlevel",flowlevel);
			model.addAttribute("flowid",flowid);
			return "workflow/flowUser";
		}
		String[] userids = ServletRequestUtils.getStringParameters(request, "userid");
		Map<String,String> useridMap = new HashMap<String, String>();
		for(String userid:userids)
		{
			String[] arr = userid.split(",");
			if(useridMap.get(arr[1])==null)
			{
				useridMap.put(arr[1], arr[0]);
			}else{
				String value = useridMap.get(arr[1]);
				useridMap.put(arr[1], value+","+arr[0]);
			}
		}
		for(String key:useridMap.keySet())
		{
			String userid = useridMap.get(key);
			int flevel=Integer.valueOf(key);
			int count=workflowUserService.getWorkflowUserByParam(flowid, flevel);
			if(count>0){
				workflowUserService.updateWorkflowUser(flowid, flevel, userid);
			}else{
				WorkflowUser wfu=new WorkflowUser();
				wfu.setUserid(userid);
				wfu.setFlevel(flevel);
				wfu.setFlowid(flowid);
				wfu.setCtime(DateTimeUtil.getTimeStamp());
				workflowUserService.save(wfu);
			}
		}	
		return null;
	}
	
	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String name = request.getParameter("param");
		int id = ServletRequestUtils.getIntParameter(request, "id", -1);
		Map<String,Object> json = new HashMap<String, Object>();
		boolean flag = workflowService.nameExists(name,id);
		if(!flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "名称重复,请重新输入！");
		}
		
		return json;
	}
}
