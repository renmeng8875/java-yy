<%@include file="../public/header.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>新增选择项</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!-- <link rel="stylesheet" type="text/css" href="styles.css"> -->
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet"	type="text/css" />

<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>

</head>

<body>
	<div class="main_border" style="border:0;">
		<div id="my_main">
		    <h2 class="adm-title"><a class="title">选择项配置</a><a href="${ctx}/choiceconfig/choicelist.do" class="fr btn">选择项列表</a></h2>
	        <div class="l-clear"></div>
			<div class="pad10">
				<form action="${ctx}/choiceconfig/add.do" method="post" id="form1">
					<input type="hidden" name="csrfToken" value="${csrfToken}" />
					<table style="font-size:12px;color:#444;line-height:32px;">
						<tr>
							<td width="80">选择项名称:</td>
							<td><input style="width:200px;" datatype="s2-20" nullmsg="请输入选择项名称" errormsg="选择项名称长度必须为2~20位字符" type="text" name="choicename" value="${choice.choicename }"></td>
							<td><div class="Validform_checktip"></div> </td>
						</tr>
						<tr>
							<td width="80">选择项标识符:</td>
							<td><input style="width:200px;" datatype="zhs2-50" nullmsg="请输入选择项名称" errormsg="必须为字母开头的2~50位字符" type="text" name="sign" value="${choice.sign }"></td>
							<td><div class="Validform_checktip"></div> </td>
						</tr>
						<tr>
							<td width="80">选择项描述:</td>
							<td><input style="width:200px;" datatype="s2-50" nullmsg="请输入选择项描述" errormsg="选择项描述长度必须为2~50位字符" type="text" name="choicedesc" value="${choice.choicedesc }"></td>
							<td><div class="Validform_checktip"></div> </td>
						</tr>
                        <tr>
							<td width="80">选择项弹出框脚本:</td>
							<td><textarea rows="5" cols="80" datatype="*2-1000" nullmsg="请输入选择项脚本" errormsg="选择项脚本长度必须为2~1000位字符" type="text" name="choicecode">${choice.choicecode }</textarea></td>
							<td><div class="Validform_checktip"></div> </td>
						</tr>
						<tr>
							<td width="80">选择项显示脚本:</td>
							<td><textarea rows="5" cols="80" datatype="*2-1000" nullmsg="请输入选择项脚本" errormsg="选择项脚本长度必须为2~1000位字符" type="text" name="viewcode">${choice.viewcode }</textarea></td>
							<td><div class="Validform_checktip"></div> </td>
						</tr>
						<tr>
						    <td  colspan="2" align="center">
						       <c:if test="${choice.choiceid>0 }"> 
						        <input type="hidden" name="choiceid" id="editid" value="${choice.choiceid}" />
						       </c:if>
								<div class="adm-btn-big"><input id="btnSub" type="submit" value="保&nbsp;&nbsp;存" /></div>
						    </td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>

	<script>
		$(function() {
			$("#form1").Validform({
				tiptype : 2,
				datatype : {
					"zhs2-20" : /^[a-zA-Z]{1}[a-zA-Z0-9_-]{1,19}$/,
					"zhs2-50" : /^[a-zA-Z]{1}[a-zA-Z0-9_-]{1,49}$/,
					"ns1-20" : /^[1-9]\d{0,3}$/,
					"ns1-99" : /^[1-9]\d{0,1}$/
				}
			});
		});
	</script>
</body>
</html>
