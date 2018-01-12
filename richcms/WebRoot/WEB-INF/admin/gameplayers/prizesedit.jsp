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
	var storageUrl='<c:url value="/upload/"/>';
	var prizesimg = '${gameprizes.prizesimg}';
	if(prizesimg!='')
	{
		try{
			$("#container1").append('<div class="display-image" attr="displayImage"  dname="'+prizesimg+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+prizesimg+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+prizesimg+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
			$("#container1").append('<input type="hidden" name="prizesimg" id="hprizesimg" value="'+prizesimg+'" />');
		}catch(e){
		}
	}
$('#prizesimg').uploadify({
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
				$('#prizesimg').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	var container = $("#container1");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="prizesimg" id="hprizesimg" value="'+jsonObj.imageName+'" />');
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
<body style="background-color: white;">
  <div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">玩家争霸-添加奖品</a>
  <a class="fr btn" href="${ctx}/gamePrizes/lists.do?pkid=${pkid}">+奖品列表</a>
</h2>
<form id="fm_es" action="${ctx}/gamePrizes/edit.do" method="post" onSubmit="return chkForm(this)">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input type="hidden" name="prizesid" value="${gameprizes.prizesid}" />
<input type="hidden" name="pkid" value="${gameprizes.pkid}" />
<div class="pad10">
  <div class="adm-text ul-float">
     <ul class="clearfix">
       <li>
      	<span class="mr0 m" style="float:left;">奖品id：</span>
         ${gameprizes.prizesid}
      </li>
      <li>
      	 <span class="mr0 m" style="float:left;">活动名称：</span>
         <span style="float:left;">
          ${pkname }
         </span>
         <span style="display:none" id="startTimeTips" class="Validform_wrong">ee</span>
         <span>&nbsp;</span>
      </li>
      <li>
      	 <span class="mr0 m" style="float:left;"><span style="color:red;">*</span>奖品名称：</span>
         <span style="float:left;"><input type="text" id="prizesname" name="prizesname"  datatype="s2-50" nullmsg="奖品名称不能为空" errormsg="奖品名称长度为2~50个字符" value="${gameprizes.prizesname}"/></span>
        <div class="Validform_checktip"></div>
         <span>&nbsp;</span>
      </li>
      
   
      <li>
      	 <span class="mr0 m" style="float:left;"><span style="color:red;">*</span>排序值：</span>
         <span style="float:left;"><input type="text" id="priority" name="priority"  datatype="ns1-99" nullmsg="排序值不能为空" errormsg="顺序为1～99数字" value="${gameprizes.priority}"/></span>
         <div class="Validform_checktip"></div>
         <span>&nbsp;</span>
      </li>
       <li>
      	<span class="mr0 m" style="float:left;"><span style="color:red;">*</span>上传图片：</span>
        <label for="imgpath"></label>
        <div wrap="imageContainer" id="container1" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input name="prizesimg" id="prizesimg"  type="file" />
        <span id="contentTip"></span>
      </li>
    </ul>
  </div>
</div>
<div class="adm-btn-big">
<input type="submit" value="提交" />
</div>
</form>
    <script>
		$(function(){
			 $("#fm_es").Validform({
				tiptype:2,
				datatype:{
							"ns":/^\+?(?!0)\d*$/,
					        "ns1-99":/^[1-9]\d{0,1}$/
						}
			});
		});
			function chkForm(obj){
	         if($("#hprizesimg").val()==""||$("#hprizesimg").val()==undefined){
	            alert("请上传奖品图片");
	            return false;
	         }
	         return true; 
		}
    </script>
</body>
</html>