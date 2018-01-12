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
		<title>数据导入</title>
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
				'uploader'  : '${ctx}/Stataccess/importExcel.do?&csrfToken=${csrfToken}',
				'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
				'fileTypeExts'  : '*.xls; *.xlsx', 
				'buttonText' : '上传',
				'fileSizeLimit': '5120KB',
				'width' : 50,
				'height' : 20,
				'onUploadSuccess' : function(file, data, response) {
						try{
						   var jsonObj = JSON2.parse(data);
						   if(jsonObj){
							    var s=jsonObj.status;
								if(s=='1'){
								    var msg=jsonObj.msg;
									alert(msg);
								}else{
									var l=jsonObj.blackList;
									if(l&&l.length>0){
										alert("部分导入成功,但还有以下数据未能成功导入!!");
										var html="";
										for(i in l){
											html+="<tr><td>"+l[i]+"</td></tr>";
										}
										$("#content").html(html);
									}else{
										alert("导入成功!!!");
									}
								}
							}
						}catch(e){
							
						}
				}
			});
		}); 
		
		function downloadTemplate(v){
			$("#ct").val(v);
			form2.submit();
		}
		</script>

	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">数据导入</a>
		</h2>
		<form id="fm_es" method="post" enctype="multipart/form-data" action="${ctx}/Stataccess/importExcel.do">
    		<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<div class="pad10">
				<div class="adm-text ul-float">
					<ul class="clearfix">
						<li>
							<span class="b mr20">模板：</span>
							<span >
								<a href="javascript:void(0);" onclick="downloadTemplate('channel')" title="渠道访问和下载.xlsx">渠道访问和下载.xlsx</a> |
								<a href=javascript:void(0);" onclick="downloadTemplate('catstat')" title="栏目统计报表_日报.xlsx">栏目统计报表_日报.xlsx</a> |
								<a href="javascript:void(0);" onclick="downloadTemplate('3wdaily')" title="MM门户日报.xlsx">MM门户日报.xlsx</a>
							</span>
						</li>
						<li>
							<span class="b mr20" style="color:red;">注：</span>
							<span style="color:red;">
								1.请下载模板，根据模板格式进行上传。
								2.单元格格式为"文本"。
							</span>
						</li>
						<li align="center">
						    <table border="0">
						    	<thead>
						    		<tr>
						    			<th>
						    				数据上传：<input id="file_upload" name="file_upload" type="file" multiple="true">
						    			</th>
						    		</tr>
						    	</thead>
						    	<tbody id="content">
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
		<form id="form2" method="post" action="${ctx}/Stataccess/downloadModel.do">
			<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<input type="hidden" name="ct" id="ct" value=""/>
		</form>
	</body>
</html>