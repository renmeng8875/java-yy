<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>风格管理</title>
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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<style>
.treeopra{
    color: #219B00;
    display: inline-block;
    height: 20px;
    line-height: 20px;
    margin: 0 10px;
    text-decoration: none;
}
</style>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : 'ID',
				name : 'styleid',
				width : 100,
				align : 'center'
			},{
				display : '风格标识',
				name : 'style',
				width : 150,
				align : 'center'
			}, {
				display : '风格中文名',
				name : 'stylename',
				width : 150,
				align : 'center'
			}, {
				display : '风格作者',
				name : 'author',
				width : 150,
				align : 'center'
			},{
				display : '风格版本',
				name : 'version',
				width : 120,
				align : 'center'
			}, {
				display : '风格模版路径',
				name : 'path',
				 
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
			data : { Rows : ${stylelist} },
			toolbar : {
				items : [ {
					text : '增加',
					click : add,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '修改',
					click : modify,
					icon : 'modify'
				}, {
					line : true
				}, {
					text : '删除',
					click : del,
					icon : 'delete'
				}
				]
			}
	 
		});
		 gridManager = $("#maingrid").ligerGetGridManager();
	});
  function add() {
		window.location.href = "${ctx}/Sysstyle/add.do";
  }
  function del(){
        var data = gridManager.getCheckedRows();
		if (data.length == 0)
			alert("请至少选中一条风格数据！");
		else {
			var checkedIds = [];
			$(data).each(function() {
				checkedIds.push(this.styleid);
			});
			$.ligerDialog.confirm('确定删除' + checkedIds.join(',') + '?',function(btn){
			   if(btn){
				$.ajax({
					   type: "post",
					   url: "${ctx}/Sysstyle/del.do?csrfToken=${csrfToken}",
					   data: "idstr="+checkedIds,
					   success: function(msg){
					     if(msg.status=='0') {
					    	 alert("删除成功！"); 
					    	 gridManager.deleteSelectedRow();
					     }
					   }
					})
				}	
			});
		}
 
	}
 
   function modify(){
		var row = gridManager.getSelectedRow();
		if(!row){
			alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = "${ctx}/Sysstyle/add.do?styleid="+row.styleid;
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title clearfix"><a class="fl title">模版管理</a><a href="${ctx }/Sysstyle/add.do" class="fr btn">+添加模版配置</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>