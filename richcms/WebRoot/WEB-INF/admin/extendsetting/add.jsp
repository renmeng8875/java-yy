<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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
<script src="${ctx}/static/admin/js/TreeSelector.js" type="text/javascript"></script>
  <script type="text/javascript">
	$(function(){
		var treedata = ${treeData};
		var settings = {selectName:"pid",
						selectId:"selectMenuid",
						data:treedata,
						containerId:"pid",
						selectLevel:"menuLevel",
						optionValue:"menuId",
						subSet:"children",
						selectedId :${pid},
						optionText:"menu"};
		var treeSelector = new TreeSelector(settings);
	});
 
	</script>
</head>
<body>
	<div class="main_border" style="border:0;">
		<div id="my_main">
			<h2 class="adm-title"> <a class="title">
			 <c:choose>
			       	    <c:when test="${info.menuId != null}">
			       	    	修改菜单
			       	    </c:when>
			        	<c:otherwise>
			        		添加子菜单
			        	</c:otherwise>
			 </c:choose>
			 </a>
			<a class="fr btn"	href="${ctx}/Extendsetting/lists.do">+返回菜单管理</a></h2>
			 <div style="margin:10px 20px;">
			<form id="fm_es" method="post"  action="${ctx}/Extendsetting/edit.do">
			<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<table style="font-size:12px;color:#444;line-height:32px;">
			   <tr>
				 <td width="100">上级菜单：</td>
				 <td><span id="pid" ></span></td>
				 <td><div class="Validform_checktip"></div></td>
			   </tr>
			   <tr>
				 <td width="100"><span class="need" >*</span>菜单名称：</td>
				 <td><input id="menu" datatype="s2-20" nullmsg="请输入菜单名！" errormsg="菜单名为2～20范围内的字符,不含特殊字符!" name="menu" type="text" value="${info.menu}" /></td>
				 <td><div class="Validform_checktip"></div></td>
			   </tr>
			   <tr>
				 <td width="100"><span class="need" >*</span>模块名：</td>
				 <td><input type="text" datatype="s2-20"  nullmsg="请输入模块名！" errormsg="模块名为以字母开头的2～20位字符,不含特殊字符!" id="control" name="control" value="${info.control}" /> </td>
				 <td><div class="Validform_checktip"></div></td>
			   </tr>
			   <tr>
				 <td width="100"><span class="need" >*</span>方法名：</td>
				 <td><input type="text" datatype="s2-20"  nullmsg="请输入方法名！" errormsg="方法名为以字母开头的2～20位字符,不含特殊字符!" id="action" name="action" value="${info.action}" /></td>
				 <td><div class="Validform_checktip"></div></td>
			   </tr>
			  
			   <tr>
				 <td width="100"><span class="need" >*</span>排序：</td>
				 <td>
				 	<input type="text" datatype="n1-9" id="orderby"  nullmsg="请输入排序号!" style="width:30px;" name="orderby"  errormsg="排序号必须是小于100的正整数"
							<c:choose>
							   <c:when test="${info.orderby!=null }">value="${info.orderby}"</c:when>
								<c:otherwise>value="99" </c:otherwise>
							</c:choose>
							/>&nbsp;&nbsp;<font color="red">(注:填写 1~99 范围内正整数)</font>
				</td>
				 <td><div class="Validform_checktip"></div></td>
			   </tr>
			    <tr>
				 <td width="100">是否显示菜单：</td>
				 <td><input type="radio" name="isHidden" value="0" <c:if test="${info.isHidden==0 || info.isHidden==null}">checked="checked"</c:if> />是 
							 <input type="radio" name="isHidden" value="1" <c:if test="${info.isHidden==1 }">checked="checked"</c:if>  />否</td>
				 <td><div class="Validform_checktip"></div></td>
			   </tr>
			   <tr>
			     <td colspan="2" align="center">
			        <c:choose>
			       	    <c:when test="${info.menuId != null}">
			       	        <input type="hidden" name="menuId" value="${info.menuId}"/>
			                <div class="adm-btn-big"><input id="btnSub" type="submit" value="修&nbsp;&nbsp;改" /></div>
			       	    </c:when>
			        	<c:otherwise>
			        	    <div class="adm-btn-big"><input id="btnSub" type="submit" value="保&nbsp;&nbsp;存" /></div>
			        	</c:otherwise>
			        </c:choose>
			        
			     </td>
			   </tr>
			</table>
			</form>
			</div>
		</div>
	</div>
<script type="text/javascript">
$(function(){
	$("#fm_es").Validform({
		tiptype:2,
		datatype:{
			"n1-9":/^[1-9][0-9]?$/
		},
		beforeSubmit:function(){
			return true;
		}
	});
})
</script>
</body>
</html>