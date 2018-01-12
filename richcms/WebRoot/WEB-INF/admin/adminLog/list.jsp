<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<style type="text/css">
a:link{color:green;font-size:14px;text-decoration: underline;}
a:visited{color:red;font-size:14px;text-decoration: underline;} 
</style>
<script type="text/javascript">
	var gridManager = null;
	var listUrl='${ctx}/adminLog/listsJson.do?csrfToken=${csrfToken}';
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [  {
				display : '操作人',
				name : 'userName',
				width : 180,
				align : 'center'
			}, {
				display : '操作时间',
				name : 'ctime',
				width : 180,
				align : 'center'
			}, {
				display : '登录IP',
				name : 'loginIP',
				width : 120,
				align : 'center'
			},{
				display:'方法描述',
				name:'mem',
				width:400,
				align:'center'
			}],
			pageSizeOptions : [ 5, 10, 15, 20 ],
            height: '97%', 
            pageSize: 20,
            rowHeight: 25,
            rownumbers:true,
            checkbox : true,
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
            url:listUrl,
            usePager:true,
			toolbar : {
			items : [ {
				text : '批量删除',
				click : batchDelete,
				icon : 'delete'
			},{
				line : true
			}
			]
		}
		});
	});

function batchDelete(){
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
			   		ids.push(rows[i].logId);	
			   	}
			   	var idstr = ids.join(",");
			    $.ajax({
				   type: "POST",
				   url: "${ctx}/adminLog/batchDelete.do?csrfToken=${csrfToken}",
				   data: "idstr="+idstr,
				   success: function(msg)
				   {
					    if(msg.status=='0')
					    {
					    	 $.ligerDialog.success("删除成功！");
					    	 gridManager.deleteSelectedRow();
					    }
				   }
				});
	   	    }
		});
}

</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix"><a class="fl title">管理员操作日志</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>