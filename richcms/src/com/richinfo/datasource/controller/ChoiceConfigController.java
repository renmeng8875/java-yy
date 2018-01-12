package com.richinfo.datasource.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.datasource.entity.Choice;
import com.richinfo.datasource.entity.ChoiceObject;
import com.richinfo.datasource.service.ChoiceConfigService;


@Controller
@RequestMapping(value = "/choiceconfig")
public class ChoiceConfigController {
 
	private ChoiceConfigService choiceService;
	
	@Autowired
	@Qualifier("ChoiceConfigService")
	public void setDataSourceConfigService(ChoiceConfigService choiceService) {
		this.choiceService = choiceService;
	}
	
	/**
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/choicelist.do")
	public String choicelist(HttpServletRequest request, Model model){
	    List<Choice> list = choiceService.listAll();
	    model.addAttribute("choicelist", JSONArray.fromObject(list));
		return "datasourceconfig/choicelist";
	}
	/**
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add.do")
	public String add(HttpServletRequest request, Model model,@ModelAttribute Choice choice){
		String method = request.getMethod();
		int id = ServletRequestUtils.getIntParameter(request, "choiceid", -1);
		
		String choicecode=ServletRequestUtils.getStringParameter(request, "choicecode", "");
		String viewcode=ServletRequestUtils.getStringParameter(request, "viewcode", "");
		String choicedesc=ServletRequestUtils.getStringParameter(request, "choicedesc", "");
		String onechoice=ServletRequestUtils.getStringParameter(request, "onechoice", "");
		String choicename=ServletRequestUtils.getStringParameter(request, "choicename", "");
		if(CommonUtil.hasInValidCharacters(choicecode,viewcode,choicedesc,onechoice,choicename)){
			return null;
		}
		
		if("GET".equals(method)){
			if(id>0){
				choice = choiceService.get(id);
				CommonUtil.escapeHtmlForObject(choice);
				model.addAttribute("choice", choice);
			}
			return "datasourceconfig/addchoice";
		}
		
		CommonUtil.unEscapeHtmlForObject(choice);
	    if(id>0){
	    	choiceService.update(choice);
	    	model.addAttribute("message", "选择项修改成功！");
	    }else{
	    	choiceService.save(choice);
	    	model.addAttribute("message", "选择项添加成功！");
	    }
		model.addAttribute("url","/choiceconfig/choicelist.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value="/delete.do")
	@ResponseBody
	public Object delete(HttpServletRequest request, Model model){
		Map<String, String> json = new HashMap<String, String>();
		int id = ServletRequestUtils.getIntParameter(request, "choiceid", -1);
		if(id>0){
			choiceService.delete(id);
			json.put("status", "0");
			json.put("msg", "选择项删除成功");
		}else{
			json.put("status", "-1");
			json.put("msg", "请选择一条数据");
		}
		return json;
	}
	
	@RequestMapping(value="/getChoiceListById.do")
	@ResponseBody
	public Object getChoiceListById(HttpServletRequest request, Model model){
		Map<String, Object> json = new HashMap<String, Object>();
		int choiceid = ServletRequestUtils.getIntParameter(request, "choiceid", -1);
		Choice choice = choiceService.get(choiceid);
		String choiceCode = choice.getChoicecode();
		List<ChoiceObject> choicelist = choiceService.getChoiceObjectList(choiceCode);
		if(choicelist.size()<1){
			json.put("status", "-1");
			return json;
		}
		json.put("choicelist", JSONArray.fromObject(choicelist));
		json.put("status", "0");
		return json;
	}
	
	
}
