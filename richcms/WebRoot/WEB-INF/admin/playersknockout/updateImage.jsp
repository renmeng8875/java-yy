<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片上传</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<style type="text/css">
.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>

<script type="text/javascript">
$(function(){
	var nextPrizeImage = '${war.nextPrizeImage}';
	if(nextPrizeImage!='')
	{	
		$("#container1").append('<div class="display-image" attr="displayImage" dname="'+nextPrizeImage+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+nextPrizeImage+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+nextPrizeImage+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
		$("#container1").append('<input type="hidden" name="nextPrizeImage" value="'+nextPrizeImage+'" />');
	}
	$('#nextPrizeImage').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=pk&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
	    'auto'      : true,
	    'fileSizeLimit': '500KB',
	    'buttonText': '单张上传',
	    'removeTimeout':0,
	    'onUploadStart':function(item){
        	var container = $("#container1");
        	var children = $("div[class='display-image']",container);
        	if(children&&children.length>=1)
        	{
        		alert("最多只能上传1张图片！");
				$('#nextPrizeImage').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	
        	var container = $("#container1");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="nextPrizeImage" value="'+jsonObj.imageName+'" />');
	    }
	  });
	
	var godBackground = '${war.godBackground}';
	if(godBackground!='')
	{	
		$("#container2").append('<div class="display-image" attr="displayImage" dname="'+godBackground+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+godBackground+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+godBackground+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
		$("#container2").append('<input type="hidden" name="godBackground" value="'+godBackground+'" />');
	}
	
	$('#godBackground').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=pk&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
	    'auto'      : true,
	    'buttonText': '单张上传',
	    'fileSizeLimit': '500KB',
	    'removeTimeout':0,
	    'onUploadStart':function(item){
        	var container = $("#container2");
        	var children = $("div[class='display-image']",container);
        	if(children&&children.length>=1)
        	{
        		alert("最多只能上传1张图片！");
				$('#godBackground').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	
        	var container = $("#container2");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="godBackground" value="'+jsonObj.imageName+'" />');
	    }
	  });
	
	
	
	$("div[wrap='imageContainer']").delegate("[attr='displayImage']",
	{
		mouseover:function()
		{
			$(this).removeClass("display-image").addClass("display-image-check");
			$(".l-icon-delete",$(this)).show();
		},
		mouseout:function()
		{
			$(this).removeClass("display-image-check").addClass("display-image");
			$(".l-icon-delete",$(this)).hide();
		}
	});
	
	$("div[wrap='imageContainer']").delegate(".l-icon-delete",{click:function(){
		var imageName = $(this).parent().attr("dname");
		var divObj = $(this).parent();
		var inputObj = $(this).parent().siblings('input[value="'+imageName+'"]');
		  $.ajax({
              type: "post",
		      url: "${ctx}/fileupload/deleteImg.do?csrfToken=${csrfToken}",
		      data:{"pathimg":imageName},
		      success: function(rs){
		        if(rs.status=="1"){
		         	divObj.remove();
					inputObj.remove();
		        }else{
		          alert("删除失败");
		        }
		      }
            });
// 		divObj.remove();
// 		inputObj.remove();
	}});
    
	
	
});
</script>
</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title"><a class="title">公用图片上传</a>
	<a class="fr btn" href="javascript:history.go(-1);">+玩家争霸管理</a></h2>
<form id="fm_es" method="post" action="${ctx}/pk/updateImage.do" >
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      
      <li><span class="b">下期奖品图片:</span>
      	<div wrap="imageContainer" id="container1" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input  id="nextPrizeImage"  type="file" />
      </li>
      
      <li><span class="b">大神背景图:</span>
      	<div wrap="imageContainer" id="container2" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input id="godBackground"  type="file" />
      </li>
      
      
      
     
     
      
    </ul>
  </div>
</div>
<div class="adm-btn-big" style="padding-left: 150px;text-align:left;"><input type="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</body>
</html>