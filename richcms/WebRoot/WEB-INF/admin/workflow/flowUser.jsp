<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分配角色</title>
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
<script type="text/javascript">
$(function ()
{
    $("#fm_es").Validform({
    	tiptype:4,
    	ajaxPost:true,
    	beforeSubmit:function(){
    		$("input[name='userid']").each(function()
    		{
    			if(this.checked)
    			{
    				$(this).val($(this).val()+","+$(this).attr("flevel"));
    			}
    		});
    	},
    	callback:function(data)
    	{
    		window.parent.addWindow.hide();
    		window.parent.showSuccessTips();
    		window.parent.location.href="${ctx}/Workflow/lists.do";
    	}
    });
    
  
});
  
       
</script>
<style type="text/css">
body{ font-size:11px;}
.tdinput{width:230px;}j
</style>
</head>
<body style="overflow: hidden; padding: 1px;height:220px;">
   <form action="${ctx }/Workflow/audiflowuser.do" method="post" id="fm_es">
   <input type="hidden" name="csrfToken" value="${csrfToken}"/>
    <div style="height: 190px;border:1px solid #A3C0E8;overflow-Y:scroll; ">
    <ul id="ul_all">
    <c:forEach begin="1" end="${flowlevel}" step="1" var="i">
    		<c:if test="${i eq 1}"><c:set var="level" value="一"/></c:if>
    		<c:if test="${i eq 2}"><c:set var="level" value="二"/></c:if>
    		<c:if test="${i eq 3}"><c:set var="level" value="三"/></c:if>
    		<c:if test="${i eq 4}"><c:set var="level" value="四"/></c:if>
	      	<li style="float: left;margin-bottom:15px;font-family:'microsoft Yahei','宋体'; COLOR: #666">
	      		<span style="font-size: 12px;">${level}级审核人员列表:&nbsp;&nbsp;</span>
		      	<c:forEach items="${userList}" var="user" varStatus="status">
		      		<c:set var="isChecked" value="false"/>
		      		<c:forEach items="${wfuserList}" var="wfuser" varStatus="status">
		      			<c:forTokens items="${wfuser.userid}" delims="," var="eachUserId">
		      				<c:if test="${fn:trim(user.userId) eq eachUserId && wfuser.flevel eq i}"><c:set var="isChecked" value="true"/></c:if>
		      			</c:forTokens>
		      		</c:forEach>
					<input type="checkbox" style="position:relative;margin:0;top:2px;" name="userid" flevel="${i}" value="${user.userId}" <c:if test="${isChecked}">checked</c:if>/> ${user.nickName}
				</c:forEach>
			<input type="hidden" name="flowid" value="${flowid}">
	        </li>
      		<br /><br />
     </c:forEach>
     </ul> 
    </div>
    <div style="margin-top: 5px;" align="center">
     	
		<input type="submit" value="提交"  class="l-button l-button-submit" />
     </div>
   </form>
</body>
</html>