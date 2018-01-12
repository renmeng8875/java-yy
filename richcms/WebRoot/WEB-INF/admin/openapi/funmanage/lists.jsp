<%@include file="../../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>开放接口功能管理</title>
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
					display : 'ID',
					name : 'funId',
					width : 80,
					align : 'center'
				}, {
					display : '类名称',
					name : 'className',
					width : 150,
					align : 'center'
				}, {
					display : '方法名',
					name : 'method',
					width : 150,
					align : 'center',
				},{
					display : '备注',
					name : 'mem',
					width : 350,
					align : 'center'
				},{
					display : '创建时间',
					name : 'ctime',
					width : 180,
					align : 'center'
				},
				{
					display:'操作',
					name:'opration',
					width : 100,
					align : 'center',
					render:function(item){
						return '<a  href="#" onclick="deleteFun('+item.funId+')"><font color="green">删除</font></a>';
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
			data : { Rows : ${funManageList} },
            
			toolbar : {
				items : [{
					text : '${operType}',
					click : ${click},
					icon : '${icon}'
				}
				]
			}
	 
		});
	});
	function choseFuns()
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
	   		ids.push(rows[i].funId);	
	   	}
	   	var idstr = ids.join(",");
		window.returnValue = idstr;
		window.close();
	}
	function deleteFuns()
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
	   		ids.push(rows[i].funId);	
	   	}
	   	var idstr = ids.join(",");
	   	if(confirm("是否确定删除?"))
	   	{
	   		$.ajax({
			   type: "POST",
			   url: "${ctx}/funmanage/deletefun.do?csrfToken=${csrfToken}",
			   data: "idstr="+idstr,
			   success: function(msg)
			   {
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 gridManager.deleteSelectedRow();
			     }else{
			    	 alert("存在被相关角色引用的数据，不允许删除！");
			    	 return false;
			     }
			   }
			});
	   	}
	    
	}
	function deleteFun(funId)
	{
		if(confirm("是否确定删除?"))
	   	{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/funmanage/deletefun.do?csrfToken=${csrfToken}",
			   data: "idstr="+funId,
			   success: function(msg)
			   {
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 gridManager.deleteSelectedRow();
			     }else{
			    	 alert("你选中的功能已被相关角色引用，不允许删除！");
			    	 return false;
			     }
				  
			   }
			});
		}
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix"><a class="fl title">功能列表</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>