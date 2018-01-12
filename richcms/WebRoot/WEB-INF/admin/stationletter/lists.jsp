<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>站内信</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/Validform.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"	type="text/javascript"></script>
<style type="text/css">
a:link{display: inline; color:dark;font-size:14px;text-decoration: none;}
a:visited{display:inline;color:gray;font-size:14px;text-decoration: none;} 
</style>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : '编号',
				name : 'letterid',
				width : 100,
				align : 'center'
			}, {
				display : '发送人',
				name : 'userNickName',
				width : 120,
				align : 'center'
			}, {
				display : '标题',
				name : 'letter',
				width : 180,
				align : 'center',
				render:function(item)
				{
					var topStr = item.top == 0 ? "" : "<p style=\"color:#f00;display:inline;\">【置顶】</p>";
					return "<a href='${ctx}/StationLetter/viewLetter.do?letterid="+item.letterid+"'>" + topStr +item.letter+"</a>";
				}
			}, {
				display : '内容预览',
				name : 'content',
				width : 300,
				align : 'center',
				render : function(item)
				{
					var tmpContent = item.content;
					if (tmpContent.length > 50) {
						tmpContent = tmpContent.substring(0, 45) + ">>";
					}
					return "<a href='${ctx}/StationLetter/viewLetter.do?letterid="+item.letterid+"'>" + tmpContent + "</a>";
				}
			}, {
				display : '收件人',
				name : 'reciversStr',
				width : 120,
				align : 'center'
			}, {
				display : '公告类型',
				name : 'announceTypeStr',
				width : 120,
				align : 'center'
			}, {
				display : '发送时间',
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
            
			data : { Rows : ${letterList} },
            
			toolbar : {
				items : [ {
					text : '增加',
					click : addLetter,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '修改',
					click : modifyLetter,
					icon : 'modify'
				}, {
					line : true
				}, {
					text : '删除',
					click : deleteLetter,
					icon : 'delete'
				}, {
					line : true
				}, {
					text : '批量导出',
					click : batchExport,
					icon : 'down'
				}, {
					line : true
				}, {
					text : '全站导出',
					click : exportAll,
					icon : 'down'
				}, {
					line : true
				}
				]
			} 
	 
		});
		$("#fm_es").Validform({
			tiptype:3,
			label:".label",
			showAllError:false,
			beforeSubmit:function(){
				var reg1=/[_~$\^&\\\/\|\.<>'"]/;
				var searchkeys = $("#paramsstr").attr("value");
 				if (searchkeys==null || searchkeys.length>30){
					$("#errLable").html("您输入的搜索条件过长，请删减后再搜索");
					return false;
				} 
				if(reg1.test(searchkeys)){
					$("#errLable").html("不能包含特殊字符");
					return false;
				}
				return true;
			}
		});
	});
 	function addLetter() 
	{
		window.location.href = "${ctx}/StationLetter/addLetter.do";
	}
	function deleteLetter()
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
	   		ids.push(rows[i].letterid);	
	   	}
	   	var idstr = ids.join(",");
	    $.ajax({
		   type: "POST",
		   url: "${ctx}/StationLetter/deleteLetter.do?csrfToken=${csrfToken}",
		   data: "idstr="+idstr,
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 alert("删除成功！"); 
		    	 window.location.href = "${ctx}/StationLetter/lists.do?csrfToken=${csrfToken}";
		     }
			  
		   }
		});
	    
	}
	function modifyLetter()
	{
		var row = gridManager.getSelectedRow();
		if(!row){
			alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = "${ctx}/StationLetter/editLetter.do?letterid="+row.letterid;
	} 

	function batchExport() 
	{
		var rows = gridManager.getSelectedRows();
		if(rows.length == 0) {
			alert("请至少选择一条数据！");
			return false;
		}
		var ids = [];
		for(var i = 0; i < rows.length; i++){
			ids.push(rows[i].letterid);
		}
		var idstr = ids.join(",");
		window.location.href = "${ctx}/StationLetter/exportExcel.do?csrfToken=${csrfToken}&idstr=" + idstr;
	}
	function exportAll()
	{
		if (confirm("确认要导出所有数据吗？"))
		{
			window.location.href = "${ctx}/StationLetter/exportExcel.do?csrfToken=${csrfToken}";
		}
	}
	
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	
	<h2 class="adm-title clearfix"><a class="fl title">站内信</a><a class="fr btn" href="${ctx}/StationLetter/addLetter.do">+添加站内信</a></h2>
	
	<div class="adm-page" >
		<form id="fm_es" action="${ctx}/StationLetter/search.do" method="post">
			<input type="hidden" name="csrfToken" value="${csrfToken}" />
			<input style="width:400px;" type="text" id="paramsstr" name="paramsstr"/>
			<input class="btn" style="width: 40px" type="submit" value="搜索" /><span style="color:#f00;" id="errLable"></span>
			<p>请输入查询关键字，多个关键字请用半角逗号“,”隔开</p>
		</form>	
	</div>
	
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>