<%@include file="../../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加开发者</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/RSA.js"></script>
<script type="text/javascript">
$(function(){
	$("#fm_es").Validform({
		tiptype:3,
		beforeSubmit:function(){
				var pwds = $("input[type='password']");
				pwds.each(function(){
					var pwd = this.value;
					pwd = RSAEncode(pwd);
					$(this).val(pwd);
				});
		}
	});	
});
</script>
</head>
<body>
<div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">开发者</a><a class="fr btn" href="${ctx}/developermanage/list.do">+管理开发者</a></h2>
<form name="fm_es" id="fm_es" method="post" action="${ctx}/developermanage/${opertype}.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<input type="hidden" name="apperId" value="${account.apperId }" />
<div class="pad10" >
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="need" >*</span><span class="mr20 m">开发者名称：</span>
        <input class="l" name="apper"  type="text"  value="${account.apper}" datatype="*2-50"  <c:if test="${opertype=='addapper' }">ajaxurl="${ctx}/developermanage/checkName.do?csrfToken=${csrfToken}"</c:if> sucmsg="验证通过,名称可用!" nullmsg="开发者名称不能为空!" errormsg="长度必须在2~50位字符范围内!"/>
      </li>
      <li><span class="need" >*</span><span class="mr20 m">开发者密码：</span>
        <input class="l" name="passwd"   type="password" value="" datatype="*2-50" sucmsg="验证通过,名称可用!" nullmsg="密码不能为空!" errormsg="长度必须在2~50位字符范围内!"/>
      </li>
      <li><span class="mr20 m">联系人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
        <input class="l" name="uname"   type="text"  value="${account.uname}" datatype="*2-50" sucmsg="验证通过,名称可用!"  errormsg="长度必须在2~50位字符范围内!" ignore="ignore"/>
      </li>
	  <li><span class="mr20 m">手机号码：&nbsp;&nbsp;&nbsp;</span>
        <input class="l" name="tel"  type="text" datatype="m" value="${account.tel}" ignore="ignore" errormsg="请输入正确格式的手机号码!"/>
      </li>
      <li><span class="mr20 m">联系邮箱：&nbsp;&nbsp;&nbsp;</span>
        <input class="l" name="email"  type="text" datatype="e" value="${account.email}" ignore="ignore"/>
      </li>
    </ul>
  </div>
</div>
<div class="adm-btn-big" style="text-align: left;padding-left: 10px;"><input type="submit" id="submit" value="提&nbsp;&nbsp;交" /></div>
</form>
</div></div>
</body>
</html>