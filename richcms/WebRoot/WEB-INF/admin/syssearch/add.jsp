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

<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
</head>
<body>
<div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">搜索设置</a><a href="${ctx}/Syssearch/lists.do" class="fr btn">返回列表</a></h2>
<form name="form1" id="form1" action="${ctx}/Syssearch/add.do" method="post">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <table style="font-size:12px;color:#444;line-height:32px;">
     <tr>
	     <td width="100"><span class="need" style="float:left;">*</span>数据源名称:</td>
		 <td> <input type="text"  style="width:180px;" datatype="s2-50" nullmsg="数据源名称不能为空" id="ssc_datasource" name="datasource" value="${data.datasource}" /></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	  <tr>
	     <td width="100"><span class="need" style="float:left;">*</span>对应数据表名:</td>
		 <td>   <select name="datatype" style="width:184px; padding:0px;">
	          <c:forEach items="${sourcelist }" var="vo">
	            <c:choose>
				<c:when test="${vo.source == data.datatype}">
					<option selected value="${vo.source}">${vo.source}</option>
				</c:when>
				<c:otherwise>
					<option  value="${vo.source}">${vo.source}</option>
				</c:otherwise>
				</c:choose>
	            
	          </c:forEach>
	        </select></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	 <tr>
	     <td width="100">优先级:</td>
		 <td> <input type="text" style="width:180px;" id="ssc_priority" name="priority" datatype="n1-4" value="${data.priority}" /></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	  <tr>
	     <td width="100">描述:</td>
		 <td> <textarea name="mem" datatype="s2-100"  ignore="ignore" style="height:100px;width:180px;">${data.mem}</textarea></td>
		 <td><div class="Validform_checktip"></div></td>
	 </tr>
	 <tr>
	     <td colspan="2" align="center">
	       <c:if test="${data!=null }">
			<input type="hidden"   name="sid" value="${data.sid}" />
			</c:if>
			<div class="adm-btn-big"><input id="btnSub" type="submit" value="保&nbsp;&nbsp;存" /></div>
	     </td>
	 </tr>
	</table>
</div>

</form>
</div></div>
<script type="text/javascript">
	$(function(){
		$("#form1").Validform({
			tiptype:2
		});
	})
</script>
 
</body>
</html>