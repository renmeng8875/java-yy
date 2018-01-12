<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>main</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
		<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/base.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js" type="text/javascript"></script>
<script type="text/javascript">
function itemclick()
{
    var rows = grid.getSelectedRows();
    if(rows&&rows.length>15)
    {
    	alert("您选择的快捷操作超过15个，请重新设置!");
    	return false;
    }
    var ids = [];
   	for(var i=0;i<rows.length;i++)
   	{
   		ids.push(rows[i].menuId);	
   	}
   	var idstr = ids.join(",");
   	$.ajax({
	   type: "POST",
	   url: "${ctx}/menu/addShortcut.do?csrfToken=${csrfToken}",
	   data: "idstr="+idstr,
	   success: function(msg)
	   {
	     if(msg.status=='0')
	     {
	    	 alert("添加快捷操作成功！"); 
	     }
		  
	   }
	});	
}
var grid;
var jsonData = ${gridData};
var menuIdArray = ${menuIdArray};
var treeMenuData = {Rows:jsonData};
$(function() {
	grid = $("#maingrid").ligerGrid( {
		columns : [ {
			display : '菜单名',
			name : 'menu',
			width : 250,
			align : 'center'
		}, {
			display : '菜单ID',
			name : 'menuId',
			id : 'id1',
			width : 250,
			type : 'int',
			align : 'center'
		},
		{
			display : '类名',
			name : 'control',
			width : 200,
			align : 'center'
		},
		{
			display : '方法名',
			name : 'action',
			width : 200,
			align : 'center'
		}
	
		],
		width : '100%',
		pageSizeOptions : [ 50,100 ],
		pageSize:50,
		height : '97%',
		allowHideColumn : false,
		rownumbers : true,
		colDraggable : true,
		rowDraggable : true,
		checkbox : true,
		data : treeMenuData,
		alternatingRow : false,
		isChecked: function(rowData){
			var menuId = rowData.menuId;
			if(jQuery.inArray(menuId, menuIdArray)>-1){
				return true;
			}
			return false; 
		},
		toolbar: { items: [
                { text: '确定', click:itemclick, icon: 'ok' },
                { line: true }
                ]
                }
	});

	
});
</script>
	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		
		<h2 class="adm-title"><a class="title" href="javascript:;">添加快捷操作</a><a class="fr btn" href="${ctx}/menu/listShortcut.do">返回上一级</a></h2>


		<div class="l-clear"></div>

		<div id="maingrid"></div>

		<div style="display: none;">

		</div>
		
		<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>

	</body>
</html>
