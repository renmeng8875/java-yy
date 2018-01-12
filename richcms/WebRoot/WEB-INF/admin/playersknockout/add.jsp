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
<title>用户登录</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"	type="text/javascript"></script>
<style type="text/css">
.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>

<script type="text/javascript">
function loadStyle()
			{
				try{
					$.ajax({
					   type: "POST",
					   url: "${ctx}/ContentCat/styleList.do?csrfToken=${csrfToken}",
					   data: '',
					   dataType:"json",
					   async:false,
					   success: function(data){
					   		var styleList=data.styleList;
					   		
					   		if(styleList && styleList.length>0) {
					   			 $("#mystyle").empty();
					   			 var styleId="";
					   			 $("#mystyle").append("<option styleid='' value='' selected>--请选择--</option>");
	                    	     $.each(styleList, function(i, item) {
	                    	          styleId=item.styleid;
	                    	          if('${info.style}'==item.style){
	                    	          		$("#mystyle").append("<option styleid='" + styleId+ "' value='" + item.style+ "' selected>" +item.stylename+ "</option>");
	                    	          }else{
	                    	          		$("#mystyle").append("<option styleid='" + styleId+ "' value='" + item.style+ "'>" +item.stylename+ "</option>");
	                    	          }
	                             });
	                             loadTpl('${info.style}'==''?styleList[0].styleId:styleId);
					   		}else{
					   			$("#mystyle").empty();
					   			$("#mystyle").append("<option styleid='' value='' selected>--请选择--</option>");
					   		} 
					   }
				 	});
				}catch(e){
					$("#mystyle").empty();
					$("#mystyle").append("<option styleid='' value='' selected>--请选择--</option>");
				}
			}
function loadTpl(v)
			{
				var styleId=v==''?$("#mystyle").find("option:selected").attr("styleid"):v;
				try{
					$.ajax({
					   type: "POST",
					   url: "${ctx}/ContentCat/tplList.do?csrfToken=${csrfToken}",
					   data:'styleId='+styleId,
					   dataType:"json",
					   async:false,
					   success: function(data){
					   		var tplList=data.tplList;
					   		if(tplList && tplList.length>0) {
					   			 $("#tplId").empty();
					   			 $("#tplId").append("<option value=''>无</option");
	
	                    	     $.each(tplList, function(i, item) {
		                    	        var paths={};
		                    	        if(item.listPath!=''){
		                    	             paths=item.listPath;
		                    	        }else{
		                    	        	 paths=item.indexPath;
		                    	        }
		                    	        for(var k in paths){
		                    	            var tplPath='${info.tplpath}';
		                    	            if(tplPath==paths[k]){
		                    	            	$("#tplId").append("<option value='" + paths[k]+ "' selected>" +k+ "</option>");
		                    	            }else{
		                    	            	$("#tplId").append("<option value='" + paths[k]+ "'>" +k+ "</option>");
		                    	            }
		                    	        	
		                    	        }
	                             });
					   		}else{
					   			$("#tplId").empty();
					   			$("#tplId").append("<option value=''>无</option>");
					   		} 
					   }
				 	});
				}catch(e){
					$("#tplId").empty();
					$("#tplId").append("<option value=''>无</option>");
				}
}

$(function(){
	var startTime = $("#starttime").ligerDateEditor(
    {
           label: '活动日期',
    	   format: "yyyy-MM-dd",
           labelWidth: 85,
           labelAlign: 'left',
           cancelable : false,
           showTime:true,
           onAfterSelect:function()
           {
    			$("#starttime").removeClass("Validform_error");
    			$("#endtime").removeClass("Validform_error");
    			$("#activityTimeTip").css("display","none");
           } 
    });
	

	var endTime = $("#endtime").ligerDateEditor(
    {
           
    	   format: "yyyy-MM-dd",
           labelWidth: 85,
           labelAlign: 'left',
           cancelable : false,
           showTime:true,
           onAfterSelect:function()
           {
    			$("#endtime").removeClass("Validform_error");
    			$("#starttime").removeClass("Validform_error");
    			$("#activityTimeTip").css("display","none");
    			if(startTime.getValue()){
    				if(startTime.getValue().getTime()>endTime.getValue().getTime())
    				{
    					alert("开始时间不能大于结束时间!");
    					$("#endtime").addClass("Validform_error");
    					endTime.setValue(null);
    					return false;
    				}
    			}
    			if(startTime.getValue()==null)
    			{
    				alert("请先选择开始时间！");
    				endTime.setValue(null);
    				$("#starttime").addClass("Validform_error");
    				return false;
    			}
           } 
    });
	
	$('#logourl').uploadify({
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
	
	$('#jsonimg').uploadify({
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
				$('#jsonimg').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	
        	var container = $("#container2");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="jsonimg" value="'+jsonObj.imageName+'" />');
	    }
	  });
	
	$('#activitiesLogo').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/fileupload/uploadify.do?region=pk&csrfToken=${csrfToken}',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
	    'auto'      : true,
	    'buttonText': '单张上传',
	    'fileSizeLimit': '500KB',
	    'removeTimeout':0,
	    'onUploadStart':function(item){
        	var container = $("#container3");
        	var children = $("div[class='display-image']",container);
        	if(children&&children.length>=1)
        	{
        		alert("最多只能上传1张图片！");
				$('#activitiesLogo').uploadify('cancel','*');	

        	}
	    },
	    'onUploadSuccess':function(file, data, response)
	    {
        	
        	var container = $("#container3");
        	var jsonObj = JSON2.parse(data);
        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="120px" height="120px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
        	container.append('<input type="hidden" name="activitiesLogo" value="'+jsonObj.imageName+'" />');
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
    
	$("#fm_es").Validform({
		tiptype:3,
		label:".label",
		showAllError:false,
		datatype:{
			"zh2-50":/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,50}$/,
			"zhs2-50":/^[a-z]{1}[a-z0-9 ]{1,49}$/
		},
		beforeSubmit:function(){
		
			var startDate = startTime.getValue(),endDate = endTime.getValue();
			var tip = $("#activityTimeTip"),startInput = $("#starttime"),endInput = $("#endtime"); 
			if(startDate==null)
			{
				$("#activityTimeTip").css("display","inline");
				startInput.addClass("Validform_error");
				return false;
			}	
			if(endDate==null)
			{
				$("#activityTimeTip").css("display","inline");
				endInput.addClass("Validform_error");
				return false;	
			}
			if(startDate.getTime()>endDate.getTime())
			{
				$("#activityTimeTip").css("display","inline");
				startInput.addClass("Validform_error");
				endInput.addClass("Validform_error");
				return false;
			}
			var isDisPlayModule=$("input[name='temporjump'][value=1]").attr("checked");
			if(isDisPlayModule)
			{
				$("#forwardUrl").val("");
			}else{
				$("#mystyle").val("");
				$("#tplId").val("");
				var isForwardModule=$("input[name='isForward'][value=1]").attr("checked");
				if($("#forwardUrl").val()==''&&isForwardModule)
				{
					$("#forwardUrlTip").css("display","inline");
					return false;
				}
			}	
			return true;
		}
		
	});
	
	//显示模板设置-显示跳转设置
	$("#temporjump1").click(function(){
		$("#templateset1").show();
		$("#templateset11").show();
		$("#templateset2").hide();
		$("#templateset22").hide();
		$("#forwardUrl").val("");
	});
	$("#temporjump2").click(function(){
		$("#templateset2").show();
		$("#templateset22").show();
		$("#templateset1").hide();
		$("#templateset11").hide();
		$("#mystyle").val("");
		$("#tplId").val("");
	});
	//风格
	loadStyle();			
	$("#forwardUrl").focus(function(){
		$("#forwardUrlTip").css("display","none");
	});
});
</script>
</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title"><a class="title">玩家争霸</a>
	<a class="fr btn" href="javascript:history.go(-1);">+玩家争霸管理</a></h2>
<form id="fm_es" method="post" action="${ctx}/pk/addpk.do" enctype="multipart/form-data">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input type="hidden" name="pkid" value="${pk.pkid}" />
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="need" >*</span><span class="mr20 m">栏目名称：</span>
        <input style="width:250px;" class="l mr20" name="pkName" id="pkName" type="text"  value="${pk.pkName}"  datatype="*2-50" sucmsg="验证通过,名称可用!" nullmsg="栏目名称不能为空!" errormsg="栏目名称长度必须在2~50位字符范围内!" />
      </li>
      <li><span class="need" >*</span><span class="mr20 m">英文名称：</span>
        <input style="width:250px;" class="l mr20" name="pk" id="pk" type="text" value="${pk.pk}" datatype="zhs2-50" sucmsg="验证通过,名称可用!" nullmsg="英文名称不能为空!" errormsg="英文名称必须在2~50位数字和小写字母组合的范围内,且第一位必为小写字母!"  ajaxurl="${ctx }/pk/checkName.do?csrfToken=${csrfToken}"  />
      </li>
      
      <li><span class="need" style="position: absolute;">*</span><span class="mr20 m" style="margin-left: 6px;">栏目描述：</span>
        <label for="textarea"></label>
        <textarea name="pkdesc" id="pkdesc" cols="45" rows="5"  datatype="*2-50" sucmsg="验证通过!" nullmsg="栏目描述不能为空!"  errormsg="栏目描述长度必须在2~50位字符范围内!" >${pk.pkdesc}</textarea>
      </li>
      
      <li><span class="b">横幅图预览:</span>
      	<div wrap="imageContainer" id="container1" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input name="logourl" id="logourl"  type="file" />
      </li>
      
      <li><span class="b">图片缩略图预览:</span>
      	<div wrap="imageContainer" id="container2" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input id="jsonimg"  type="file" />
      </li>
      
      <li><span class="b">活动列表图:</span>
      	<div wrap="imageContainer" id="container3" style="height: 125px;width: 380px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;">
      	</div>
        <input id="activitiesLogo"  type="file" />
      </li>
      
      <li>
        <span class="need" style="float:left;">*</span><span style="float:left;">
        <input type="text"  id="starttime" name="startTimeStr"  readonly="readonly"  value="${pk.startTimeStr}"/>
        </span>
        <span style="float:left;">&nbsp;~&nbsp;</span><span style="float:left;">
        <input type="text"  id="endtime" name="endTimeStr" readonly="readonly" value="${pk.endTimeStr}"/>
        </span>
        <span style="float:left;display: none;" id="activityTimeTip" class="Validform_checktip Validform_wrong">
        	活动时间选择不合法,开始时间必须小于结束时间且都不为空！
        </span>
        <div class="clear"></div> 
      </li>
      <li><span class="mr20 m">显示/隐藏：</span>
        <input name="status"  type="radio"  value="1" />显示   &nbsp;&nbsp;&nbsp;&nbsp;
        <input name="status"  type="radio"  value="0"  checked="checked"/>隐藏
      </li>
     <li><span class="need" >*</span><span class="mr20 m">seo标题&nbsp;&nbsp;&nbsp;：</span>
        <input style="width:250px;" class="l mr20" name="seoTitle" id="seoTitle" type="text"  value="${pk.seoTitle}" datatype="*2-50" sucmsg="验证通过!" nullmsg="标题不能为空!" errormsg="标题长度必须在2~50位字符范围内!" />
      </li>
      <li><span class="need" >*</span><span class="mr20 m">seo关键字：</span>
        <input style="width:250px;" class="l mr20" name="seoKeyword" id="seoKeyword" type="text"  value="${pk.seoKeyword}" datatype="*2-50" sucmsg="验证通过!" nullmsg="关键字不能为空!" errormsg="关键字长度必须在2~50位字符范围内!" />
      </li>
      <li><span class="need" style="position: absolute;">*</span><span class="mr20 m" style="margin-left: 6px;">seo描述&nbsp;&nbsp;&nbsp;：</span>
        <label for="textarea"></label>
        <textarea name="seoDesc" id="seoDesc" cols="45" rows="5"  datatype="*2-200" sucmsg="验证通过!" nullmsg="seo描述不能为空!" errormsg="seo描述长度必须在2~200位字符范围内!" >${pk.seoDesc}</textarea>
      </li>
      <li>
		<input type="radio" name="temporjump" id="temporjump1" value="1" checked="checked" />显示模板设置&nbsp;&nbsp;
		<input type="radio" name="temporjump" id="temporjump2" value="0" />显示跳转设置
	  </li>
	  <li id="templateset1">
		<span class="mr20 m">风&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;格：</span>
		<span><select id="mystyle" name="style"onchange="javascript:loadTpl('');"></select></span>
	  </li>
	  <li id="templateset11">
		<span class="mr20 m">模&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;板：</span>
		<span><select id="tplId" name="tplpath"><option value="">无</option></select></span>
	  </li>
	  <li id="templateset22" style="display: none;">
	  	<span class="mr20 m">是否跳转：</span>
	  	<span>
       		<input name="isForward"  type="radio"  value="1" />是   &nbsp;&nbsp;&nbsp;&nbsp;
        	<input name="isForward"  type="radio"  value="0" checked="checked"/>否
        </span>
      </li>
      <li id="templateset2" style="display: none;">
      	<span class="need" >*</span><span class="mr20 m">跳转地址：</span>
        <span>
        	<input style="width:250px;" class="l mr20" name="forwardUrl" id="forwardUrl" type="text"  value="${pk.forwardUrl}" datatype="url" ignore="ignore" sucmsg="验证通过!"  errormsg="跳转地址不是有效的url"/>
        </span>
        <span style="display: none;" id="forwardUrlTip" class="Validform_checktip Validform_wrong">
        	跳转地址不为空！
        </span>
      </li>
    </ul>
  </div>
</div>
<div class="adm-btn-big"><input type="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</body>
</html>