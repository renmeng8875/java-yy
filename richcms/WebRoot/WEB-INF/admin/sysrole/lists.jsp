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
<style>  
table{width:100%;table-layout:fixed;}
.col1{width:10%;}
.col2{width:15%;} 
.col3{width:35%;} 
.col4{width:40%;}
.remark{white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}
</style>
</head>
<body>
  <div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title clearfix">
	<a class="fl title">角色管理</a>
	<a class="fr btn" href="${ctx}/SysRole/addrole.do?param=in">+添加角色</a>
</h2>
<div class="pad10">
  <table class="adm-table">
  <thead>
    <tr class="tr1">
      <th class="col1">角色id</th>
      <th class="col2">角色名称</th>
      <th class="col3">角色描述</th>
      <th class="col4">管理操作</th>
    </tr>
    </thead>
    <tbody>
	<c:forEach items="${roleList}" var="role" varStatus="status">
    <tr id="tr_${role.roleid}" class='<c:if test="${status.index % 2 == 1}" >tr2</c:if>'>
      <td width="15%">${role.roleid}</td>
      <td width="25%">${role.roleName}</td>
      <td width="35%" class="remark">${role.mem}</td>
      <td width="25%">
		  <a href="${ctx}/SysRole/grantsys.do?roleid=${role.roleid}">系统权限</a>
		  
		  <c:if test="${role.roleid!=1}">
		   <a href="${ctx}/SysRole/grantcat.do?roleid=${role.roleid}">栏目权限</a>
		  </c:if>
		  <c:if test="${role.roleid==1}">
		   <a style="TEXT-DECORATION: line-through;color:#ccc;">栏目权限</a>
		  </c:if>
		  <a href="${ctx}/SysRole/membermanage.do?roleid=${role.roleid}">成员管理</a>
		  <a href="${ctx}/SysRole/editrole.do?roleid=${role.roleid}">修改</a>
		  <a href="javascript:;" tid="tr_${role.roleid}"  op="${ctx}/SysRole/deleterole.do?roleid=${role.roleid}"> 删除</a>
	  </td>
    </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

  </div></div>
  <script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
  <script type="text/javascript">
 
	$("a[tid]").click(function()
	{
		
		var tid = $(this).attr("tid");
		var data = $(this).attr("op");
		if(confirm("你确定删除该角色吗?"))
		{
		   $.ajax({
			   type: "POST",
			   url: data,
			   data:"csrfToken=${csrfToken}",
			   success: function(msg)
			   {
		     		if(msg.status=='0')
		     		{
			   			alert("删除成功！");	
		     			$("#"+tid).remove();
		     		}
		     		if(msg.status=='1')
		     		{
		     			var nickNamestr = msg.nickNames;
		     			alert("当前角色正在被  "+nickNamestr+"使用,不允许删除！")
		     		}
		     		if(msg.status=='2')
		     		{
		     			alert("当前角色属于超级系统管理员,不允许删除！")
		     		}
			   }
		   });
		}
	});
	
	
  </script>
  
  
</body>
</html>