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
</head>
<body>
  <div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">个人信息 - 修改个人信息</a></h2>
  <div style="margin:25px 20px;">
  <form id="fm_es" action="${ctx }/Myinfo/edit.do" method="post">
  <input type="hidden" name="csrfToken" value="${csrfToken}"/>
	<table style="font-size:12px;color:#444;line-height:32px;">
		<tr>
			<td width="80">用户名</td>
			<td>${userinfo.userName}</td>
		</tr>
		<tr>
			<td width="80">最后登录时间</td>
			<td>${userinfo.lastLoginStr}</td>
		</tr>
		<tr>
			<td width="80">最后登录IP</td>
			<td>${userinfo.ipstr} </td>
		</tr>
		<tr>
			<td width="80">真实姓名</td>
			<td>
				<input type="text" name="nickName" id="nickname" value="${userinfo.nickName}" datatype="zh2-4" nullmsg="请输入真实姓名！" errormsg="用户名必须为二到四个汉字！"/>
			</td>
			<td><div class="Validform_checktip"></div></td>
		</tr>
		<tr>
			<td width="80">E-mail</td>
			<td>
				<input type="text" name="email" id="email" value="${userinfo.email}" datatype="e" nullmsg="请输入邮箱！" />
			</td>
			<td><div class="Validform_checktip"></div></td>
		</tr>
		<tr>
			<td  colspan="2" align="center">
			    <input type="hidden" name="userName" value="${userinfo.userName }">
			    <input type="hidden" name="userId" value="${userinfo.userId }">
				<div class="adm-btn-big"><input type="submit"  value="提&nbsp;&nbsp;交" /></div>
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
			"zh2-4":/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,4}$/
		}
		
	});
})
</script>
</body>
</html>