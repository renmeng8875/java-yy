<%@include file="../../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>开发者管理</title>
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
					display : '开发者ID',
					name : 'apperId',
					width : 80,
					align : 'center'
				}, {
					display : '开发者名称',
					name : 'apper',
					width : 160,
					align : 'center'
				}, {
					display : '状态',
					name : 'status',
					width : 100,
					align : 'center',
					render:function(item)
					{
						if(item.status=='1'){
							text = "<select id=\"apperstatus\" onchange=\"modifyStatus("+item.apperId+",0)\"><option selected value=\"1\">通过</option><option value=\"0\">未审核</option></select>";
						}else{
							text = "<select id=\"apperstatus\" onchange=\"modifyStatus("+item.apperId+",1)\"><option value=\"1\">通过</option><option selected value=\"0\">未审核</option></select>";
						}
						return text;
					}
				},{
					display : '联系人',
					name : 'uname',
					width : 100,
					align : 'center'
				},{
					display : '手机号码',
					name : 'tel',
					width : 150,
					align : 'center'
				},{
					display : '邮箱地址',
					name : 'email',
					width : 200,
					align : 'center'
				},{
					display : '提交时间',
					name : 'ctime',
					width : 150,
					align : 'center'
				},
				{
					display:'操作',
					name:'opration',
					width : 120,
					align : 'center',
					render:function(item){
						return '<a href="#" onclick="deleteApper('+item.apperId+')"><font color="green">删除</font></a>';
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
			data : { Rows : ${developerManagerlist} },
            
			toolbar : {
				items : [ {
					text : '增加',
					click : addApper,
					icon : 'add'
				},
				{
					line:true
				},
				{
					text:'修改',
					click:modifyApper,
					icon:'modify'
				},
				{
					line:true
				},
				{
					text : '删除',
					click : deleteAppers,
					icon : 'delete'
				}
				]
			}
	 
		});
	});
	function addApper() 
	{
		window.location.href = "${ctx}/developermanage/addapper.do";
	}
	function modifyApper()
	{
		var row = gridManager.getSelectedRow();
		if(!row){
			$.ligerDialog.alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = '${ctx}/developermanage/editApper.do?apperId='+row.apperId;
	}
	function deleteAppers()
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
	   		ids.push(rows[i].apperId);	
	   	}
	   	var idstr = ids.join(",");
	   	if(confirm("是否确定删除?"))
	   	{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/developermanage/deleteapper.do?csrfToken=${csrfToken}",
			   data: "idstr="+idstr,
			   success: function(msg)
			   {
			   	 if(msg.idstr)
			   	 {
			   	 	alert("删除失败,开发者ID为"+msg.idstr+"已被应用管理引用!");
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
	function deleteApper(apperId)
	{
		if(confirm("是否确定删除?"))
	   	{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/developermanage/deleteapper.do?csrfToken=${csrfToken}",
			   data: "idstr="+apperId,
			   success: function(msg)
			   {
			   	 if(msg.idstr)
			   	 {
			   	 	alert("删除失败,该开发者已被应用管理引用!");
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
	function modifyStatus(apperId,status)
	{
		var row = gridManager.getSelectedRow();
		if(!row){
			alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = "${ctx}/developermanage/updateStatus.do?apperid="+apperId+"&status="+status;
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix">
	<a class="fl title">开发者管理</a>
	<a class="fr btn" href="${ctx}/developermanage/addapper.do">+添加开发者</a>
	</h2>
	
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>