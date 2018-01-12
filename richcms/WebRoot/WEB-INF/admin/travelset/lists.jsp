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
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
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
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : 'MM之旅递增ID',
				name : 'travelid',
				width : 100,
				align : 'center'
			},{
				display : 'MM之旅中文标识',
				name : 'travelname',
				width : 120,
				align : 'center'
			}, {
				display : '标签类型',
				name : 'tagtype',
				width : 100,
				align : 'center',
				render:function(item){
					var text = item.tagtype;
					if(text=='os'){
						text = '手机系统';
					}
					else if(text=='tag'){
						text = '身份标签';
					}
					else if(text=='like'){
						text = '个人爱好';
					}
					return text;
				}
			}, {
				display : '对应的栏目中文标识',
				name : 'catname',
				width : 200,
				align : 'center'
			},{
				display : '对应的栏目英文标识',
				name : 'cat',
				width : 200,
				align : 'center'
			}, {
				display : 'MM之旅英文标识',
				name : 'travel',
				width : 200,
				align : 'center'
			}, {
				display : '创建时间',
				name : 'ctimeStr',
				width : 150,
				align : 'center'
			}],
			width : '100%',
			pageSize: 5,
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'sid',
            width: '100%', height: '100%', pageSize: 10,rownumbers:true,
            checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
            
			data : { Rows : ${travelsetlist} },
            
			toolbar : {
				items : [ {
					text : '增加',
					click : addTravelset,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '修改',
					click : modifyTravelset,
					icon : 'modify'
				}, {
					line : true
				}, {
					text : '删除',
					click : deleteTravelset,
					icon : 'delete'
				} ]
			}
	 
		});
		
	});
	function deleteTravelset() 
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
	   		ids.push(rows[i].travelid);	
	   	}
	   	var idstr = ids.join(",");
	    $.ajax({
		   type: "POST",
		   url: "${ctx}/TravelSet/deletetravelset.do?csrfToken=${csrfToken}",
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
	
	function addTravelset()
	{
		window.location.href = "${ctx}/TravelSet/addtravelset.do";
	}
	
	function modifyTravelset()
	{
		var row = gridManager.getSelectedRow();
		if(!row){
			alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = "${ctx}/TravelSet/edittravelset.do?travelid="+row.travelid;
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title clearfix"><a class="fl title">MM之旅列表</a><a class="fr btn" href="${ctx}/TravelSet/addtravelset.do">+添加</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>