package com.richinfo.common.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.richinfo.common.SystemProperties;

@Controller
@RequestMapping(value = "/fileupload")
public class FileuploadController 
{
	

	@RequestMapping(value = "/uploadify.do")
	public void fileUpload(HttpServletRequest request, HttpServletResponse response){
		String dir = ServletRequestUtils.getStringParameter( request,"region","");
		dir = dir.split(";")[0];
		if(StringUtils.isEmpty(dir)){
			dir = "default";
		}
		String region = SystemProperties.getInstance().getProperty("fileupload"+"."+dir);
		String savePath = SystemProperties.getInstance().getProperty("fileupload.savePath")+region;
		String contextPath = SystemProperties.getInstance().getProperty("fileupload.contextPath")+region;
		
		File uploadify_dir = new File(savePath);
		if(!uploadify_dir.exists())
		{
			uploadify_dir.mkdirs();
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		Map<String,String> map = new HashMap<String, String>();

		
		for(String key:fileMap.keySet())
		{
			MultipartFile file = fileMap.get(key);
			String fileName = file.getOriginalFilename();
			String suffix = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf("."), fileName.length()) : "";
			String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
			String newFileName = uuid + suffix;
			File uploadFile = new File(savePath + newFileName);
			try {
				FileCopyUtils.copy(file.getBytes(), uploadFile);
			} catch (IOException e) {
				
			}
			String url = contextPath+newFileName;
			map.put("imagePx",getImagePx(uploadFile));
			map.put("imageName",region+newFileName);
			map.put("srcName", fileName);
			map.put("url", url);
			break;
		}
		JSONObject json = JSONObject.fromObject(map);
		try {
			Writer out = response.getWriter();
			out.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/deleteImg")
	@ResponseBody
	public Object deleteImg(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> json = new HashMap<String, Object>();
		try{
			String pathimg = ServletRequestUtils.getStringParameter( request,"pathimg","");
			String savePath = SystemProperties.getInstance().getProperty("fileupload.savePath");
//			String datatype = ServletRequestUtils.getStringParameter( request,"region","");
//			"http://i1.mm-img.mmarket.com/";//
			String prefix = SystemProperties.getInstance().getProperty("fileupload.prefix");
			if(pathimg.contains(prefix)){
				json.put("status", "1");
			}else{
				File file = new File(savePath+pathimg);
				if(file.exists())
					file.delete();
				json.put("status", "1");
			}
		}catch (Exception e) {
			// TODO: handle exception
			json.put("status", "0");
		}
		return json;
		
	}
	
	@RequestMapping(value = "/kindeditor.do")
	public void upload(HttpServletRequest request, HttpServletResponse response)
	{
		String dir = ServletRequestUtils.getStringParameter( request,"region","");
		String isAddWaterMask = ServletRequestUtils.getStringParameter( request,"isAddWaterMask","0");
		if(StringUtils.isEmpty(dir)){
			dir = "default";
		}
		String region = SystemProperties.getInstance().getProperty("fileupload"+"."+dir);
		String savePath = SystemProperties.getInstance().getProperty("fileupload.savePath")+region;
		String prefix = SystemProperties.getInstance().getProperty("fileupload.prefix");
		String contextPath = prefix+region;
		
		
		Map<String,Object> json = new HashMap<String, Object>();
		//定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		//最大文件大小
		long maxSize = 1000000;


		if(!ServletFileUpload.isMultipartContent(request)){
			json.put("error", "1");
			json.put("message", "请选择文件。");
			writeMessage(response, json);
			return ;
		}
		//检查目录
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			json.put("error", "1");
			json.put("message", "上传目录不存在。");
			writeMessage(response, json);
			return ;
		}
		//检查目录写权限
		if(!uploadDir.canWrite()){
			json.put("error", "1");
			json.put("message", "上传目录没有写权限。");
			writeMessage(response, json);
			return ;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if(!extMap.containsKey(dirName)){
			json.put("error", "1");
			json.put("message", "目录名不正确。");
			writeMessage(response, json);
			return ;
		}
		//创建文件夹
		savePath += dirName + "/";
		contextPath += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		contextPath += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for(String key:fileMap.keySet())
		{
			MultipartFile item = fileMap.get(key);
			
			String fileName = item.getOriginalFilename();
			
			//检查文件大小
			if(item.getSize() > maxSize){
				json.put("error", "1");
				json.put("message", "上传文件大小超过限制。");
				writeMessage(response, json);
				return ;
			}
			//检查扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
				json.put("error", "1");
				json.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
				writeMessage(response, json);
				return ;
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			try {
				File uploadedFile = new File(savePath, newFileName);
				FileCopyUtils.copy(item.getBytes(), uploadedFile);
				//加水印
				if(!"0".equals(isAddWaterMask)){
					URL url = this.getClass().getClassLoader().getResource("/"); 
					File pfile = new File(url.getPath());  
					File parentFile = new File(pfile.getParent());  
					String webRoot = parentFile.getParent();
					File waterMask = new File(webRoot+File.separator+SystemProperties.getInstance().getProperty("watermark.watermarkImg"));
					Thumbnails.of(uploadedFile.getAbsoluteFile()) 
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(waterMask), 0.8f) 
					.scale(1.0)
					.toFile(uploadedFile.getAbsoluteFile());
				}
			} catch (IOException e) {
				json.put("error", "1");
				json.put("message", "上传文件失败。");
				writeMessage(response, json);
				return ;
			}
			
			json.put("error", 0);
			json.put("url", contextPath + newFileName);
			writeMessage(response, json);
			return ;
			
		}
		
		json.put("error",0);
		writeMessage(response, json);
		return ;
	}
	
	public void writeMessage(HttpServletResponse response ,Object obj){
		JSONObject json = JSONObject.fromObject(obj);
		try {
			response.setCharacterEncoding("UTF-8");
			Writer out = response.getWriter();
			out.write(json.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getImagePx(File file){
		//图片扩展名
		
		if(file!=null && file.exists()){
			String fileType = FilenameUtils.getExtension(file.getName());
			if(!isImage(fileType)){
				return ""; 
			}
			try{
				Image src = javax.imageio.ImageIO.read(file); //构造Image对象
				int wideth=src.getWidth(null); //得到源图宽
				int height=src.getHeight(null); //得到源图长
				return wideth+"x"+height;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return "";
	}
	
	private boolean isImage(String fileType){
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		for(String s:fileTypes)
		{
			if(s.equalsIgnoreCase(fileType)){
				return true;
			}
		}
		
		return false;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fileHandler.do")
	@ResponseBody
	public Object fileHandle(HttpServletRequest request, Model model){
		String dir = ServletRequestUtils.getStringParameter( request,"region","");
		if(StringUtils.isEmpty(dir)){
			dir = "default";
		}
		String region = SystemProperties.getInstance().getProperty("fileupload"+"."+dir);
		String savePath = SystemProperties.getInstance().getProperty("fileupload.savePath")+region;
		String prefix = SystemProperties.getInstance().getProperty("fileupload.prefix");
		String contextPath = prefix+region;

		
		//图片扩展名
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

		String dirName = request.getParameter("dir");
		
		Map<String,String> json = new HashMap<String, String>();
		
		if (dirName != null) {
			if(!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(dirName)){
				json.put("message", "Invalid Directory name.");
				return json;
			}
			savePath += dirName + "/";
			contextPath += dirName + "/";
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
		}
		//根据path参数，设置各路径和URL
		String path = request.getParameter("path") != null ? request.getParameter("path") : "";
		String currentPath = savePath + path;
		String currentUrl = contextPath + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}


		String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";


		if (path.indexOf("..") >= 0) {
			json.put("message", "Access is not allowed.");
			return json;
		}

		if (!"".equals(path) && !path.endsWith("/")) {
			json.put("message", "Parameter is not valid.");
			return json;
		}

		File currentPathFile = new File(currentPath);
		if(!currentPathFile.isDirectory()){
			json.put("message", "Directory does not exist.");
			return json;
		}


		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}

		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}
		
		JSONObject result = new JSONObject();
		result.put("moveup_dir_path", moveupDirPath);
		result.put("current_dir_path", currentDirPath);
		result.put("current_url", currentUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	class NameComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
			}
		}
	}
	 @SuppressWarnings("unchecked")
	 class SizeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
					return 1;
				} else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}
	 
	 @SuppressWarnings("unchecked")
	 class TypeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
			}
		}
	}
}
