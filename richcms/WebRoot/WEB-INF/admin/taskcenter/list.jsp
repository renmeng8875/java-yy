<%@include file="../public/header.jsp"%>
<%@page import="com.richinfo.common.TokenMananger"%>
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
<link href="${ctx}/static/admin/css/jquery.loadmask.css" rel="stylesheet"	type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/jquery.loadmask.js"	type="text/javascript"></script>

<style type="text/css">
a:link{color:green;font-size:14px;text-decoration: underline;}
a:visited{color:red;font-size:14px;text-decoration: underline;} 
</style>
<script type="text/javascript">
	var token = "<%=TokenMananger.getTokenFromSession(session)%>";
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : '应用ID',
				name : 'appId',
				width : 80,
				align : 'center'
				
			}, {
				display : '应用标题',
				name : 'title',
				width : 300,
				align : 'center',
				render:function(rowdata){
					if(rowdata.title==''){
						return '<span style="color:red;text-decoration: line-through;">库中暂无此应用</span>';
					}
					return rowdata.title;
				}
			},{
				display : '大小',
				name : 'appSize',
				width : 70,
				align : 'center'
			},
			{
				display : '版本',
				name : 'version',
				width : 50,
				align : 'center'
				
			}, {
				display : '价格',
				name : 'price',
				width : 50,
				align : 'center'
			}, {
				display : '更新时间',
				name : 'utimeStr',
				width : 120,
				align : 'center'
			},{
				display:'预览图',
				name :'logo',
				width:180,
				align:'center',
				render:function(rowdata){
					if(rowdata.title!=''){
						return '<span><img height="80px" src="'+rowdata.logo+'"/></span>';
					}
					return '<span><img height="80px" src="${ctx}/static/admin/images/cry.png"></span>';
				}
			},{
				display:'操作',
				name:'operation',
				width:180,
				align:'center',
				render:function(rowdata){
					var rowid = rowdata['__id'];
					var text = "";
					
					if(rowdata.title!=''){
						text = "<a href=\"#\"  onclick='fetch(\""+rowid+"\")'>更新</a>&nbsp;&nbsp;";
					}else{
						text = "<a href=\"#\"  onclick='fetch(\""+rowid+"\")'>抓取</a>&nbsp;&nbsp;<a href=\"#\"  onclick='cancleFetch(\""+rowid+"\")'>取消抓取</a>&nbsp;&nbsp;";
					}
					
					return text;
				}
			}
			
			],
			width : '100%',
			pageSize: 5,
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'sid',
            height: '97%', 
            rownumbers:true,
            checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
            
			data : { Rows : ${iosList} },
            
			toolbar : {
				items : [ {
						text : '批量抓取',
						click : batchFetch,
						icon : 'ok'
					},{
					line : true
					},
					{
						text:'刷新',
						click:refresh,
						icon:'refresh'
					}
				]
			}
	 
		});
	});
	
	function refresh()
	{
		window.location.reload() ;

	}
	
	function batchFetch(){
		$("body").mask("任务中心爬虫已启动..");
		var rows = gridManager.getSelectedRows();
		if(rows.length==0)
		{
			$.ligerDialog.alert("请至少选中一条数据！");
			return false;
		}
		var appids = [];
		for(var i=0;i<rows.length;i++)
		{
			appids.push(rows[i].appId);	
		}
		$.ajax({
		   type: "POST",
		   url: "${ctx}/taskcenter/fetch.do?csrfToken="+token,
		   data:"appids="+appids.join(","),                                
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 $("body").unmask(); 
		    	 $.ligerDialog.success("批量抓取任务已启动,稍后刷新查看！"); 
		     }
		   }
   		});
	}
	
	
	function fetch(rowid)
	{
        $("body").mask("任务中心爬虫已启动..");
		var rowdata = gridManager.getRow(rowid);
		$.ajax({
		   type: "POST",
		   url: "${ctx}/taskcenter/fetch.do?csrfToken="+token,
		   data:"appids="+rowdata.appId,                                
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 $("body").unmask(); 
		    	 $.ligerDialog.success("任务启动成功,稍后刷新查看！"); 
		     }
		   }
   		});
	}
	
	
	function cancleFetch(rowid)
	{
		var rowdata = gridManager.getRow(rowid);
		$.ajax({
		   type: "POST",
		   url: "${ctx}/taskcenter/cancle.do?csrfToken="+token,
		   data: "appid="+rowdata.appId,
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
	
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix"><a class="fl title">抓取应用列表</a><a class="fr btn" href="javascript:history.go(-1);">返回</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none; "></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>