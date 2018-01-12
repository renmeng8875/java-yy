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
function itemclick()
{
 	var rows = grid.getSelectedRows();
    var ids = [];
   	for(var i=0;i<rows.length;i++)
   	{
   		ids.push(rows[i].catId);	
   	}
   	var idstr = ids.join(",");
   	$.ajax({
	   type: "POST",
	   url: "${ctx}/SysRole/grantcat.do?roleid=${role.roleid}",
	   data: "idstr="+idstr+"&csrfToken=${csrfToken}",
	   success: function(msg)
	   {
	     if(msg.status=='0')
	     {
	    	 alert("${role.roleName}的栏目权限分配成功！"); 
	     }
		  
	   }
	});
}
var grid;
var jsonData = ${treeData};
var catIdArray = ${catIdArray};
var treeMenuData = {Rows:[jsonData]};
$(function() {
	grid = $("#maingrid").ligerGrid( {
		columns : [ {
			display : '栏目名',
			name : 'name',
			width : 250,
			align : 'center'
		}, {
			display : '栏目ID',
			name : 'catId',
			id : 'id1',
			width : 250,
			type : 'int',
			align : 'center'
		},
		{
			display : '创建人',
			name : 'userId',
			width : 250,
			align : 'center'
		},
		{
			display : '优先级',
			name : 'priority',
			width : 250,
			align : 'center'
		}
		],
		width : '100%',
		height : '97%',
		usePager:false,
		allowHideColumn : false,
		rownumbers : true,
		colDraggable : true,
		rowDraggable : true,
		checkbox : true,
		data : treeMenuData,
		alternatingRow : false,
		tree : {
			columnName : 'name'
		},
		
		isChecked: function(rowData){
			var catId = rowData.catId;
			if(jQuery.inArray(catId, catIdArray)>-1)
			{
				return true;
			}
			return false; 
		},
		toolbar: { items: [
                { line: true },
                { text: '确认修改', click:itemclick, icon: 'modify' },
                { line: true }
                ]
                }
	});
});
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
<h2 class="adm-title">
<a class="title" href="javascript:;">角色管理 - <font color="green">${role.roleName }</font>&nbsp;&nbsp;栏目权限设置</a>
<a class="fr btn" href="javascript:history.go(-1);">+角色管理</a>
</h2>
<div class="l-clear"></div>

		<div id="maingrid"></div>

		<div style="display: none;">

		</div>
		
		<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>