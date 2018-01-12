<%@include file="../public/header.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.richinfo.common.TokenMananger"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>下载数据统计</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/page.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/jscal2.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<style type="text/css">


.myButton {
	-moz-box-shadow:inset 0px 1px 0px 0px #ffffff;
	-webkit-box-shadow:inset 0px 1px 0px 0px #ffffff;
	box-shadow:inset 0px 1px 0px 0px #ffffff;
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ededed), color-stop(1, #dfdfdf));
	background:-moz-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:-webkit-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:-o-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:-ms-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:linear-gradient(to bottom, #ededed 5%, #dfdfdf 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ededed', endColorstr='#dfdfdf',GradientType=0);
	background-color:#ededed;
	-moz-border-radius:6px;
	-webkit-border-radius:6px;
	border-radius:6px;
	border:1px solid #dcdcdc;
	display:inline-block;
	cursor:pointer;
	color:#777777;
	font-family:Arial;
	font-size:15px;
	font-weight:bold;
	padding:6px 24px;
	text-decoration:none;
	text-shadow:0px 1px 0px #ffffff;
}
.myButton:hover {
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #dfdfdf), color-stop(1, #ededed));
	background:-moz-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:-webkit-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:-o-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:-ms-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:linear-gradient(to bottom, #dfdfdf 5%, #ededed 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#ededed',GradientType=0);
	background-color:#dfdfdf;
}
.myButton:active {
	position:relative;
	top:1px;
}

.myBlackButton {
	-moz-box-shadow:inset 0px 1px 0px 0px #ffffff;
	-webkit-box-shadow:inset 0px 1px 0px 0px #ffffff;
	box-shadow:inset 0px 1px 0px 0px #ffffff;
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ccc8cc), color-stop(1, #bdbdbd));
	background:-moz-linear-gradient(top, #ccc8cc 5%, #bdbdbd 100%);
	background:-webkit-linear-gradient(top, #ccc8cc 5%, #bdbdbd 100%);
	background:-o-linear-gradient(top, #ccc8cc 5%, #bdbdbd 100%);
	background:-ms-linear-gradient(top, #ccc8cc 5%, #bdbdbd 100%);
	background:linear-gradient(to bottom, #ccc8cc 5%, #bdbdbd 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ccc8cc', endColorstr='#bdbdbd',GradientType=0);
	background-color:#ccc8cc;
	-moz-border-radius:6px;
	-webkit-border-radius:6px;
	border-radius:6px;
	border:1px solid #ededed;
	display:inline-block;
	cursor:pointer;
	color:#878487;
	font-family:Arial;
	font-size:15px;
	font-weight:bold;
	padding:6px 24px;
	text-decoration:none;
	text-shadow:0px 1px 0px #ffffff;
}
.myBlackButton:hover {
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #bdbdbd), color-stop(1, #ccc8cc));
	background:-moz-linear-gradient(top, #bdbdbd 5%, #ccc8cc 100%);
	background:-webkit-linear-gradient(top, #bdbdbd 5%, #ccc8cc 100%);
	background:-o-linear-gradient(top, #bdbdbd 5%, #ccc8cc 100%);
	background:-ms-linear-gradient(top, #bdbdbd 5%, #ccc8cc 100%);
	background:linear-gradient(to bottom, #bdbdbd 5%, #ccc8cc 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#bdbdbd', endColorstr='#ccc8cc',GradientType=0);
	background-color:#bdbdbd;
}
.myBlackButton:active {
	position:relative;
	top:1px;
}

.myDfButton {
	-moz-box-shadow:inset 0px 1px 0px 0px #ffffff;
	-webkit-box-shadow:inset 0px 1px 0px 0px #ffffff;
	box-shadow:inset 0px 1px 0px 0px #ffffff;
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ededed), color-stop(1, #dfdfdf));
	background:-moz-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:-webkit-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:-o-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:-ms-linear-gradient(top, #ededed 5%, #dfdfdf 100%);
	background:linear-gradient(to bottom, #ededed 5%, #dfdfdf 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ededed', endColorstr='#dfdfdf',GradientType=0);
	background-color:#ededed;
	-moz-border-radius:6px;
	-webkit-border-radius:6px;
	border-radius:6px;
	border:1px solid #dcdcdc;
	display:inline-block;
	cursor:pointer;
	color:#777777;
	font-family:Arial;
	font-size:15px;
	font-weight:bold;
	padding:6px 24px;
	text-decoration:none;
	text-shadow:0px 1px 0px #ffffff;
}
.myDfButton:hover {
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #dfdfdf), color-stop(1, #ededed));
	background:-moz-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:-webkit-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:-o-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:-ms-linear-gradient(top, #dfdfdf 5%, #ededed 100%);
	background:linear-gradient(to bottom, #dfdfdf 5%, #ededed 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#ededed',GradientType=0);
	background-color:#dfdfdf;
}
.myDfButton:active {
	position:relative;
	top:1px;
}
</style>
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/page.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/jscal2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"	type="text/javascript"></script>
<script type="text/javascript">
		$(function(){
				  var startTime = $("#beginTime").ligerDateEditor( {
				           label: '开始',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 50,
				           labelAlign: 'center',
				           cancelable : false,
				           showTime:true,
				           onAfterSelect:function() {
				    			$("#beginTime").removeClass("Validform_error");
				    			$("#endTime").removeClass("Validform_error");
				    			$("#activityTimeTip").css("display","none");
				           } 
				    });
					var endTime = $("#endTime").ligerDateEditor( {
					       label: '结束',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 50,
				           labelAlign: 'center',
				           cancelable : false,
				           showTime:true,
				           onAfterSelect:function() {
				    			$("#endTime").removeClass("Validform_error");
				    			$("#beginTime").removeClass("Validform_error");
				    			$("#activityTimeTip").css("display","none");
				    			if(startTime.getValue()){
				    				if(startTime.getValue().getTime()>endTime.getValue().getTime()) {
				    					alert("开始时间不能大于结束时间!");
				    					$("#endTime").addClass("Validform_error");
				    					endTime.setValue(null);
				    					return false;
				    				}
				    			}
				    			if(startTime.getValue()==null) {
				    				alert("请先选择开始时间！");
				    				endTime.setValue(null);
				    				$("#beginTime").addClass("Validform_error");
				    				return false;
				    			}
				           }
				         });
					
					$(".myButton").bind("click", function(){
		  					var cto=$(this).attr("ctt");
		  					if(cto){
		  						$("#ctone").val(cto);
		  					}
		  					form1.submit();
					});
					
					$(".myDfButton").bind("click", function(){
		  					if('${count}'=='0'){
		  						alert("当前没有数据可以导出!");
		  						return false;
		  					}
						    $("#bt").val($("#beginTime").val());
		  					$("#et").val($("#endTime").val());
		  					form2.submit();
					});
	     });
		 
</script>
  </head>
  <body>
    <h2 class="adm-title clearfix">
    	<a class="fl title">下载数据统计</a>
    </h2>
	<div>
		<form id="form1" method="post" action="${ctx}/Stataccess/datatLists.do">
		<input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
		<input type="hidden" name="ctone" id="ctone" value="${ctone}"/>
		<ul class="clearfix" >
		  <li class="adm-page">
		       <div>
		       <div style="float:left;"> <input type="text"  id="beginTime" name="beginTime" class="s mr5 ml5" value="${beginTime}" style="width:110px;"></div>
               <div style="float:left;"> <input type="text"  id="endTime" name="endTime" class="s mr5 ml5" value="${endTime}" style="width:110px;"></div>
		      <div style="float:left;">&nbsp;&nbsp;<input type="submit" class="btn" value="搜索" style="width:39px;"></div>
		      
		      <div style="float:left;"><a href="javascript:void(0);" class="myDfButton">数据导出</a></div>
		       </div>
		      
		  </li>
		</ul>
		</form>
		<form id="form2" method="post"  action="${ctx}/Stataccess/exportTotExcel.do">
			<input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
			<input type="hidden" name="ct" id="ctone" value="${ctone}"/>
			<input type="hidden" name="bt" id="bt" value="${beginTime}"/>
			<input type="hidden" name="et" id="et" value="${endTime}"/>
		</form>
    </div>
    
    <table style="width:1500;height:9%;table-layout:fixed;">
	    <tbody id="tbmain" >
	        <tr class="tr1">
		      <td width="9%"><a href="javascript:void(0);" <c:choose><c:when test="${ctone eq 'taccessNum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose> ctt="taccessNum">总体访问</a></td>
		      <td width="9%"><a href="javascript:void(0);" <c:choose><c:when test="${ctone eq 'downldNum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose> ctt="downldNum">下载量</a></td>
		      <td width="9%"><a href="javascript:void(0);" <c:choose><c:when test="${ctone eq 'avgsum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose>   ctt="avgsum">人均浏览</a></td>
		      <td width="9%"><a href="javascript:void(0);" <c:choose><c:when test="${ctone eq 'uvsum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose>  ctt="uvsum">UV趋势</a></td>
		      <td width="9%">&nbsp;</td>
		      <td width="9%">&nbsp;</td>
		      <td width="9%">&nbsp;</td>
		      <td width="9%">&nbsp;</td>
		      <td width="9%">&nbsp;</td>
		      <td width="9%">&nbsp;</td>
		      <td width="9%">&nbsp;</td>
		      <td width="9%"></td>
		    </tr>
	    </tbody>
    </table>
	<table class="adm-table">
    <thead>
        <c:if test="${dataimg ne ''}">
    		<tr>
    			<td colspan="12"><img src="${ctx}${dataimg}?rnd=<%=(new java.text.SimpleDateFormat("MM_dd_HH_mm")).format((new Date()))%>" height="400" width="700" /></td>
    		</tr>
    	</c:if>
        <tr>
	      <td>时间</td>
	      <td>IP</td>
	      <td>UV</td>	
	      <td>PV</td>
	      <td>人均浏览量</td>
	      <td>总下载量</td>
	      <td>下载量(登录)</td>
	    </tr>
    </thead>
    <tbody id="tbmain" >
        <c:forEach items="${list}" var="vs" varStatus="status">
		       <tr> 
	      			<td>${vs.STATDATESTR}</td>
					<td>${vs.IP}</td>
					<td>${vs.UV}</td>
					<td>${vs.PV}</td>
					<td>${vs.PVUV}</td>
					<td>${vs.TOTALDLNUM}</td>
					<td>${vs.LOGINDLNUM}</td>
		       </tr>
		</c:forEach>
    </tbody>
    <tfoot>
    	<tr>
    		<td colspan="<c:choose><c:when test="${cttwo eq 'singledata'}">14</c:when><c:otherwise>12</c:otherwise></c:choose>">
	    		<input type="hidden" id="url" value="${ctx}/Stataccess/datatLists.do?csrfToken=${csrfToken}&&ctone=${ctone}&&beginTime=${beginTime}&&endTime=${endTime}">
			    <div class="page" id="pagediv">
			    <c:if test="${count>1}">
			    	<script>setPage(document.getElementById("pagediv"),${count},${currentpage},$("#url").val());</script>
			    </c:if>
			    </div>
    		</td>
    	</tr>
    </tfoot>
    </table>
    
  </body>
</html>
