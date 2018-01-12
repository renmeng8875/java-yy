<%@include file="../../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色管理</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
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
<style type="text/css">
a:link{color:green;font-size:14px;text-decoration: underline;}
a:visited{color:red;font-size:14px;text-decoration: underline;} 
</style>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
					display : '角色ID',
					name : 'roleId',
					width : 80,
					align : 'center'
				}, {
					display : '角色名称',
					name : 'roleName',
					width : 280,
					align : 'center'
				}, {
					display : '角色描述',
					name : 'roleMem',
					width : 250,
					align : 'center',
				},
				{
					display:'操作',
					name:'opration',
					width : 450,
					align : 'center',
					render:function(item){
						var oper = '<a href="#" onclick="openFunOfRole('+item.roleId+')"><font color="green">功能权限</font></a>&nbsp;&nbsp;&nbsp;&nbsp;';
							oper += '<a href="#"  onclick="openCatOfRole('+item.roleId+')"><font color="green">栏目权限</font></a>&nbsp;&nbsp;&nbsp;&nbsp;';
							oper += '<a href="#"  onclick="modifyRole(\''+item.roleId+'\',\''+item.roleName+'\',\''+item.roleMem+'\')"><font color="green">修改</font></a>&nbsp;&nbsp;&nbsp;&nbsp;';
							oper += '<a href="#"  onclick="deleteRole('+item.roleId+')"><font color="red">删除</font></a>';
						return oper;
					}
			}
			],
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'sid',
            width: '100%', 
            height: '97%', 
            rowHeight: 32,                       
            pageSize: 10,
            rownumbers:true,
            checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
			data : { Rows : ${roleManageList} },
            
			toolbar : {
				items : [{
					text : '增加',
					click : addRole,
					icon : 'add'
				},{
					text : '删除',
					click : deleteRoles,
					icon : 'delete'
				}
				]
			}
	 
		});
	});
	function addRole() 
	{
		window.location.href = "${ctx}/rolemanage/add.do";
	}
	function modifyRole(roleId,roleName,mem) 
	{
		window.location.href = "${ctx}/rolemanage/modify.do?roleId="+roleId+"&roleName="+encodeURIComponent(encodeURIComponent(roleName))+"&roleMem="+encodeURIComponent(encodeURIComponent(mem));
	}
	function openCatOfRole(roleId)
	{
		window.location.href = "${ctx}/rolemanage/grantcat.do?roleId="+roleId;
	}
	function openFunOfRole(roleId,roleName,mem) 
	{
		window.location.href = "${ctx}/rolemanage/funmanage/list.do?roleId="+roleId;
	}
	function deleteRoles()
	{
		var rows = gridManager.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一条数据！");
			return false;
		}
	    var ids = [];
	   	for(var i=0;i<rows.length;i++)
	   	{
	   		ids.push(rows[i].roleId);	
	   	}
	   	var idstr = ids.join(",");
	   	if(confirm("是否确定删除?"))
	   	{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/rolemanage/delete.do?csrfToken=${csrfToken}",
			   data: "idstr="+idstr,
			   success: function(msg)
			   {
			   	 if(msg.idstr)
			   	 {
			   	 	alert("删除失败,角色ID为"+msg.idstr+"已被应用管理引用!");
			   	 	return false;
			   	 } 
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 gridManager.deleteSelectedRow();
			     }
			   }
			});
		}
	}
	function deleteRole(roleId)
	{
		if(confirm("是否确定删除?"))
	   	{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/rolemanage/delete.do?csrfToken=${csrfToken}",
			   data: "idstr="+roleId,
			   success: function(msg)
			   {
			   	 if(msg.idstr)
			   	 {
			   	 	alert("删除失败,该角色已被应用管理引用!");
			   	 	return false;
			   	 }
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 gridManager.deleteSelectedRow();
			     }
				  
			   }
			});
		}
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix"><a class="fl title">角色管理</a>
	<a class="fr btn" href="${ctx}/rolemanage/add.do">+添加角色</a>
	</h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>