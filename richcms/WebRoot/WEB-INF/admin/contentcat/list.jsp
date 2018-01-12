<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理栏目</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/easyui/easyui.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/easyui/easyui-icon.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
.panel-body {
  border-color: #dddddd;
}
.tree-expanded {
  background: url('${ctx}/static/admin/images/grid/grid-tree-open.gif') no-repeat 3px 5px;
}
.tree-collapsed {
  background: url('${ctx}/static/admin/images/grid/grid-tree-close.gif') no-repeat 3px 5px;
}
.tree-folder{
  background: url('');	
}
.tree-file{
  background: url('');	
}
</style>

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/easyui/jquery.easyui.min.js" type="text/javascript"></script>
<script type="text/javascript">
//------------------------------------------function
	
	function orderCategory(pid){
		window.open('${ctx}/ContentCat/sortcontentcat.do?pid='+pid,'newwindow','height=500,width=500,top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
	}
	
	function batchUpdate(state)
	{
		var rows = $("#myTreeGrid").treegrid('getSelections');
		if(rows.length==0)
		{
			alert("请至少选中一个栏目！");
			return false;
		}
		
		var ids = [];
		
	   	for(var i=0;i<rows.length;i++)
	   	{
	   		if(state==1){
	   			if(rows[i].CATLEVEL>=4){
	   				
	   				ids.push(rows[i].CATID);	
	   			}
	   		}else{
	   			ids.push(rows[i].CATID);	
	   		}
	   	}
	   	var idstr = ids.join(",");
	    $.ajax({
		   type: "POST",
		   url: "${ctx}/ContentCat/updateHidden.do?csrfToken=${csrfToken}",
		   data: "idstr="+idstr+"&isHidden="+state,
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 alert("操作成功！");
		    	 location.reload();  
		     }else{
		     	 alert("操作失败！"); 
		     }
		   }
		});
	}
	
	
	function batchUpdateChildId()
	{
	    $.ajax({
		   type: "POST",
		   url: "${ctx}/ContentCat/updateChildId.do",
		   data: "csrfToken=${csrfToken}",
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 alert("操作成功！");
		     }else{
		     	 alert("操作失败！"); 
		     }
		   }
		});
	}
	
	function del(catId) 
	{
		if(confirm("你确定删除该栏目吗?"))
		{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/ContentCat/del.do?csrfToken=${csrfToken}",
			   data: "catId="+catId,
			   success: function(msg)
			   {
			  	 if(msg.status=='-1'){
			  	 	alert("删除失败,该栏目下还有子栏目,不能删除！");
			  	 	return false;
			     }
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 $('#myTreeGrid').treegrid('remove',catId);
			     }
			     if(msg.status=='1'){
			     	alert("删除失败！");
			     	return false;
			     }
			   }
			});
		}
	}


$(function() {
	$("#myTreeGrid").treegrid({
	    url:"${ctx}/ContentCat/list.do",
	    queryParams:{"pid":'-1','csrfToken':'${csrfToken}'},
	    method:'POST',
	    treeField:'CATNAME',
	    rownumbers:true,
	    idField:'CATID',
	    singleSelect:false,  //是否单选 
	    columns:[[
	    	{field:"PK",title:"全选",checkbox:true,width:200},
	    	{field:"CATID",title:"栏目ID",align:'center',width:100},
	    	{field:"CATNAME",title:"栏目名称",width:250},
	    	{field:"CAT",title:"英文名称",align:'center',width:200},
	    	{field:'ISHIDDEN',title:'状态',width:100,align:'center',formatter:function(value, row, index){
	    		 var html = row.ISHIDDEN=='1'?'<font color="red">隐藏</font>':'显示';
	    		 return html;
 			}},
	    	{field:'id',title:'操作',width:300,align:'center',formatter:function(value, row, index){
	    		var html='<a class="treeopra" style="color:green;font-size:12px;text-decoration: underline;" href="${ctx}/ContentCat/add.do?pid='+row.CATID+'">添加子栏目</a>&nbsp;&nbsp;'+
                            '<a class="treeopra" style="color:green;font-size:12px;text-decoration: underline;" href="${ctx}/ContentCat/add.do?categoryid='+row.CATID+'">修改</a>&nbsp;&nbsp;'+
                            '<a class="treeopra" style="color:green;font-size:12px;text-decoration: underline;" href="javascript:void(0);" onclick="del('+row.CATID+');">删除</a>&nbsp;&nbsp;'+
                            '<a class="treeopra" style="color:green;font-size:12px;text-decoration: underline;" href="javascript:void(0);" onclick="orderCategory('+row.PID+')">排序</a>';
 				return html;
 			}}
	    ]],
	    toolbar: ["-",
	    	{id: 'batchDis',text: '批量显示',iconCls:'',handler:function(){batchUpdate(0);}},"-",
	    	{id: 'batchHid',text: '批量隐藏',iconCls:'',handler:function(){batchUpdate(1);}},"-",
	    	{id: 'batchUpChildId',text: '更新节点关系',iconCls:'',handler:function(){batchUpdateChildId();}},"-"],
	    onBeforeExpand:function(row){  
	        var url = "${ctx}/ContentCat/getChildren.do?pid="+row.CATID;  
	        $("#myTreeGrid").treegrid("options").url = url;
	        return true;      
	    }
	});
});
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
<h2 class="adm-title">
<a class="title" href="javascript:;">管理栏目</a>
<a class="fr btn" href="${ctx}/ContentCat/add.do">+添加栏目</a>
</h2>
<div class="l-clear"></div>
<table id="myTreeGrid"></table>
</body>
</html>