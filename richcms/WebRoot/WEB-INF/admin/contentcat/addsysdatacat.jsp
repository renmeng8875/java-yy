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
		<title>栏目管理 - <c:choose><c:when test="${info.catId!=null}">修改</c:when><c:otherwise>添加</c:otherwise></c:choose>栏目</title>
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
			$(function(){
				var treedata = ${treeData};
				var settings = {selectName:"pid",
								selectId:"selectCagtogyid",
								data:treedata,
								containerId:"pid",
								selectLevel:"catLevel",
								optionValue:"catId",
								subSet:"children",
								selectedId :'${pid}',
								optionText:"name",
								isMultiSelect:true,
								style:'max-height:300px; min-height:200px;'
								};
				var treeSelector = new TreeSelector(settings);
			});
			
			$(function(){
				$("#selectCagtogyid").dblclick(function(){
					var value = $('option:selected',this).val(),txt = $('option:selected',this).text();
					if(txt){
						txt = txt.replace(/\|/g,'');
						txt = txt.replace(/-/g,'');
						txt = $.trim(txt);
					}
					var isExists=0;//记录是否有存在的项，如果有就不从左添加右框
					$("#seledCat option").each(function(index, element){
			            if(value == $(this).val()){
							isExists++;
						}
			        });
					if(isExists==0 && $('option:selected',this).data('haschild')=='0'){
						$("#seledCat").append('<option value="'+value+'">'+txt+'</option>');
					}
				});
					
				$("#seledCat").dblclick(function(){
					$('option:selected',this).remove();
				});

				$("#fm_es").Validform({
						tiptype:3,
						label:".label",
						showAllError:false,
						datatype:{
							"zh2-50":/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,50}$/,
							"zhs2-50":/^[a-z]{1}[a-z0-9]{1,49}$/,
							"ns1-99":/^[1-9]\d{0,1}$/,
							"checkUrl":function(gets,obj,curform,regxp){
								var reg1=/^[\w\W]{0,300}$/,
									reg2=/^(\w+:\/\/)?\w+(\.\w+)+.*$/;
								if(!reg1.test(gets)){
									return "请填写300位字符范围内！";
								}
								if(gets!=''&&!reg2.test(gets)){
									return "请填写网址！";
								}
								return true;
							}
						},
						beforeSubmit:function(){
							var arr = Array();
							
							$("#seledCat option").each(function(index, element) {
								arr.push($(this).val());
								$("#pcatId").val(arr.join(','));
					        });
							return true;
						}
					});
				
		});
		</script>

	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">内容标签管理 - <c:choose><c:when test="${info.catId!=null}">修改</c:when><c:otherwise>添加</c:otherwise></c:choose>
			</a>
			<a class="fr btn" href="${ctx}/ContentCat/contenttaglist.do">+返回</a>
		</h2>
		<form id="fm_es" method="post" enctype="multipart/form-data" action="${ctx}/ContentCat/editTag.do">
			<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<div class="pad10">
				<div class="adm-text ul-float">
					<ul class="clearfix">
						<li>
							<span class="b mr20">栏目名称：</span>
							<span id="pid" ></span>
							选择发布
							<select multiple="multiple" id="seledCat" style="max-height: 300px; min-height: 200px; min-width: 200px;">
								<c:forEach var="item" items="${catNameMap}">
									<option value="${item.key}">${item.value}</option>
								</c:forEach>
							</select>
							<input id="pcatId" name="pcatId" type="hidden" value="${info.pcatId}" />
						</li>
						<li>
							<span class="b mr20">数据源：</span>
							<select name="dataType" id="dataType">
								<c:forEach var="item" items="${dataTypeList}" varStatus="status">
									<option value="${item.datatype}"<c:if test="${item.datatype eq info.dataType}">selected="selected"</c:if>>
										${item.formdesc}
									</option>
								</c:forEach>
							</select>
							<span id="catnameTip"></span>
						</li>
						<li>
							<span class="b mr20">标签名称：</span>
							<input name="appType" type="text" value="${info.appType}" id="appType"  datatype="s2-10" nullmsg="请输入标签名称!" errormsg="标签名称必须在2～10位字符范围内!" />
							<span id="apptypeTip"></span>
							<span id="catnameTip"></span>
						</li>
						<li>
							<span class="b mr20">跳转地址：</span>
							<input type="text" name="jumpUrl" value="${info.jumpUrl}" id="jumpUrl" datatype="checkUrl"  />
							<span id="catTip"></span>
						</li>
						<li>
						
							<span class="b mr20">排序：</span>
							<c:choose>
							   <c:when test="${info.priority==null}">	<input type="text" name="priority" value="" id="priority" datatype="ns1-99" nullmsg="请输入排序值!" errormsg="排序值必须在1~99范围内!"/></c:when>
						       <c:otherwise><input type="text" name="priority" value="${info.priority}" id="priority" datatype="ns1-99" nullmsg="请输入排序值!" errormsg="排序值必须在1~99范围内!"/></c:otherwise>
							</c:choose><span style="color:red;">(排序值必须在1~99范围内)</span>
							<span id="catTip"></span>
						</li>
					</ul>
				</div>
			</div>
			<div class="adm-btn-big">
				<c:if test="${!empty info.catId}">
					<input name="catId" type="hidden" value="${info.catId}" />
				</c:if>
				<input type="submit" value="提&nbsp;&nbsp;交" />
			</div>
		</form>
	</body>
</html>