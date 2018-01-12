package com.richinfo.module.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;


import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.richinfo.module.entity.ContentAd;
import com.richinfo.module.service.ContentAdService;

@Controller
@RequestMapping(value = "/ContentAd")
public class ContentAdController {

	private ContentAdService contentAdService;
	

	@Autowired
	@Qualifier("ContentAdService")
	public void setContentAdService(ContentAdService contentAdService) {
		this.contentAdService = contentAdService;
	}

	/**
	 * 登陆后 跳转至后台首页
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<ContentAd> contentadlist = contentAdService.listAll();
        model.addAttribute("contentadlist", JSONArray.fromObject(contentadlist));
		return "/contentad/lists";
	}

	 

	

}
