<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/TreeSelector.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
<style type="text/css">
.display-image{height: 98px;width: 98px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 98px;width: 98px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}

</style>
<script type="text/javascript">
$(function(){
	var treedata = ${treeData};
	var settings = {selectName:"contentcat",
					selectId:"selectCatid",
					data:treedata,
					containerId:"contentcatId",
					selectLevel:"catLevel",
					optionValue:"catId",
					subSet:"children",
					optionText:"catName"};
	var treeSelector = new TreeSelector(settings);
	  var uploader = $('#jsonimg').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?csrfToken=${csrfToken}&region=default',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
	    'auto'      : true,
	    'buttonText': '批量上传',
	    'removeTimeout':0,
	    'onUploadStart':function(item){
        	var container = $("#imageContainer");
        	var children = $("div[class='display-image']",container);
        	if(children&&children.length>=2)
        	{
        		alert("最多只能上传2张图片！");
				$('#jsonimg').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	var container = $("#imageContainer");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="97px" height="97px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:82px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="jsonimg" value="'+jsonObj.imageName+'" />');
	    }
	  });
	
	$("#imageContainer").delegate("[attr='displayImage']",
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
	
	$("#imageContainer").delegate(".l-icon-delete",{click:function(){
		var imageName = $(this).parent().attr("dname");
		var divObj = $(this).parent();
		var inputObj = $(this).parent().siblings('input[value="'+imageName+'"]');
		divObj.remove();
		inputObj.remove();
		
	}});
	$("#fm_es").Validform({
			tiptype:3
		});
	
});
	

</script>

</head>
<body style=" padding: 4px;background-color: #fff;" >
	<h2 class="adm-title"><a class="title">MM之旅添加</a><a class="fr btn" href="javascript:history.go(-1);">+返回</a></h2>
<form id="fm_es" method="post" action="${ctx}/TravelSet/addtravelset.do" >
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix"> 
      <li>
      <span class="need" style="float:left;">*</span>
      <span  style="width:280px;" >MM之旅中文标识：</span>
        <input type="text" name="travelname" id="travelname"  datatype="*2-50" ajaxurl="${ctx}/TravelSet/checkName.do?csrfToken=${csrfToken}"/>
      </li>
      <li>
      <span class="need" style="float:left;">*</span>
      <span style="width:280px;" >MM之旅英文标识：</span>
        <input type="text" name="travel" id="travel" datatype="*2-50"/>
      </li>      
      <li><span class="b">标签类:</span>
        <select id="tagtype" name="tagtype">
        	<option value="os" >手机系统</option>
        	<option value="tag" >身份标签</option>
        	<option value="like" >个人爱好</option>                        
        </select>
        
      </li>
      <li><span class="b">对应的栏目:</span>
      <span id="contentcatId" >
	   </span>
		
      </li>
      <li><span class="b">图片缩略图预览:</span>
      	<div id="imageContainer" style="height:120px;width: 220px;overflow: hidden;padding: 2px;border:2px solid gray;margin-left:10px;margin-bottom: 4px;">
      	</div>
        <input id="jsonimg"  type="file" />

        
      </li>

    </ul>
  </div>
</div>
<div class="adm-btn-big" style="text-align: left;padding-left: 10px;">
<input type="submit" id="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</body>
</html>