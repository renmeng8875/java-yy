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

</head>
<body>
  <div class="main_border" style="border:0;"><div id="my_main">
   <h2 class="adm-title clearfix"><a class="fl title">管理员管理</a><a class="fr btn" href="${ctx}/Adminsetting/adduser.do">+添加管理员</a></h2>
	<div class="pad10">
	  <table class="adm-table">
	    <tr class="tr1">
	      <td>管理员ID</td>
	      <td>用户名</td>
	      <td>所属角色</td>
	      <td>最后登录IP</td>
	      <td>最后登录时间</td>
	      <td>真实姓名</td>
	      <td>管理操作</td>
	    </tr>
		<c:forEach items="${ulist}" var="user" varStatus="status">
	    <tr id="tr_${user.userId}">
	      <td>${user.userId}</td>
	      <td>${user.userName}</td>
	      <td>${user.role.roleName}</td>
	      <td>${user.ipstr}</td>
	      <td>${user.lastLoginStr}</td>
	      <td>${user.nickName}</td>
          <td>
	            <c:if test="${user.userId!=1 }">
	      			<a href="${ctx}/Adminsetting/changerole.do?userId=${user.userId}"> 修改角色</a>
	      		</c:if>	
	      		 <c:if test="${user.userId==1 }">
	      			<a href="javascript:void(0);" style="color: gray;">系统默认</a>
	      		</c:if>	
	      			<a href="${ctx}/Adminsetting/changePwd.do?userId=${user.userId}"> 修改密码</a>
	      			<c:if test="${user.status ne -1}">
	      				<a href="javascript:;" op="lock" userid="${user.userId}" tid="tr_${user.userId}" data-action="${ctx}/Adminsetting/lock.do?userId=${user.userId}">锁定</a>
	      			</c:if>
      				<c:if test="${user.status eq -1}">
      				<a href="javascript:;" style="color:red;" userid="${user.userId}" op="unlock"  tid="tr_${user.userId}" data-action="${ctx}/Adminsetting/unlock.do?userId=${user.userId}">解锁</a>
      				</c:if>
      				<a href="javascript:;"  op="delete" tid="tr_${user.userId}"  roleid="${user.role.roleid}"   data-action="${ctx}/Adminsetting/deluser.do?userId=${user.userId}"> 删除</a>
	      		
      	  </td>
	      
	      
	    </tr>
		</c:forEach>
	  </table>
	</div>
	
  </div></div>
  <script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
  <script type="text/javascript">
	$("a[data-action]").click(function()
	{
		var text = $(this).text();
		var op = $(this).attr("op");
		var tid = $(this).attr("tid");
		var data = $(this).attr("data-action");
		var roleid = $(this).attr("roleid");
		var userid = $(this).attr("userid");
		if(userid==1){
			alert("系统默认的超级管理员不允许锁定或解锁！");
			return false;
		}
		if(roleid==1&&op=='delete')
		{
			alert("超级管理员不允许删除！");
			return false;
		}	
		if(confirm("你确定"+text+"该用户吗?"))
		{
		   $.ajax({
			   type: "POST",
			   url: data,
			   data:'csrfToken=${csrfToken}',
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
	
	
  </script>
</body>
</html>