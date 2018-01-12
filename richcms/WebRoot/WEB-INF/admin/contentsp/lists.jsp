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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js" type="text/javascript" ></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js" type="text/javascript"></script>
<style type="text/css">
.doaudit{cursor: pointer;_cursor:hand;color:red;}
.opwaudit{ position: relative;}
</style>
<script type="text/javascript">
	var listUrl='${ctx}/ContentSp/findLists.do?csrfToken=${csrfToken}';
	var gridManager = null;
	var startTime=null;
	var endTime=null;
	var addWindow=null;
	
	
	function getParams(){
		var params=[
			{name:'stime', value:$("#stime").val()},
			{name:'etime', value:$("#etime").val()},
			{name:'txthead', value:$.trim($("#txthead").val())},
			{name:'ttype', value:$("#ttype").val()},
			{name:'stype', value:$("#stype").val()}
		];
		return params;
	}
	
	function showSuccessTips(){
		alert("提交成功!");
	}
	
	function queryList()
	{
		var params=getParams();
		gridManager.loadServerData(params);
	}
	
	function batchSortContentSp()
	{
		var rows = gridManager.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一条数据！");
			return false;
		}
		var sortStr = [];
		for(var i=0;i<rows.length;i++)
		{
			var id = rows[i].companyId;
			var priority = $("input[data-action='"+id+"']").val();
			if(!checksort(priority))
			{
				return false;
			}
			sortStr.push(id+"-"+priority);
		}
		$.ajax({
		   type: "POST",
		   url: "${ctx}/ContentSp/batchsort.do?csrfToken=${csrfToken}",
		   data: "sortStr="+sortStr.join(","),
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 alert("批量排序成功！");
		    	 queryList(); 
		     }
		   }
		});
	}
	
	function toAudit(id,stu,msg){
		$.ajax({
		   type: "POST",
		   url: "${ctx}/ContentSp/doAudit.do?csrfToken=${csrfToken}",
		   data: {'id':id,'stu':stu,'msg':msg},
		   success: function(data)
		   {
		      if(data.status==0){
		      	alert("操作成功！");
				queryList();
			  }else{
			  	 alert("操作失败！");
			  }
		   }
		});
	}
	
	function showAuditWin(id,status){
		addWindow = $.ligerDialog.open({
			 height: 300, 
			 title:'填写不通过原因',
			 url: '${ctx}/ContentSp/audit.do?id='+id+'&status='+status,
			 width: 480, 
			 showMax: true, 
			 showToggle: true, 
			 showMin: true, 
			 allowClose: true,
			 isResize: true, 
			 slide: true
		});
		$(".l-dialog-content").css("overflow","hidden");
	}
	
	function doAudit(id,status){
		if(status==99){
			toAudit(id,status,'');
		}else{
			showAuditWin(id,status);
		}
	}
	function checksort(v)
	{
			if(!$.isNumeric(v))
			{
				alert("请输入1到9999范围内的数字！")
				return false;
			}
			if(v.indexOf(".")>-1)
			{
				alert("请输入整数！");
				return false;
				
			}
			if(v>9999||v<1)
			{
				alert("请输入1到9999范围内的数字！");
				return false;
			}
			return true;
	}
	
	
	function checksortinput(o)
	{
			if(!$.isNumeric(o.value))
			{
				alert("请输入1到9999范围内的数字！")
				this.value="";
				this.focus();
				return false;
			}
			if(o.value.indexOf(".")>-1)
			{
				alert("请输入整数！");
				this.value="";
				this.focus();
				return false;
				
			}
			if(o.value>9999||o.value<1)
			{
				alert("请输入1到9999范围内的数字！");
				this.value="";
				this.focus();
				return false;
			}
	}
	
	
	$(function() {
		startTime=$("#stime").ligerDateEditor(
				    {
				           label:'',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 0,
				           labelAlign: 'left',
				           cancelable : false
				    });
		endTime =$("#etime").ligerDateEditor(
				    {
				           label: '',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 5,
				           labelAlign: 'center',
				           cancelable : false,
				           onAfterSelect:function()
				           {
				    			if(startTime.getValue())
				    			{
				    				if(startTime.getValue().getTime()>endTime.getValue().getTime())
				    				{
				    					alert("开始时间不能大于结束时间!");
				    					endTime.setValue(null);
				    					return false;
				    				}
				    			}
				           } 
				    });
	
		gridManager = $("#maingrid").ligerGrid({
			columns : [ {
				display : '排序',
				name : 'priority',
				width : 50,
				align : 'center',
				render:function(item)
				{
					return '<input type="text" optype="sortinput" style="color:#FF00FF;" value="'+item.priority+'" onblur="checksortinput(this)" data-action="'+item.companyId+'"/>';
				}
			}, {
				display : '品牌店ID',
				name : 'companyId',
				width : 80,
				align : 'center'
			}, {
				display : '品牌店名称',
				name : 'title',
				width : 180,
			}, {
				display : '发布时间',
				name : 'pubTimeStr',
				width : 120,
				align : 'center'
			}, {
				display : '提交时间',
				name : 'submitTimeStr',
				width : 120,
				align : 'center'
			}, {
				display : 'seo关键字',
				name : 'seoKeyword',
				width : 120,
				align : 'center'
			}, {
				display : '状态',
				name : 'status',
				width : 180,
				align : 'center',
				render:function(item)
				{
					var text = "<font color='green'>通过</font>";
					if(item.status=='1'){
						text = "<font color='blue'>未审核</font>"
					}else if(item.status=='2'){
						text = "<font color='red'>打回</font>";
					}
					return text;
				}
			},
			{
				display:'操作',
				name:'opration',
				width : 200,
				align : 'center',
				render:function(row){
					var html='';
					if(row.have>0){
						html+='<a href="${frontendDomain}/brand/'+row.companyId+'.html" target="_blank"><font color="green">查看</font></a>&nbsp;&nbsp;&nbsp;';
					}
					
					if(row.status == 1 || row.status == 2 ){
							html+='<a href="${frontendDomain}/brand/'+row.companyId+'.html?preview='+row.code+'" target="_blank" class="dodel"><font color="green">预览</font></a>&nbsp;&nbsp;&nbsp;';
						if(row.status == 1){
								html+='<span class="doaudit" onclick="doAudit('+row.companyId+','+99+')">通过</span>&nbsp;&nbsp;&nbsp;';
								html+='<span class="doaudit opwaudit" id="nopass_'+row.companyId+'" onclick="doAudit('+row.companyId+','+2+')">不通过</span>';
						}
					}
					return html;				
				}
			}
			],
			pageSizeOptions : [ 5, 10, 15, 20 ],
			sortName: 'sid',
            width: '100%', 
            height: '100%', 
            pageSize: 10,
            rownumbers:true,
            checkbox : true,
            //应用灰色表头
            cssClass: 'l-grid-gray', 
            heightDiff: -6,
            width : '100%',
            fixedCellHeight:false,
            url:listUrl,
			parms:getParams,
			toolbar : {
				items : [{
					text : '批量排序',
					click : batchSortContentSp,
					icon : 'ok'
				}
				]
			}
		});
	
		$("#btnsearch").bind('click',function(){
			if($.trim($("#stime").val())!='')
			{
				    if(startTime.getValue().getTime()>endTime.getValue().getTime())
				    {
				    	alert("开始时间不能大于结束时间!");
				    	endTime.setValue(null);
				    	return false;
				    }
			}
			queryList();
		});
		
		
		
	});
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
	<h2 class="adm-title clearfix"><a class="fl title">品牌店管理</a></h2>
	
	<div class="l-clear"></div>
		<ul class="clearfix">
			<li class="adm-page" style="margin: 0px;">
				<span style="float: left;"> <select name="stype" id="stype">
						<option value="title">
							品牌店名称
						</option>
						<option value="companyid">
							品牌店id
						</option>
					</select> </span>
				<span style="float: left;">
					<input type="text" value="" name="txthead" id="txthead" maxlength="50" class="s mr5 ml5" style="float: left;">
				</span>
				<span style="float: left;">时间：</span>
				<select name="ttype" id="ttype" style="float: left">
					<option value="pubtime">
						发布时间
					</option>
					<option value="submittime">
						提交时间
					</option>
				</select>
				<span style="float: left;">开始：</span>
				<span style="float: left;">
					<input type="text" readonly id="stime" name="stime" class="s mr5 ml5" value="" style="width: 80px; float: left">
				</span>
				<span class="b" style="float: left;">结束：</span>
				<span style="float: left;">
					<input type="text" readonly id="etime" name="etime" class="s mr5 ml5" value="" style="width: 80px; float: left;">
				</span>
				<span style="float: left;">
					<a class="btn" href="javascript:;" id="btnsearch" style="float: left;">搜索</a> 
				</span>
			</li>
		</ul>
		<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
</body>
</html>