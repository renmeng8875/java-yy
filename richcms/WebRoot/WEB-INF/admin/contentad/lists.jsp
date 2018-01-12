<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>广告管理</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"	type="text/javascript"></script>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : '排序',
				name : 'priority',
				width : 100,
				align : 'center'
			},{
				display : '广告ID',
				name : 'dataid',
				width : 120,
				align : 'center'
			}, {
				display : '广告标题',
				name : 'title',
				width : 120,
				align : 'center'
			}, {
				display : '广告类型',
				name : 'imgtype',
				width : 200,
				align : 'center'
			},{
				display : '小标语',
				name : 'editorlanguage',
				width : 200,
				align : 'center'
			}, {
				display : '开始时间',
				name : 'starttime',
				width : 200,
				align : 'center'
			}, {
				display : '结束时间',
				name : 'endtime',
				width : 120,
				align : 'center'
			}, {
				display : '创建时间',
				name : 'utime',
				width : 120,
				align : 'center'
			}, {
				display : '发布者',
				name : 'author',
				width : 120,
				align : 'center'
			}],
			width : '100%',
			pageSize: 5,
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'sid',
            width: '100%', height: '100%', pageSize: 10,rownumbers:true,
            checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
            
			data : { Rows : ${contentadlist} },
            
			toolbar : {
				items : [ {
					text : '增加',
					click : itemclick,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '修改',
					click : itemclick,
					icon : 'modify'
				}, {
					line : true
				}, {
					text : '删除',
					click : itemclick,
					icon : 'delete'
				} ]
			}
	 
		});
		 gridManager = $("#maingrid").ligerGetGridManager();
	});
	function itemclick(item) {
		if (item.id) {
			switch (item.id) {
			case "modify":
				var rowsdata = gridManager.getCheckedRows();
				var str = "";
				$(rowsdata).each(function() {
					str += this.CustomerID + ",";
				});
				if (!rowsdata.length)
					alert('请选择行');
				else
					alert(str);
				return;
			case "delete":
				var data = gridManager.getCheckedRows();
				if (data.length == 0)
					alert('请选择行');
				else {
					var checkedIds = [];
					$(data).each(function() {
						checkedIds.push(this.CustomerID);
					});
					$.ligerDialog.confirm('确定删除' + checkedIds.join(',') + '?',
							function() {
								alert('演示数据，不能删除');
							});
				}
				return;
			}
		}
		alert(item.text);
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title clearfix"><a class="fl title">MM之旅列表</a><a class="fr btn" href="{:U('TravelSet/addtravel')}">+添加</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>