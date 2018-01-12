<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="keywords" content=" " />
		<meta name="description" content="" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>管理栏目</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
		<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/base.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerTree.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerLayout.js" type="text/javascript"></script>
		<style>
			.catlist td {
				padding: 0;
				margin: 0;
				line-height: 100%;
			}
		</style>
		<script type="text/javascript">
//------------------------------------------function var
var tree;
var data = [];
var jsonData = ${treeData};
var treeMenuData = jsonData;
	function onBeforeExpand(note){              
            if(note.data.children && note.data.children.length == 0){
             	tree.loadData(note.target,"${ctx}/Content/getChildren.do?csrfToken=${csrfToken}&pid="+note.data.CATID);
            }
	}

	function forwardContentList(){
		var node = tree.getSelected();
		if(node){
			var catId='';
			var catName='';

			nodeData=node.data;
			catId=nodeData.CATID;
			catName=nodeData.CATNAME;
			document.getElementById('myFrame').src="${ctx}/Content/listtpl.do?csrfToken=${csrfToken}&catId="+catId+"&catName="+encodeURIComponent(encodeURIComponent(catName));
		}
	}
//------------------------------------------ligerUi
$(function() {
	$("#layout1").ligerLayout({leftWidth:250});
	
	tree = $("#categoryTree").ligerTree({
			 data:treeMenuData,
             idFieldName :'CATID',
             single:true,
             textFieldName:'CATNAME',
             nodeWidth:'100%',
             slide : false,
             checkbox :false,
             parentIDFieldName :'PID',
             isExpand:false,
             onBeforeExpand: onBeforeExpand,
             onSelect:forwardContentList
    });
    treeManager = $("#categoryTree").ligerGetTreeManager();
    treeManager.collapseAll();
});
</script>

	</head>
	<body style="overflow-x: hidden;overflow-y: hidden; padding: 4px;">
	<div id="layout1">
            <div position="left" style="width:90%; height:95%; margin:10px; float:left; border:1px solid #ccc; overflow:auto; ">
            	<ul id="categoryTree"></ul>
            </div>
            <div position="center">
            	<iframe class="leftcat" src="${ctx}/Content/listtpl.do?csrfToken=${csrfToken}&catId=${catId}" name="myFrame" frameborder="0" marginheight="0" id="myFrame" marginwidth="0"  style="width: 100%;height:100%"></iframe>
            </div>  
    </div> 
	</body>
</html>