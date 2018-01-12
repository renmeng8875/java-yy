package com.richinfo.playersknockout.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.playersknockout.entity.Video;
import com.richinfo.playersknockout.service.VideoService;
import com.richinfo.privilege.entity.User;

@Controller
@RequestMapping(value = "/video")
public class VideoController 
{
	private VideoService videoService;
	
	@Autowired
	@Qualifier("videoService")
	public void setVideoService(VideoService videoService) 
	{
		this.videoService = videoService;
	}

	@RequestMapping(value = "/list.do")
	public String listAll(HttpServletRequest request, Model model) 
	{
		List<Video> list = videoService.listAll();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"utime"});
		Object jsonArray = JSONArray.fromObject(list,config);
		model.addAttribute("videoList", jsonArray);
        return "playersknockout/videoList";
	}
	
	@RequestMapping(value = "/add.do")
	public String addVideo(HttpServletRequest request, Model model) 
	{
		String methodType = request.getMethod();
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		if("GET".equals(methodType))
		{
			String batch = ServletRequestUtils.getStringParameter(request, "batch","-1");
			model.addAttribute("user", user);
			model.addAttribute("batch", batch);
			return "playersknockout/videoUpload";
		}	
		String[] videoUrls = ServletRequestUtils.getStringParameters(request, "videoUrl");
		String vname = ServletRequestUtils.getStringParameter(request, "vname","");
		String introduce = ServletRequestUtils.getStringParameter(request, "introduce","");
		String capture = ServletRequestUtils.getStringParameter(request, "capture","");
		int sortNum = ServletRequestUtils.getIntParameter(request, "sortNum", 99);
		String sortNumTmp = ServletRequestUtils.getStringParameter(request, "sortNum", "");
		//appScan
		if(CommonUtil.hasInValidCharacters(capture,vname,introduce,sortNumTmp)){
			return null;
		}
		
		for(String videoUrl:videoUrls)
		{
			//appScan
			if(CommonUtil.hasInValidCharacters(videoUrl)){
				return null;
			}
			String[] ss = videoUrl.split(",");
			Video v = new Video();
			v.setVideoUrl(ss[0]);
			v.setUploader(user.getNickName());
			v.setLastModifier(user.getNickName());
			if(StringUtils.isEmpty(vname))
			{
				vname = ss[1];
			}
			if(StringUtils.isEmpty(capture))
			{
				capture = "default/videoDefault.jpg";
			}
			v.setCapture(capture);
			v.setSortNum(sortNum);
			v.setVname(vname);
			vname = null;
			v.setIntroduce(introduce);
			v.setUtime(new Date());
			videoService.save(v);
		}
		model.addAttribute("url", "/video/list.do");
		model.addAttribute("message", "添加成功！");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/modify.do")
	public String modifyVideo(HttpServletRequest request, Model model) 
	{
		String methodType = request.getMethod();
		String videoTemp = ServletRequestUtils.getStringParameter(request, "videoId","");
		//appScan
		if(CommonUtil.hasInValidCharacters(videoTemp)){
			return null;
		}
		int videoId = ServletRequestUtils.getIntParameter(request, "videoId",-1);
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		if("GET".equals(methodType))
		{
			Video v = videoService.get(videoId);
			model.addAttribute("video", v);
			String batch = ServletRequestUtils.getStringParameter(request, "batch","-1");
			model.addAttribute("batch",batch);
			return "playersknockout/videoUpload";
		}	
		String videoUrl = ServletRequestUtils.getStringParameter(request, "videoUrl","");
		String capture = ServletRequestUtils.getStringParameter(request, "capture","");
		int sortNum = ServletRequestUtils.getIntParameter(request, "sortNum", 99);
		String vname = ServletRequestUtils.getStringParameter(request, "vname","");
		String introduce = ServletRequestUtils.getStringParameter(request, "introduce","");
		String sortNumTmp = ServletRequestUtils.getStringParameter(request, "sortNum", "");
		//appScan
		if(CommonUtil.hasInValidCharacters(videoUrl,capture,vname,introduce,sortNumTmp)){
			return null;
		}
		
		Video v = videoService.get(videoId);
		v.setLastModifier(user.getNickName());
		v.setIntroduce(introduce);
		v.setSortNum(sortNum);
		if(StringUtils.isEmpty(capture))
		{
			capture = "default/videoDefault.jpg";
		}
		v.setCapture(capture);
		if(videoUrl.contains(","))
		{
			String[] ss = videoUrl.split(",");
			videoUrl = ss[0];
			if(StringUtils.isEmpty(vname))
			{
				vname = ss[1];
			}
		}
		v.setVideoUrl(videoUrl);
		v.setVname(vname);
		v.setUtime(new Date());
		videoService.update(v);
		model.addAttribute("url", "/video/list.do");
		model.addAttribute("message", "修改成功！");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public Object deleteVideo(HttpServletRequest request, Model model) 
	{
		String idstr = ServletRequestUtils.getStringParameter(request, "idstr","");
		String[] ids = idstr.split(",");
		String savepath = SystemProperties.getInstance().getProperty("fileupload.savePath");
		String contextpath = SystemProperties.getInstance().getProperty("fileupload.contextPath");
		for(String id:ids)
		{
			Video v = videoService.get(Integer.valueOf(id));
			String url = v.getVideoUrl();
			url = url.replace(contextpath, savepath);
			File destFile = new File(url);
			if(destFile!=null&&destFile.exists())
			{
				destFile.delete();
			}
			videoService.delete(Integer.valueOf(id));
			
		}
		Map<String,String> json = new HashMap<String, String>();
		json.put("status", "0");
		return json;
	}
}
