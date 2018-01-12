<%@include file="../../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加角色</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#fm_es").Validform({
		tiptype:3
	});	
});
</script>

</head>
<body>
<div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">添加角色</a><a class="fr btn" href="${ctx}/rolemanage/list.do">+角色管理</a></h2>
<form name="fm_es" id="fm_es" method="post" action="${ctx}/rolemanage/${opertype}.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10" >
  <div class="adm-text ul-float">
    <ul class="clearfix">
		<input class="l" name="roleId" id="roleId"  type="hidden" value="${appRole.roleId}" />
      <li><span class="mr20 m">角色名称：</span>
        <input class="l" name="roleName" id="roleName"  type="text" value="${appRole.roleName}" />
      </li>
      <li><span class="mr20 m">角色描述：</span>
		  <textarea name="roleMem" id="roleMem" rows="5" cols="40" class="l"  >${appRole.roleMem}</textarea>
      </li>
    </ul>
  </div>
</div>
<div class="adm-btn-big" style="text-align: left;padding: 10px;"><input type="submit" id="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</div></div>
</body>
</html>