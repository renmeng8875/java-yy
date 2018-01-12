<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>视频列表</title>
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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : '视频ID',
				name : 'videoId',
				width : 60,
				align : 'center'
			},{
				display:'排序值',
				name : 'sortNum',
				width :60,
				align:'center'
			},
			{
				display : '视频名称',
				name : 'vname',
				width : 120,
				align : 'center',
				render:function(item)
				{
					var text = '<span title="如果未定义视频名称,则显示视频文件上传前的原名称！">'+item.vname+'</span>';
					return text;
				}
			}, 
			{
				display : '视频地址',
				name : 'videoUrl',
				width : 400,
				align : 'center'
			},{
				display : '视频上传者',
				name : 'uploader',
				width : 150,
				align : 'center'
			}, 
			{
				display : '最后操作人',
				name : 'lastModifier',
				width : 140,
				align : 'center'
			}, 
				{
					display : '操作',
					name : 'operation',
					width : 180,
					align : 'center',
					render:function(item)
					{
				   		var rowid = item['__id'];
						return "<input type='button' value='修改' onclick='modify(\""+rowid+"\");' style='width:50px;'/>&nbsp;&nbsp;&nbsp;<input type='button' value='删除' onclick='deleteVideo(\""+rowid+"\");' style='width:50px;'/>";
					}
				}
			],
			rowHeight: 28,
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'videoId',
            width: '100%', 
            height: '97%', 
            pageSize: 15,
            rownumbers:true,
            checkbox : true,
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            fixedCellHeight:false,
			data : { Rows : ${videoList} },
			toolbar : {
				items : [ {
					text : '上传视频',
					click : add,
					icon : 'add'
				}, {
					line : true
				},{
					text : '批量上传',
					click : addBatch,
					icon : 'add'
				},{line:true}, 
				{
					text : '批量删除',
					click : deleteBatch,
					icon : 'delete'
				}
				]
			}
	 
		});
	});
		
		
		

	function add() 
	{
		window.location.href = "${ctx}/video/add.do?batch=0";
	}
	
	function addBatch() 
	{
		window.location.href = "${ctx}/video/add.do?batch=1";
	}
	
	function deleteBatch(){
		var rows = gridManager.getSelectedRows();
		if(rows.length==0)
		{
			$.ligerDialog.alert("请至少选中一条数据！");
			return false;
		}
		$.ligerDialog.confirm('是否确定删除?',function(flag){
			if(flag){
			    var ids = [];
			   	for(var i=0;i<rows.length;i++)
			   	{
			   		ids.push(rows[i].videoId);	
			   	}
			   	var idstr = ids.join(",");
			    $.ajax({
				   type: "POST",
				   url: "${ctx}/video/delete.do?csrfToken=${csrfToken}",
				   data: "idstr="+idstr,
				   success: function(msg)
				   {
					    if(msg.status=='0')
					    {
					    	 gridManager.deleteSelectedRow();
					    }
				   }
				});
	   	    }
		});
	}
	
	function deleteVideo(rowid){
		$.ligerDialog.confirm('是否确定删除?',function(flag){
	   		if(flag){
			    var rowdata = gridManager.getRow(rowid);
				$.ajax({
				   type: "POST",
				   url: "${ctx}/video/delete.do?csrfToken=${csrfToken}",
				   data: "idstr="+rowdata.videoId,
				   success: function(msg)
				   {
				     if(msg.status=='0')
				     {
				    	 $.ligerDialog.success("删除成功！"); 
				    	 gridManager.deleteRow(rowid);
				     }
				   }
				});
		    }
		});
	}
	
	function modify(rowid)
	{
		var rowdata = gridManager.getRow(rowid);
		window.location.href = "${ctx}/video/modify.do?batch=0&videoId="+rowdata.videoId;
	}
	 
	
	
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix"><a class="fl title">视频列表</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>