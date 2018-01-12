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
<link href="${ctx}/static/admin/js/kindeditor/themes/default/default.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/jqueryui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
<style type="text/css">
.display-image{height: 104px;width: 104px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 104px;width: 104px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>

<script type="text/javascript">
	$(function() {
		KindEditor.create('textarea[name="introduce"]', 
		{
			uploadJson      : '${ctx}/fileupload/kindeditor.do?region=pk&csrfToken=${csrfToken}',
			fileManagerJson : '${ctx}/fileupload/fileHandler.do?region=pk&csrfToken=${csrfToken}',
			allowFileManager : true,
			mode:'absolute',
			afterCreate : function() 
			{
				var self = this;
				KindEditor.ctrl(document, 13, function() {
					self.sync();
					document.forms['PkContentEdit'].submit();
				});
				KindEditor.ctrl(self.edit.doc, 13, function() {
					self.sync();
					document.forms['PkContentEdit'].submit();
				});
			}
		});
	
		
		KindEditor.create('textarea[name="evaluation"]', 
		{
			uploadJson      : '${ctx}/fileupload/kindeditor.do?region=pk&csrfToken=${csrfToken}',
			fileManagerJson : '${ctx}/fileupload/fileHandler.do?region=pk&csrfToken=${csrfToken}',
			mode:'absolute',
			allowFileManager : true,
			afterCreate : function() 
			{
				var self = this;
				KindEditor.ctrl(document, 13, function() {
					self.sync();
					document.forms['PkContentEdit'].submit();
				});
				KindEditor.ctrl(self.edit.doc, 13, function() {
					self.sync();
					document.forms['PkContentEdit'].submit();
				});
			}
		});
		
		KindEditor.create('textarea[name="joinway"]', 
		{
			uploadJson      : '${ctx}/fileupload/kindeditor.do?region=pk&csrfToken=${csrfToken}',
			fileManagerJson : '${ctx}/fileupload/fileHandler.do?region=pk&csrfToken=${csrfToken}',
			mode:'absolute',
			allowFileManager : true,
			afterCreate : function() 
			{
				var self = this;
				KindEditor.ctrl(document, 13, function() {
					self.sync();
					document.forms['PkContentEdit'].submit();
				});
				KindEditor.ctrl(self.edit.doc, 13, function() {
					self.sync();
					document.forms['PkContentEdit'].submit();
				});
			}
		});
		
	
		
		
		$("#fm_es").Validform({
			tiptype:2,
			beforeSubmit:function(){
			    if($("#contentid").hasClass("Validform_error")||$("#nextContentid").hasClass("Validform_error"))
			    {
			    	return false;
			    }	
				var appTypes = $("input[name='appType']"),nextAppTypes = $("input[name='nextAppType']"),appType=0,nextAppType=0;
				for(var i=0;i<appTypes.length;i++)
				{
					if(appTypes[i].checked==true)
					{
						appType = appTypes[i].value;
						break;
					}	
				}					
				for(i=0;i<nextAppTypes.length;i++)
				{
					if(nextAppTypes[i].checked==true)
					{
						nextAppType = nextAppTypes[i].value;
						break;
					}	
				}
				
				
				if(appType==nextAppType&&$("#nextContentid").val()==$("#contentid").val()&&$("#contentid").val()!='')
				{
					alert("本期游戏不能和下期游戏相同");
					$("#nextContentid").val("");
					$("#nextContentid").focus();
					$("#nextContentid").addClass("Validform_error");
					$("span",$("#div_parent")).hide();
					return false;
				}
				
				var container = $("#container2"),children=$(".display-image",container);
				for(var i=0;i<children.length;i++)
				{
					var inputobj = $("input",$(children[i]));
					var imageName = inputobj.val();
					if(imageName.indexOf(",")==-1)
					{
						inputobj.val(imageName+","+(i+1));
					}
				}
				
				return true;
			}
		});
		
		$("input[name='appType']").bind("click",function(){
			var contentid = $("#contentid").val();
			var url = "${ctx }/pk/getAppName.do?csrfToken=${csrfToken}&appType="+this.value+"&contentid="+contentid;
		 	$.ajax({
			   type: "POST",
			   url: url,
			   success: function(msg)
			   {
			     if(msg.status=='y')
			     {
			    	 $("#contentidTip").addClass("Validform_right").removeClass("Validform_wrong").text(msg.info);
			     	 $("#contentid").removeClass("Validform_error");
			     }	 
			     else{
			    	 $("#contentidTip").addClass("Validform_wrong").removeClass("Validform_right").text(msg.info)
			     	 $("#contentid").addClass("Validform_error");
			    	
			     }
			   }
			});
		});
		
		$("#contentid").bind("blur",function(){
			var contentid = $("#contentid").val();
			var value = 0;
			$("input[name='appType']").each(function(){
				if(this.checked==true)
				{
					value = this.value;
				}	
			});
			
			var url = "${ctx }/pk/getAppName.do?csrfToken=${csrfToken}&appType="+value+"&contentid="+contentid;
		 	$.ajax({
			   type: "POST",
			   url: url,
			   success: function(msg)
			   {
			     if(msg.status=='y')
			     {
			    	 $("#contentidTip").addClass("Validform_right").removeClass("Validform_wrong").text(msg.info);
			     	 $("#contentid").removeClass("Validform_error");
			     }	 
			     else{
			    	 $("#contentidTip").addClass("Validform_wrong").removeClass("Validform_right").text(msg.info)
			     	 $("#contentid").addClass("Validform_error");
			     }
			   }
			});
		});
		
		$("input[name='nextAppType']").bind("click",function(){
			var contentid = $("#nextContentid").val();
			var url = "${ctx}/pk/getAppName.do?csrfToken=${csrfToken}&appType="+this.value+"&contentid="+contentid;
		 	$.ajax({
			   type: "POST",
			   url: url,
			   success: function(msg)
			   {
			     if(msg.status=='y')
			     {
			    	 $("#nextcontentidTip").addClass("Validform_right").removeClass("Validform_wrong").text(msg.info);
			     	 $("#nextContentid").removeClass("Validform_error");
			     }	 
			     else{
			    	 $("#nextcontentidTip").addClass("Validform_wrong").removeClass("Validform_right").text(msg.info)
			     	 $("#nextContentid").addClass("Validform_error");
			    	
			     }
			   }
			});
		});
		
		$("#nextContentid").bind("blur",function(){
			var contentid = $("#nextContentid").val();
			var value = 0;
			$("input[name='nextAppType']").each(function(){
				if(this.checked==true)
				{
					value = this.value;
				}	
			});
			
			var url = "${ctx }/pk/getAppName.do?csrfToken=${csrfToken}&appType="+value+"&contentid="+contentid;
		 	$.ajax({
			   type: "POST",
			   url: url,
			   success: function(msg)
			   {
			     if(msg.status=='y')
			     {
			    	 $("#nextcontentidTip").addClass("Validform_right").removeClass("Validform_wrong").text(msg.info);
			     	 $("#nextContentid").removeClass("Validform_error");
			     }	 
			     
			   }
			});
		});
		
		
		var storageUrl='<c:url value="/upload/"/>';
		$('#jsonimg').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=pk&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
	    'auto'      : true,
	    'fileSizeLimit': '500KB',
	    'buttonText': '批量上传',
	    'removeTimeout':0,
	    'onUploadStart':function(item){
        	var container = $("#container2");
        	var children = $("div[class='display-image']",container);
        	if(children&&children.length>=6)
        	{
        		alert("最多只能上传6张图片！");
				$('#jsonimg').uploadify('cancel','*');	

        	}
	    },
	    
	    'onUploadSuccess':function(file, data, response)
	    {
        	
        	var container = $("#container2");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image"  attr="displayImage" ' +
        	'dname="'+jsonObj.imageName+'" title="点击显示原图"><input type="hidden" ' +
        	'name="jsonimg"  value="'+jsonObj.imageName+'" />' +
        	'<a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" ' +
        	'target="blank"><img width="104px" height="104px"' +
        	' src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute;' +
        	' margin-left:84px;display:none;cursor:hand;" ' +
        	'class="l-icon l-icon-delete"></div></div>');
	    }
	  });
	var imgstr = '${pk.gameImages}';
	
	if(imgstr!='')
	{
		var imgs = JSON2.parse(imgstr);
		for(var i=1;i<7 ;i++)
		{
			if(imgs[i])
			{
				var item=imgs[i];
				var path=storageUrl+item.path;
				var imagePath=item.path;
				$("#container2").append('<div class="display-image" attr="displayImage" dname="'+imagePath+'" title="点击显示原图"><input type="hidden" name="jsonimg" value="'+imagePath+'" /><a style="position: absolute;" href="'+path+'" target="blank"><img width="104px" height="104px" src="'+path+'"/></a><div style="position: absolute; margin-left:84px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
			}
		}
	}
	
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
	
		
	}});
		
	$("div[wrap='imageContainer']").sortable({ axis: "x" }); 


	
	});
	
	
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;background-color: white;">
	<h2 class="adm-title"><a class="title">内容编辑</a><a class="fr btn" href="javascript:history.go(-1);">+返回</a></h2>
<form id="fm_es" name="PkContentEdit" method="post" action="${ctx}/pk/contentEdit.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input name="pkid" type="hidden" value="${pk.pkid}" />
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
    
      
   <table style="width:700px;" border="1" cellspacing="0" bordercolor="gray" cellpadding="2">
	<tr>
	  <td  width="20%"><span class="need" style="float:left;">*</span><span style="font-style: italic;font-size: 15px;">本期游戏</span> </td>
	  <td width="50%">
	    <input class="l mr20" name="contentid" id="contentid"  datatype="n" nullmsg="应用ID不能为空" type="text"  value="${pk.contentid}"  />
        <input name="appType"  type="radio"  value="0" <c:if test="${pk.appType==0 or (empty pk.appType)}">checked="checked" </c:if>/>android   &nbsp;&nbsp;&nbsp;&nbsp;
        <input name="appType"  type="radio"  value="1" <c:if test="${pk.appType==1 }">checked="checked" </c:if>  />ios
      </td>
      <td><div id="contentidTip" class="Validform_checktip"></div></td>
	</tr>
	
	
	<tr>
	  <td width="20%"><span style="font-style: italic;font-size: 15px;">下期游戏</span></td>
	  <td width="50%"><div id="div_parent">
	    <input class="l mr20" name="nextContentid" id="nextContentid" datatype="n" type="text"  value="${pk.nextContentid}" ignore="ignore"/>
        <input name="nextAppType"  type="radio"  value="0"  <c:if test="${pk.nextAppType==0 or (empty pk.appType)}">checked="checked" </c:if> />android   &nbsp;&nbsp;&nbsp;&nbsp;
        <input name="nextAppType"  type="radio"  value="1" <c:if test="${pk.nextAppType==1 }">checked="checked" </c:if> />ios
        </div>
        <td><div id="nextcontentidTip" class="Validform_checktip"></div></td>
      </td>
	</tr>
	
	<tr>
	  <td width="20%"><span style="font-style: italic;font-size: 15px;">下期标题</span></td>
	  <td colspan="2">
	  <input class="l mr20" type="text" name="nextName"  width="300px" value="${pk.nextName}"/><span>&nbsp;(填入期数+游戏名)</span>
      </td>
	</tr>
	
	<tr>
	  <td width="20%"><span class="need" style="float:left;">*</span><span style="font-style: italic;font-size: 15px;">微博话题墙代码</span></td>
	  <td colspan="2">
	  <textarea name="code" id="code" cols="45" rows="5"  datatype="*2-800" sucmsg="验证通过!" nullmsg="微博话题墙代码不能为空!"  errormsg="微博话题墙代码长度必须在2~800位字符范围内!" >${pk.code}</textarea>
      </td>
	</tr>
	
	<tr>
	  <td width="20%"><span style="font-style: italic;font-size: 15px;">下期预告内容</span></td>
	  <td colspan="2">
	  <textarea name="nextTipcontent" id="nextTipcontent" cols="45" rows="5"  datatype="*2-800" sucmsg="验证通过!" ignore="ignore" errormsg="微博话题墙代码长度必须在2~800位字符范围内!" >${pk.nextTipcontent}</textarea>
      </td>
	</tr>
	
	
	</table>


  

  <li><span class="b">游戏截图预览:</span>
      	<div wrap="imageContainer" id="container2" style="height: 125px;width: 690px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input id="jsonimg"  type="file" />
  </li>
    </div>
  <br />
  <span class="mr20 m" style="font-style: italic;font-size: 16px;">本期活动介绍：</span>
  <textarea name="introduce" style="width:700px;height:200px;" >${pk.introduce}</textarea>
  <br />
  <span class="mr20 m" style="font-style: italic;font-size: 16px;">活动参与方式：</span>
  <textarea name="joinway" style="width:700px;height:200px;" >${pk.joinway}</textarea>
  <br />
  <span class="mr20 m" style="font-style: italic;font-size: 16px;">游戏评测：</span>
  <textarea name="evaluation" style="width:700px;height:200px;" >${pk.evaluation}</textarea>
  

</div>
<div class="adm-btn-big" style="text-align: left;padding-left: 300px;"><input type="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</body>
</html>