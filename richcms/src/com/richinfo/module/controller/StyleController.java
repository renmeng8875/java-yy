package com.richinfo.module.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.utils.AntiXSSUtil;
import com.richinfo.common.utils.ZipUtil;
import com.richinfo.module.entity.Style;
import com.richinfo.module.entity.Tpl;
import com.richinfo.module.service.StyleService;
import com.richinfo.module.service.TplService;

@Controller
@RequestMapping(value = "/Sysstyle")
public class StyleController {

	private StyleService styleService;
	
	private TplService tplService;
	
	@Autowired
	@Qualifier("TplService")
	public void setTplService(TplService tplService) {
		this.tplService = tplService;
	}

	@Autowired
	@Qualifier("StyleService")
	public void setStyleService(StyleService styleService) {
		this.styleService = styleService;
	}

	/**
	 * 登陆后 跳转至后台首页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<Style> stylelist = styleService.listAll();
		model.addAttribute("stylelist", JSONArray.fromObject(stylelist));
		return "sysstyle/lists";
	}

	/**
	 * add edit
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	public String add(HttpServletRequest request, Model model) {
		//appScan
		String styleidTemp = ServletRequestUtils.getStringParameter(request, "styleid", "0");
		if(!StringUtils.isNumeric(styleidTemp)){
			return null;
		}
		// to 新增 修改
		int styleid = ServletRequestUtils.getIntParameter(request, "styleid", -1);
		if (styleid > 0) {// edit
			Style vo = styleService.get(styleid);
			model.addAttribute("style", vo);
		}
		return "sysstyle/add";
	}

	/**
	 * 判断是否存在
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ajaxCheck.do")
	@ResponseBody
	public Map<String,String> ajaxCheck(HttpServletRequest request, Model model) {
		Map<String,String> json = new HashMap<String,String>();
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		int oldid = ServletRequestUtils.getIntParameter(request, "oldid", -1);
		List<Style> lst = styleService.listByParam(key, value, oldid);
		if (lst.size() > 0)
			json.put("status", "0");
		else
			json.put("status", "1");
		return json;
	}

	
	/**
	* 方法说明:判断标识是否重复  
	* @param request
	* @param model
	* @return Object
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-6-6
	 */
	@RequestMapping(value = "/checkStyle.do")
	@ResponseBody
	public Object checkStyle(HttpServletRequest request, Model model) {
		Map<String,String> json = new HashMap<String,String>();
		String style = ServletRequestUtils.getStringParameter(request,"param", "");
		int styleid=ServletRequestUtils.getIntParameter(request, "styleid", -1);
		List<Style> list = styleService.listByParam("style", style, styleid);
		if(list!=null&&list.size()>0)
		{
			json.put("status", "n");
			json.put("info", "风格标识重复,请重新输入！");
		}else{
			json.put("status", "y");
		}	
		return json;
	}
	
	/**
	* 方法说明:判断风格名称是否重复  
	* @param request
	* @param model
	* @return Object
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-6-6
	 */
	@RequestMapping(value = "/checkStyleName.do")
	@ResponseBody
	public Object checkStyleName(HttpServletRequest request, Model model) {
		Map<String,String> json = new HashMap<String,String>();
		String stylename = ServletRequestUtils.getStringParameter(request,"param", "");
		int styleid=ServletRequestUtils.getIntParameter(request, "styleid", -1);
		List<Style> list = styleService.listByParam("stylename", stylename,styleid);
		if(list!=null&&list.size()>0)
		{
			json.put("status", "n");
			json.put("info", "风格名称重复,请重新输入！");
		}else{
			json.put("status", "y");
		}	
		return json;
	}
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Map<String, String> del(HttpServletRequest request, Model model) {
		Map<String, String> json = new HashMap<String, String>();
		String idstr = request.getParameter("idstr");
		String[] ids = idstr.split(",");
		for (String id : ids) {
			styleService.delete(Integer.valueOf(id));
		}
		json.put("status", "0");
		return json;
	}
	/**
	 * 
	 * @param request
	 * @param model
	 * @param file
	 * @param style
	 * @return
	 */
	@RequestMapping(value = "/upload.do")
	public String upload(HttpServletRequest request, Model model,@RequestParam(value = "file", required = false) MultipartFile file,@ModelAttribute Style style) {
		//由于表单multipart/form-data只能在此每个值重新设置
		style.setStyle(AntiXSSUtil.antiXSSNEW(style.getStyle()));
		style.setStylename(AntiXSSUtil.antiXSSNEW(style.getStylename()));
		style.setAuthor(AntiXSSUtil.antiXSSNEW(style.getAuthor()));
		style.setVersion(AntiXSSUtil.antiXSSNEW(style.getVersion()));
		
		String styleMark=style!=null?AntiXSSUtil.antiXSSNEW(style.getStyle()):"";
		String tplpath = SystemProperties.getInstance().getProperty("fileupload.stylePath");
		String savePath =SystemProperties.getInstance().getProperty("fileupload.savePath");
		String fileName = file.getOriginalFilename();
		File targetFile = new File(savePath+tplpath, fileName);
		File configxml = null;
		// 保存
		try {
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			file.transferTo(targetFile);
			ZipUtil.unZipFiles(targetFile,savePath+tplpath+File.separator);
			targetFile.delete();
			configxml = new File(savePath+tplpath+File.separator+styleMark+File.separator+"config.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		style.setPath(savePath+tplpath+"/"+styleMark);
		styleService.merge(style);
		int styleid = style.getStyleid();
		if(styleid == 0){
			List<Style> styleList = styleService.listByParam("style", styleMark, 0);
			if(styleList!=null&&styleList.size()>0)
			{	
				styleid= styleList.get(0).getStyleid();
			}
		}
		if(configxml.exists())
		{
			tplService.deleteByStyleId(styleid);
			List<Tpl> tplList = loadTemplates(configxml,styleid);
			for(Tpl t:tplList)
			{
				tplService.save(t);
			}
		}	
		
		model.addAttribute("message", "保存样式成功");
		model.addAttribute("url", "/Sysstyle/lists.do");
		return Constants.FORWARDURL;
	}
	
	@SuppressWarnings("unchecked")
	private List<Tpl> loadTemplates(File configFilePath,int styleId)
	{
		List<Tpl> tplList = new ArrayList<Tpl>();
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(configFilePath);
		} catch (Exception e) {
		}
		Element root = document.getRootElement();
		List<Element> moduleList = root.elements("template");
		for(Element element:moduleList)
		{
			Tpl t = new Tpl();
			Map<String,String> map = new HashMap<String,String>();
			List<Element> propertyList = element.elements("property");
			for(Element property:propertyList)
			{
				map.put(property.attributeValue("name"), property.attributeValue("value"));
			}
			t.setStyleId(styleId);
			t.setTyPeen(map.get("typeen"));
			t.setTypeName(map.get("typename"));
			t.setListPath(map.get("listpath"));
			t.setInfoPath(map.get("infopath"));
			t.setIndexPath(map.get("indexpath"));
			tplList.add(t);
		}
		return tplList;
	}

}
