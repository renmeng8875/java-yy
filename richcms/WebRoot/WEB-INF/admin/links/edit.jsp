<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友情链接修改</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<style type="text/css">
.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/swfobject.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	var storageUrl='<c:url value="/upload/"/>';
	var logo = '${linkinfo.logo}';
	if(logo!='')
	{
		try{
			var logoJson = JSON2.parse(logo);
			var path=storageUrl+(logoJson[1]?logoJson[1].path:'');
			var logoPath=(logoJson[1]?logoJson[1].path:'');
			$("#container1").append('<div class="display-image" attr="displayImage"  dname="'+logoPath+'" title="点击显示原图"><a style="position: absolute;" href="'+path+'" target="blank"><img width="120px" height="120px" src="'+path+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
			$("#container1").append('<input type="hidden" name="logo" value="'+logoPath+'" />');
		}catch(e){
		}
	}
	
	$('#logourl').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=fridendsLink&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
	    'auto'      : true,
	    'buttonText': '单张上传',
	    'removeTimeout':0,
	    'onUploadStart':function(item){
        	var container = $("#container1");
        	var children = $("div[class='display-image']",container);
        	if(children&&children.length>=1)
        	{
        		alert("最多只能上传1张图片！");
				$('#logourl').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	var container = $("#container1");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="logo" value="'+jsonObj.imageName+'" />');
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
	
	$("#fm_es").Validform({
		tiptype:2
	});
});
</script>
</head>
<body>
<div class="main_border" style="border:0;"><div id="my_main">
   <h2 class="adm-title"><a class="title">友情链接</a><a class="fr btn" href="${ctx}/Links/lists.do">+管理友情链接</a></h2>
<form name="fm_es" id="fm_es" method="post" action="${ctx}/Links/edit.do" >
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input class="l" name="linkid" id="linkid"  type="hidden" value="${linkinfo.linkid}" />
<div class="pad10">
<table style="font-size:12px;color:#444;line-height:32px;">
	
    <tr>
	     <td width="80">网站名称：</td>
		 <td> 
		 	<input style="width:188px;" class="l"  name="site" id="site" type="text" value="${linkinfo.site}" datatype="*2-50" sucmsg="验证通过,名称可用!" nullmsg="请输入网站名称" errormsg="网站名称长度必须在3～50范围内"   ajaxurl="${ctx}/Links/checkName.do?linkid=${linkinfo.linkid}&csrfToken=${csrfToken}"/>
		 </td>
		 <td><div class="Validform_checktip"></div></td>
	</tr>
	
	 <tr>
	     <td width="80">网站网址：</td>
		 <td> <input class="l" style="width:188px;" datatype="url" nullmsg="请输入网址链接" errormsg="网站链接格式不正确" name="linkurl" id="linkurl"  type="text" value="${linkinfo.linkurl}"/></td>
		 <td><div class="Validform_checktip"></div></td>
	</tr>
	 <tr>
	     <td width="80">网站优先级:</td>
		 <td> <input class="l" style="width:188px;" name="priority" id="priority"  type="text" value="${linkinfo.priority}" datatype="*1-3" errormsg="网站优先级长度必须在1-3范围内"/></td>
		 <td><div class="Validform_checktip"></div></td>
	</tr>
	
	 <tr>
	     <td width="80">网站LOGO：</td>
		 <td>  
		 	<div wrap="imageContainer" id="container1" style="height: 125px;width: 182px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;"></div>
        	<input name="logourl" id="logourl"  type="file" ignore="ignore"/>
         </td>
		 
	</tr>
	 <tr>
	     <td width="80">网站联系人:</td>
		 <td> <input class="l" style="width:188px;" datatype="s" nullmsg="请输入网站联系人" errormsg="真实姓名长度必须在2～20范围内" name="contact" id="contact"  type="text" value="${linkinfo.contact}" /></td>
		 <td><div class="Validform_checktip"></div></td>
	</tr>
	<tr>
	     <td width="80">网站描述:</td>
		 <td>  <textarea class="l" name="mem" id="mem" style="height:100px;width: 188px;">${linkinfo.mem}</textarea></td>
		 <td><div class="Validform_checktip"></div></td>
	</tr>
	 <tr>
	     <td colspan="2" align="center">
	     	 <div class="adm-btn-big"><input type="submit" id="submit" value="提&nbsp;&nbsp;交" /></div>
	     </td>
	 </tr>
  </table>
  
</div>

</form>
</div></div>

 
</body>
</html>