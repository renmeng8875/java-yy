<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<link href="${ctx}/static/admin/js/kindeditor/themes/default/default.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<style type="text/css">
.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>
<script type="text/javascript">
	$(function() {
		var buttonText = "上传视频";
		var videoId = "${video.videoId}";
		if(videoId!=""){
			buttonText = "重新上传";
		}
		
		$('#videoUrl_id').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=pk&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.swf; *.flv; *.mp4', 
	    'auto'      : true,
	    'fileSizeLimit': '300MB',
	    'buttonText':buttonText,
	    'removeTimeout':0,
	    'onUploadSuccess':function(file, data, response)
	    {
        	var jsonObj = JSON2.parse(data);
        	var url = jsonObj.imageName+","+jsonObj.srcName;
        	if("${batch}"!="1")
        	{
        		$("input[name='videoUrl']").val('');
        		$("input[name='videoUrl']").val(url);
        			
        	}
        	else{
        		var text = '<tr><td width="25%" >视频地址</td><td width="75%" style="height:0px;text-align: left;"><input type="text"  name="videoUrl" style="width:600px;text-align: left;" readonly="readonly" value="'+url+'"/> </td></tr>'
        		var lastTr = $(".adm-table").find('tbody>tr:last');
        		lastTr.before(text);
        	}
        	
        	
	    }
	  });
		
	$("#videoForm").Validform({
		tiptype:3,
		label:".label",
		showAllError:false
	});
	var capture = '${video.capture}';
	if(capture!='')
	{	
		$("#container1").append('<div class="display-image" attr="displayImage" dname="'+capture+'" title="点击显示原图"><a style="position: absolute;margin:0px;" href="<c:url value="/upload/"/>'+capture+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+capture+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
		$("#container1").append('<input type="hidden" name="capture" value="'+capture+'" />');
	}
	$('#videoCapture').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=pk&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.jif', 
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
				$('#videoCapture').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	
        	var container = $("#container1");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;margin:0px;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="capture" value="'+jsonObj.imageName+'" />');
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
<body style="overflow-x: hidden; padding: 4px;background-color: white;">
<h2 class="adm-title"><a class="title">视频<c:if test="${batch eq 1}">批量</c:if>上传</a>
	<a class="fr btn" href="javascript:history.go(-1);">+视频管理</a></h2>
	
	<c:if test="${!empty video.videoId}">
		<form action="${ctx}/video/modify.do" method="post" id="videoForm">
	</c:if>
	<c:if test="${empty video.videoId}">
		<form action="${ctx}/video/add.do" method="post" id="videoForm">
	</c:if>
	<input type="hidden" name="csrfToken" value="${csrfToken}"/>
	<input type="hidden" name="videoId" value="${video.videoId}"/>
		<div class="adm-text ul-float">
			<table class="adm-table" width="100%">
				
				<tbody id="tbmain">
				 <c:if test="${batch eq 0}">
					
					<tr>
							<td width="25%" style="height:0px;"><span class="need" >*</span>视频名称</td>
							<td width="75%" style="height:0px;text-align:left;">
								   <input type="text" name="vname" style="width:600px;text-align: left;"  value="${video.vname}" datatype="*2-50" sucmsg="验证通过,名称可用!" nullmsg="栏目名称不能为空!" errormsg="栏目名称长度必须在2~50位字符范围内!"/> 
							</td>
					</tr>
					
					<tr>
							<td width="25%" >视频简介</td>
							<td width="75%" style="text-align:left;">
								   <input type="text" name="introduce" style="width:600px;text-align: left;"  value="${video.introduce}"/> 
							</td>
					</tr>	
					
					
					<tr>
							<td width="25%"><span class="need" >*</span>排序权重值</td>
							<td width="75%" style="text-align:left;">
								   <input type="text" name="sortNum" style="width:600px;text-align: left;"  value="${video.sortNum}" datatype="n" errorMsg="请输入整型数字"/> 
							</td>
					</tr>	
					
					<tr>
							<td width="25%">视频截图</td>
							<td width="75%" style="text-align:left;">
								  <div wrap="imageContainer" id="container1" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
							      </div>
							      <input id="videoCapture"  type="file" />
							</td>
					</tr>	
					
					
				</c:if>		
					
				    <tr>
						<td width="25%" >视频上传者</td>
						<td width="75%" style="text-align:left;">
							   <input type="text" name="uploader" style="width:600px;text-align: left;"  readonly="readonly" value="${video.uploader}${user.nickName}"/> 
						</td>
					 
					</tr>
					
					<tr>
						<td width="25%" >最后修改者</td>
						<td width="75%" style="text-align:left;">
							   <input type="text" name="lastModifier" style="width:600px;text-align: left;" readonly="readonly" value="${video.lastModifier}${user.nickName}"/> 
						</td>
					</tr>
					
					<c:if test="${batch eq 0}">
						<tr>
							<td width="25%"><span class="need" >*</span>视频地址</td>
							<td width="75%" style="text-align: left;">
								 <input type="text"  name="videoUrl" style="width:600px;text-align: left;"  value="${video.videoUrl}" datatype="*2-500" sucmsg="验证通过!" nullmsg="必须上传视频！" readonly="readonly"/> 
							</td>
						</tr>
					</c:if>
					
				   <tr>
						
						<td width="100%"  colspan="2">
							   <div style="padding-left: 45%;">	
							   <input type="file" id="videoUrl_id"  /> 
							   </div>	
						</td>
					 
					</tr>
				    
				</tbody>
			</table>
		</div>

		<div class="adm-btn-big" style="margin-top: 50px;"><input type="reset" value="重&nbsp;&nbsp;置" /><input type="submit" value="提&nbsp;&nbsp;交" /></div>	
	
	</form>
</body>
</html>