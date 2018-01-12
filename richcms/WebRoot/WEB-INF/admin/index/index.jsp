<%@include file="../public/header.jsp" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MM移动商城管理后台</title>
<frameset frameborder=0 framespacing=0 border=0 rows="72, *">
  <frame src="${ctx}/admin/top.do" name="topmenu" frameborder=0 noresize scrolling='no' marginwidth=0 marginheight=0>
  <frameset frameborder=0  framespacing=0 border=0 cols="219,10, *" id="frame-body">
    <frame src="${ctx}/admin/menu.do" frameborder=0 id="menu-frame" name="leftmenu">
    <frame src="${ctx}/admin/drag.do" id="drag-frame" name="drag" frameborder="no" scrolling="no">
    <frame src="${ctx}/admin/main.do" frameborder=0 id="main-frame" name="main" scrolling="yes">
  </frameset>
</frameset>
<noframes></noframes>
</html>
