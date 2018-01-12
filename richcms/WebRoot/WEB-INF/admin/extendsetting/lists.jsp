<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js" type="text/javascript"></script>
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
var ctx = '${ctx}';
var grid;
var jsonData = ${treeData};
var treeMenuData = {Rows:[jsonData]};
$(function() {
     
	grid = $("#maingrid").ligerGrid( {
		columns : [
		{
			display : '菜单名',
			name : 'menu',
			width : 250,
			align : 'center'
		}, {
			display : '菜单ID',
			name : 'menuId',
			id : 'id1',
			width : 100,
			type : 'int',
			align : 'center'
		},{
			display : '模块名',
			name : 'control',
			width : 150,
			align : 'center'
		},{
			display : '方法名',
			name : 'action',
			width : 150,
			align : 'center'
		},
		{display : '排序值',
			name : 'orderby',
			width : 50,
			align : 'center'
		},{
			display:'菜单状态',
			name:'status',
			width:80,
			align:'center',
			render:function(item)
			{
				var html = "<span style='color:green'>显示</span>";
				if(item.isHidden=='1')
				{
					html = "<span style='color:red'>隐藏</span>";
				}	
				return html;
			}
			
		},
		{
             display: '操作', isAllowHide: false,
             width : 250,
             render: function (row)
             {
                 var html = '<a class="treeopra" href="${ctx}/Extendsetting/add.do?pid='+row.menuId+'">添加子菜单</a>'+
                            '<a class="treeopra" href="${ctx}/Extendsetting/add.do?menuid='+row.menuId+'">修改</a>'+
                            '<a class="treeopra" style="cursor: pointer;" onclick="javascript:del('+row.menuId+');">删除</a>';
                 return html;
             }
         }],
		width : '100%',
		usePager:false,
		height : '97%',
		allowHideColumn : false,
		colDraggable : true,
		rowDraggable : true,
		data : treeMenuData,
		alternatingRow : false,
		tree : {
			columnName : 'menu'
		}/*,
		toolbar : {
			items : [ {
					text : '菜单备份',
					click : backup,
					icon : 'ok'
				},{line:true},
				{
					text:'恢复默认配置',
					click:restore,
					icon:'ok'
				}
			]
		}*/
  
	});
	
	
	
	
	
});
   
  function backup()
  {
	  	$.ajax({
		   type: "post",
		   url: "${ctx}/handleData/exportData.do?csrfToken=${csrfToken}",
		   success: function(res){
		     if(res.status=='0') 
		     {
		    	 alert("菜单数据已在服务器端备份！");
		     }
		   }
		});
  }
  function restore()
  {
	  	$.ligerDialog.confirm('请谨慎操作，确认恢复默认配置？',function(btn){
	  		if(btn)
	  		{
	  			$.ajax({
				   type: "post",
				   url: "${ctx}/handleData/importData.do?csrfToken=${csrfToken}",
				   success: function(res){
				     if(res.status=='0') 
				     {
				    	 alert("初始数据已恢复！");
				     }
				   }
				});
	  		}	
	  	});
	  	
  }
  function del(menuid){
		if (menuid==null)
			alert("请至少选中一条数据！");
		else {
			$.ligerDialog.confirm('确定删除' + menuid + '?',function(btn){
			   if(btn){	
			   		$.ajax({
					   type: "post",
					   url: "${ctx}/Extendsetting/del.do?csrfToken=${csrfToken}",
					   data: "menuid="+menuid,
					   success: function(res){
					     if(res.status=='0') {
					    	 alert("删除成功！"); 
					    	 grid.deleteSelectedRow();
					     }else{
					         alert(res.msg);return false;
					     }
					   }
					});
				}	
			});
		}
 
	}
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
<h2 class="adm-title">
<a class="title" href="javascript:;" onclick="collapseAll()">菜单管理  </a>
<a class="fr btn" href="${ctx}/Extendsetting/add.do" >+添加菜单</a>
</h2>
<div class="l-clear"></div>

		<div id="maingrid"></div>

		<div style="display: none;">

		</div>
		
		<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>