package com.richinfo.datasource.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.module.entity.DataRelation;
import com.richinfo.module.entity.Tpl;
import com.richinfo.module.service.DataRelationService;

@Controller
@RequestMapping(value = "/Sysdatarlt")
public class SysdatarltController {

	private DataRelationService dataRelationService;

	@Autowired
	@Qualifier("DataRelationService")
	public void setDataRelationService(DataRelationService dataRelationService) {
		this.dataRelationService = dataRelationService;
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
		List<DataRelation> drlist = dataRelationService.listAll();

		model.addAttribute("drlist", JSONArray.fromObject(drlist));
		return "/sysdatarlt/lists";
	}

	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model) {
		int srcid = ServletRequestUtils.getIntParameter(request, "srcid", -1);
		String typeen = ServletRequestUtils.getStringParameter(request, "type", "");
		List<Tpl> tpllist = this.dataRelationService.decodeInfoPath("infopath", typeen);
		System.out.println(JSONArray.fromObject(tpllist));
		model.addAttribute("data", tpllist);
		model.addAttribute("srcid", srcid);
		return "/sysdatarlt/edit";

	}

	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public Map<String, String> save(HttpServletRequest request, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		int srcid = ServletRequestUtils.getIntParameter(request, "srcid", -1);
		String str = ServletRequestUtils.getStringParameter(request, "str", "");
		DataRelation dr = dataRelationService.get(srcid);
		Map<String, String> m = new HashMap<String, String>();
		if (!"".equals(str)) {
			String[] templist = str.split(";");
			for (String s : templist) {
				String[] tpls = s.split(":");
				m.put(tpls[0], tpls[1]);
			}
		}
		dr.setTemplate(JSONObject.fromObject(m).toString());
		try {
			dataRelationService.update(dr);
			map.put("status", "1");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("status", "-1");
		}
		return map;
	}

}
