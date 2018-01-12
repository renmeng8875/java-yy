<%@include file="../public/header.jsp"%>
<%@page import="com.richinfo.common.TokenMananger"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>main</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
		<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	$("#fetchIosForm").submit(function(){
		var appIds = $("#appids").val();
		if(appIds=='')
		{
			alert("请输入待抓取的应用ID！");
			return false;
		}
		var idArray = appIds.split(",");
		var Reg = new RegExp("^[0-9]{1,12}$");
		for(var i=0;i<idArray.length;i++)
		{
			if(!Reg.test($.trim(idArray[i]))) 
			{
				alert("输入ID号只能是12位以内数字,多个ID以英文逗号隔开！");
				return false;
			}
		}
		
		
		
		
	});
	
	
	
});
</script>
	</head>
	<body style="overflow-x: hidden; padding: 4px;">
	    <div style="color: red;font-size:14px;padding:20px;">请在下方文本框中输入待抓取的应用ID,多个ID之间用半角英文逗号隔开</div>
	   
	    <form id="fetchIosForm" action="${ctx}/taskcenter/list.do" method="post">
	    <input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
		<div style="margin:20px;">
		    <textarea  rows="20" cols="100" name="appids" id="appids"></textarea>
		</div>
		<div class="adm-btn-big" style="text-align: left;padding-left: 250px;">
		<input id="btnSub" type="submit" value="提&nbsp;&nbsp;交" />
		</div>
		</form>
	</body>
</html>
