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
	var gridManager,addWindow;
	$(function() {
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : '工作流名称',
				name : 'workflow',
				width : 280,
				align : 'center'
			},{
				display : '工作流级别',
				name : 'flowlevel',
				width : 150,
				align : 'center'
			}, {
				display : '工作流描述',
				name : 'mem',
				width : 350,
				align : 'center',
				render:function(row){
				    var mem=row.mem;
				    if(mem.length>20)
				    {
				    	mem = mem.substr(0,20)+"..";
				    }
					var html = '<span title="'+row.mem+'">'+mem+'</span>';
					return html;
				}
			}, {
				 display: '管理操作', isAllowHide: false,
	             render: function (row)
	             {
	                 var html = '<a class="treeopra"  href="${ctx}/Workflow/add.do?flowid='+row.flowid+'">修改</a>'+
	                            '<a class="treeopra" href="#" onclick="flowUser('+row.flowid+','+row.flowlevel+');">人员</a>'+
	                            '<a class="treeopra" style="cursor: pointer;" onclick="javascript:del('+row.flowid+');">删除</a>';
	                 return html;
	             }
			} ],
			width : '100%',
			pageSize: 5,
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'flowid',
            width: '100%', height: '100%', pageSize: 10,rownumbers:true,
//             checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
			data : { Rows : ${flowList} }
		});
		 gridManager = $("#maingrid").ligerGetGridManager();
	});
	
  function 	flowUser(flowid,flowlevel){
	  addWindow = $.ligerDialog.open({
			 height: 320, 
			 title:'工作流配置',
			 url: '${ctx}/Workflow/audiflowuser.do?flowid='+flowid+'&flowlevel='+flowlevel,
			 width: 450, 
			 showMax: true, 
			 showToggle: false, 
			 showMin: false, 
			 allowClose: true,
			 isResize: true, 
			 slide: true
		});
	  $(".l-dialog-content").css("overflow","hidden");
  }
  
  function showSuccessTips(retCode){
  		alert("提交成功!");
	}
 
  function del(flowid){
		if (flowid == null)
			alert("请至少选中一条流程数据！");
		else {
			$.ligerDialog.confirm('确定删除?',function(btn){
			  if(btn){
			   $.ajax({
					   type: "post",
					   url: "${ctx}/Workflow/del.do?csrfToken=${csrfToken}",
					   data: {flowid:flowid},
					   success: function(res){
					     if(res.status=="1") {
					    	 alert("删除成功！"); 
					    	 gridManager.deleteSelectedRow();
					     }else{
					         alert(res.msg);
					     }
					      
					   }
					})
				}	
			});
		}
	}
 
 
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
<div class="main_border" style="border:0;"><div id="my_main">
<div class="pad10">
	<h2 class="adm-title clearfix"><a  class="fl title">工作流配置</a><a href="${ctx }/Workflow/add.do" class="fr btn">+添加工作流</a></h2>
	<div class="l-clear"></div>

	<div id="maingrid"></div>
	<div style="display: none;"></div>

	<a class="l-button"
		style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
		
</div>

<div id="workuser" style="width:500px;box-shadow:0px 0px 10px #d2d2d2; border:1px solid #d2d2d2; padding-bottom:30px;background:#fff;" class="jqmWindow">
<form id="frm_user" onSumbit="return false;">
<input type="hidden" name="csrfToken" value="${csrfToken}"/>
<h2 class="adm-title"><a class="title"><span id="wfl_title"></span>－工作流人员配置</a><a class="fr jqmClose" style="margin-right:20px;" href="javascript:;">X</a></h2>
<div class="pad10">
  <div class="adm-text ul-float">
    <ul class="clearfix" id="uluser">
      <volist name="lname" id="vo" key="k">  
      <li id="flv_{$key}"><span class="l">{$vo}级审核人员列表：</span>
        <volist name="userinfo" id="user">
        <input type="checkbox" name="wfl[{$k}][]" value="{$user['userid']}" class="adm-chk" />{$user['nickname']}
        </volist>
      </li>
    </volist>      
    </ul>
  </div>
</div>
<div class="adm-btn-big"><input id="btnuser" type="button" value="提&nbsp;&nbsp;交" /></div>
</form>
</div>

</div></div>
</body>
</html>