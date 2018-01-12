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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"	type="text/javascript"></script>

<style type="text/css">
a:link{color:green;font-size:14px;text-decoration: underline;}
a:visited{color:red;font-size:14px;text-decoration: underline;} 
</style>
<script type="text/javascript">
	var gridManager ,allocateWindow,addWindow,modifyWindow;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ 
				{
					display : '开发者',
					name : 'account.apper',
					width : 120,
					align : 'center'
				}, {
					display : '应用名称',
					name : 'appName',
					width : 100,
					align : 'center'
					
				},{
					display : 'appkey',
					name : 'appKey',
					width : 250,
					align : 'center'
				},{
					display : '角色',
					name : 'role.roleName',
					width : 100,
					align : 'center'
				},{
					display : '最大调用次数',
					name : 'times',
					width : 120,
					align : 'center'
				},{
					display : '应用状态',
					name : 'appStatus',
					width : 80,
					align : 'center',
					render:function(item)
					{
						if(item.appStatus=='0')
						{
							return "<font color='grey'>未通过</font>";
						}
						return "<font color='green'>已通过</font>";
					}
				},
				{
					display : '上次修改时间',
					name : 'utimeStr',
					width : 150,
					align : 'center'
				},
				{
					display:'操作',
					name:'opration',
					width : 180,
					align : 'center',
					render:function(item){
						var rowid = item['__id'];
				   		var showflag = item.appStatus=='1'?"disabled='disabled'":" ";
				   		var hideflag = item.appStatus=='0'?"disabled='disabled'":" "
				   		var renderStr = "<input "+showflag+"id='show_"+rowid+"' type='button' value='通过' style='width:40px;color:green;' onclick='showOrHide(\""+rowid+"\",\"1\");' />&nbsp;&nbsp;<input "+hideflag+" type='button' id='hide_"+rowid+"' value='拒绝' style='width:40px;color:red;' onclick='showOrHide(\""+rowid+"\",\"0\");' />&nbsp;&nbsp;";
				   		if(item.appStatus=='1')
						{
				   			renderStr += "<input type='button' value='分配角色' onclick='allcationRole(\""+item.appId+"\");' style='width:60px;color:green;'/>";
						}
				   		return renderStr;	
					}
				}
			],
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'appId',
            width: '100%', 
            height: '97%', 
            rowHeight: 32,                       
            pageSize: 10,
            rownumbers:true,
            checkbox : true,
            heightDiff: -6,
            fixedCellHeight:false,
			data : { Rows :${appList}},
			toolbar : {
				items : [ {
					text : '增加',
					click : addApplication,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '修改',
					click : modifyApplication,
					icon : 'modify'
				}, {
					line : true
				}, {
					text : '删除',
					click : deleteApplication,
					icon : 'delete'
				}
				]
			}
	 
		});
	});
	
	function addApplication()
	{
		addWindow = $.ligerDialog.open({
			 height: 300, 
			 title:'添加应用',
			 url: '${ctx}/application/addApp.do',
			 width: 520, 
			 showMax: true, 
			 showToggle: true, 
			 showMin: true, 
			 allowClose: true,
			 isResize: true, 
			 slide: true
		});
		$(".l-dialog-content").css("overflow","hidden");
	}
	
	function modifyApplication()
	{
		var row = gridManager.getSelectedRow();
		if(!row){
			alert("请先选择一条要修改的数据！");
			return false;
		}
		modifyWindow = $.ligerDialog.open({
			 height: 300, 
			 title:'修改应用',
			 url: '${ctx}/application/editApp.do?appId='+row.appId,
			 width: 520, 
			 showMax: true, 
			 showToggle: true, 
			 showMin: true, 
			 allowClose: true,
			 isResize: true, 
			 slide: true
		});
		$(".l-dialog-content").css("overflow","hidden");
	}
	
	function deleteApplication()
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
	   		ids.push(rows[i].appId);	
	   	}
	   	var idstr = ids.join(",");
	    $.ajax({
		   type: "POST",
		   url: "${ctx}/application/deleteApplication.do?csrfToken=${csrfToken}",
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
	
	function showOrHide(rowid,status){
		var rowdata = gridManager.getRow(rowid);
		var showButton = $("#show_"+rowid),hideButton = $("#hide_"+rowid);
		$.ajax({
		   type: "POST",
		   url: "${ctx}/application/changeStatus.do?appStatus="+status,
		   data: "appId="+rowdata.appId+"&csrfToken=${csrfToken}",
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 var value="";
		    	 
		    	 if(status=='0'){
		    		alert("拒绝"+rowdata.appName+" 成功！");
		    		hideButton.attr("disabled",true);
		    		showButton.attr("disabled",false);
		    	 }else{
		    		alert(rowdata.appName+"审核通过！");
		    		hideButton.attr("disabled",false);
		    		showButton.attr("disabled",true);
		    	 }
		    	 gridManager.updateCell('appStatus',status,rowdata);
		    	 gridManager.updateCell('opration',status,rowdata);
		     }
			  
		   }
		});
	}
	
	function allcationRole(appId)
	{
		allocateWindow = $.ligerDialog.open({
			 height: 300, 
			 title:'角色分配',
			 url: '${ctx}/application/getAllocation.do?appId='+appId, 
			 width: 500, 
			 showMax: true, 
			 showToggle: true, 
			 showMin: true, 
			 allowClose: true,
			 isResize: true, 
			 slide: true
		});
		$(".l-dialog-content").css("overflow","hidden");
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix">
	<a class="fl title">功能列表管理</a>
	</h2>
	
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>