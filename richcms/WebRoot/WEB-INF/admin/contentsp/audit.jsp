<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>填写不通过原因</title>
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
<script type="text/javascript">
var navtab = null;
$(function ()
{
    $("#fm_es").Validform({
    	tiptype:2,
    	ajaxPost:true,
    	callback:function(data)
    	{
    		window.parent.addWindow.hide();
    		window.parent.showSuccessTips();
    		window.parent.location.href="${ctx}/ContentSp/lists.do";
    	}
    });
});
  
       
</script>
<style type="text/css">
body{ font-size:11px;}
.tdinput{width:230px;}
</style>
</head>
<body style="overflow: hidden; padding: 1px;">
<div id="tab1" style="overflow:hidden; border:1px solid #A3C0E8; "> 
            <div title="填写不通过原因">
            <form id="fm_es" action="${ctx}/ContentSp/doAudit.do" method="post">
              <input type="hidden" name="csrfToken" value="${csrfToken}"/>
              <table style="font-size:12px;color:#444;line-height:32px; margin:10px;">
				 <tr>
					 <td width="100"><span class="need" >*</span>原因:</td>
					 <td>
					 	<textarea name="msg" cols="56" rows="6"  datatype="*1-4000" sucmsg="验证通过!" nullmsg="原因不能为空!" errormsg="原因必须在1~4000个字符内!" ></textarea>
					 </td>
				 </tr>
				 <tr>
					 <td colspan="2" align="center">
					   <input type="hidden" name="id" value="${id}"/>
					   <input type="hidden" name="stu" value="${status}"/>
					   <input type="submit" value="提交" class="l-button l-button-submit" />
					   <input type="reset" value="重置"  class="l-button l-button-submit" />  
					 </td>
				 </tr>
              </table>
              </form>
            </div>
        </div>
</body>
</html>