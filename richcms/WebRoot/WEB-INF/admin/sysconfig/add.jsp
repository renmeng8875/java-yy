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
  <h2 class="adm-title"><a class="title">系统配置设置</a><a href="${ctx}/Sysconfig/lists.do" class="fr btn">配置列表</a></h2>
  <div style="margin:15px 20px;">
<form name="form1" id="form1" action="${ctx}/Sysconfig/add.do" method="post">
  <input type="hidden" name="csrfToken" value="${csrfToken}"/>
  <table style="font-size:12px;color:#444;line-height:32px;">
     <tr>
		<td width="80"><span class="need" >*</span>字段类型:</td>
		<td><input type="text" id="sc_systype" name="systype" datatype="s2-50" nullmsg="字段类型不能为空！" value="${data.systype}" class="l" /></td>
		<td><div class="Validform_checktip"></div></td>
	 </tr>
	 <tr>
		<td width="80"><span class="need" >*</span>字段名称:</td>
		<td> <input type="text" id="sc_syskey" name="syskey" datatype="s2-50" nullmsg="字段名称不能为空！"  value="${data.syskey}" class="l" ajaxurl="${ctx}/Sysconfig/checkName.do?csrfToken=${csrfToken}&pk=${data.sid}"/></td>
		<td><div class="Validform_checktip"></div></td>
	 </tr>
	 <tr>
		<td width="80"><span class="need" >*</span>字段值:</td>
		<td> <textarea rows="3" id="sc_sysvalue" cols="30" datatype="*2-1000" nullmsg="字段值不能为空！" name="sysvalue">${data.sysvalue}</textarea></td>
		<td><div class="Validform_checktip"></div></td>
	 </tr>
	 <tr>
		<td width="80"><span class="need" >*</span>字段描述:</td>
		<td>  <textarea rows="3" id="sc_mem" cols="30" datatype="s2-200" nullmsg="字段描述不能为空！"  name="mem">${data.mem}</textarea></td>
		<td><div class="Validform_checktip"></div></td>
	 </tr>
    <tr>
    <td  colspan="2" align="center">
       <c:if test="${data.sid>0 }"> 
        <input type="hidden" name="sid" id="editid" value="${data.sid}" />
       </c:if>
		<div class="adm-btn-big"><input id="btnSub" type="submit" value="保&nbsp;&nbsp;存" /></div>
    </td>
    </tr>
  </table>

</form>
</div>
</div></div>
 
<script type="text/javascript">
$(function(){
	$("#form1").Validform({
		tiptype:3
	});
})
</script>
</body>
</html>