package com.richinfo.playersknockout.controller;

import java.util.ArrayList;
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

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.playersknockout.entity.GamePrizes;
import com.richinfo.playersknockout.entity.PlayersKnockout;
import com.richinfo.playersknockout.service.GamePrizesService;
import com.richinfo.playersknockout.service.PlayersKnockoutService;

@Controller
@RequestMapping(value = "/gamePrizes")
public class GamePrizesController {

	private GamePrizesService gamePrizesService;
 
	private PlayersKnockoutService playersKnockoutService;
	
	@Autowired
	@Qualifier("GamePrizesService")
	public void setGamePrizesService(GamePrizesService gamePrizesService) {
		this.gamePrizesService = gamePrizesService;
	}
 
	@Autowired
	@Qualifier("PlayersKnockoutService")
	public void setWarcraftService(PlayersKnockoutService warcraftService) 
	{
		this.playersKnockoutService = warcraftService;
	}
	
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) {
		
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid", 0); 
		List<GamePrizes> prizesList = gamePrizesService.findPrizesList(pkid);
		List<GamePrizes> gpList = new ArrayList<GamePrizes>();
		for(GamePrizes gp :prizesList){
			PlayersKnockout pk = playersKnockoutService.get(gp.getPkid());
			gp.setPkname(pk.getPkName());
			gpList.add(gp);
		}
		 
		model.addAttribute("prizesList", JSONArray.fromObject(gpList));
		model.addAttribute("pkid", pkid);
		return "/gameplayers/prizeslists";
	}

	@RequestMapping(value = "/add.do")
	public String add(HttpServletRequest request, Model model,@ModelAttribute GamePrizes gameprizes) {
		String method = request.getMethod();
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid", 0);
		//to 新增 修改
		if("GET".equals(method)){
			PlayersKnockout pk = playersKnockoutService.get(pkid);
			model.addAttribute("pkname", pk.getPkName());
			model.addAttribute("pkid", pkid);
			return "/gameplayers/prizesadd";
		}
		//appscan
		if(!CommonUtil.isValidImageType(gameprizes.getPrizesimg())){
			return null;
		}
		if(!CommonUtil.isValidCharacter(gameprizes.getPkname())){
			return null;
		}
		gameprizes.setCtime(DateTimeUtil.getTimeStamp());
		gamePrizesService.save(gameprizes);
		model.addAttribute("message", "奖品保存成功");
		model.addAttribute("url","/gamePrizes/lists.do?pkid="+pkid);
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model, @ModelAttribute GamePrizes gameprizes) {
		String method = request.getMethod();
		int prizesid = ServletRequestUtils.getIntParameter(request, "prizesid", 0);
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid", 0);
		//to 新增 修改
		if("GET".equals(method)){
			GamePrizes gp = gamePrizesService.get(prizesid);
			PlayersKnockout pk = playersKnockoutService.get(pkid);
			model.addAttribute("pkname", pk.getPkName());
			model.addAttribute("pkid", pkid);
			model.addAttribute("gameprizes", gp);
			return "/gameplayers/prizesedit";
		}
		//appscan
		if(!CommonUtil.isValidImageType(gameprizes.getPrizesimg())){
			return null;
		}
		if(!CommonUtil.isValidCharacter(gameprizes.getPkname())){
			return null;
		}
		gamePrizesService.update(gameprizes);
		model.addAttribute("message", "奖品修改成功");
		model.addAttribute("url","/gamePrizes/lists.do?pkid="+pkid);
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Map<String, String> del(HttpServletRequest request, Model model) {
		Map<String, String> json = new HashMap<String, String>();
		String idstr = request.getParameter("idstr");
		String[] ids = idstr.split(",");
		for (String id : ids) {
			gamePrizesService.delete(Integer.valueOf(id));
		}
		json.put("status", "0");
		return json;
	}
	
	public static void main(String[] args) {
		String score = "用户名+,+大神介绍+,+玩家的加赞数+,+视频id（可为空）+,+电话（可为空）+,+QQ（可为空）+,+微博（可为空）+, , ";
		System.out.println(score.split(",").length);
	}
}
