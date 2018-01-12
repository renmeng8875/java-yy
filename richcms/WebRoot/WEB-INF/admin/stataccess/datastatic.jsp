<%@include file="../public/header.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.richinfo.common.TokenMananger"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>圈子下载访问数据</title>
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
		  					var cto=$(this).attr("cto");
		  					var ctt=$(this).attr("ctt");
		  					if(cto){
		  						$("#ctone").val(cto);
		  					}
		  					if(ctt){
		  						$("#cttwo").val(ctt);
		  					}
		  					
		  					$("#bt").val($("#beginTime").val());
		  					$("#et").val($("#endTime").val());
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
    	<a class="fl title">圈子下载访问数据</a>
    </h2>
	<div>
		<form id="form1" method="post" action="${ctx}/Stataccess/lists.do">
		<input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
		<input type="hidden" name="ctone" id="ctone" value="${ctone}"/>
		<input type="hidden" name="cttwo" id="cttwo" value="${cttwo}"/>
		<ul class="clearfix" >
		  <li class="adm-page">
		       <div>
		       <div style="float:left;"> <input type="text"  id="beginTime" name="beginTime" class="s mr5 ml5" value="${beginTime}" style="width:110px;"></div>
               <div style="float:left;"> <input type="text"  id="endTime" name="endTime" class="s mr5 ml5" value="${endTime}" style="width:110px;"></div>
               <div style="float:left;"> <input type="submit" class="btn" value="搜索" style="width:39px;">&nbsp;&nbsp;</div>
               <div style="float:left;"> <a href="javascript:void(0);" class="myDfButton">数据导出</a></div>
               </div>
		  </li>
		</ul>
		</form>
		<form id="form2" method="post"  action="${ctx}/Stataccess/exportExcel.do">
			<input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
			<input type="hidden" name="ct" id="ct" value="${cttwo}"/>
			<input type="hidden" name="co" id="co" value="${ctone}"/>
			<input type="hidden" name="bt" id="bt" value="${beginTime}"/>
			<input type="hidden" name="et" id="et" value="${endTime}"/>
		</form>
    </div>
    <hr style="height:1px;border:none;border-top:1px solid #D9D9D9;" />
    <div>
       <div style="display: inline;float:left;padding-left:10px;">
          <a href="javascript:void(0);" <c:choose><c:when test="${ctone eq 'accessNum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose> cto="accessNum">访问量</a>
          <a href="javascript:void(0);" <c:choose><c:when test="${ctone eq 'downldNum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose> cto="downldNum">下载量</a>
       </div>
       <div style="display: inline;float:right;">
          <a href="javascript:void(0);"  <c:choose><c:when test="${cttwo eq 'daysum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose>   ctt="daysum">按日汇总</a>
		  <a href="javascript:void(0);"  <c:choose><c:when test="${cttwo eq 'classisum'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose>  ctt="classisum">分类汇总</a>
		  <a href="javascript:void(0);"  <c:choose><c:when test="${cttwo eq 'singledata'}">class="myBlackButton"</c:when><c:otherwise>class="myButton"</c:otherwise></c:choose>  ctt="singledata">单独数据</a>
       </div>
    </div>
	<table class="adm-table">
    <thead>
    	<c:if test="${dataimg ne ''}">
    		<tr>
    			<td colspan="12"><img src="${ctx}${dataimg}?rnd=<%=(new java.text.SimpleDateFormat("MM_dd_HH_mm")).format((new Date()))%>" height="400" width="600" /></td>
    		</tr>
    	</c:if>
	    <tr class="tr1">
	      <td width="6%">
	      	<c:choose>
	      		<c:when test="${cttwo eq 'classisum'}">分类名称</c:when>
	      		<c:when test="${cttwo eq 'singledata'}">统计时间</c:when>
	      		<c:otherwise>统计时间</c:otherwise>
	      	</c:choose>
	      </td>
	      <c:if test="${cttwo eq 'singledata'}">
	      	 <td >应用类型</td>
	      	 <td >二级分类</td>	
	      </c:if>
	      <td >总访问次数</td>
	      <td >总IP</td>	
	      <td >总PV</td>
	      <td >总UV</td>
	      <td >PV/IP比</td>
	      <td >登录下载次数</td>
	      <td >登录下载用户数</td>
	      <td >免登录下载次数</td>
	      <td >免登录下载用户数</td>
	      <td >下载总次数</td>
	      <td >下载用户总数</td>
	    </tr>
    </thead>
    <tbody id="tbmain" >
        <c:forEach items="${list}" var="vs" varStatus="status">
		       <tr> 
				<td>
					<c:choose>
	      				<c:when test="${cttwo eq 'classisum'}">${vs.LEVELTHREE}</c:when>
	      				<c:when test="${cttwo eq 'singledata'}">${vs.STATDATESTR}</c:when>
	      				<c:otherwise>${vs.STATDATESTR}</c:otherwise>
	      			</c:choose>
				</td>
				<c:choose>
	      				<c:when test="${cttwo eq 'singledata'}">
	      						<td>${vs.LEVELTWO}</td>
	      						<td>${vs.LEVELTHREE}</td>
			      	 			<td>${vs.TOTALACCESSNUM}</td>
						        <td>${vs.TOTALIP}</td>
						        <td>${vs.TOTALPV}</td>
						        <td>${vs.TOTALUV}</td>
						        <td>${vs.PVIP}</td>
						        <td>${vs.LOGINDLNUM}</td>
						        <td>${vs.LOGINDLUSERNUM}</td>
						        <td>${vs.NLOGINDLNUM}</td>
						        <td>${vs.NLOGINDLUSERNUM}</td>
						        <td>${vs.TOTALDLNUM}</td>
						        <td>${vs.TOTALDLUSERNUM}</td>
	      				</c:when>
	      				<c:otherwise>
	      					<td>${vs.DTASSNUM}</td>
					        <td>${vs.DTIP}</td>
					        <td>${vs.DTPV}</td>
					        <td>${vs.DTUV}</td>
					        <td>${vs.DTPVIP}</td>
					        <td>${vs.DLOGINDLNUM}</td>
					        <td>${vs.DLOGINDLUSERNUM}</td>
					        <td>${vs.DNLOGINDLNUM}</td>
					        <td>${vs.DNLOGDLUSERNUM}</td>
					        <td>${vs.DTOTALDLNUM}</td>
					        <td>${vs.DTDLUSERNUM}</td>
	      				</c:otherwise>
	      			</c:choose>
		       </tr>
		</c:forEach>
    </tbody>
    <tfoot>
    	<tr>
    		<td colspan="<c:choose><c:when test="${cttwo eq 'singledata'}">14</c:when><c:otherwise>12</c:otherwise></c:choose>">
	    		
	    		<input type="hidden" id="url" value="${ctx}/Stataccess/lists.do?csrfToken=${csrfToken}&&ctone=${ctone}&&cttwo=${cttwo}&&beginTime=${beginTime}&&endTime=${endTime}">
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
