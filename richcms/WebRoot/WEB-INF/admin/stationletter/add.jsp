<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加站内信</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#fm_es").Validform({
		tiptype:3,
		label:".label",
		showAllError:false,
		datatype:{
			"spchar":function(gets,obj,curform,regxp){
				var reg1=/[~$\^&\\\/\|\.<>',"]/;
				if(reg1.test(gets)){
					return "包含特殊字符";
				}
				return true;
			}
		}
	});
})
</script>
</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title"><a class="title">站内信</a><a class="fr btn" href="javascript:history.go(-1);">+站内信管理</a></h2>
<form id="fm_es" method="post" action="${ctx}/StationLetter/addLetter.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li>
      	<span class="need" style="float:left;">*</span>
      	<span class="mr0 m" style="float:left;">标题：</span>
        <input class="l mr20" name="letter" id="letter"  type="text" datatype="spchar,*2-36" ajaxurl="${ctx}/StationLetter/checkName.do?csrfToken=${csrfToken}" nullmsg="请输入标题!" errormsg="标题长度必须在2~36位字符范围内!" stlye="width:180px;"/>
        <span>&nbsp;</span>
      </li>
      <li>
      	<span class="need" style="float:left;">*</span>
      	<span class="mr0 m" style="float:left;">置顶：</span>
        <input type="radio" value="0" name="isTop" />否
        <input type="radio" value="1" name="isTop" checked="checked"/>是
        <span>&nbsp;</span>
      </li>
      <li>
      	<span class="need" style="float:left;">*</span>
      	<span class="mr0 m" style="float:left;">内容：</span>
        <label for="textarea"></label>
        <textarea name="content" id="content" cols="50" rows="21" datatype="spchar,*1-1024" errormsg="内容长度必须为1-1024个字符！"   nullmsg="请输入内容!" ></textarea>
      </li>
	  
    </ul>
  </div>
</div>
<div class="adm-btn-big"><input type="submit" value="发&nbsp;&nbsp;送" /></div>
</form>
</body>
</html>