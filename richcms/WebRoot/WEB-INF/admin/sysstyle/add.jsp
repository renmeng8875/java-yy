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
<h2 class="adm-title"><a class="title">模版管理</a><a href="${ctx }/Sysstyle/lists.do" class="fr btn">模版配置列表</a></h2>
<form name="form1" id="form1" action="${ctx }/Sysstyle/upload.do" method="post" enctype="multipart/form-data">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
	<table style="font-size:12px;color:#444;line-height:32px;">
	 <tr>
	     <td width="120"><span class="need" style="float:left;">*</span>风格标识：</td>
		 <td><input name="style"  id="ss_style" type="text" datatype="az2-50" nullmsg="请输入风格标识" errormsg="只能输入英文字符或者下划线 " ajaxurl="${ctx}/Sysstyle/checkStyle.do?csrfToken=${csrfToken}&styleid=${style.styleid}" value="${style.style}" style="width: 180px;"/></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	  <tr>
	     <td width="120"><span class="need" style="float:left;">*</span>风格名称：</td>
		 <td> <input name="stylename"  id="ss_stylename" datatype="*2-50" nullmsg="请输入风格名称" ajaxurl="${ctx}/Sysstyle/checkStyleName.do?csrfToken=${csrfToken}&styleid=${style.styleid}" value="${style.stylename}" type="text" style="width: 180px;"/></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	  <tr>
	     <td width="120">风格作者：</td>
		 <td><input name="author"  value="${style.author}" datatype="*2-50" type="text" style="width: 180px;" ignore="ignore"/></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	  <tr>
	     <td width="120">风格版本：</td>
		 <td> <input name="version"   value="${style.version}" type="text" datatype="*2-50" style="width: 180px;" ignore="ignore"/></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	  <tr>
	     <td width="120">模板文件包(*.zip)：</td>
		 <td>  <input name="file" class="l" type="file" id="ufile" /></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	 <tr>
	    <td colspan="2" align="center">
			<c:if test="${style!=null }">
			<input type="hidden" id="editid" name="styleid" value="${style.styleid}" />
			</c:if>
			<input type="hidden" id="ajaxurl" value="${ctx }/Sysstyle/ajaxCheck.do" />
			<div class="adm-btn-big"><input id="btnSub" type="submit" value="保&nbsp;&nbsp;存" /></div>
		</td>
	 </tr>
  </table>
</div>
</form>

</div></div>
 
<script>
$(function(){
	$("#ufile").change(function(){
		if($(this).val()!='' && $(this).val()!='undefined')
		{
			var ab = $(this).val().toLowerCase();
			if(ab.substring(ab.lastIndexOf('.')+1) != 'zip')
			{
				alert('文件类型错误');
				$(this).val('');
			}
		}
	});
	 
	 $("#form1").Validform({
		tiptype:2,
		datatype:{
			"az2-50":/^([A-Za-z_]+){2,50}$/
		},
		beforeSubmit:function()
		{
		 	if($("#ufile").val()=='')
		 	{
	        	alert("请上传模板文件");
	        	return false;
	     	}
		 	var zipName = $("#ufile").val();
		 	if(zipName!='' && zipName!='undefined')
		 	{
		 		zipName = zipName.toLowerCase();
		 		zipName = zipName.substring(zipName.lastIndexOf(".")+1)
		 	}
		 	if(zipName!='zip')
		 	{
		 		alert('文件类型错误！');
		 		$("#ufile").val()=='';
		 		return false;
		 	}
		}
	});
});
</script>
</body>
</html>