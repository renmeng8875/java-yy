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
				display : '排序',
				name : 'priority',
				width : 50,
				align : 'center',
				render:function(item)
				{
					return '<input type="text" style="color:#FF00FF;" value="'+item.priority+'" data-action="'+item.announceid+'"/>';
				}
			}, {
				display : 'ID',
				name : 'announceid',
				width : 50,
				align : 'center'
			}, {
				display : '标题',
				name : 'announce',
				width : 180,
				align : 'center',
				render:function(item)
				{
					return "<a href='${ctx}/Announce/viewAnnounce.do?announceid="+item.announceid+"'>"+item.announce+"</a>";
				}
			}, {
				display : '开始时间',
				name : 'startTimeStr',
				width : 180,
				align : 'center'
			}, {
				display : '结束时间',
				name : 'endTimeStr',
				width : 180,
				align : 'center'
			}, {
				display : '发布者',
				name : 'publisher',
				width : 120,
				align : 'center'
			}, {
				display : '浏览次数',
				name : 'viewtimes',
				width : 120,
				align : 'center'
			}, {
				display : '发布时间',
				name : 'publishTimeStr',
				width : 180,
				align : 'center'
			}],
			width : '100%',
			pageSize: 5,
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'sid',
            width: '100%', height: '97%', pageSize: 10,rownumbers:true,
            checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
            
			data : { Rows : ${announcelist} },
            
			toolbar : {
				items : [ {
					text : '增加',
					click : addAnnounce,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '修改',
					click : modifyAnnounce,
					icon : 'modify'
				}, {
					line : true
				}, {
					text : '删除',
					click : deleteAnnounce,
					icon : 'delete'
				},{
					line : true
				},{
					text : '批量排序',
					click : batchSortAnnounce,
					icon : 'ok'
				}
				]
			}
	 
		});
	});
	function addAnnounce() 
	{
		window.location.href = "${ctx}/Announce/addAnnounce.do";
	}
	function deleteAnnounce()
	{
		var rows = gridManager.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一条公告数据！");
			return false;
		}
	    var ids = [];
	   	for(var i=0;i<rows.length;i++)
	   	{
	   		ids.push(rows[i].announceid);	
	   	}
	   	var idstr = ids.join(",");
	    $.ajax({
		   type: "POST",
		   url: "${ctx}/Announce/deleteAnnounce.do?csrfToken=${csrfToken}",
		   data: "idstr="+idstr,
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 alert("删除成功！"); 
		    	 gridManager.deleteSelectedRow();
		     }
			  
		   }
		});
	   	
	}
	function modifyAnnounce()
	{
		var row = gridManager.getSelectedRow();
		if(!row){
			alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = "${ctx}/Announce/editAnnounce.do?announceid="+row.announceid;
	}
	function batchSortAnnounce()
	{
		var rows = gridManager.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一条公告数据！");
			return false;
		}
		var sortStr = [];
		for(var i=0;i<rows.length;i++)
		{
			var id = rows[i].announceid;
			var priority = $("input[data-action='"+id+"']").val();
			sortStr.push(id+"-"+priority);
		}
		$.ajax({
		   type: "POST",
		   url: "${ctx}/Announce/batchsort.do?csrfToken=${csrfToken}",
		   data: "sortStr="+sortStr.join(","),
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 alert("批量排序成功！"); 
		     }
			  
		   }
		});
		
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix"><a class="fl title">公告</a><a class="fr btn" href="${ctx}/Announce/addAnnounce.do">+添加公告</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>