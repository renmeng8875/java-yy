<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
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

var grid;
var jsonData = ${treeData};

var menuidArray = ${menuidArray};
var treeMenuData = {Rows:[jsonData]};
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
			width : 250,
			align : 'center'
		},
		{
			display : '方法名',
			name : 'action',
			width : 250,
			align : 'center'
		}
		],
		width : '100%',
		usePager:false,
		height : '97%',
		allowHideColumn : false,
		rownumbers : true,
		colDraggable : true,
		rowDraggable : true,
		checkbox : true,
		data : treeMenuData,
		alternatingRow : false,
		tree : {
			columnName : 'menu'
		},
		isChecked: function(rowData){
			var menuId = rowData.menuId;
			if(jQuery.inArray(menuId, menuidArray)>-1){
				return true;
			}
			return false; 
		},
		toolbar: { items: [
                { line: true },
                { text: '确认修改', click:itemClick, icon: 'modify' },
                { line: true }
                ]
                }
	});
	
});

function itemClick()
{
	var rows = grid.getSelectedRows();
    var ids = [];
   	for(var i=0;i<rows.length;i++)
   	{
   		ids.push(rows[i].menuId);	
   	}
   	var idstr = ids.join(",");
   	$.ajax({
	   type: "POST",
	   url: "${ctx}/SysRole/grantsys.do?roleid=${roleid}",
	   data: "idstr="+idstr+"&csrfToken=${csrfToken}",
	   success: function(msg)
	   {
	     if(msg.status=='0')
	     {
	    	 alert("系统权限分配成功,退出重新登录生效！"); 
	     }
		  
	   }
	});
}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
<h2 class="adm-title">
<a class="title" href="javascript:;">角色管理 - <font color="green">${role.roleName }</font>&nbsp;&nbsp;系统权限设置</a>
<a class="fr btn" href="javascript:history.go(-1);">+角色管理</a>
</h2>
<div class="l-clear"></div>

		<div id="maingrid"></div>

		<div style="display: none;">

		</div>
		
		<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>