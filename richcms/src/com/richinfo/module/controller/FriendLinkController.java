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
import com.richinfo.common.SystemProperties;
import com.richinfo.module.entity.FriendLink;
import com.richinfo.module.service.FriendLinkService;
import com.richinfo.playersknockout.service.PlayersKnockoutService;

@Controller
@RequestMapping(value = "/Links")
public class FriendLinkController {

	private FriendLinkService friendLinkService;
	private PlayersKnockoutService playersKnockoutService;

	@Autowired
	@Qualifier("FriendLinkService")
	public void setStyleService(FriendLinkService friendLinkService) {
		this.friendLinkService = friendLinkService;
	}
	
	@Autowired
	@Qualifier("PlayersKnockoutService")
	public void setPlayersKnockoutService(
			PlayersKnockoutService playersKnockoutService) {
		this.playersKnockoutService = playersKnockoutService;
	}

	/**
	 * 登陆后 跳转至后台首页
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		List<FriendLink> linkslist = friendLinkService.listAll();
        model.addAttribute("linkslist", JSONArray.fromObject(linkslist));
		return "/links/lists";
	}

	 
	/**
	 * add edit
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add.do")
    public String add(HttpServletRequest request, Model model,@ModelAttribute FriendLink link) {
		String method = request.getMethod();
		//to 新增 修改
		if("GET".equals(method)){
			return "links/add";
		}
		String logo=ServletRequestUtils.getStringParameter(request, "logo", "");
		String imagePx=ServletRequestUtils.getStringParameter(request, "imagePx", "");
		link.setLogo(playersKnockoutService.getImageJsonFormat("", logo, imagePx));
		//保存
		friendLinkService.save(link);
		model.addAttribute("message", "友情鏈接保存成功");
		model.addAttribute("url","/Links/lists.do");
		return Constants.FORWARDURL;
    }
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model,@ModelAttribute FriendLink link){
		String method = request.getMethod();
		//to 新增 修改
		if("GET".equals(method)){
			int linkid = ServletRequestUtils.getIntParameter(request, "linkid", -1);
			FriendLink vo = friendLinkService.get(linkid);
			model.addAttribute("linkinfo", vo);
			model.addAttribute("storageUrl", SystemProperties.getInstance().getProperty("fileupload.contextPath"));
			return "links/edit";
		}
		String logo=ServletRequestUtils.getStringParameter(request, "logo", "");
		String imagePx=ServletRequestUtils.getStringParameter(request, "imagePx", "");
		link.setLogo(playersKnockoutService.getImageJsonFormat("", logo,imagePx));
		//保存
		friendLinkService.update(link);
		model.addAttribute("message", "修改鏈接成功");
		model.addAttribute("url","/Links/lists.do");
		
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Map<String,String> del(HttpServletRequest request, Model model){
		Map<String,String> json = new HashMap<String,String>();
		String idstr =request.getParameter("idstr");
		String[] ids = idstr.split(",");
		for(String id:ids){
			friendLinkService.delete(Integer.valueOf(id));
		}
		json.put("status", "0");
		return json;
	}

	@RequestMapping(value = "/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String pk = request.getParameter("param");
		int pkid = ServletRequestUtils.getIntParameter(request, "linkid", -1);
		Map<String,Object> json = new HashMap<String, Object>();
		boolean flag = friendLinkService.nameExists(pk,pkid);
		if(!flag){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "名称重复,请重新输入！");
		}
		return json;
	}
	

}
