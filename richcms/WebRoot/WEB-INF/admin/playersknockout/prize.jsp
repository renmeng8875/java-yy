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
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<style type="text/css">
.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>
<script type="text/javascript">
	$(function() {
		var editor = KindEditor.create('textarea[name="playerList"]', {
				uploadJson      : '${ctx}/fileupload/kindeditor.do?region=pk&csrfToken=${csrfToken}',
				fileManagerJson : '${ctx}/fileupload/fileHandler.do?region=pk&csrfToken=${csrfToken}',
				allowFileManager : true,
				afterCreate : function() {
					var self = this;
					KindEditor.ctrl(document, 13, function() {
						self.sync();
						document.forms['playerListForm'].submit();
					});
					KindEditor.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['playerListForm'].submit();
					});
				}
			});
		
		$('#jpImageUrl').uploadify({
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
				$('#jpImageUrl').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	
        	var container = $("#container1");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px"  src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="jpImage" value="'+jsonObj.imageName+'" />');
	    }
	  });
		
		var jpImage = '${pk.jpImage}';
		var storageUrl='<c:url value="/upload/"/>';
		
		if(jpImage!="")
		{
			var jpImageJson = JSON2.parse(jpImage);
			var path=storageUrl+(jpImageJson[1]?jpImageJson[1].path:'');
			var jpPath=(jpImageJson[1]?jpImageJson[1].path:'');
			$("#container1").append('<div class="display-image" attr="displayImage"  dname="'+jpPath+'" title="点击显示原图"><a style="position: absolute;" href="'+path+'" target="blank"><img width="120px" height="120px" src="'+path+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
			$("#container1").append('<input type="hidden" name="jpImage" value="'+jpPath+'" />');
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
// 		divObj.remove();
// 		inputObj.remove();
	}});
	
		
	});
	
	
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;background-color: white;">
	<h2 class="adm-title"><a class="title">中奖名单编辑</a><a class="fr btn" href="javascript:history.go(-1);">+返回</a></h2>
<form id="fm_es" name="playerListForm" method="post" action="${ctx}/pk/prizeEdit.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input name="pkid" type="hidden" value="${pk.pkid}" />
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="mr20 m">本期栏目名称：</span>
        <input class="l mr20" name="contentid" id="contentid"  type="text"  value="${pk.pkName}" readonly="readonly" disabled="disabled"/>
      </li>

	 <li><span class="b">奖品图预览:</span>
      	<div id="container1"  wrap='imageContainer' style="height: 130px;width: 132px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input name="jpImageUrl" id="jpImageUrl"  type="file" />
     </li>
      
    </ul>
  </div>
  <span class="mr20 m" style="font-style: italic;font-size: 15px;">中奖名单编辑：</span>
  <textarea name="playerList" id="playerListid" style="width:680px;height:300px;">
  	<c:if test="${empty pk.playerList}">
  		活动进行中~~~活动结束后8个工作日内公布中奖名单，敬请留意！
  	</c:if>
  	<c:if test="${!empty pk.playerList}">
  		${pk.playerList}
  	</c:if>
  </textarea>
</div>
<div class="adm-btn-big" style="text-align: left;padding-left: 300px;"><input type="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</body>
</html>