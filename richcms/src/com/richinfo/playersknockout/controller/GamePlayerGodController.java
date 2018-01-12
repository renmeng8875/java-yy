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
import com.richinfo.playersknockout.entity.PlayersKnockout;
import com.richinfo.playersknockout.entity.Video;
import com.richinfo.playersknockout.service.GamePlayerService;
import com.richinfo.playersknockout.service.PlayersKnockoutService;
import com.richinfo.playersknockout.service.VideoService;

@Controller
@RequestMapping(value = "/gameGod")
public class GamePlayerGodController {

	private GamePlayerService gamePlayerService;
	private PlayersKnockoutService playersKnockoutService;
    private VideoService videoService;
	
	@Autowired
	@Qualifier("videoService")
	public void setVideoService(VideoService videoService) 
	{
		this.videoService = videoService;
	}

	static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"  
            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)"; 
	static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE); 
	
	@Autowired
	@Qualifier("GamePlayerService")
	public void setGamePlayerService(GamePlayerService gamePlayerService) {
		this.gamePlayerService = gamePlayerService;
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
		player.setIsgod("1");
		List<GamePlayer> playersList = gamePlayerService.listPlayerByObj(player);
		int i = 1;
		for(GamePlayer gp : playersList){
			gp.setRank(i);
			CommonUtil.escapeHtmlForObject(gp);
			i++;
		}
		model.addAttribute("playersList", JSONArray.fromObject(playersList));
		model.addAttribute("pkid", pkid);
		return "/gameplayers/godlists";
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
//			用户名+&&+大神介绍+&&+玩家的加赞数+&&+视频id（可为空）+&&+电话（可为空）+&&+QQ（可为空）+&&+微博（可为空）+&&+微信（可为空）+ &&+ 图片地址（可为空）+英文分号；
			players = ServletRequestUtils.getStringParameter(request, "players");
			String pkid = ServletRequestUtils.getStringParameter(request, "pkid");
			pkid = !"".equals(pkid)?pkid:"0";
			String[] playerlist = players.split("##");
			if (playerlist.length > 0) {
				for (String player : playerlist) {
					String[] obj = player.split("&&");
					if(obj.length<10){
						errorplayers.add(obj[0]);
				    	continue;
					}
					if("".equals(obj[0].trim())){
						errorplayers.add(obj[0]);
				    	continue;
					}
					GamePlayer gp = new GamePlayer();
					gp.setPlayername(obj[0].trim().replaceAll("\n", ""));
				    PlayersKnockout pk = playersKnockoutService.getPkByName(obj[1].trim());
				    gp.setPkname(pk==null?obj[1].trim():pk.getPkName());
					gp.setPk(pk==null?"":pk.getPk());
					gp.setPkid(pk==null?"0":pk.getPkid()+"");
					gp.setPlayerdesc(obj[2]);
				    if(StringUtils.isNumeric(obj[3].trim().toString())){
				    	if(!"".equals(obj[3].trim())){
				    		gp.setPraisenum(Long.parseLong(obj[3].trim().toString()));
				    	}else{
				    		gp.setPraisenum(0L);
				    	}
				    }else{
				    	errorplayers.add(obj[0]);
				    	continue;
				    }
				    if(!"".equals(obj[4].trim().toString())){
				    	if(StringUtils.isNumeric(obj[4].trim().toString())){
				    		gp.setVideoid(obj[4].trim().toString());
				    	}else{
				    		errorplayers.add(obj[0]);
					    	continue;
				    	}
				    }else{
				    	gp.setVideoid(obj[4].trim().toString());
				    }
					gp.setPhonenumber(obj[5].trim().toString());
					gp.setQq(obj[6]);
					gp.setWeibo(obj[7]);
					gp.setWeixin(obj[8]);
					if("".equals(obj[9].trim().toString())){
						gp.setImgpath("default/god160x160.jpg");
					}else{
						gp.setImgpath(obj[9]);
					}
					gp.setCtime(DateTimeUtil.getTimeStamp());
					gp.setIsgod("1");
					try {
						GamePlayer p = gamePlayerService.exists(gp);
						if (p!=null) {
							gp.setPlayerid(p.getPlayerid());
							gamePlayerService.merge(gp);
						} else {
							gamePlayerService.save(gp);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						errorplayers.add((obj[0]));
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
		GamePlayer gp = gamePlayerService.findGamePlayer(playerid);
//		List<Video> list = videoService.listAll();
//		model.addAttribute("videoList", list);
		CommonUtil.escapeHtmlForObject(gp);
		model.addAttribute("gameplayer", gp);
		return "/gameplayers/editGod";
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
		int pkid = ServletRequestUtils.getIntParameter(request, "pkid", -1);
		PlayersKnockout pk = null;
		//appScan
		if(CommonUtil.hasInValidCharacter(player)){
			return null;
		}
		if(pkid>0){
		    pk = playersKnockoutService.get(pkid);
			player.setPk(pk==null?"":pk.getPk());
			player.setPkname(pk==null?player.getPkname():pk.getPkName());
		}else{
			pk = playersKnockoutService.getPkByName(player.getPkname().trim());
			player.setPkid(pk==null?"0":pk.getPkid().toString());
			player.setPk(pk==null?"":pk.getPk());
		}
		if(player.getImgpath()==null){
			player.setImgpath("default/god160x160.jpg");
		}
		player.setPlayerid(playerid);
		player.setIsgod("1");
		player.setCtime(DateTimeUtil.getTimeStamp());
		GamePlayer p = gamePlayerService.exists(player);
		if(p!=null&&!"0".equals(player.getPkid())&&!p.getPlayerid().equals(player.getPlayerid())){
		    player.setScore(p.getScore());
		    player.setMeasurement(p.getMeasurement());
		    player.setPrizesid(p.getPrizesid());
		    gamePlayerService.delete(p.getPlayerid());
		    
		    CommonUtil.unEscapeHtmlForObject(player);
		    gamePlayerService.update(player);
		}else{
			CommonUtil.unEscapeHtmlForObject(player);
			gamePlayerService.merge(player);
		}
		model.addAttribute("message", "保存成功");
		model.addAttribute("url", "/gameGod/lists.do");
		return Constants.FORWARDURL;
	}
	
	
	@RequestMapping(value="/checkVideo.do")
	@ResponseBody
	public Object checkVideo(HttpServletRequest request, Model model)
	{
		int videoid = ServletRequestUtils.getIntParameter(request, "videoid", 0);
		Video p =  this.videoService.get(videoid);
		Map<String,String> jsonMap = new HashMap<String, String>();
		if(p==null){
			jsonMap.put("status", "n");
			jsonMap.put("info", "改视频不存在，请重新输入");
			return jsonMap;
		}
		jsonMap.put("status", "y");
		return jsonMap;
	}

	
	public static void main(String[] args) {
		String score = "张三&&大神介绍&&10001&&8901&&18688447523&&374039103&&微博&&微信&&a.jpg##";
		System.out.println(score.split(",").length);
	}
}
