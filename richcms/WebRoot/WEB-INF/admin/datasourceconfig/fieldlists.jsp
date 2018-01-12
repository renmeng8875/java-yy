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
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
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
    $(function () {
         gridManager= $("#maingrid1").ligerGrid({
             
             columns: [
             { display: '字段id', name: 'fieldid',  width: 80 },
             { display: '字段名称', name: 'fieldname', width: 120 },
             { display: '字段类型', name: 'fieldtype', width: 120 }, 
             { display: '字段长度', name: 'fieldattr', width: 80 }, 
             { display: '显示名称', name: 'labelname', width: 120 }, 
             { display: '是否显示',  minWidth: 120,
               render:function(row){
				    var html = new Array();
				    if(row.status.indexOf('1')>=0){
				       html.push("列表显示");
				    }
				    if(row.status.indexOf('2')>=0){
				       html.push("明细显示");
				    }
				    if(row.status.indexOf('3')>=0){
				       html.push("搜索显示");
				    }
                    return html.join("<br/>");
				}
             }, 
		     { display: '是否可编辑',  width: 120 ,
		        render:function(row){
				    var html = "";
				    if(row.isedit==1){
				       html = "字段可编辑";
				    }
				    if(row.isedit==0){
				       html="字段不可编辑";
				    }
                    return html;
				}
		     },{
				display : '操作',
				align : 'center',
				width: 120 ,
				render:function(row){
					var html = '<a class="treeopra" href="${ctx}/dataconfig/addfield.do?formtype=${formtype}&fieldid='+row.fieldid+'">修改</a>'+
                               '<a class="treeopra" style="cursor: pointer;" onclick="javascript:del('+row.fieldid+');">删除</a>';
                    return html;
				}
			 }
             
             ], 
             pageSize:10,
             pageSizeOptions : [ 5, 10, 15, 20 ],
             rownumbers:true,
             checkbox: true,
             heightDiff: -6,
             fixedCellHeight:false,
             data : { Rows : ${fieldlist} },
             width: '99%',height:'98%'
           
         });

         gridManager = $("#maingrid1").ligerGetGridManager();
         $("#pageloading").hide(); 
         columnAuto();
     });
 
	 
	 function del(fieldid){
	    $.ligerDialog.confirm('提示内容', function (btn) { 
	      if(btn){	
	      	$.ajax({
				   type: "post",
				   url: "${ctx}/dataconfig/deleteField.do?csrfToken=${csrfToken}",
				   data: "fieldid="+fieldid,
				   success: function(rs){
				        if(rs.status=='0'){
				    		gridManager.deleteSelectedRow();
				    	}
				    	alert(rs.msg);
				   }
				})
			}	
	    });
	 }
     function columnAuto(){
	       $(".l-grid-header-inner","#maingrid").css("width","100%");
	       $(".l-grid-header-table","#maingrid").css("width","100%"); 
	       $(".l-grid-body-inner","#maingrid").css("width","100%");
	       $(".l-grid-body-table","#maingrid").css("width","100%");
	       $(".l-grid-row-cell-inner","#maingrid").css("width","100%");
	       $("table","#maingrid").css("table-layout","fixed");        
	 }
	

	
</script>

</head>
<body style="" onload="columnAuto()">

	<div id="maingrid1" style="padding:1px 4px;"></div>

	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>