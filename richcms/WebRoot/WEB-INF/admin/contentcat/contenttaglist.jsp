<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内容标签列表</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js" type="text/javascript"></script>
<style>
.treeopra{
    color: #219B00;
    display: inline-block;
    height: 20px;
    line-height: 20px;
    margin: 0 10px;
    text-decoration: none;
}
</style>
<script type="text/javascript">
//------------------------------------------function
var listGrid;

function batchDel()
	{
		var rows = listGrid.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一个内容标签！");
			return false;
		}
		if(confirm("你确定删除该内容标签吗?"))
		{
			var ids = [];
		   	for(var i=0;i<rows.length;i++)
		   	{
		   		ids.push(rows[i].catId);	
		   	}
		   	var idstr = ids.join(",");
		   	
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/ContentCat/batchDelete.do?csrfToken=${csrfToken}",
			   data: "idstr="+idstr,
			   success: function(msg)
			   {
			     if(msg.status=='0')
			     {
			    	 alert("批量删除成功！");
			    	 listGrid.deleteSelectedRow();
			     }else{
			     	 alert("批量删除失败！"); 
			     }
			   }
			});
		}
	}
	
	function del(catId) 
	{
		if(confirm("你确定删除该内容标签吗?"))
		{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/ContentCat/delTag.do?csrfToken=${csrfToken}",
			   data: "catId="+catId,
			   success: function(msg)
			   {
			  	
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 listGrid.deleteSelectedRow();
			     }
			     if(msg.status=='1'){
			     	alert("删除失败！");
			     	return false;
			     }
			   }
			});
		}
	}
//------------------------------------------ligerUi

$(function() {
	listGrid = $("#maingrid").ligerGrid( {
		columns : [{
			display : '标签ID',
			name : 'catId',
			id : 'id1',
			width : 100,
			type : 'int',
			align : 'center'
		},
		{
			display : '数据源',
			name : 'dataType',
			width : 100,
			align : 'center'
		},
		{
			display : '排序',
			name : 'priority',
			width : 50,
			align : 'center'
		},
		{
			display : '标签名称',
			name : 'appType',
			width : 150,
			align : 'center'
		},
		{
			display : '栏目名称',
			name : 'pcatName',
			width : 350,
			align : 'center'
		},
		{
             display: '操作', 
             isAllowHide: false,
             width : 200,
             render: function (row)
             {
                 var html = '<a class="treeopra" href="${ctx}/ContentCat/addsysdatacat.do?catId='+row.catId+'">修改</a>'+
                            '<a class="treeopra" style="cursor: pointer;" onclick="javascript:del('+row.catId+');">删除</a>';
                 return html;
             }
         }
		],
		width : '100%',
		pageSize: 5,
		pageSizeOptions : [ 5, 10, 15, 20 ],
		//sortName: 'sid',
        width: '100%', 
        height: '100%', 
        pageSize: 20,
        rownumbers:true,
        checkbox : true,
        cssClass: 'l-grid-gray', 
        heightDiff: -6,
        width : '100%',
        fixedCellHeight:false,
		url:'${ctx}/ContentCat/listContentTag.do?csrfToken=${csrfToken}',  
		toolbar: {
			items: [{line: true},{text: '批量删除', click:batchDel, icon: 'modify' },{line: true}]
		}
	});
});
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
<h2 class="adm-title">
<a class="fl title">内容标签列表</a>
<a class="fr btn" href="${ctx}/ContentCat/addsysdatacat.do">+添加内容标签</a>
</h2>
<div class="l-clear"></div>
		<div id="maingrid"></div>
		<div style="display: none;"></div>
		<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>