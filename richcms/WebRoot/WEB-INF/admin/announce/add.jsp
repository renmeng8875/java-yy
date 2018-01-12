<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"	type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	var startTime =$("#starttime").ligerDateEditor(
    {
           label: '',
    	   format: "yyyy-MM-dd hh:mm",
           labelWidth: 85,
           labelAlign: 'left',
           cancelable : false,
           showTime:true,
           onAfterSelect:function()
           {
    			$("#starttime").removeClass("Validform_error");
    			$("#endtime").removeClass("Validform_error");
    			
    			$("#startTimeTips").removeClass("Validform_error");
    			$("#startTimeTips").css("display","none");
           } 
    });
	var endTime = $("#endtime").ligerDateEditor(
    {
           label: '',
    	   format: "yyyy-MM-dd hh:mm",
           labelWidth: 85,
           labelAlign: 'left',
           cancelable : false,
           showTime:true,
           onAfterSelect:function()
           {
    			$("#endtime").removeClass("Validform_error");
    			$("#starttime").removeClass("Validform_error");
    			
    			$("#startTimeTips").removeClass("Validform_error");
    			$("#startTimeTips").css("display","none");
    			
    			$("#endTimeTips").removeClass("Validform_error");
    			$("#endTimeTips").css("display","none");
    			
    			if(startTime.getValue()){
    				if(startTime.getValue().getTime()>endTime.getValue().getTime())
    				{
    					$("#startTimeTips").css("display","inline");
						$("#startTimeTips").html("");
						$("#startTimeTips").html("起始日期不能大于结束日期!");
    					$("#endtime").addClass("Validform_error");
    					endTime.setValue(null);
    					return false;
    				}
    			}
    			if(startTime.getValue()==null)
    			{
    				$("#startTimeTips").css("display","inline").css("float","left");
					$("#startTimeTips").html("");
					$("#startTimeTips").html("请先选择起始日期！");
    				endTime.setValue(null);
    				$("#starttime").addClass("Validform_error");
    				return false;
    			}
           } 
    });

	$("#fm_es").Validform({
		tiptype:3,
		beforeSubmit:function(){
			var startDate = startTime.getValue(),endDate = endTime.getValue();
			var startInput = $("#starttime"),endInput = $("#endtime"); 
			
			if(startDate==null)
			{
				$("#startTimeTips").css("display","inline").css("float","left");
				$("#startTimeTips").html("");
				$("#startTimeTips").html("起始日期不为空！");
				startInput.addClass("Validform_error");
				return false;
			}	
			if(endDate==null)
			{
				$("#endTimeTips").css("display","inline").css("float","left");
				$("#endTimeTips").html("");
				$("#endTimeTips").html("结束日期不为空！");
				endInput.addClass("Validform_error");
				return false;	
			}
			if(startDate!=null&&endDate!=null&&startDate.getTime()>endDate.getTime())
			{
				$("#startTimeTips").css("display","inline").css("float","left");
				$("#startTimeTips").html("");
				$("#startTimeTips").html("起始日期必须小于结束日期！！");
				startInput.addClass("Validform_error");
				endInput.addClass("Validform_error");
				return false;
			}	
			return true;
		}
	});
})
</script>
</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title"><a class="title">公告</a><a class="fr btn" href="javascript:history.go(-1);">+公告管理</a></h2>
<form id="fm_es" method="post" action="${ctx}/Announce/addAnnounce.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li>
      	<span class="need" style="float:left;">*</span>
      	<span class="mr0 m" style="float:left;">公告标题：</span>
        <input class="l mr20" name="announce" id="announce"  type="text" datatype="*2-50" ajaxurl="${ctx}/Announce/checkName.do?csrfToken=${csrfToken}" nullmsg="请输入公告标题!" errormsg="公告标题长度必须在2~50位字符范围内!" stlye="width:180px;"/>
         <span>&nbsp;</span>	
  
      <li>
      	 <span class="need" style="float:left;">*</span>
      	 <span class="mr0 m" style="float:left;">起始日期：</span>
         <span style="float:left;"><input type="text" id="starttime" name="starttime" /></span>
         <span style="display:none" id="startTimeTips" class="Validform_wrong"></span>
         <span>&nbsp;</span>
      </li>
      <li>
      	<span class="need" style="float:left;">*</span>
      	<span class="mr0 m" style="float:left;">结束日期：</span>
        <span style="float:left;"><input type="text" id="endtime" name="endtime" /></span>
        <span style="display:none" id="endTimeTips" class="Validform_wrong"></span>
        <span>&nbsp;</span>
      </li>
      <li>
      	<span class="need" style="float:left;">*</span>
      	<span class="mr0 m" style="float:left;">公告内容：</span>
        <label for="textarea"></label>
        <textarea name="content" id="content" cols="45" rows="5" datatype="*2-100" errormsg="公告内容长度必须在2~100位字符范围内!"   nullmsg="请输入公告内容!" ></textarea>
      </li>

    </ul>
  </div>
</div>
<div class="adm-btn-big"><input type="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</body>
</html>