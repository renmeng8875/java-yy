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

</head>
<body style="background-color: white;">
 <h2 class="adm-title"><a class="title">管理员管理</a><a class="fr btn" href="javascript:history.go(-1);">+管理员管理</a></h2>
<form name="fm_es" id="fm_es" method="post" action="${ctx}/Adminsetting/changerole.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="b">用户id：</span>
        <input type="text" name="userId" style="border:0;" id="userId" class="l" value="${user.userId}" readonly />
      </li>
      <li><span class="b">用户名：</span>
        <input type="text" name="userName" style="border:0;" id="userName" class="l" value="${user.userName }" readonly />
      </li>
      <li><span class="b">所属角色：</span>
        <select style="width:148px; padding:0;" name="role" id="roleId">
			<c:forEach items="${roleList}" var="role" varStatus="status">
				<option value="${role.roleid}"  <c:if test="${role.roleid==user.role.roleid }"> selected="selected" </c:if> >
					${role.roleName}
				</option>
			</c:forEach>
        </select>
      </li>
    </ul>
  </div>
</div>
<div class="adm-btn-big"><input type="submit" id="submit" value="提&nbsp;&nbsp;交" /></div>
</form>

</body>
</html>