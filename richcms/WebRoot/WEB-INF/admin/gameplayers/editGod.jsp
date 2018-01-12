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
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<style type="text/css">
.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>
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
					"ns1-99":/^[1-9]\d{0,1}$/
				}
	});

	var storageUrl='<c:url value="/upload/"/>';
	var logo = '${gameplayer.imgpath}';
	if(logo!='')
	{
		try{
			var path=storageUrl+ logo;
			$("#container1").append('<div class="display-image" attr="displayImage"  dname="'+logo+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+logo+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+logo+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
			$("#container1").append('<input type="hidden" name="imgpath" value="'+logo+'" />');
		}catch(e){
		}
	}
$('#imgpath').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=pk&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
	    'auto'      : true,
	    'fileSizeLimit': '200KB',
	    'buttonText': '单张上传',
	    'removeTimeout':0,
	    'onUploadStart':function(item){
        	var container = $("#container1");
        	var children = $("div[class='display-image']",container);
        	if(children&&children.length>=1)
        	{
        		alert("最多只能上传1张图片！");
				$('#imgpath').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	var container = $("#container1");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="imgpath" value="'+jsonObj.imageName+'" />');
	    }
	  });
	  
	  $("div[wrap='imageContainer']").delegate("[attr='displayImage']",
	{
		mouseover:function()
		{
			$(this).removeClass("display-image").addClass("display-image-check");
			$(".l-icon-delete",$(this)).css("display","block");
		},
		mouseout:function()
		{
			$(this).removeClass("display-image-check").addClass("display-image");
			$(".l-icon-delete",$(this)).css("display","none");
		}
	});
	
	$("div[wrap='imageContainer']").delegate(".l-icon-delete",{click:function(){
		var imageName = $(this).parent().attr("dname");
		var divObj = $(this).parent();
		var inputObj = $(this).parent().siblings('input[value="'+imageName+'"]');
		divObj.remove();
		inputObj.remove();
		
	}});
 });
 
</script>
</head>
<body style="background-color: white;">
  <div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">玩家管理-修改大神信息</a>
</h2>
<form id="fm_es" action="${ctx}/gameGod/edit.do" method="post">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input type="hidden" name="playerid" value="${gameplayer.playerid}" />
<input type="hidden" name="isgod" value="${gameplayer.isgod}" />
<input type="hidden" name="pkid" value="${gameplayer.pkid}" />
<!-- 冗余玩家信息 -->
<input type="hidden" name="score" value="${gameplayer.score }">
<input type="hidden" name="measurement" value="${gameplayer.measurement }">
<input type="hidden" name="prizesid" value="${gameplayer.prizesid }">
<div class="pad10">
  <div class="adm-text ul-float">
    	<ul class="clearfix">
      <li>
      	<span class="mr0 m" style="float:left;">大神id：</span>
         ${gameplayer.playerid}
         </li>
      <li>
      	 <span class="mr0 m" style="float:left;">用户名：</span>
        <label for="playername"></label>
         <input type="text" id="playername" name="playername"  datatype="s2-20" nullmsg="用户名不能为空" errormsg="用户名不能超过20字符" ajaxurl="${ctx}/gamePlayers/checkName.do?csrfToken=${csrfToken}&pkid=${gameplayer.pkid}&playerid=${gameplayer.playerid}"  value="${gameplayer.playername}"/>
         <span id="contentTip"></span>
         <span>&nbsp;</span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">玩家争霸名称：</span>
        <span style="float:left;">
        <input type="text"  name="pkname" value="${gameplayer.pkname}"/></span>
        <span style="display:none" id="endTimeTips" class="Validform_wrong">dddd</span>
        <span>&nbsp;</span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">大神介绍：</span>
        <label for="playerdesc"></label>
        <textarea name="playerdesc" id="playerdesc" cols="45" rows="5" >${gameplayer.playerdesc}</textarea><span id="contentTip"></span>
      </li>
      <li>
      	<span class="mr0 m" style="float:left;">加赞数：</span>
        <label for="praisenum"></label>
        <input name="praisenum" id="praisenum" value="${gameplayer.praisenum}" datatype="ns" nullmsg="请输入加赞数" errormsg="加赞数必须为整数"><span id="contentTip"></span>
      </li>
        <li>
      	<span class="mr0 m" style="float:left;">电话：</span>
        <label for="phonenumber"></label>
        <input name="phonenumber" id="phonenumber" value="${gameplayer.phonenumber}" datatype="m|s0-0" errormsg="请输入正确的手机号码"><span id="contentTip"></span>
      </li>
       <li>
      	<span class="mr0 m" style="float:left;">QQ：</span>
        <label for="qq"></label>
        <input name="qq" id="qq" value="${gameplayer.qq}" datatype="ns" errormsg="QQ号码必须为整数"><span id="contentTip"></span>
      </li>
       <li>
      	<span class="mr0 m" style="float:left;">微博：</span>
        <label for="weibo"></label>
        <input name="weibo" id="weibo" value="${gameplayer.weibo}"><span id="contentTip"></span>
      </li>
       <li>
      	<span class="mr0 m" style="float:left;">微信：</span>
        <label for="weixin"></label>
        <input name="weixin" id="weixin" value="${gameplayer.weixin}"><span id="contentTip"></span>
      </li>
       <li>
      	<span class="mr0 m" style="float:left;">参赛视频id：</span>
        <label for="videoid"></label>
        <input name="videoid" id="videoid" value="${gameplayer.videoid}" datatype="ns" errormsg="请填写数字id"><span id="contentTip"></span>
      </li>
       <li>
      	<span class="mr0 m" style="float:left;">上传图片：</span>
        <label for="imgpath"></label>
        <div wrap="imageContainer" id="container1" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input name="imgpath" id="imgpath"  type="file" />
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