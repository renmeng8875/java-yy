<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">



</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title"><a class="title">公告</a><a class="fr btn" href="javascript:history.go(-1);">+公告管理</a></h2>
 
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="mr20 m">公告标题：</span>
      ${announce.announce}
      </li>
      <li><span class="mr20 m">起始日期：</span>
      ${announce.startTimeStr}
      </li>
      <li><span class="mr20 m">结束日期：</span>
      ${announce.endTimeStr}
      </li>
      <li><span class="mr20 m">公告内容：</span>
      ${announce.content}
      </li>
     
    </ul>
  </div>
</div>
</body>
</html>