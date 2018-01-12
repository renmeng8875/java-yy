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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
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
					return '<input type="text"  optype="sortinput"  style="color:#FF00FF;" value="'+item.priority+'" data-action="'+item.pkid+'"/>';
				}
			}, {
				display : 'ID',
				name : 'pkid',
				width : 60,
				align : 'center',
				render:function(item){
					return "<font color='#7FFF00'>"+item.pkid+"</font>"
				}
			}, {
				display : '玩家争霸名称',
				name : 'pkName',
				width : 180,
				align : 'center',
				render:function(item){
					return "<font color='#8A2BE2'>"+item.pkName+"</font>"
				}
			},{
				display : '英文名称',
				name : 'pk',
				width : 150,
				align : 'center',
				render:function(item){
					return "<font color='#008B8B'>"+item.pk+"</font>"
				}
			}, 
			{
				display : '活动时间',
				name : 'activeTime',
				width : 240,
				align : 'center',
				render:function(item){
				    if(item.vaild==false){
				    	return '<span title="已过期" style="TEXT-DECORATION: line-through;color:red">'+item.startTimeStr+"~"+item.endTimeStr+'</span>';
				    }
					return '<span title="活动进行中" style="color:blue">'+item.startTimeStr+"~"+item.endTimeStr+'</span>';
				}
			},{
				display : '状态',
				name : 'status',
				width : 100,
				align : 'center',
				render:function(item){
					if(item.status=='0'){
						return "<font color='red'>隐藏</font>";
					}else{
						return "<font color='green'>显示</font>";
					}
				}
			   },
				{
					display : '操作',
					name : 'operation',
					width : 260,
					align : 'center',
					render:function(item){
				   			
				   		var rowid = item['__id'];
				   		var showflag = item.status=='1'?"disabled='disabled'":" ";
				   		var hideflag = item.status=='0'?"disabled='disabled'":" "
				   		
						return "<input "+showflag+"id='show_"+rowid+"' type='button' value='显示' style='width:50px;color:green;' onclick='showOrHide(\""+rowid+"\",\"1\");' />&nbsp;&nbsp;<input "+hideflag+" type='button' id='hide_"+rowid+"' value='隐藏' style='width:50px;color:red;' onclick='showOrHide(\""+rowid+"\",\"0\");' />&nbsp;&nbsp;<input type='button' value='删除' onclick='deletePk(\""+rowid+"\");' style='width:50px;'/>";
					}
				}
			],
			rowHeight: 28,
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'sid',
            width: '100%', 
            height: '97%', 
            pageSize: 10,
            rownumbers:true,
            checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            fixedCellHeight:false,
            
			data : { Rows : ${warcraftList} },
            
			toolbar : {
				items : [ {
					text : '增加',
					click : add,
					icon : 'add'
				}, {
					line : true
				}, {
					text : '修改',
					click : modify,
					icon : 'modify'
				}, {
					line : true
				}, {
					text : '批量删除',
					click : deleteBatchPk,
					icon : 'delete'
				},{
					line : true
				},{
					text : '批量排序',
					click : batchSort,
					icon : 'ok'
				},
				{
					line : true
				},{
					text : '玩家',
					click : playersListEdit,
					icon : 'customers'
				},{
					line : true
				},{
					text : '内容编辑',
					click : contentEdit,
					icon : 'archives'
				},
				{
					line : true
				},
				{
					text : '奖品管理',
					click : prizes,
					icon : 'attibutes'
				},
				{
					line : true
				},
				{
					text : '中奖名单',
					click : prizeEdit,
					icon : 'attibutes'
				},
				{
					line:true
				},
				{
					text:'公用图片',
					click:uploadImage,
					icon : 'config'
				}
				]
			}
	 
		});
		
		$('input[optype="sortinput"]').blur(function(){
			if(!$.isNumeric(this.value))
			{
				alert("请输入1到9999范围内的数字！")
				this.value="";
				this.focus();
				return false;
				
			}
			if(this.value.indexOf(".")>-1)
			{
				alert("请输入整数！");
				this.value="";
				this.focus();
				return false;
				
			}
			if(this.value>9999||this.value<1)
			{
				alert("请输入1到9999范围内的数字！");
				this.value="";
				this.focus();
				return false;
			}
		});
	});
	function add() 
	{
		window.location.href = "${ctx}/pk/add.do";
	}
	
	function prizes() 
	{
		var rows = gridManager.getSelectedRows();
		if(rows.length==0||rows.length>1)
		{
			$.ligerDialog.alert("请选中一条数据！");
			return false;
		}else{
		   var pkid = rows[0].pkid;
		   window.location.href = "${ctx}/gamePrizes/lists.do?pkid="+pkid;
		}
	}
	function deletePk(rowid){
		$.ligerDialog.confirm('是否确定删除?',function(flag){
		   		if(flag){
				    var rowdata = gridManager.getRow(rowid);
					$.ajax({
					   type: "POST",
					   url: "${ctx}/pk/delete.do?csrfToken=${csrfToken}",
					   data: "idstr="+rowdata.pkid,
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
	
	function showOrHide(rowid,status){
		var msg=(status=='0'?'是否确定隐藏?':'是否确定显示? ');
		$.ligerDialog.confirm(msg,function(flag){
			if(flag){
				var rowdata = gridManager.getRow(rowid);
				var showButton = $("#show_"+rowid),hideButton = $("#hide_"+rowid);
				$.ajax({
				   type: "POST",
				   url: "${ctx}/pk/changeStatus.do?status="+status,
				   data: "pkid="+rowdata.pkid+"&csrfToken=${csrfToken}",
				   success: function(msg)
				   {
				     if(msg.status=='0')
				     {
				    	 var value="";
				    	 
				    	 if(status=='0'){
				    		$.ligerDialog.success("隐藏 "+rowdata.pkName+" 成功！");
				    		hideButton.attr("disabled",true);
				    		showButton.attr("disabled",false);
				    	 }else{
				    		$.ligerDialog.success("显示 "+rowdata.pkName+" 成功！");
				    		hideButton.attr("disabled",false);
				    		showButton.attr("disabled",true);
				    	 }
				    	 gridManager.updateCell('status',status,rowdata);
				     }
					  
				   }
				});
			}
		});
	}
	function deleteBatchPk()
	{
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
			   		ids.push(rows[i].pkid);	
			   	}
			   	var idstr = ids.join(",");
			    $.ajax({
				   type: "POST",
				   url: "${ctx}/pk/delete.do?csrfToken=${csrfToken}",
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
	function modify()
	{
		var row = gridManager.getSelectedRow();
		if(!row){
			$.ligerDialog.alert("请先选择一条要修改的数据！");
			return false;
		}
		window.location.href = "${ctx}/pk/edit.do?pkid="+row.pkid;
	}
	
	function contentEdit()
	{
		var rows = gridManager.getSelectedRows();
		
		if(rows.length==0){
			$.ligerDialog.alert("请先选择一条要修改的数据！");
			return false;
		}
		if(rows.length>1){
			$.ligerDialog.alert("请不要选择多条数据！");
			return false;
		}
		window.location.href = "${ctx}/pk/contentEdit.do?pkid="+rows[0].pkid;
	}
	
	function playersListEdit()
	{
		var rows = gridManager.getSelectedRows();
		
		if(rows.length==0){
			$.ligerDialog.alert("请先选择一条要修改的数据！");
			return false;
		}
		if(rows.length>1){
			$.ligerDialog.alert("请不要选择多条数据！");
			return false;
		}
		window.location.href = "${ctx}/gamePlayers/lists.do?pkid="+rows[0].pkid;
	}
	
	function prizeEdit()
	{
		var rows = gridManager.getSelectedRows();
		
		if(rows.length==0){
			$.ligerDialog.alert("请先选择一条要修改的数据！");
			return false;
		}
		if(rows.length>1){
			$.ligerDialog.alert("请不要选择多条数据！");
			return false;
		}
		window.location.href = "${ctx}/pk/prizeEdit.do?pkid="+rows[0].pkid;
	}
	
	function batchSort()
	{
		var rows = gridManager.getSelectedRows();
		if(rows.length==0)
		{
			$.ligerDialog.alert("请至少选中一条数据！");
			return false;
		}
		var sortStr = []; 
		for(var i=0;i<rows.length;i++)
		{
			var id = rows[i].pkid;
			var priority = $("input[data-action='"+id+"']").val();
			sortStr.push(id+"-"+priority);
		}
		$.ajax({
		   type: "POST",
		   url: "${ctx}/pk/batchSort.do?csrfToken=${csrfToken}",
		   data: "sortStr="+sortStr.join(","),
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 $.ligerDialog.success("批量排序成功！"); 
		    	 location.reload();
		     }
			  
		   }
		});
		
	}
	
	function uploadImage()
	{
		window.location.href = "${ctx}/pk/updateImage.do";	
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">

	<h2 class="adm-title clearfix"><a class="fl title">玩家争霸</a><a class="fr btn" href="${ctx}/pk/add.do">+添加玩家争霸</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>