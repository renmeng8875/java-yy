<%@include file="../public/header.jsp" %>
<%@page import="com.richinfo.common.TokenMananger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>

<link href="${ctx}/static/admin/css/login.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/RSA.js"></script>
<script type="text/javascript">
$(function(){
	
	//$("#refreshVerifyCode").trigger("click");
	
	$("#fmlogin").Validform({
		tiptype:function(msg,o,cssctl)
		{
			if(!o.obj.is("form")){
				var objtip=o.obj.siblings(".Validform_checktip");
				cssctl(objtip,o.type);
				objtip.text(msg);
			}
		},
		datatype:{
			"ns8-15":function(gets,obj,curform,regxp){
				var reg1=/(?=.*[\d]+)(?=.*[a-zA-Z]+)(?=.*[^a-zA-Z0-9]+).{8,15}/;
				if(reg1.test(gets)){
					return true;
				}
				return "请输入8到15位字符";
			},
			"n4":/^\d{4}$/
		},
		ajaxPost:true,
		callback:function(data)
		{
			var ctx = $("#fmlogin").attr("data-ctx");
			if(data.status=='y')
			{
				var ref = (ctx+data.info) || '/';
				if(ref !='/')
				{
					window.location.replace(ref);
					window.event.returnValue = false;
				}
				else
				{
					window.location.reload();
				}
				
			}
			else(data.status=='n')
			{
				switch(data.info*1)
				{
					case 1:
						$('#lusernameTip').html('账号或密码不正确').addClass("Validform_wrong");
						$("#refreshVerifyCode").click();
						$("#passwordid").val('');
						$("#passwordid").focus();
						break;
					case 2:
						$('#lverifyTip').html('验证码错误').addClass("Validform_wrong");
						$("#refreshVerifyCode").click();
						$("#passwordid").val('');
						break;
					case 3:
						$('#lusernameTip').html('账号被锁定').addClass("Validform_wrong");
						$("#refreshVerifyCode").click();
						break;
					case 4:
						$('#lusernameTip').html('非法IP地址').addClass("Validform_wrong");
						$("#refreshVerifyCode").click();
						break;
					default:
						$("#refreshVerifyCode").click();
						break;	
				}	
				
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
	
	$("#refreshVerifyCode").click(function(){
		var imgObj = $("#VerifyCode");
		imgObj.attr('src',imgObj.attr("context")+'/login/cvc?'+(+new Date()));
	});
	
	
	
	
});
		
</script>

</head>
<body id="bg">
	<div id="wraper">
		<div class="logo">
			<img src="${ctx}/static/admin/images/bg_logo.png" alt="" class="fl" />
			<h1>移动应用商城后台管理系统</h1>
			<p>Moblie Market Management System</p>
			<div class="clear"></div>
		</div>
		<div class="content">
			<div class="title">
				<a class="fl">用户登录</a><span class="fr"></span>
			</div>
			<form id="fmlogin" data-ctx="${ctx}" method="post" action="${ctx}/login/dologin">
				<div class="login">
					<ul>
					    
						<li class="p1 relative">
						    <span class="fl sp1">用户名 :</span> 
						    <input class="input1 fl" type="text" name="username" datatype="s3-20" sucmsg="验证通过" nullmsg="请输入用户名" errormsg="请输入3到20位字符" /> 
						    <span class="Validform_checktip" id="lusernameTip">
						    <a class="a1"><img src="${ctx}/static/admin/images/bg_9x5.png">  </a> 
							<span class="onShow" >请输入用户名</span>   </span>
							
						</li>
							    
						<div class="clear"></div>
						<li class="relative"><span class="fl sp1">密&nbsp;&nbsp;&nbsp;&nbsp;码 :</span> 
						    <input type="password" name="passwd" id="passwordid" class="input1 fl" datatype="ns8-15" sucmsg="验证通过" /> 
							<span class="Validform_checktip">
							<a class="a2"><img src="${ctx}/static/admin/images/bg_11x15.png"></a>
							<span class="onShow" id="lpasswdTip" >请输入密码</span></span>
						</li>
						
						<div class="clear"></div>
					
						<li class="relative"><span class="fl sp1">验证码 :</span> 
						<input class="input3 fl" type="text" name="verify" datatype="n4" ignore="ignore" sucmsg="验证通过" nullmsg="请输入验证码" errormsg="请输入4位整数"/> 
							<img id="VerifyCode" context="${ctx}" src="${ctx}/login/cvc" />
							<a id="refreshVerifyCode" href="javascript:void(0);" style="text-decoration: underline; line-height:30px; margin-left:15px;"  >&nbsp;看不清?</a>
							<span class="Validform_checktip" id="lverifyTip">
							<span class="onShow" >请输入验证码</span></span>
						</li>
						<div class="clear"></div>
						<li class="p5"><span class="fl sp1">&nbsp;</span> 
						   <input type="submit" class="l-btn fl" value="" >
						</li>
					</ul>
				</div>
				<input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
			</form>
		</div>
	</div>
	</body>
</html>
