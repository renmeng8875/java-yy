<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>

<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
    <script type="text/javascript" src="${ctx}/static/admin/js/RSA.js"></script>
</head>
<body>
 <div class="main_border" style="border:0;"><div id="my_main">
   <h2 class="adm-title"><a class="title">个人信息 - 修改密码</a></h2>
   <div style="margin:25px 20px;">
   <form id="fm_es"  action="${ctx }/Myinfo/editpassword.do" method="post" >
   <input type="hidden" name="csrfToken" value="${csrfToken}"/>
	<table style="font-size:12px;color:#444;line-height:32px;">
		<tr>
			<td width="80">用户名:</td>
			<td>${userinfo.userName}</td>
		</tr>
		<tr>
			<td width="80">真实姓名:</td>
			<td>${userinfo.nickName}</td>
		</tr>
		<tr>
			<td width="80"><span class="need" >*</span>旧密码:</td>
			<td>
				<input type="password" name="password"   id="oldpasswd" style="width:200px" datatype="*" sucmsg="旧密码验证通过！" nullmsg="请输入旧密码！" errormsg="旧密码输入不正确！" ajaxurl="${ctx }/Myinfo/checkPwd.do?csrfToken=${csrfToken}"/>
				
			</td>
			<td><div class="Validform_checktip"></div></td>
		</tr>
		<tr>
			
			<td width="80"><span class="need" >*</span>新密码:</td>
			<td>
				<input type="password" name="newpassword" id="newpassword" class="inputxt" datatype="ns8-15"  style="width:200px"/>
			</td>
			<td><div class="Validform_checktip"></div></td>
		</tr>
		<tr>
			<td width="80"><span class="need" >*</span>重复新密码:</td>
			<td>
				<input style="width:200px" type="password" name="repeatpassword" id="repeatpassword" class="inputxt" recheck="newpassword"  datatype="ns8-15" />
			</td>
			<td><div class="Validform_checktip"></div></td>
			
		</tr>
		<tr>
			<td  colspan="2" align="center">
			    <input type="hidden" name="userName" id="userName" value="${userinfo.userName }">
			    <input type="hidden" name="userId" value="${userinfo.userId }">
				<div class="adm-btn-big">
				<input type="submit"   value="提&nbsp;&nbsp;交" /></div>
			</td>
		</tr>
	</table>
	</form>
	</div>
	</div></div>
	
   <script type="text/javascript">
$(function(){
   	$("#fm_es").Validform({
		tiptype:2,
		datatype:{
				"ns8-15":function(gets,obj,curform,regxp){
					var reg1=/(?=.*[\d]+)(?=.*[a-zA-Z]+)(?=.*[^a-zA-Z0-9]+).{8,15}/;
					if(reg1.test(gets)){
						return true;
					}
					return "密码长度在8～15个字符范围内,且必须为数字和字母和特殊字符的组合!";
				}
			},
		beforeSubmit:function(){
				var pwds = $("input[type='password']");
				pwds.each(function(){
					var pwd = this.value;
					pwd = RSAEncode(pwd);
					$(this).val(pwd);
				});
		}	
	});
});
   </script>
</body>
</html>