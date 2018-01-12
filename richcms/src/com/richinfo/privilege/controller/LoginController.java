package com.richinfo.privilege.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.SessionFixationProtectionStrategy;
import com.richinfo.common.SystemContext;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.TokenMananger;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.common.utils.encryptUtil.RSAUtil;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.UserService;

@Controller
@RequestMapping(value="/login")
public class LoginController {
	
	
	 private final static Logger log = Logger.getLogger("accessLog");
	 
	 private UserService userService;
	 
	 
	 @Autowired
	 @Qualifier("UserService")
	 public void setUserService(UserService userService) {
		this.userService = userService;
	 }

	 
	@RequestMapping(value = "/auth.do")
	 public String login(HttpServletRequest request, Model model){
		 int reload = ServletRequestUtils.getIntParameter(request, "reload",0);
		 model.addAttribute("reload", reload);
		 return "login/auth";
	 }
	 
	 @RequestMapping(value = "/dologin",method = {RequestMethod.POST})
	 @ResponseBody
	 public Map<String ,String> dologin(HttpServletRequest request, Model model){
		 
		 Map<String,String> json = new HashMap<String,String>();
	     String userName = ServletRequestUtils.getStringParameter(request, "username","");
		 String passwd = ServletRequestUtils.getStringParameter(request, "passwd","");
		 passwd = RSAUtil.restoreText(passwd);
		 String validateCode = ServletRequestUtils.getStringParameter(request, "verify","");
		 SessionFixationProtectionStrategy sp=new SessionFixationProtectionStrategy();
		 HttpSession session = sp.applySessionFixation(request);//request.getSession(true);
		 String remoteIp = CommonUtil.getClientIp(request);//request.getRemoteAddr();
		 String whiteIps = SystemProperties.getInstance().getProperty("ipModule.whiteList");
		  
		 String clientToken = request.getParameter(TokenMananger.CSRF_PARAM_NAME);
     	 String serverToken = (String)session.getAttribute(TokenMananger.CSRF_TOKEN_SESSION_ATTR_NAME);
     	 if((StringUtils.isNotEmpty(clientToken)&&!clientToken.equals(serverToken))||StringUtils.isEmpty(clientToken))
     	 {
     		log.warn("发现非法请求,url:"+request.getRequestURI()+",ip:"+CommonUtil.getClientIp(request));
     		return null ;
     	 }
		 //检查IP是否合法
		 if(!"-1".equals(whiteIps)&&!CommonUtil.ipContains(whiteIps, remoteIp))
		 {
			 log.error("invalid client ip:"+remoteIp+",whiteIps:"+whiteIps);
			 json.put("status", "n");
			 json.put("info", "4");
			 return json;
		 }
		 String specialUser = SystemProperties.getInstance().getProperty("authentication.specialUser");
		 
		 //校验验证码是否输入正确
		 if(!specialUser.equals(userName)&&!validateCode.equals(session.getAttribute(Constants.VALIDATE_CODE_KEY)))
		 {
			 json.put("status", "n");
			 json.put("info", "2");
			 return json;
		 }
		 SystemContext.setSessionContext(session);
		 User u =  userService.login(userName, passwd);
		 if(u!=null)
		 {
			 
			 if(u.getStatus()==-1)
			 {
				 json.put("status", "n");
				 json.put("info", "3");
				 return json;
			 }
			 Long lastIp = u.getLoginIp();
			 Long lastLogintime = u.getLastLogin();
			 u.setLastLoginStr(DateTimeUtil.getTimeStamp(lastLogintime, "yyyy-MM-dd HH:mm:ss"));
			 u.setLoginIp(CommonUtil.ipToLong(remoteIp));
			 
			 u.setLastLogin(DateTimeUtil.getTimeStamp());
			 userService.update(u);
			 u.setLoginIp(lastIp);
			 u.setLastLogin(lastLogintime);
			 session.setAttribute(Constants.CURRENT_USER_ACCOUNT, u);
			 session.setAttribute(Constants.CURRENT_USER_ROLE, u.getRole());
			 
			 
		 }else{
			 json.put("status", "n");
			 json.put("info", "1");
			 return json;
		 }
		 json.put("status", "y");
		 json.put("info", "/admin/index.do");
		 return json;
	 }
	 
	 @RequestMapping(value = "/logout.do",method=RequestMethod.POST)
	 @ResponseBody
	 public Object logout(HttpServletRequest request, Model model)
	 {
		 Map<String,String> json = new HashMap<String,String>();
		 HttpSession session = request.getSession();
		 String clientToken = request.getParameter(TokenMananger.CSRF_PARAM_NAME);
     	 String serverToken = (String)session.getAttribute(TokenMananger.CSRF_TOKEN_SESSION_ATTR_NAME);
     	 if((StringUtils.isNotEmpty(clientToken)&&!clientToken.equals(serverToken))||StringUtils.isEmpty(clientToken))
     	 {
     		json.put("status", "1");
     		json.put("message", "缺少令牌,无效请求！");
     		return json;
     	 }
		 
		 if(session!=null)
		 {
			 session.removeAttribute(Constants.CURRENT_USER_ACCOUNT);
			 session.removeAttribute(Constants.CURRENT_USER_ROLE);
			 session.invalidate();
		 }
		 
		 json.put("status", "0");
		 return json;
	 }
	 
	 @RequestMapping(value = "/logout.do",method=RequestMethod.GET)
	 public String getLogout(HttpServletRequest request, Model model)
	 {
		 HttpSession session = request.getSession();
		 if(session!=null)
		 {
			 session.removeAttribute(Constants.CURRENT_USER_ACCOUNT);
			 session.removeAttribute(Constants.CURRENT_USER_ROLE);
			 session.invalidate();
		 }
		 model.addAttribute("url","/admin/index.do");
		 return Constants.FORWARDURL;
	 }
	 
	 @RequestMapping(value = "/cvc")
	 public String createValidateCode(HttpServletRequest request, Model model)
	 {
		 return "public/image";
	 }
	
	 @RequestMapping(value = "/inValid")
	 public String inValid(HttpServletRequest request, Model model)
	 {
		 return "invalid";
	 }
	 
	
	 
}
