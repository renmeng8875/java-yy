<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员管理</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/RSA.js"></script>
<script type="text/javascript">
$(function(){
		$("#fm_es").Validform({
			tiptype:3,
			label:".label",
			showAllError:false,
			datatype:{
				"zh2-4":/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,4}$/,
				"ns8-15":function(gets,obj,curform,regxp){
					//var reg1=/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,15}$/g;
					var reg1=/(?=.*[\d]+)(?=.*[a-zA-Z]+)(?=.*[^a-zA-Z0-9]+).{8,15}/;
					if(reg1.test(gets)){
						return true;
					}
					return "密码长度在8～15个字符范围内,且必须为数字、字母和特殊字符的组合!";
				}
			},
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
<body style="background-color: white;">
 <h2 class="adm-title"><a class="title">管理员管理</a><a class="fr btn" href="javascript:history.go(-1);">+管理员管理</a></h2>
<form id="fm_es" method="post" action="${ctx}/Adminsetting/adduser.do">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix">
      <li><span class="need" >*</span><span class="b">用&nbsp;户&nbsp;名：</span>
        <input type="text" name="username" id="username" class="l" datatype="s3-20" sucmsg="验证通过!" nullmsg="请输入用户名!"  ajaxurl="${ctx}/Adminsetting/checkName.do?csrfToken=${csrfToken}"/>
      </li>
      <li><span class="need" >*</span><span class="b">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</span>
        <input type="password" name="passwd" id="passwd" value="" class="l" datatype="ns8-15"/>
      </li>
      <li><span class="need" >*</span><span class="b">确认密码：</span>
        <input type="password" name="passwd1" id="passwd1" value="" class="l" datatype="ns8-15" sucmsg="验证通过" recheck="passwd"/>
      </li>
      <li><span class="need" >*</span><span class="b">真实姓名：</span>
        <input type="text" name="realname" id="realname"  datatype="zh2-4" class="l" nullmsg="请输入真实姓名！" errormsg="用户名必须为二到四个汉字！"/>
      </li>
      <li><span class="b">所属角色：</span>
        <select style="width:148px; padding:0;" name="role" id="role">
			
			<c:forEach items="${roleList}" var="role" varStatus="status">
				<option value="${role.roleid}">${role.roleName}</option>
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