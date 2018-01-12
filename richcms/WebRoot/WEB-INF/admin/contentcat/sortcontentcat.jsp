<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>栏目排序</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
	<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript">
		function submitForm() 
		{
				var idstr="";
				var lastIndex;
				$("input[name='idgroup']").each(function(i,n){
  					idstr+=$(this).val()+"";
  					if(i!=lastIndex){
  						idstr+=",";
  					}
  					lastIndex++;
				});
				
			    $.ajax({
				   type: "POST",
				   url: "${ctx}/ContentCat/updatePriority.do?csrfToken=${csrfToken}",
				   data: "idstr="+idstr,
				   success: function(msg)
				   {
				     if(msg.status=='0')
				     {
				    	 alert("排序成功！"); 
				    	 opener.location.reload();
						 window.close();
				     }else{
				  	 	 alert("排序失败！");
				  	 	 return false;
				     }
				   }
				});
				
		}
	</script>
	
	
	
  </head>
  
  <body>
    <h2 class="adm-title clearfix">
	<a class="fl title">管理栏目</a>
</h2>

<div class="pad10">	
<form id="fm_es" method="post" action="${ctx}/ContentCat/updatePriority.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
  <table class="adm-table">
    <tr class="tr1">
	  <td>初始序号</td>    
      <td>栏目ID</td>
      <td>栏目名称</td>      
      <td>是否隐藏</td>
      
    </tr>
    <tbody id="tbmain">      
	    <c:forEach var="item" items="${childCategoryList}" varStatus="status">
		    <tr <c:if test="${status.index%2 eq 1}">class="tr2"</c:if>>
		      <td>${status.index+1}</td>
		      <td>${item.catId}</td>
		      <td style="text-align:left">${item.name}</td>
		      <td>
		      	<c:choose>
			      	<c:when test="${item.isHidden eq '' or item.isHidden eq '0'}">
			      		显示
			      	</c:when>
			      	<c:otherwise>
			      		<font color="#FF0000">隐藏</font>
			      	</c:otherwise>
		      	</c:choose>
		      	<input type="hidden" name="idgroup" value="${item.catId}">
		      </td>
		    </tr>
	   </c:forEach>
   </tbody>   		     
  </table>
  <input type="button" value="提交" onClick="submitForm()"/>
  </form>
  

<script type="text/javascript" src="${ctx}/static/admin/js/jquery.tablednd.js"></script>
 </body>
</html>
<script>
$(function(){	
	$("#tbmain").tableDnD();			
})
</script>
