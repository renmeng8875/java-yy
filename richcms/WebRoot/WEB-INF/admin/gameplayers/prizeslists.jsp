<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>风格管理</title>
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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
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
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ 
			{
				display : '奖品id',
				name : 'prizesid',
				width : 100,
				align : 'center'
			}, {
				display : '排序',
				name : 'priority',
				width : 100,
				align : 'center'
			},{
				display : '奖品名称',
				name : 'prizesname',
				width : 150,
				align : 'center'
			},{
				display : '活动名称',
				name : 'pkname',
				width : 150,
				align : 'center'
			},{
				display : '图片',
				name : 'prizesimg',
				width : 350,
				align : 'center'
			}, { display: '操作', name : 'operation', width : 100, align : 'center',
					render:function(item){
						return "<div id=u_"+item.prizesid+" ><input type='button' value='修改' onclick='modify("+item.prizesid+")'/>&nbsp;&nbsp;<input type='button' value='删除' onclick='deletePrizes("+item.__index+")'/></div>";
					}
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
			data : { Rows : ${prizesList} },
			toolbar : {
				items : [ {
					text : '增加奖品',
					click : add,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '删除选中',
					click : del,
					icon : 'delete'
				}
				]
			}
	 
		});
		 gridManager = $("#maingrid").ligerGetGridManager();
	});
  function add() {
		window.location.href = "${ctx}/gamePrizes/add.do?pkid=${pkid}";
  }
  
  function deletePrizes(rowid){
		$.ligerDialog.confirm('是否确定删除?',function(flag){
	   		if(flag){
			    var rowdata = gridManager.getRow(rowid);
				$.ajax({
				   type: "POST",
				   url: "${ctx}/gamePrizes/del.do?csrfToken=${csrfToken}",
				   data: "idstr="+rowdata.prizesid,
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
	
  function del(){
        var data = gridManager.getCheckedRows();
		if (data.length == 0)
			alert("请至少选中一条奖品数据！");
		else {
			var checkedIds = [];
			$(data).each(function() {
				checkedIds.push(this.prizesid);
			});
			$.ligerDialog.confirm('确定删除' + checkedIds.join(',') + '?',function(btn){
			   if(btn){
				$.ajax({
					   type: "post",
					   url: "${ctx}/gamePrizes/del.do?csrfToken=${csrfToken}",
					   data: "idstr="+checkedIds,
					   success: function(msg){
					     if(msg.status=='0') {
					    	 alert("删除成功！"); 
					    	 gridManager.deleteSelectedRow();
					     }
					   }
					})
				}	
			});
		}
 
	}
 
   function modify(rowIndex){
		if(!rowIndex){
			alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = "${ctx}/gamePrizes/edit.do?pkid=${pkid}&prizesid="+rowIndex;
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title clearfix"><a class="fl title">玩家争霸-奖品列表</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>