<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  request.setAttribute("ctx", request.getContextPath());
  
  //response.setHeader("Cache-Control", "no-store"); //HTTP 1.1 
  //response.setHeader("Pragma", "no-cache"); //HTTP 1.0 
  //response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

