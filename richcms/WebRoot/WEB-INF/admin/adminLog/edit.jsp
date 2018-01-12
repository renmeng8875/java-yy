<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>轮播图管理</title>
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />

	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">方法详情查看</a>
			<a class="fr btn" href="${ctx}/adminLog/lists.do">+日志列表</a>
		</h2>
	
	

		<div class="adm-text ul-float">
			<table class="adm-table" width="100%">
				
				<tbody id="tbmain">
					<tr>
							<td width="25%" style="height:auto;">操作人</td>
							<td width="75%" style="height:auto;text-align:left;">
								  ${model.userName} 
							</td>
							
					</tr>
					
					<tr>
							<td width="25%" >操作时间</td>
							<td width="75%" style="height:auto;text-align:left;">
								  ${model.ctime}
							</td>
					</tr>	
					
					<tr>
							<td width="25%" style="height:auto;">登录IP</td>
							<td width="75%" style="height:auto;text-align:left;">
								  ${model.loginIP} 
							</td>
							
					</tr>
					
					<tr>
							<td width="25%" >方法描述</td>
							<td width="75%" style="height:auto;text-align:left;">
								  ${model.mem}
							</td>
					</tr>	
				    
				    
				</tbody>
			</table>
		</div>
	</body>
</html>