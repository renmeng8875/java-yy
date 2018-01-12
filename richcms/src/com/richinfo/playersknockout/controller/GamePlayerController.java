package com.richinfo.playersknockout.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.playersknockout.entity.GamePlayer;
import com.richinfo.playersknockout.entity.GamePrizes;
import com.richinfo.playersknockout.entity.PlayersKnockout;
import com.richinfo.playersknockout.service.GamePlayerService;
import com.richinfo.playersknockout.service.GamePrizesService;
import com.richinfo.playersknockout.service.PlayersKnockoutService;

@Controller
@RequestMapping(value = "/gamePlayers")
public class GamePlayerController {

	private GamePlayerService gamePlayerService;

	private GamePrizesService gamePrizesService;
	
	private PlayersKnockoutService playersKnockoutService;
	
	static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"  
            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)"; 
	static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE); 
	
	@Autowired
	@Qualifier("GamePlayerService")
	public void setGamePlayerService(GamePlayerService gamePlayerService) {
		this.gamePlayerService = gamePlayerService;
	}
	
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
	public String lists(HttpServletRequest request, Model model,@ModelAttribute GamePlayer player) {
		String  pkid = ServletRequestUtils.getStringParameter(request, "pkid","0");
		player.setPkid(pkid);
		List<GamePlayer> playersList = gamePlayerService.listPlayerByObj(player);
		int i = 1;
		for(GamePlayer gp : playersList){
			gp.setRank(i);
			GamePrizes prizes = gamePrizesService.get(gp.getPrizesid());
			gp.setPrizesname(prizes==null?"":prizes.getPrizesname());
			if("".equals(gp.getPkname())||gp.getPkname()==null){
				PlayersKnockout pk = playersKnockoutService.get(Integer.valueOf(gp.getPkid()==null?"0":gp.getPkid()));
				gp.setPkname(pk==null?"":pk.getPkName());
			}
			i++;
		}
		 
		
		model.addAttribute("playersList", JSONArray.fromObject(playersList));
		model.addAttribute("pkid", pkid);
		return "/gameplayers/lists";
	}

	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	public Object del(HttpServletRequest request, Model model) {
		Map<String, String> json = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		String[] ids = idstr.split(",");
		for (String id : ids) {
			gamePlayerService.delete(Integer.valueOf(id));
		}
		json.put("status", "0");
		return json;
	}

	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public Object add(HttpServletRequest request, Model model) {
		Map<String, Object> json = new HashMap<String, Object>();
		List<String> errorplayers = new ArrayList<String>();
		String players = "";
		try {
			players = ServletRequestUtils.getStringParameter(request, "players");
			String pkid = ServletRequestUtils.getStringParameter(request, "pkid");
			pkid = !"".equals(pkid)?pkid:"0";
			String[] playerlist = players.split(";");
			if (playerlist.length > 0) {
				for (String player : playerlist) {
					// 张三,13800138000,849278分;
					String[] obj = player.split(",");
					GamePlayer gp = new GamePlayer();
					if("".equals(obj[0])||"\n".equals(obj[0])||"\n\r".equals(obj[0])){
						errorplayers.add(obj[1]);
						continue;
					}
					gp.setPlayername(obj[0].trim());
					if(!"".equals(obj[1].trim())&&!CommonUtil.isMobileNO(obj[1].trim())){
						errorplayers.add("".equals(obj[0])?obj[1]:obj[0]);
						continue;
					}
					gp.setPhonenumber(obj[1].trim());
					String score = obj[2].substring(0, obj[2].length() - 1).trim();
					if("".equals(score)||!StringUtils.isNumeric(score)){
						errorplayers.add("".equals(obj[0])?obj[1]:obj[0]);
						continue;
					}
					gp.setScore(score);
					gp.setMeasurement(obj[2].substring(obj[2].length() - 1));
					if(!"".equals(obj[3].trim())&&StringUtils.isNumeric(obj[3].trim())){
						gp.setPrizesid(Integer.valueOf(obj[3].trim()));
					}
					gp.setCtime(DateTimeUtil.getTimeStamp());
					PlayersKnockout pk = playersKnockoutService.get(Integer.valueOf(pkid));
					gp.setPkname(pk==null?"":pk.getPkName());
					gp.setPk(pk==null?"":pk.getPk());
					gp.setPkid(pkid);
					try {
						GamePlayer p = gamePlayerService.exists(gp);
						if (p!=null) {
							gp.setPlayerid(p.getPlayerid());
							gp.setIsgod(p.getIsgod());
							gp.setWeixin(p.getWeixin());
							gp.setWeibo(p.getWeibo());
							gp.setQq(p.getQq());
							gp.setPraisenum(p.getPraisenum());
							gp.setImgpath(p.getImgpath());
							gp.setPlayerdesc(p.getPlayerdesc());
							gp.setVideoid(p.getVideoid());
							gamePlayerService.merge(gp);
						} else {
							gamePlayerService.save(gp);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						errorplayers.add("".equals(obj[0])?obj[1]:obj[0]);
				   }
				}
			}
		} catch (ServletRequestBindingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.put("status", "0");
		json.put("errorplayers", JSONArray.fromObject(errorplayers));
		return json;
	}

	@RequestMapping(value = "/toEdit.do")
	public String toEdit(HttpServletRequest request, Model model) {
		int  playerid = ServletRequestUtils.getIntParameter(request, "playerid",0);
		int  pkid = ServletRequestUtils.getIntParameter(request, "pkid",0);
		GamePlayer gp = gamePlayerService.findGamePlayer(playerid);
//		List<PlayersKnockout> warcraftList = playersKnockoutService.listBySort();
		if(pkid==0){
			pkid = Integer.valueOf(gp.getPkid());
		}
		List<GamePrizes> prizeslist = gamePrizesService.findPrizesList(pkid);
//		model.addAttribute("warcraftList", warcraftList);
		
		model.addAttribute("prizeslist", prizeslist);
		model.addAttribute("gameplayer", gp);
		return "/gameplayers/editPlayer";
	}
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model,@ModelAttribute GamePlayer player)
			throws ServletRequestBindingException {
		int playerid = ServletRequestUtils.getIntParameter(request, "playerid", -1);
//		int pkid = player.getPkid()==null?0:Integer.valueOf(player.getPkid());
//		PlayersKnockout pk = playersKnockoutService.get(pkid);
//		player.setPkname(pk==null?"":pk.getPkName());
//		player.setPk(pk==null?"":pk.getPk());
		player.setPlayerid(playerid);
		//appScan
		if(CommonUtil.hasInValidCharacter(player)){
			return null;
		}
		
		
		GamePrizes prizes = gamePrizesService.get(Integer.valueOf(player.getPrizesid()));
		player.setPrizesname(prizes==null?"":prizes.getPrizesname());
		player.setCtime(DateTimeUtil.getTimeStamp());
		gamePlayerService.update(player);
		
		model.addAttribute("message", "保存成功");
		model.addAttribute("url", "/gamePlayers/lists.do?pkid="+player.getPkid());
		return Constants.FORWARDURL;
	}

	@RequestMapping(value="/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String playername = ServletRequestUtils.getStringParameter(request,"param","");
		int playerid = ServletRequestUtils.getIntParameter(request, "playerid", -1);
		String pkid = ServletRequestUtils.getStringParameter(request, "pkid", "");
		GamePlayer player = new GamePlayer();
		player.setPkid(pkid);
		player.setPlayerid(playerid);
		player.setPlayername(playername);
		GamePlayer p = this.gamePlayerService.checkIsExists(player);
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(p!=null){
			jsonMap.put("status", "n");
			jsonMap.put("info", "已存在该用户，请重新输入");
			return jsonMap;
		}
		jsonMap.put("status", "y");
		return jsonMap;
	}
	 
	public static void main(String[] args) {
		String score = "849278分";
		System.out.println(score.substring(0, score.length() - 1));
	}
}
