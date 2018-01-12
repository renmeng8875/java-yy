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
import com.richinfo.module.entity.DataRelation;
import com.richinfo.module.entity.Search;
import com.richinfo.module.service.DataRelationService;
import com.richinfo.module.service.SearchService;

@Controller
@RequestMapping(value = "/Syssearch")
public class SearchController {

	private SearchService searchService;
	private DataRelationService dataRelationService;
	

	@Autowired
	@Qualifier("SearchService")
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	
	@Autowired
	@Qualifier("DataRelationService")
	public void setDataRelationService(DataRelationService dataRelationService) {
		this.dataRelationService = dataRelationService;
	}

	/**
	 * 登陆后 跳转至后台首页
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<Search> searchlist = searchService.listAll();
        model.addAttribute("searchlist", JSONArray.fromObject(searchlist));
		return "/syssearch/lists";
	}

	/**
	 * add edit
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add.do")
    public String add(HttpServletRequest request, Model model,@ModelAttribute Search search) {
		String method = request.getMethod();
		//to 新增 修改
		if("GET".equals(method)){
			int sid = ServletRequestUtils.getIntParameter(request, "sid", -1);
			if(sid>0){//edit
				Search vo = searchService.get(sid);
				model.addAttribute("data", vo);
			}
			List<DataRelation> relationlist = dataRelationService.listAll();
			model.addAttribute("sourcelist",relationlist);
			return "syssearch/add";
		}
		//保存
		if(search.getSid()>0){
			searchService.update(search);
			model.addAttribute("message", "修改搜索成功");
			model.addAttribute("url","/Syssearch/lists.do");
		}else{
			searchService.save(search);
			model.addAttribute("message", "样式搜索成功");
			model.addAttribute("url","/Syssearch/lists.do");
		}
		
		return Constants.FORWARDURL;
    }
	
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Map<String,String> del(HttpServletRequest request, Model model){
		Map<String,String> json = new HashMap<String,String>();
		String idstr =request.getParameter("idstr");
		String[] ids = idstr.split(",");
		for(String id:ids){
			searchService.delete(Integer.valueOf(id));
		}
		json.put("status", "0");
		return json;
	}

	

}
