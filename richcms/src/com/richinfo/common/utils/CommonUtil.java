package com.richinfo.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.util.HtmlUtils;

public final class CommonUtil 
{
	private static final Pattern regxIp = Pattern.compile("((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)");
	
	private static final Pattern regxIpSection = Pattern.compile("((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)-((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)");
	
	public static boolean isWap20Ua(String useragent) 
	{
		useragent = StringUtils.isEmpty(useragent) ? "" : useragent.toLowerCase();

		if (((useragent.indexOf("msie") > -1) && 
			(useragent.indexOf("windows") > -1))
			|| (useragent.indexOf("iphone") > -1)
			|| (useragent.indexOf("ipad") > -1)
			|| (useragent.indexOf("android") > -1)
			|| (useragent.indexOf("firefox") > -1)
			|| (useragent.indexOf("chrome") > -1)
			|| (useragent.indexOf("opera") > -1)) 
		{
			return true;
		}
		return false;
	}

	public static boolean isProtocol(String verStr) 
	{
		if (!StringUtils.isEmpty(verStr)) 
		{
			verStr = verStr.toLowerCase();
			if ((verStr.indexOf("http:") > -1)|| (verStr.indexOf("https:") > -1)||(verStr.indexOf("ftp:") > -1)|| (verStr.indexOf("mailto:") > -1)) 
			{
				return true;
			}
			if (verStr.length() > 2) 
			{
				if ("//".equals(verStr.substring(0, 2))) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getCookie(HttpServletRequest request, String cookieKey) 
	{
		Map<String, String> map = getCookies(request);
		return (String) map.get(cookieKey.toUpperCase());
	}

	public static Map<String, String> getCookies(HttpServletRequest request) 
	{
		Map<String, String> map = new HashMap<String, String>();
		String cookieStr = request.getHeader("cookie");
		if (!StringUtils.isEmpty(cookieStr)) 
		{
			String[] cookies = cookieStr.split(";");
			for (String cookie : cookies) 
			{
				cookie = StringUtils.isEmpty(cookie) ? "" : cookie.trim();
				int nameIndex = cookie.indexOf("=");
				if (nameIndex != -1) 
				{
					String cookieName = cookie.substring(0, nameIndex);
					String cookieValue = cookie.substring(nameIndex + 1);
					map.put(cookieName.toUpperCase(), cookieValue);
				}
			}
		}
		return map;
	}

	public static boolean isContain(String src, String dest)
	{
		if (StringUtils.isEmpty(src)) 
		{
			return false;
		}
		
		String[] s = src.split(",");
		for (String st : s) 
		{
			if (st.equals(dest)) 
			{
				return true;
			}
		}
		return false;
	}

	public static boolean ipContains(String ips, String ip) 
	{
		
		if (StringUtils.isEmpty(ip)) 
		{
			return false;
		}
		if (StringUtils.isEmpty(ips)) 
		{
			return false;
		}
		if (!regxIp.matcher(ip).matches()) 
		{
			return false;
		}
		String[] ipArr = ips.split(",");
		for (int i = 0; i < ipArr.length; i++) 
		{
			if (ipArr[i].equals(ip)) 
			{
				return true;
			}
			if (regxIpSection.matcher(ipArr[i]).matches()) 
			{
				if (ipIsValidSection(ip, ipArr[i])) 
				{
					return true;
				}
			}
		}
		return false;
	}

	private static boolean ipIsValidSection(String ip, String ipSection) {
		int idx = ipSection.indexOf(45);
		String[] sips = ipSection.substring(0, idx).split("\\.");
		String[] sipe = ipSection.substring(idx + 1).split("\\.");
		String[] sipt = ip.split("\\.");

		long ips = 0L;
		long ipe = 0L;
		long ipt = 0L;

		for (int i = 0; i < 4; i++) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}

		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return (ips <= ipt) && (ipt <= ipe);
	}
    /**
     * 判断是否是手机号码
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles)
    {       
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");       
        Matcher m = p.matcher(mobiles);       
        return m.matches();       
    }   
    
    public static String null2String(Object strIn)
    {
        return strIn != null ? (new StringBuilder()).append("").append(strIn).toString() : "";
    }
    
    
    public static long ipToLong(String ipAddress) 
    {
    	long result = 0;
    	if(StringUtils.isEmpty(ipAddress)){
    		return result;
    	}
    	String[] ipAddressInArray = ipAddress.split("\\.");
    	for (int i = 3; i >= 0; i--) 
    	{
    		long ip = Long.parseLong(ipAddressInArray[3 - i]);
    		result |= ip << (i * 8);
    	}
    	return result;
    }
    
   

    public static String longToIp(Long ip) 
    {
    	if(ip==null||ip==0)
    	{
    		return "";
    	}	
    	return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
    }
    
	public static String hashNumber(long number) {
		StringBuffer path = new StringBuffer(20);
		long tmpValue = (long)number/1000;
		path.append((long)(tmpValue/10000)+"/");
		path.append((long)(tmpValue/100)%100+"/");
		path.append(number+"/");
		return path.toString();
	}
	
	/**
	 * liangdawen 改成強行把IPV6地址换成IPV4地址。
	 * 暂时适应没有IPV6的真正业务
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(ip)|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip)|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if (StringUtils.isEmpty(ip)|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if(!StringUtils.isEmpty(ip) && IpLimit.isLoopbackIp(ip)){
				ip = IpLimit.LoopbackStringIpV4; //"2001:4d0:9700:903:198:9:3:30";//debug
			}
			
			if(!StringUtils.isEmpty(ip) && IpLimit.isIPV6Format(ip)){
				BigInteger bint = IpLimit.StringToBigInt(ip);
				int ipv4int = bint.intValue(); 
				String ipv4 =IpLimit.intToIpV4(ipv4int);
				ip = ipv4;
			}
			
			if (ip.equals("127.0.0.1")) {
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ip = inet.getHostAddress();
			}
		} 
		
		if(!StringUtils.isEmpty(ip)&&ip.length()>15&&StringUtils.contains(ip, ",")){
			ip=ip.substring(0,ip.indexOf(","));
		}
		return ip;
	} 
	
	public static String getUUID()
	{
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
 
	public static String urlComponentEncode(String srcUrl)
	{
		try {
			srcUrl = java.net.URLEncoder.encode(srcUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return srcUrl;
	}
	
	public static String urlComponentDecode(String encodeUrl)
	{
		try {
			encodeUrl = java.net.URLDecoder.decode(encodeUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeUrl;
	}
	
	public static String triggerTaskcenter(String urlStr)
	{
		try {
			URLConnection conn = new URL(urlStr).openConnection();
			conn.setConnectTimeout(3000);
			
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			StringBuffer sb = new StringBuffer(); 
			while((line=br.readLine())!=null)
			{
				sb.append(line);
			}	
			in.close();
			br.close();
			conn = null;
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean isInValidCharacter(String source){
		if(StringUtils.isEmpty(source)){
			return false;
		}
		String[] regex=new String[]{"%\'"};
		for(int i=0;i<regex.length;i++){
			Pattern pattern = Pattern.compile(regex[i]);
			Matcher matcher = pattern.matcher(source);
			if(matcher.find()){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidCharacter(String source){
		if(StringUtils.isEmpty(source)){
			return true;
		}
		String[] regex=new String[]{"[\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w\\.\\s]+$"};
		for(int i=0;i<regex.length;i++){
			Pattern pattern = Pattern.compile(regex[i]);
			Matcher matcher = pattern.matcher(source);
			if(matcher.find()){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasInValidCharacters(String...source){
		for(int k=0;k<source.length;k++){
			if(StringUtils.isEmpty(source[k])){
				return false;
			}
			String[] regex=new String[]{"%27","%29","%28","\\+","\\*%2F"};
			boolean isInValid=false;
			for(int i=0;i<regex.length;i++){
				Pattern pattern = Pattern.compile(regex[i]);
				Matcher matcher = pattern.matcher(source[k]);
				if(matcher.find()){
					isInValid=true;
					break;
				}
			}
			if(isInValid)
				return isInValid;
		}
		return false;
	}
	
	public static boolean hasInValidCharacter(Map<String,Object> map){
		if(map==null)
			return false;
		
		Iterator<Map.Entry<String,Object>> itr=map.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String,Object> entry=itr.next();
			String val=ObjectUtils.toString(entry.getValue());
			if(StringUtils.isEmpty(val)){
				return false;
			}
			String[] regex=new String[]{"%27","%29","%28","\\+","\\*%2F"};
			boolean isInValid=false;
			for(int i=0;i<regex.length;i++){
				Pattern pattern = Pattern.compile(regex[i]);
				Matcher matcher = pattern.matcher(val);
				if(matcher.find()){
					isInValid=true;
					break;
				}
			}
			if(isInValid)
				return isInValid;
		}
		return false;
	}
	
	public static boolean hasInValidCharacter(Object model){
		if(null==model){
			return false;
		}

		try {
			// 获取实体类的所有属性，返回Field数组
	        Field[] field = model.getClass().getDeclaredFields();
	        // 获取属性的名字

	        for (int i = 0; i < field.length; i++){
	            // 获取属性的名字
	            String name = field[i].getName();
	            // 获取属性类型
	            String type = field[i].getGenericType().toString();
	            field[i].setAccessible(true);
	            // 将属性的首字母大写
	            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());

	            if (type.equals("class java.lang.String")){ 
	                // 如果type是类类型，则前面包含"class "，后面跟类名
	                Method m = model.getClass().getMethod("get" + name);
	                // 调用getter方法获取属性值
	                String value = (String) m.invoke(model);
	                if(hasInValidCharacters(value)){
	                	return true;
	                }
	            }
	            if (type.equals("class java.lang.Integer")){
	                Method m = model.getClass().getMethod("get" + name);
	                Integer value = (Integer) m.invoke(model);
	                String valueStr=(value==null?"":String.valueOf(value));
	                if(hasInValidCharacters(valueStr)){
	                	return true;
	                }
	            }
	            if (type.equals("class java.lang.Short")){
	                Method m = model.getClass().getMethod("get" + name);
	                Short value = (Short) m.invoke(model);
	                String valueStr=(value==null?"":String.valueOf(value));
	                if(hasInValidCharacters(valueStr)){
	                	return true;
	                }
	            }
	            if (type.equals("class java.lang.Double")){
	                Method m = model.getClass().getMethod("get" + name);
	                Double value = (Double) m.invoke(model);
	                String valueStr=(value==null?"":String.valueOf(value));
	                if(hasInValidCharacters(valueStr)){
	                	return true;
	                }
	            }
	            if (type.equals("class java.lang.Boolean")){
	                Method m = model.getClass().getMethod("get" + name);
	                Boolean value = (Boolean) m.invoke(model);
	                String valueStr=(value==null?"":String.valueOf(value));
	                if(hasInValidCharacters(valueStr)){
	                	return true;
	                }
	            }
	            if (type.equals("class java.util.Date")){
	                Method m = model.getClass().getMethod("get" + name);
	                Date value = (Date) m.invoke(model);
	                String valueStr=(value==null?"":String.valueOf(value));
	                if(hasInValidCharacters(valueStr)){
	                	return true;
	                }
	            }
	        }
		} catch (Exception e) {
			
		}
		return false;
	}
	
	public static boolean escapeHtmlForObject(Object model){
		if(null==model){
			return false;
		}

		try {
			// 获取实体类的所有属性，返回Field数组
	        Field[] field = model.getClass().getDeclaredFields();
	        // 获取属性的名字

	        for (int i = 0; i < field.length; i++){
	            // 获取属性的名字
	            String name = field[i].getName();
	            // 获取属性类型
	            String type = field[i].getGenericType().toString();
	            field[i].setAccessible(true);
	            // 将属性的首字母大写
	            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
	            
	            
	            if (type.equals("class java.lang.String")){ 
	                // 如果type是类类型，则前面包含"class "，后面跟类名
	                Method m = model.getClass().getMethod("get" + name);
	                // 调用getter方法获取属性值
	                String value = (String) m.invoke(model);
	                field[i].set(model,HtmlUtils.htmlEscape(value));
	            }
	            if (type.equals("class java.lang.Integer")){
	                Method m = model.getClass().getMethod("get" + name);
	                Integer value = (Integer) m.invoke(model);
	                if(value!=null){
		               field[i].set(model,Integer.valueOf(HtmlUtils.htmlEscape(String.valueOf(value))));
	                }
	            }
	            
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean unEscapeHtmlForObject(Object model){
		if(null==model){
			return false;
		}

		try {
			// 获取实体类的所有属性，返回Field数组
	        Field[] field = model.getClass().getDeclaredFields();
	        // 获取属性的名字

	        for (int i = 0; i < field.length; i++){
	            // 获取属性的名字
	            String name = field[i].getName();
	            // 获取属性类型
	            String type = field[i].getGenericType().toString();
	            field[i].setAccessible(true);
	            // 将属性的首字母大写
	            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
	            
	            
	            if (type.equals("class java.lang.String")){ 
	                // 如果type是类类型，则前面包含"class "，后面跟类名
	                Method m = model.getClass().getMethod("get" + name);
	                // 调用getter方法获取属性值
	                String value = (String) m.invoke(model);
	                field[i].set(model,HtmlUtils.htmlUnescape(value));
	            }
	            if (type.equals("class java.lang.Integer")){
	                Method m = model.getClass().getMethod("get" + name);
	                Integer value = (Integer) m.invoke(model);
	                if(value!=null){
		               field[i].set(model,Integer.valueOf(HtmlUtils.htmlUnescape(String.valueOf(value))));
	                }
	            }
	            
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static boolean escapeHtmlForModel(Model model){
		if(null==model){
			return false;
		}
		try {
			// 获取实体类的所有属性，返回Field数组
	        Map<String,Object> oldMap = model.asMap();
	        Iterator<Map.Entry<String,Object>> itr=oldMap.entrySet().iterator();
	        while(itr.hasNext()){
	        	Map.Entry<String,Object> entry=itr.next();
	        	Object val=entry.getValue();
	        	if(val instanceof java.lang.String){
	        		entry.setValue(HtmlUtils.htmlEscape(ObjectUtils.toString(val)));
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean unEscapeHtmlForModel(Model model){
		if(null==model){
			return false;
		}
		try {
			// 获取实体类的所有属性，返回Field数组
			Map<String,Object> oldMap = model.asMap();
			Iterator<Map.Entry<String,Object>> itr=oldMap.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<String,Object> entry=itr.next();
				Object val=entry.getValue();
				if(val instanceof java.lang.String){
					entry.setValue(HtmlUtils.htmlUnescape(ObjectUtils.toString(val)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isValidUrl(String url){
		String regexp  ="^(\\w+:\\/\\/)?\\w+(\\.\\w+)+.*$";  
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(url);
		return matcher.find();
	}
	
	public static boolean isValidImageType(String image){
		String regexp  =".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";  
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(image);
		return matcher.find();
	}
	
	/**
     * 判断str参数是否是浮点数表示方式
     * @param str
     * @return
     */
	public static boolean isDecimal(String str) {
		if (str == null || "".equals(str))
			return false;
		str=str.trim();
		java.util.regex.Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	/**
     * 判断str参数是否是百分比
     * @param str
     * @return
     */
	public static boolean isPercentages(String str) {
		if (str == null || "".equals(str))
			return false;
		str=str.trim();
		java.util.regex.Pattern pattern = Pattern.compile("^\\d+\\.?\\d*\\%?$");
		return pattern.matcher(str).matches();
	}
	
	
	public static void main(String[] args) {
//		String url = "http://localhost:8080/taskcenter/doservice?request=%7b%22classname%22%3a%22com.mm.taskcenter.model.IosItunes%22%2c%22method%22%3a%22run%22%2c%22callback%22%3a%22%22%2c%22validtime%22%3a%22%22%2c%22parame%22%3a%5b%22appids%3a577011200%2c507388249%2c424170534%22%5d%7d";
//		triggerTaskcenter(url);
		System.out.println(isDecimal("5"));
	}
   
}
