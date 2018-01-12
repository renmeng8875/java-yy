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
<h2 class="adm-title"><a class="title">工作流配置</a><a class="fr btn" href="${ctx}/Workflow/lists.do">+返回菜单管理</a></h2>

<form id="fm_es" method="post" action="${ctx}/Workflow/add.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10" style="margin:15px 20px;">
 <table style="font-size:12px;color:#444;line-height:32px;">
  <tr>
		<td width="80"><span class="need" >*</span>工作流名称:</td>
		<td><input style="width:196px;margin-left:4px;" type="text" datatype="*2-20" errormsg="工作流名称长度必须在2～20范围内!" nullmsg="请输入工作流名称！" value="${info.workflow}" name="workflow" id="workflow" class="l" ajaxurl="${ctx}/Workflow/checkName.do?csrfToken=${csrfToken}&id=${id}"/></td>
		<td><div class="Validform_checktip"></div></td>
  </tr>
  <tr>
		<td width="80"><span class="need" >*</span>描      述:</td>
		<td> <textarea class="l" style="height:100px;width:194px;margin-left:4px;" name="mem" id="mem" datatype="*2-50" errormsg="工作流名称长度必须在2～50个汉字范围内！">${info.mem}</textarea></td>
		<td><div class="Validform_checktip"></div></td>
  </tr>
    <tr>
		<td width="80">审核级数：</td>
		<td><select style="width:196px;margin-left:4px;" name="flowlevel" id="flowlevel">
          <option value="1" <c:if test="${info.flowlevel==1 }">selected</c:if>>一级审核</option>
          <option value="2" <c:if test="${info.flowlevel==2 }">selected</c:if>>二级审核</option>
          <option value="3" <c:if test="${info.flowlevel==3 }">selected</c:if>>三级审核</option>
          <option value="4" <c:if test="${info.flowlevel==4 }">selected</c:if>>四级审核</option>
        </select></td>
		<td><div class="Validform_checktip"></div></td>
  </tr>
  
    <tr>
	    <td  colspan="2" align="center">
	      <div class="adm-btn-big">
				<c:choose>
				  <c:when test="${id!=null}"><input name="flowid" type="hidden" value="${id}" /><input type="submit" value="修&nbsp;&nbsp;改" /></c:when>
				  <c:otherwise><input type="submit" value="提&nbsp;&nbsp;交" /></c:otherwise>
				</c:choose>
		 </div>
	    </td>
    </tr>
  </table>
</div>
</form>
</div></div>

<script type="text/javascript">
$(function(){
	$("#fm_es").Validform({
		tiptype:2
	});
})
</script>
</body>
</html>