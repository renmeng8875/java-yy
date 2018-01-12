<%@include file="public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>开发管理员</title>
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js"	type="text/javascript"></script>
<style type="text/css">
a:link{color:green;font-size:14px;text-decoration: underline;}
a:visited{color:red;font-size:14px;text-decoration: underline;} 
</style>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : '模块名',
				name : 'moduleName',
				width : 200,
				align : 'left',
				isSort:true
			},
			{
				display : '属性名',
				name : 'propertyName',
				width : 200,
				align : 'left'
			}, 
			{
				display : '属性值',
				name : 'value',
				width : 650,
				align : 'left',
				render:function(rowdata, rowindex, value){
					return '<input type="text"  data-action="'+rowdata.moduleName+"."+rowdata.propertyName+'" style="color:#FF00FF;width:650px;" value="'+rowdata.value+'" />';
				}
			},
			{
				display:'操作',
				name:'operation',
				width:120,
				align:'center',
				render:function(rowdata, rowindex, value)
				{
					
                    return "<a href='javascript:void(0);'  data-action="+rowdata.moduleName+"."+rowdata.propertyName+">修改</a> "
				}
			}],
			width : '100%',
			pageSize: 20,
			pageSizeOptions : [ 5, 10, 15, 20 ],
            width: '100%',
            height: '97%',
            sortName: 'moduleName',
            rownumbers:true,
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
			data : { Rows : ${configList} }
		});
		
		$("a").click(function(){
			var data = $(this).attr("data-action");
			var value = $("input[data-action='"+data+"']").val()
			$.ajax({
			type: "POST",
			url: "${ctx}/mmport/setProperty.do?csrfToken=${csrfToken}",
			data: 'devkey=${devkey}&value='+value+"&id="+data,
			success: function(data){
				if(data&&data.status){
					if(data.status=="1"){
						alert("认证失败，你不具有此权限！");
					}else{
						alert("设置成功");
					}
				}
			}
			});	
		});
	
	
	});
	
	
</script>
</head>
<body style="overflow-x: hidden; padding: 4px;">

	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>


</body>
</html>