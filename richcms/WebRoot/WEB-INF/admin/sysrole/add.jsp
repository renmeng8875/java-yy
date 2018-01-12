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
<script type="text/javascript">
$(function(){
	$("#fm_es").Validform({
		tiptype:3
	});
})
</script>
</head>
<body style="background-color: white;">
 <h2 class="adm-title"><a class="title">角色管理 - 添加</a><a class="fr btn" href="${ctx}/SysRole/lists.do">+角色管理</a></h2>
<form id="fm_es" action="${ctx}/SysRole/addrole.do" method="post">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="need" style="float:left">*</span><span class="b fl">角色名称:</span>
        <input type="text" value="" class="l" name="rolename" id="rolename" datatype="s2-25" nullmsg="请输入角色名称!"  ajaxurl="${ctx}/SysRole/checkName.do?csrfToken=${csrfToken}"/>
      </li>
      <li><span class="need" style="float:left">*</span><span class="b fl">角色描述:</span><textarea class="l" style="height:100px; width:146px;" name="mem" id="mem" datatype="*2-50" nullmsg="请输入角色描述!" ></textarea>
      </li>
    </ul>
  </div>
</div>
<div class="adm-btn-big">
<input type="submit" value="提交" />
</div>
</form>
</body>
</html>