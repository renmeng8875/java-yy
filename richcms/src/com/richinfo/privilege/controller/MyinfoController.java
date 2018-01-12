package com.richinfo.privilege.controller;



import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.encryptUtil.RSAUtil;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.UserService;

@Controller
@RequestMapping(value = "/Myinfo")
public class MyinfoController {

	private UserService userService;
	

	@Autowired
	@Qualifier("UserService")
	public void setMenuService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 登陆后 跳转至后台首页
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit.do")
	public String edit(HttpServletRequest request, Model model,@ModelAttribute User user) {
		String methodType = request.getMethod();
		if("GET".equals(methodType))
		{
			return "/myinfo/edit";
		}
		User old = userService.getUserByName(user.getUserName());
		old.setNickName(user.getNickName());
		old.setEmail(user.getEmail());
		userService.update(old);
		request.getSession().setAttribute(Constants.CURRENT_USER_ACCOUNT, old);
		model.addAttribute("message", "修改用户信息成功！");
		model.addAttribute("url","/Myinfo/edit.do");
		return Constants.FORWARDURL;
	}
    
	/**
	 * 修改密码
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editpassword.do")
	public String savePassword(HttpServletRequest request, Model model){
		String methodType = request.getMethod();
		if("GET".equals(methodType)){
			return "/myinfo/editpassword";
		}
		String userName = request.getParameter("userName");
		String oldpwd = request.getParameter("password");
		oldpwd = RSAUtil.restoreText(oldpwd);
		String newpwd = request.getParameter("newpassword");
		newpwd = RSAUtil.restoreText(newpwd);
		//appScan
		if(CommonUtil.hasInValidCharacters(userName,oldpwd,newpwd)){
			return null;
		}
		
	    User user = userService.login(userName, oldpwd);
		if(user!=null){
			try {
				user.setPasswd(newpwd);
				if(userService.updateUserPwd(user)){
					model.addAttribute("message", "密码修改成功，请重新登陆！");
					model.addAttribute("url","/login/logout.do");
					model.addAttribute("isTop","true");
				}else{
					model.addAttribute("message", "密码修改失败，请重试！");
					model.addAttribute("url","/Myinfo/editpassword.do");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.addAttribute("message", "密码修改失败，请重试！");
				model.addAttribute("url","/Myinfo/editpassword.do");
			}
		}else{
			model.addAttribute("message", "旧密码错误，请重试！");
			model.addAttribute("url","/Myinfo/editpassword.do");
		}
		return Constants.FORWARDURL;
	}
 
 
	@RequestMapping(value = "/checkPwd.do")
	@ResponseBody
	public Object checkPassword(HttpServletRequest request, Model model){
		String oldpwd = request.getParameter("param");
		User currentUser = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
	    User user = userService.login(currentUser.getUserName(), oldpwd);
	    Map<String,Object> json = new HashMap<String, Object>();
		if(user!=null){
			json.put("status", "y");
		}else{
			json.put("status", "n");
			json.put("info", "密码输入不正确");
		}
		return json;
	}
 
	

}
