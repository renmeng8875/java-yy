<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : 'ID',
				name : 'sid',
				width : 50,
				align : 'center'
			}, {
				display : '字段类型',
				name : 'systype',
				width : 150,
				align : 'center'
			}, {
				display : '字段名称',
				name : 'syskey',
				width : 200,
				align : 'center'
			}, {
				display : '字段值',
				name : 'sysvalue',
				width : 400,
				align : 'center',
				type:'string'
			}, {
				display : '字段描述',
				name : 'mem',
				width : 250,
				align : 'center'
			}, {
				display : '修改时间',
				name : 'ctimeStr',
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
			data : { Rows : ${configlist} },
			toolbar : {
				items : [  {
				    id   : '1',
					text : '增加',
					click : itemclick,
					icon : 'add'
				}, {
					line : true
				},{
				    id   : '2',
					text : '修改',
					click : itemclick,
					icon : 'modify'
				}, {
					line : true
				}, {
				    id   : '3',
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
			case "1":
				window.location.href="${ctx}/Sysconfig/add.do";
				break;
			case "2":
				var row = gridManager.getSelectedRow();
				if(!row){
					alert("请先选择一条要修改的数据！");
					return false;
				}
				window.location.href = "${ctx}/Sysconfig/add.do?sid="+row.sid;
				return;
			case "3":
				var data = gridManager.getCheckedRows();
				if (data.length == 0)
					alert("请至少选中一条分类数据！");
				else {
					var checkedIds = [];
					var isFlag=false;
					$(data).each(function() {
						checkedIds.push(this.sid);
						if(this.systype=='system')
						{
							isFlag=true;
							return false;
						}
					});
					
					if(isFlag)
					{
						alert("字段类型为system的数据不能删除!");
						return false;
					}
					
					$.ligerDialog.confirm('确定删除' + checkedIds.join(',') + '?','确认',function(btn){
					       if(btn){
						       $.ajax({
								   type: "post",
								   url: "${ctx}/Sysconfig/del.do?csrfToken=${csrfToken}",
								   data: "idstr="+checkedIds,
								   success: function(msg){
								     if(msg.status=='0') {
								    	 alert("删除成功！"); 
								    	 gridManager.deleteSelectedRow();
								     }
								   }
								});
					       }
					});
					
				}
				return;
			}
		}
		
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title clearfix">
		<a class="fl title">系统参数管理</a><a
			href="${ctx}/Sysconfig/add.do"
			class="fr btn">+添加参数</a>
	</h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>