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
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
<script src="${ctx}/static/admin/js/uploadify/swfobject.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#fm_es").Validform({
		tiptype:3,
		datatype:{
					"ns":/^\+?(?!0)\d*$/,
					"ns1-20":/^(?:0|[1-9][0-9]*)$/
				}
	});
 });
</script>
</head>
<body style="background-color: white;">
  <div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">玩家争霸-玩家管理</a>
</h2>
<form id="fm_es" action="${ctx}/gamePlayers/edit.do" method="post">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input type="hidden" name="playerid" value="${gameplayer.playerid}" />
<input type="hidden" name="isgod" value="${gameplayer.isgod}" />
<input type="hidden" name="pkid" value="${gameplayer.pkid}" />
<!-- 冗余大神信息 -->
<input type="hidden" name="playerdesc" value="${gameplayer.playerdesc }">
<input type="hidden" name="praisenum" value="${gameplayer.praisenum }">
<input type="hidden" name="qq" value="${gameplayer.qq }">
<input type="hidden" name="weixin" value="${gameplayer.weixin }">
<input type="hidden" name="weibo" value="${gameplayer.weibo }">
<input type="hidden" name="videoid" value="${gameplayer.videoid }">
<input type="hidden" name="imgpath" value="${gameplayer.imgpath }">
<input type="hidden" name="pkname" value="${gameplayer.pkname }">
<input type="hidden" name="pk" value="${gameplayer.pk }">
<div class="pad10">
  <div class="adm-text ul-float">
    	<ul class="clearfix">
      <li>
      	<span class="mr0 m" style="float:left;">玩家id：</span>
         ${gameplayer.playerid}
         </li>
      <li>
      	 <span class="mr0 m" style="float:left;">用户名：</span>
         <span style="float:left;"><input type="text" id="playername" name="playername" ajaxurl="${ctx}/gamePlayers/checkName.do?csrfToken=${csrfToken}&pkid=${gameplayer.pkid}&playerid=${gameplayer.playerid}"  datatype="s2-20" nullmsg="用户名不能为空" errormsg="用户名不能超过20字符" value="${gameplayer.playername}"/></span>
         <span style="display:none" id="startTimeTips" class="Validform_wrong">ee</span>
         <span>&nbsp;</span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">玩家争霸名称：</span>
        <span style="float:left;">
       	 ${gameplayer.pkname}
         </span>
        <span style="display:none" id="endTimeTips" class="Validform_wrong">dddd</span>
        <span>&nbsp;</span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">手机号：</span>
        <label for="phonenumber"></label>
        <input name="phonenumber" id="phonenumber" value="${gameplayer.phonenumber}" datatype="m" ignore="ignore" nullmsg="请输入玩家手机号码" errormsg="请输入正确的手机号码"><span id="contentTip"></span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">成绩：</span>
        <label for="score"></label>
        <input name="score" id="score" value="${gameplayer.score}" datatype="ns1-20" nullmsg="玩家成绩不能为空" errormsg="玩家成绩不能超过20位"><span id="contentTip"></span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">单位：</span>
        <label for="measurement"></label>
        <input name="measurement" id="measurement" value="${gameplayer.measurement}" datatype="s1-5" nullmsg="单位不能为空" errormsg="单位必须为1~5位字符"><span id="contentTip"></span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">奖品：</span>
        <label for="prizesname"></label>
        <select name="prizesid">
           <option value="0" >--请选择获奖奖品--</option>
	       <c:forEach items="${prizeslist }" var="item">
	      	 <option value="${item.prizesid }" <c:if test="${gameplayer.prizesid==item.prizesid }">selected="selected"</c:if>>${item.prizesname }</option>
	       </c:forEach>
        </select>
       <span id="contentTip"></span>
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