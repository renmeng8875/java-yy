<%@include file="../../public/header.jsp"%>
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
var navtab = null;
$(function ()
{

    $("#fm_es").Validform({
    	tiptype:3,
    	datatype:{
    	"s32":/^[a-zA-Z0-9]{32}$/
    	},
    	ajaxPost:true,
    	callback:function(data)
    	{
    		if(data.status=='y')
    		{
    			//window.parent.allocateWindow.hide();
    		}	
    	}
    });
    
  
  
});
function createAppKey(appId)
{
   $.ajax({
   type: "POST",
   url: "${ctx}/application/createAppkey.do?appId="+appId,
   data:'csrfToken=${csrfToken}',                                        
   success: function(msg)
   {
     if(msg.status=='0')
     {
    	 $("#appKey").val(msg.appKey);
     }else
     {
    	 alert("生成appkey失败！");
     }	 
	  
   }
});
}    
       
</script>
<style type="text/css">
body{ font-size:11px;}
.tdinput{width:230px;}
</style>
</head>
<body style="overflow: hidden; padding: 1px;">
<div id="tab1" style="overflow:hidden; border:1px solid #A3C0E8; "> 
            <div title="分配角色">
            <form action="${ctx }/application/allocationRole.do" method="post" id="fm_es">
              <input type="hidden" name="csrfToken" value="${csrfToken}"/>
              <table style="font-size:12px;color:#444;line-height:32px; margin:10px;">
				 <tr>
					 <td width="100">开发者:</td>
					 <td><input class="tdinput" type="text" value="${app.account.apper}" disabled="disabled"/></td>
				 </tr>
				  <tr>
					 <td width="100">应用名称:</td>
					 <td><input class="tdinput" type="text" value="${app.appName}" disabled="disabled" /></td>
				 </tr>
				  <tr>
					 <td width="100">修改角色：</td>
					 <td>
					 	<select class="tdinput" name="roleId">
					    <c:forEach items="${roleList}" var="role" >
					    	<option value="${role.roleId}"  <c:if test="${role.roleId==app.role.roleId}">selected="selected"</c:if>>${role.roleName}</option>
					    </c:forEach>
					 	</select>
					 </td>
					 
				 </tr>
				 <tr>
					 <td width="100"><input style="width:80px" type="button" value="生成appKey" class="l-button l-button-submit" onclick="createAppKey('${app.appId}');"></td>
					 <td><input class="tdinput" type="text" name="appKey" id="appKey" value="${app.appKey }" datatype="s32" nullmsg="appkey不能为空" errormsg="appkey必须为32位英文和数字的组合！"></td>
				 </tr>
				 
				  <tr>
					 <td colspan="2" align="center">
					   <input type="hidden" name="appId" value="${app.appId}">
					   <input type="submit" value="提交"  class="l-button l-button-submit" />
					   <input type="reset" value="重置"   class="l-button l-button-submit" />  
					 </td>
				 </tr>
				 
              </table>
              </form>
            </div>
         
        </div>
</body>
</html>