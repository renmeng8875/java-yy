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
		<title>${catName} - 栏目内容管理</title>
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
		<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/base.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerComboBox.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"></script>
		<script src="${ctx}/static/admin/js/TreeSelector.js" type="text/javascript"></script>
		<style type="text/css">
			.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
			.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
		</style>
		<script type="text/javascript">
			function multsubmit() {
				var data = "catId=" + $("#catId").val();
				if($("#datatype1").is(":checked")) {
					data += "&datatype=android";
				} else if($("#datatype2").is(":checked")) {
					data += "&datatype=ios";
				} else if($("#datatype3").is(":checked")) {
					data += "&datatype=news";
				}else {
					alert("请选择应用类型");
					return;
				}
				var ids=$("#multiapps").val();
				if($.trim(ids) == "") {
					alert("请填写应用ID列表");
					return;
				} else {
					data += "&ids=" + $.trim(ids.replace(/\r|\n/ig,""));	
				}
				
				var reg = new RegExp(/^[0-9,;\r\n]*$/);
                if(!reg.test(ids)){
                   alert("请输入正确的数据格式！");return false;
                }
				var idArray=ids.split(";");
				if(idArray.length>21){
					alert("应用ID不能超过20个!");
					return false;
				}
				data+='&csrfToken=${csrfToken}';
				$.ajax({
					type: "POST",
					url: "${ctx}/Content/multImportApp.do",
					data: data,
					success: function(data) {
						if(data.status===undefined)
						{
							alert("导入不成功,请检查数据是否存在或者格式是否正确!");
							return false;
						}
						if(data.status == 1) {
							alert("批量导入成功");
							back();
						} else {
							if(data.status == -1){
								alert("导入不成功,请检查数据是否存在或者格式是否正确!");
								return false;
							}else if(data.status==-2){
							    alert("栏目应用类型不匹配,请检查应用类型是否正确!");
							    return false;
							}else{
								alert("以下应用导入不成功:"+data.status+" 请检查导入应用类型！");
								 return false;
							}
						}
					}
				});
			}
			
			function back(){
				window.location.href="${ctx}/Content/listtpl.do?catId=${catId}";
			}
		</script>

	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">${catName} - 栏目内容管理</a><a class="fr btn"
				href="javascript:back();">返回</a>
		</h2>
		<div style="margin: 20px;">
			<form name="multform" id="multform">
				<input type="hidden" name="csrfToken" value="${csrfToken}"/>
				<div style="margin-bottom: 20px; border: 1px dashed #FC3; width: 620px;">
					批量添加应用：每次添加上限为20个。
					<br>
					格式：排序值+应用/资讯ID，中间用英文逗号间隔。一行只能填一个应用，且每行必须以英文分号结尾。例如99,10254;
					android频道下面的栏目必须导入android应用，ios频道下面的栏目必须导入ios应用。
				</div>
				<div>
					<input type="radio" id="datatype1" name="datatype" value="android">
					安卓&nbsp;
					<input type="radio" id="datatype2" name="datatype" value="ios">
					IOS&nbsp;
					<input type="radio" id="datatype3" name="datatype" value="news">
					资讯
				</div>
				<div>
					<textarea id="multiapps" name="multiapps" cols="80" rows="5"></textarea>
				</div>
				<div style="margin: 10px;">
					<input type="button" value="批量添加" onClick="multsubmit()">
					&nbsp;&nbsp;
					<input type="button" value="取消" onClick="history.go(-1)">
				</div>
				<input type="hidden" id="catId" name="catId" value="${catId}">
			</form>
		</div>
		<form name="fm_im" id="fm_im" method="post" action="${ctx}/Content/listtpl.do">
			<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<input type="hidden" name="catId" id="catId" value="${catId}"/>
			<input type="hidden" name="catName" id="catName" value="${catName}"/>
		</form>
	</body>
</html>