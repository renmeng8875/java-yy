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
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/RSA.js"></script>
<script type="text/javascript">
$(function(){
		$("#fm_es").Validform({
			tiptype:3,
			label:".label",
			showAllError:false,
			datatype:{
				"ns8-15":function(gets,obj,curform,regxp){
					//var reg1=/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,15}$/g;
					var reg1=/(?=.*[\d]+)(?=.*[a-zA-Z]+)(?=.*[^a-zA-Z0-9]+).{8,15}/;
					if(reg1.test(gets)){
						return true;
					}
					return "密码长度在8～15个字符范围内,且必须为数字、字母和特殊字符的组合!";
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
</head>
<body style="background-color: white;">
  <h2 class="adm-title"><a class="title">管理员管理</a><a class="fr btn" href="javascript:history.go(-1);">+管理员管理</a></h2>
<form name="fm_es" id="fm_es" method="post" action="${ctx}/Adminsetting/changePwd.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="b">用户id：</span>
        <input type="text" name="userId" style="border:0;" id="userId" class="l" value="${user.userId}" readonly />
      </li>
      <li><span class="b">用户名：</span>
        <input type="text" name="userName" style="border:0;" id="userName" class="l" value="${user.userName}" readonly />
      </li>
	 <li><span class="b">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</span>
        <input type="password" name="passwd" id="passwd" value="" class="l"  datatype="ns8-15"  nullmsg="请输入密码!"/>
      </li>
      <li><span class="b">确认密码：</span>
        <input type="password" name="passwd1" id="passwd1" value="" class="l" recheck="passwd"  datatype="ns8-15"/>
      </li>
    </ul>
  </div>
</div>
<div class="adm-btn-big"><input type="submit" id="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
  </div>
  </div>
</body>
</html>