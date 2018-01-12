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
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
</head>
<body>
<div class="main_border" style="border:0;"><div id="my_main">
  <h2 class="adm-title"><a class="title">修改模版配置</a>
  <c:choose>
     <c:when test="${formtype=='data' }"><a href="${ctx }/dataconfig/catconfig.do" class="fr btn">数据源列表</a></c:when>
     <c:otherwise><a href="${ctx }/dataconfig/formlists.do" class="fr btn">数据源列表</a></c:otherwise>
  </c:choose>
  </h2>
<div class="pad10">
  <table class="adm-table">
    <tr class="tr1">
      <td>风格</td>
      <td>infopath</td>
    </tr>
    <c:forEach items="${data}" var="vo">
        <tr>
          <td>${vo.stylename}</td>
          <td>
          	<select name="style${vo.tId}" id="${vo.style}">
              <c:forEach items="${vo.template}" var="tpl">
                <option value="${tpl.path}">${tpl.name}</option>
              </c:forEach>
            </select>
          </td>
        </tr>
    </c:forEach>
  </table>
  <br />
</div>

<input type="hidden" id="srcid" name="srcid" value="${srcid}" />
<div class="adm-btn-big"><input id="btnSub" type="button" value="保&nbsp;&nbsp;存" onclick="submitTPL()" /></div>
</div></div>
<script type="text/javascript">
	$(function(){
		$("#form1").Validform({
			tiptype:2
		});
	})
	
	//异步提交模版设置表单
function submitTPL() {
	var str = "";
	$("select[name^=style]").each(function(e) {
		if(str == "") {
			str += $(this).attr("id") + ":" + $(this).val();	
		} else {
			str += ";" + $(this).attr("id") + ":" + $(this).val();	
		}								   
	})	
	$.ajax({
		type: "POST",
		dataType:"json",
		url: "${ctx}/dataconfig/savetemplate.do?csrfToken=${csrfToken}",
		data: "str=" + encodeURI(str) + "&srcid=" + $("#srcid").val(),
		success: function(res) {
			if(res.status == 1) {
				alert("保存成功");	
				if(${formtype=='data' }){
					location = "${ctx}/dataconfig/catconfig.do";
				}else{
					location = "${ctx}/dataconfig/formlists.do";
				}
				
			} else {
				alert("保存失败，请重试");
				return;
			}
		}
	})
}
</script>
 
</body>
</html>