<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>站内信</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<style>
	.letterTitle {
	font-family: 微软雅黑,Arial,Helvetica;
	font-size: 24px;
	line-height: 24px;
	padding: 20px 0 10px;
	text-align:center
	}
	
	.letterInfo {
	padding:10px 120px;
	text-align:right;
	}
	
	.letterContent {
		border: 1px solid #D9D9D9;
	margin: 0 100px 10px 100px;
	min-height: 100px;
	text-align: left;
	padding: 10px;
	font-size:18px;
	}
	
	.letterCalled {
		font-family: 微软雅黑,Arial,Helvetica;
	font-size: 22px;
	line-height: 24px;
	padding: 10px;
	}
</style>
<script type="text/javascript">



</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title"><a class="title">站内信</a><a class="fr btn" href="javascript:history.go(-1);">+站内信管理</a></h2>
	<div>
		<!-- 标题栏 -->
		<div class="letterTitle">
			<span>${letter.letter}</span>
		</div>
		<!-- 信息栏 -->
		<div class="letterInfo">
			<span>发送人：${letter.userNickName}&nbsp;&nbsp;&nbsp;&nbsp;时间：${letter.publishTimeStr}</span>
		</div>
		
		<!-- 内容栏 -->
		<div class="letterContent">
			<!-- 称呼栏 -->
			<div class="letterCalled">
				<span>${letter.reciversStr}：</span>
			</div>
			&nbsp;&nbsp;&nbsp;&nbsp;<span>${letter.content}</span>
		</div>
	</div>
</body>
</html>