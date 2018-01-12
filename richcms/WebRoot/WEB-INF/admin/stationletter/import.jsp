<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="keywords" content=" " />
		<meta name="description" content="" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">  
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>站内信导入</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
		
		<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/json2.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>

		<style type="text/css">
			.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
			.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
		</style>
		<script type="text/javascript">
		$(document).ready(function()
        {
			$('#file_upload').uploadify({
				'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
				'uploader'  : '${ctx}/StationLetter/importExcel.do?&csrfToken=${csrfToken}',
				'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
				'buttonText' : '上传',
				'width' : 50,
				'height' : 20,
				'onUploadSuccess' : function(file, data, response) {
					alert('successfully uploaded ' + ':' + data);
				}
			});
		});  
		</script>

	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">站内信导入</a>
		</h2>
		<form id="fm_es" method="post" enctype="multipart/form-data" action="${ctx}/StationLetter/importExcel.do">
    		<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<div class="pad10">
				<div class="adm-text ul-float">
					<ul class="clearfix">
						<li>
							<span class="b mr20">导入步骤：</span>
						</li>
						<li>
							<span class="b mr20">1. 点击下载模板文件：</span>
							<span >
								<a href="${ctx}/static/template/lettertemplate.xls" title="站内信模板.xlsx">站内信模板.xls</a>
							</span>
						</li>
						<li>
							<span style="color:red;">
								2. 根据模板格式填写数据，单元格格式为文本。
							</span>
						</li>
						<li align="center">
						    <table border="0">
						    	<thead>
						    		<tr>
						    			<th>
						    				3. 上传：<input id="file_upload" name="file_upload" type="file" multiple="true">
						    			</th>
						    		</tr>
						    	</thead>
						    	<tbody>
						    		<tr>
						    			<td></td>
						    		</tr>
						    	</tbody>
						    </table>
							
						</li>
					</ul>
				</div>
			</div>
    		  
		</form>
	</body>
</html>