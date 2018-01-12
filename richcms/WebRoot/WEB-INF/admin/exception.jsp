<%@include file="public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
        statusCode = 0;
    }
    if(statusCode !=404){
    	statusCode = 500;
    }
    

%>
<html>
    <head>
        <title>页面<%=statusCode%>错误</title>
      
    </head>
    <body style="background-color: white;">
   <div  style="padding: 200px;color: red"><h2>对不起，系统发生<%=statusCode%>错误 <%-- <%if(statusCode==500){ %>，请<a href="${ctx}/mmport/seeLog.do?ex=1">下载附件</a>发送给<a id="photoTip" href="mailto:zhangsc@richinfo.cn">后台负责人</a><%}%></h2>--%>
    </div>
    
    
    </body>
</html>
