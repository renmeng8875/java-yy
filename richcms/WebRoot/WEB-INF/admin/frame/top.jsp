<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<title>top</title>

</head>
<body class="bordercolor">
<div class="frmtop_bg">
    <div class="header wraper">
        <div class="fl logo"></div>
        <div class="fl nav_list" id="topmenu">
            <c:forEach items="${topmenulist }" var="vo" varStatus="status">
            	<a href="javascript:;" data-modid="${vo.menuId}" id="menu_${vo.menuId}" class="block  <c:choose> <c:when test="${status.count==1}">on</c:when> <c:otherwise>nav2</c:otherwise></c:choose> fl "><span class="pic${status.count}">&nbsp;</span>${vo.menu }</a>
            </c:forEach>  
        </div>
        <div class="userinfo">
           <div style="float:right;">
            <span class="block text fl sp1">您好!</span>
            <a href="javascript:;" class="youname block text fl">${userinfo.nickName}</a>
            <span class="block text fl"> ${userrole.roleName}</span>
            
            <a href="javascript:;" id="logout_link" target="_parent" class="block text fl">[退出]</a> 
            </div>
            <div style="width:350px;"> 
            </div>
       </div>
        
    </div>	
</div>
</body>
<script type="text/javascript">
$(function(){
	$('#topmenu>a').click(function(){
		if($(this).hasClass('on')) return true;
		$(this).addClass('on').removeClass('nav2').siblings('.on').removeClass('on').addClass('nav2');
		window.parent.leftmenu.getmenu($(this).data('modid'));
        window.parent.drag.autoOpen();
        if($(this).data('modid')==1){
            window.parent.main.location.href = '${ctx}/admin/main.do';
        }
	});
	
	
	
	$("#logout_link").bind("click",function(){
		if(confirm('是否确定退出?'))
		{
			$.ajax({
					   type: "POST",
					   url: "${ctx}/login/logout.do?csrfToken=${csrfToken}",
					   success: function(data)
					   {
						 if(data.status=='0')
					     {
					    	 var ref = "${ctx}/login/auth.do";
					    	 window.top.location.replace(ref);
					    	 window.event.returnValue = false;
					     }else if(data.status=="1") {
					    	alert(data.message); 
					    	return false;
					     }
					   }
					});
		}	
	})
	
	
});
</script>
</html>