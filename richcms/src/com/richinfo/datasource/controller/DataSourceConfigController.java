package com.richinfo.datasource.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.richinfo.common.SystemProperties;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.datasource.entity.Choice;
import com.richinfo.datasource.entity.FormField;
import com.richinfo.datasource.entity.FormInfo;
import com.richinfo.datasource.service.ChoiceConfigService;
import com.richinfo.datasource.service.DataSourceConfigService;
import com.richinfo.module.entity.Tpl;
import com.richinfo.module.service.DataRelationService;


@Controller
@RequestMapping(value = "/dataconfig")
public class DataSourceConfigController {
 
	private DataSourceConfigService dataConfigService;
	
	private DataRelationService dataRelationService;
	
	private ChoiceConfigService choiceService;

	@Autowired
	@Qualifier("DataRelationService")
	public void setDataRelationService(DataRelationService dataRelationService) {
		this.dataRelationService = dataRelationService;
	}
	
	@Autowired
	@Qualifier("DataSourceConfigService")
	public void setDataSourceConfigService(DataSourceConfigService dataConfigService) {
		this.dataConfigService = dataConfigService;
	}
	
	@Autowired
	@Qualifier("ChoiceConfigService")
	public void setChoiceConfigService(ChoiceConfigService choiceService) {
		this.choiceService = choiceService;
	}
	
	/**
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/formlists.do")
	public String formlists(HttpServletRequest request, Model model){
		List<FormInfo> formlist = dataConfigService.getFormInfoListsByType("extends");
		model.addAttribute("formlist", JSONArray.fromObject(formlist));
		return "datasourceconfig/formlists";
	}
	
	/**
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/catconfig.do")
	public String catconfig(HttpServletRequest request, Model model){
		List<FormInfo> formlist = dataConfigService.getFormInfoListsByType("base");
		model.addAttribute("formlist", JSONArray.fromObject(formlist));
		return "datasourceconfig/formlists";
	}
	/**
	 * 修改数据源配置
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/editform.do")
	public String editform(HttpServletRequest request, Model model){
		int formid = ServletRequestUtils.getIntParameter(request, "formid", -1);
		if(formid==-1){
			return null;
		}
		FormInfo forminfo = dataConfigService.getFormInfoById(formid);
		String datatype = SystemProperties.getInstance().getProperty("datasource.datatype");
		List<String> datalist = Arrays.asList(datatype.split(","));
		model.addAttribute("forminfo", forminfo);
		model.addAttribute("formid", formid);
		model.addAttribute("datalist", datalist);
		return "datasourceconfig/editform";
	}
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="/saveform.do")
	public String saveform(HttpServletRequest request, Model model,@ModelAttribute FormInfo forminfo){
		//appScan
		if(CommonUtil.hasInValidCharacter(forminfo)){
			return null;
		}
		forminfo.setFormname(Constants.FORMPREFIX + forminfo.getDatatype());
		boolean bln = dataConfigService.saveOrUpdateFormInfo(forminfo);
		if(bln){
			model.addAttribute("message", "表单操作成功！");
		}else{
			model.addAttribute("message", "表单操作成功！");
		}
		if("data".equals(forminfo.getDatatype()))
			model.addAttribute("url","/dataconfig/catconfig.do");
		else
			model.addAttribute("url","/dataconfig/formlists.do");
		return Constants.FORWARDURL;
	}
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/deleteForm.do")
	@ResponseBody
	public Map<String,String> deleteForm(HttpServletRequest request, Model model){
		Map<String,String> json = new HashMap<String, String>();
		int formid = ServletRequestUtils.getIntParameter(request, "formid", -1);
		boolean bln = dataConfigService.deleteForm(formid);
		if(bln){
		    json.put("status", "0");
		    json.put("msg", "数据源删除成功");
		}else{
			json.put("status", "-1");
			json.put("msg", "数据源已使用，不可删除");
		}
		return json;
	}
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/formfieldlists.do")
	public String formfieldlists(HttpServletRequest request, Model model){
		String lselected = ServletRequestUtils.getStringParameter(request, "lselected", "0");
		String formid = ServletRequestUtils.getStringParameter(request, "formid", "0");
		String formtype = ServletRequestUtils.getStringParameter(request, "type", "0");
		List<FormField> fieldlist = dataConfigService.getFieldListByTableId(formid);
		model.addAttribute("fieldlist", JSONArray.fromObject(fieldlist));
		model.addAttribute("formid", HtmlUtils.htmlEscape(formid));
		model.addAttribute("formtype", HtmlUtils.htmlEscape(formtype));
		model.addAttribute("lselected", HtmlUtils.htmlEscape(lselected));
		return "datasourceconfig/fieldlists";
	}
	
	@RequestMapping(value="/addfield.do")
	public String addfield(HttpServletRequest request, Model model,@ModelAttribute FormField formfield){
		String method = request.getMethod();
		int fieldid = ServletRequestUtils.getIntParameter(request, "fieldid", -1);
		String formtype = ServletRequestUtils.getStringParameter(request, "formtype", "");
		if("GET".equals(method)){
			FormField field = dataConfigService.getFormFieldById(fieldid);
			List<FormInfo> formlist = dataConfigService.getFormInfoLists();
			List<Choice> choicelist = choiceService.listAll();
			model.addAttribute("choicelist", choicelist);
			
			model.addAttribute("field", field);
			model.addAttribute("formlist", formlist);
			if(fieldid>0){
				model.addAttribute("formid", field.getFormid());
			}else{
				int formid = ServletRequestUtils.getIntParameter(request, "formid", -1);
				model.addAttribute("formid", formid);
			}
			model.addAttribute("formtype", formtype);
			CommonUtil.escapeHtmlForModel(model);
			return "datasourceconfig/addfield";
		}
		//appScan
		if(CommonUtil.hasInValidCharacter(formfield)){
			return null;
		}
		String formidTemp=ServletRequestUtils.getStringParameter(request, "formid", "0");
		if(!StringUtils.isNumeric(formidTemp)){
			return null;
		}
		
		try {
			formfield.setCtime(DateTimeUtil.getTimeStamp());
			if(formfield.getIspush()==null){
				formfield.setIspush("0");
			}
			dataConfigService.addTableField(formfield);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		model.addAttribute("message", "字段添加成功！");
		model.addAttribute("url","/dataconfig/formfieldlists.do?formid="+formfield.getFormid()+"&lselected=1");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value="/deleteField.do")
	@ResponseBody
	public Map<String,String> deleteField(HttpServletRequest request, Model model){
		int fieldid = ServletRequestUtils.getIntParameter(request, "fieldid", -1);
		Map<String,String> json = dataConfigService.deleteField(fieldid);
		return json;
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edittemplate.do")
	public String edittemplate(HttpServletRequest request, Model model) {
		int srcid = ServletRequestUtils.getIntParameter(request, "formid", -1);
		String typeen = ServletRequestUtils.getStringParameter(request, "type", "");
		
		if(!CommonUtil.isValidCharacter(typeen)||srcid==-1){
			return null;
		}
		
		List<Tpl> tpllist = this.dataRelationService.decodeInfoPath("infopath", typeen);
		model.addAttribute("data", tpllist);
		model.addAttribute("srcid", srcid);
		model.addAttribute("formtype",typeen);
		return "/sysdatarlt/edit";

	}
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/savetemplate.do")
	@ResponseBody
	public Map<String, String> savetemplate(HttpServletRequest request, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		int srcid = ServletRequestUtils.getIntParameter(request, "srcid", -1);
		String str = ServletRequestUtils.getStringParameter(request, "str", "");
		System.out.println(str);
		FormInfo form = dataConfigService.getFormInfoById(srcid);
		Map<String, String> m = new HashMap<String, String>();
		if (!"".equals(str)) {
			String[] templist = str.split(";");
			System.out.println(templist.length);
			for (String s : templist) {
				String[] tpls = s.split(":");
				m.put(tpls[0], tpls[1]);
			}
		}
        String temp = JSONObject.fromObject(m).toString();
        System.out.println(temp);
		form.setTemplate(JSONObject.fromObject(m).toString());
		try {
			dataConfigService.saveOrUpdateFormInfo(form);
			map.put("status", "1");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("status", "-1");
		}
		return map;
	}
 
	
	@RequestMapping(value = "/checkFieldName.do")
	@ResponseBody
	public Object checkFieldName(HttpServletRequest request, Model model){
		String fieldname = request.getParameter("param");
		String formid = ServletRequestUtils.getStringParameter(request, "formid", "0");
		Map<String,Object> json = new HashMap<String, Object>();
		FormField f = this.dataConfigService.getFormFieldByName(fieldname,formid);
		boolean flag = f==null?true:false;
		if(flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "字段重复,请重新输入！");
		}
		return json;
	}
	
}
