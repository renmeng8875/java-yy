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
<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
<script type="text/javascript">

$(function(){
	$("a[data-action]").click(function()
	{
		var text = $(this).text();
		var op = $(this).attr("op");
		var tid = $(this).attr("tid");
		var data = $(this).attr("data-action");
		var userId = $(this).attr("userId");
		if(userId==1||userId=='1')
		{
			alert("系统默认的管理员不允许锁定或解锁操作！");
			return false;
		}	
		if(confirm("你确定"+text+"该用户吗?"))
		{
		   $.ajax({
			   type: "POST",
			   url: data,
			   data:"csrfToken=${csrfToken}",
			   success: function(msg)
			   {
		     		alert(text+"成功！");	
		     		if(op=='delete')
		     		{
		     			$("#"+tid).remove();	
		     		}else if(op=='lock'||op=='unlock'){
		     			location.reload();
		     		}
		     		else{
		     			$(this).css("color","red");
		     		}
			   }
		   });
		}
	});
	
	$("a[url]").click(function(){
		var tid = $(this).attr("tid");
		var url = $(this).attr("url");
		var roleid = $(this).attr("roleid");
		if(roleid=='1'||roleid==1){
			alert("超级管理员不能删除！");
			return false;
		}
		if(confirm("你确定删除该成员吗")){
			$.ajax({
			   type: "POST",
			   url: url,
			   data:"csrfToken=${csrfToken}",
			   success: function(msg)
			   {
		     		alert("删除成功！");	
		     		$("#"+tid).remove();
			   }
		   });
		}
	});
	
	
});
	
  </script>
</head>
<body style="background-color: white;">
 <h2 class="adm-title">
<a class="fl title">角色管理-成员管理</a>
<a class="fr btn" href="javascript:history.go(-1);">+角色管理</a>
</h2>
<div class="pad10">
  <table class="adm-table">
    <tr class="tr1">
      <td>用户ID</td>
      <td>用户名</td>
      <td>所属角色</td>
      <td>最后登录IP</td>
      <td>最后登录时间</td>
      <td>E-mail</td>
      <td>真实姓名</td>
      <td>管理操作</td>
    </tr>
   <c:forEach items="${userList}" var="user" varStatus="status">
    <tr  id="tr_${user.userId}" class='<c:if test="${status.index % 2 == 1}">tr2</c:if>'>
      <td>${user.userId}</td>
      <td>${user.userName}</td>
      <td>${user.role.roleName}</td>
      <td>${user.ipstr}</td>
      <td>${user.lastLoginStr}</td>
      <td>${user.email}</td>
      <td>${user.nickName}</td>
      <td>
      
		<c:choose>
			<c:when test="${user.status == 0}">
				<a href="javascript:;" op="lock"  userId=${user.userId} tid="tr_${user.userId}" data-action="${ctx}/Adminsetting/lock.do?userId=${user.userId}">锁定</a>
			</c:when>
			<c:otherwise>
				<a href="javascript:;" op="unlock" userId=${user.userId}  tid="tr_${user.userId}" data-action="${ctx}/Adminsetting/unlock.do?userId=${user.userId}" style="color: #F00">解锁</a>
			</c:otherwise>
		</c:choose>
		<a href="${ctx}/Adminsetting/changerole.do?param=in&userId=${user.userId}">修改</a>
		<a href="javascript:void(0);" roleid=${user.role.roleid}  tid="tr_${user.userId}" url="${ctx}/Adminsetting/deluser.do?userId=${user.userId}"> 删除</a>
	  
	  </td>
    </tr>
	</c:forEach>
  </table>
</div>
 
</body>
</html>