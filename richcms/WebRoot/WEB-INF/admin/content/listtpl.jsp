<%@include file="../public/header.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内容列表</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js" type="text/javascript" ></script>
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
//------------------------------------------function
	var listGrid,startTime,endTime;
	var listUrl='${ctx}/Content/findContentList.do';
	function getParams(){
		var params=[
			{name:'sTime', value:$("#stime").val()},
			{name:'eTime', value:$("#etime").val()},
			{name:'stxtHead', value:$("#txthead").val()},
			{name:'ttype', value:$("#ttype").val()},
			{name:'catId', value:'${catId}'},
			{name:'status', value:$("#status").val()},
			{name:'csrfToken',value:'${csrfToken}'}
		];
		return params;
	}
	function checkForm()
	{
		var retObj={isFlag:true,msg:''};
		if($("#txthead").val().length>50)
		{
			retObj.isFlag=false;
			retObj.msg='名称不能超过50个字符!';
		}
		
		return retObj;
	}
	function queryList()
	{
		var params=getParams();
		var retObj=checkForm();
		if(retObj.isFlag)
		{
			listGrid.loadServerData(params);
		}else{
			alert(retObj.msg);
		}
		//showBtn();
	}
	
	function loadListByStatus(status){
		$("#status").val(status);
		showBtn();
		queryList();
	}
	
	function del(contentDataId) 
	{
		if(confirm("你确定删除该内容吗?"))
		{
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/Content/del.do?csrfToken=${csrfToken}",
			   data: "contentDataId="+contentDataId,
			   success: function(msg)
			   {
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 location.reload();
			     }
			     if(msg.status=='1'){
			     	alert("删除失败！");
			     	return false;
			     }
			   }
			});
		}
	}
	
	function importBatch(){
		window.location.href='${ctx}/Content/multImport.do?csrfToken=${csrfToken}&catId=${catId}';
	}
	
	function batchSort()
	{
		var rows = listGrid.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一条数据！");
			return false;
		}
		var sortStr = []; 
		for(var i=0;i<rows.length;i++)
		{
			var id = rows[i].contentDataId;
			var priority = $("input[data-action='"+id+"']").val();
			sortStr.push(id+"-"+priority);
		}
		$.ajax({
		   type: "POST",
		   url: "${ctx}/Content/batchSort.do?csrfToken=${csrfToken}",
		   data: "sortStr="+sortStr.join(","),
		   success: function(msg)
		   {
		     if(msg.status=='0')
		     {
		    	 alert("批量排序成功！");
		    	 queryList(); 
		     }else{
		     	 alert("批量排序失败！");
		     }
		   }
		});
	}
	
	
	
	function deleteBatch()
	{
		var rows = listGrid.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一条数据！");
			return false;
		}
	    var ids = [];
	    if(confirm("你确定删除该数据吗?"))
		{
		   	for(var i=0;i<rows.length;i++)
		   	{
		   		ids.push(rows[i].contentDataId);	
		   	}
		   	var idstr = ids.join(",");
		    $.ajax({
			   type: "POST",
			   url: "${ctx}/Content/delete.do?csrfToken=${csrfToken}",
			   data: "idstr="+idstr,
			   success: function(msg)
			   {
			     if(msg.status=='0')
			     {
			    	 alert("删除成功！"); 
			    	 //listGrid.deleteSelectedRow();
			    	 location.reload();
			     }
			   }
			});
		}
	}
	
	function batchAudit(){
		var rows = listGrid.getSelectedRows();
		if(rows.length==0)
		{
			alert("请至少选中一条数据！");
			return false;
		}
	    var ids = [];
	    if(confirm("你确定审批该数据吗?"))
		{
		   	var rows = listGrid.getSelectedRows();
			if(rows.length==0)
			{
				alert("请至少选中一条数据！");
				return false;
			}
			var auditStr = [];
			var status = $("#status").val(); 
			for(var i=0;i<rows.length;i++)
			{
				var id = rows[i].contentDataId;
				auditStr.push(id);
			}
			$.ajax({
			   type: "POST",
			   url: "${ctx}/Content/batchAudit.do?csrfToken=${csrfToken}",
			   data: "status="+status+"&workflowlv=${workflowlv}&auditStr="+auditStr.join(","),
			   success: function(msg)
			   {
			     if(msg.status=='0')
			     {
			    	 alert("批量审批成功！");
			    	 location.reload();
			     }else{
			     	 alert("批量审批失败！");
			     }
			   }
			});
		}
	}
	
	function showBtn(){
		var toolbarManager=listGrid.toolbarManager;
		var status = $("#status").val(); 
		if(status>0 && status!=99){
			toolbarManager.setEnabled('auditBtn');
		}else{
			toolbarManager.setDisabled('auditBtn');
		}
	}

//------------------------------------------ligerUi

$(function() {

		startTime=$("#stime").ligerDateEditor(
				    {
				           label:'',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 0,
				           labelAlign: 'left',
				           cancelable : false,
				           showTime:true
				    });
		endTime =$("#etime").ligerDateEditor(
				    {
				           label: '',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 5,
				           labelAlign: 'center',
				           cancelable : false,
				           showTime:true,
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
				    			if(startTime.getValue()==null)
				    			{
				    				alert("请先选择开始时间！");
				    				endTime.setValue(null);
				    				return false;
				    			}
				           } 
				    });


		listGrid = $("#maingrid").ligerGrid({
			columns : [{
					display : '排序',
					name : 'priority',
					width : 50,
					align : 'center',
					render:function(item)
					{
						return '<input type="text" style="color:#FF00FF;" value="'+item.priority+'" data-action="'+item.contentDataId+'"/>';
					}
				}, {
					display : '内容ID',
					name : 'contentId',
					width : 100,
					align : 'center'
				},
				{
					display : '内容类型 ',
					name : 'dataType',
					width : 150,
					align : 'center'
				},
				{
					display : '名称 ',
					name : 'txtHead',
					width : 150,
					align : 'center'
				},
				{
					display : '发布时间 ',
					name : 'createTimeStr',
					width : 150,
					align : 'center'
				},
				{
					display : '更新时间 ',
					name : 'updateTimeStr',
					width : 150,
					align : 'center'
				},
				{
					display : '编辑 ',
					name : 'author',
					width : 150,
					align : 'center'
				},
				{
					display : '状态 ',
					name : 'status',
					width : 150,
					align : 'center',
					render:function(item)
					{
						var status=item.status;
						var html='';
						if(status==0||status==99){
							html='发布';
						}
						if(status==1){
							html='一审';
						}
						if(status==2){
							html='二审';
						}
						if(status==3){
							html='三审';
						}
						if(status==4){
							html='四审';
						}
						return html;
					}
				},{
		             display: '操作', 
		             isAllowHide: false,
		             width : 300,
		             render: function (row)
		             {
		             	 var catName=encodeURIComponent(encodeURIComponent('${catName}'));
		             	 var html = '<a href="${frontendDomain}/'+row.dataType+'/info/'+row.contentId+'.html" target="_blank" class="treeopra" style="cursor: pointer;">预览</a>';
		             	 var contentDataId=row.contentDataId;
		             	 html += '<a class="treeopra" style="cursor: pointer;" data-id="'+contentDataId+'" onclick="javascript:del('+row.contentDataId+');">删除</a>';
		             	 html += '<a href="${ctx}/Content/editpublishedcontent.do?catId=${catId}&contentDataId='+contentDataId+'" class="treeopra" style="cursor: pointer;" data-id="'+contentDataId+'" data-imgs="" data-imagejson="'+row.image+'" data-logojson="'+row.logo+'" data-txthead="'+row.txtHead+'">编辑</a>';
		                 return html;
		             }
	            }
			],
			pageSizeOptions : [ 5, 10, 15, 20 ],
	        width: '100%', 
	        height: '100%', 
	        pageSize: 10,
	        rownumbers:true,
	        checkbox : true,
	        cssClass: 'l-grid-gray', 
	        heightDiff: -6,
	        fixedCellHeight:false,
			url:listUrl,
			parms:getParams,  
			toolbar: {
				items: [
				{line: true},
				{text: '批量导入',icon: '',click:importBatch},
				{line: true},
				{text: '批量删除',icon: 'delete',click:deleteBatch},
				{line: true},
				{text: '批量排序',icon: '',click:batchSort},
				{line: true},
				{text: '批量审批',id:'auditBtn',icon: '',click:batchAudit}
				]
			}
		});
		
		showBtn();
});
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title" href="javascript:;">${catName}-栏目内容管理</a>
		</h2>
		<c:if test="${not empty workflowlv}">
			<ul class="adm-s-nav">
			      <li><a href="javascript:void(0);" onclick="loadListByStatus('0')">全部<sup> </sup></a> |&nbsp;</li>
			      <li><a href="javascript:void(0);" onclick="loadListByStatus('99')">审核通过<sup> </sup></a> |&nbsp;</li>
			      <c:if test="${workflowlv>=1 && (roleId == 1 || fn:contains(userwflv,'1'))}">
			      	<li>
			      		<a href="javascript:void(0);" onclick="loadListByStatus('1')">一审
			      			<sup>
			      				<c:if test="${not empty countList}">
			      					<c:set var="num" value="0"/>
					      			<c:forEach items="${countList}" var="item">
					      				<c:choose>
					      					<c:when test="${item.STATUS==1}">
					      						<c:set var="num" value="${item.COUNTNUM}"/>
					      					</c:when>
					      				</c:choose>
					      			</c:forEach>
					      			<c:out value="${num}"></c:out>
				      			</c:if>
				      			<c:if test="${empty countList}">0</c:if>
			      			</sup>
			      		</a> |&nbsp;
			      	</li>
			      </c:if>
			      <c:if test="${workflowlv>=2 && (roleId == 1 || fn:contains(userwflv,'2'))}">
			      	<li>
			      		<a href="javascript:void(0);" onclick="loadListByStatus('2')">二审
			      			<sup>
			      				<c:if test="${not empty countList}">
			      					<c:set var="num" value="0"/>
					      			<c:forEach items="${countList}" var="item">
					      				<c:choose>
					      					<c:when test="${item.STATUS==2}">
					      						<c:set var="num" value="${item.COUNTNUM}"/>
					      					</c:when>
					      				</c:choose>
					      			</c:forEach>
					      			<c:out value="${num}"></c:out>
				      			</c:if>
				      			<c:if test="${empty countList}">0</c:if>
			      			</sup>
			      		</a> |&nbsp;
			      	</li>
			      </c:if>
			      <c:if test="${workflowlv>=3 && (roleId == 1 || fn:contains(userwflv,'3'))}">
			      	<li>
			      		<a href="javascript:void(0);" onclick="loadListByStatus('3')">三审
			      			<sup>
			      				<c:if test="${not empty countList}">
			      					<c:set var="num" value="0"/>
					      			<c:forEach items="${countList}" var="item">
					      				<c:choose>
					      					<c:when test="${item.STATUS==3}">
					      						<c:set var="num" value="${item.COUNTNUM}"/>
					      					</c:when>
					      				</c:choose>
					      			</c:forEach>
					      			<c:out value="${num}"></c:out>
				      			</c:if>
				      			<c:if test="${empty countList}">0</c:if>
			      			</sup>
			      		</a> |&nbsp;
			      	</li>
			      </c:if>
			      <c:if test="${workflowlv>=4 && (roleId == 1 || fn:contains(userwflv,'4'))}">
			      	<li>
			      		<a href="javascript:void(0);" onclick="loadListByStatus('4')">四审
			      			<sup>
			      				<c:if test="${not empty countList}">
			      					<c:set var="num" value="0"/>
					      			<c:forEach items="${countList}" var="item">
					      				<c:choose>
					      					<c:when test="${item.STATUS==4}">
					      						<c:set var="num" value="${item.COUNTNUM}"/>
					      					</c:when>
					      				</c:choose>
					      			</c:forEach>
					      			<c:out value="${num}"></c:out>
				      			</c:if>
				      			<c:if test="${empty countList}">0</c:if>
			      			</sup>
			      		</a> |&nbsp;
			      	</li>
			      </c:if>	
		   </ul>
		</c:if>
		<form id="fm_es" method="post">
		<input type="hidden" name="csrfToken" value="${csrfToken}"/>
		<ul class="clearfix">
				<li class="adm-page">
					<span style="float:left;">名称：</span>
					<span style="float:left;">
						<input type="text" value="${stxtHead}" id="txthead" name="txthead" class="s mr5 ml5">
					</span>
					
					<span style="float:left;">时间：</span>
					<span style="float:left;">
						<select name="ttype" id="ttype">
							<option value="ctime" <c:if test='${ttype eq ctime}'>selected</c:if>>
								发布时间
							</option>
							<option value="utime" <c:if test='${ttype eq utime}'>selected</c:if>>
								更新时间
							</option>
						</select>
					</span>
					
					<span style="float:left;">开始：</span>
					<span style="float:left;">
						<input type="text" id="stime" name="stime" class="s mr5 ml5" value="${startTime}" style="width: 80px;">
					</span>
					<span style="float:left;">结束：</span>
					<span style="float:left;">
						<input type="text" id="etime" name="etime" class="s mr5 ml5" value="${endTime}" style="width: 80px;">
					</span>
					<span style="float:left;">
						<a class="btn" href="javascript:queryList();" id="btnsearch">搜索</a>
					</span>
				</li>
		</ul>
		<div class="l-clear"></div>
		<div id="maingrid"></div>
		<div style="display: none;"></div>
		<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
		<input type="hidden" name="status" id="status"/>
		</form>
		<form name="fm_im" id="fm_im" method="post" action="${ctx}/Content/multImport.do">
			<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<input type="hidden" name="catId" id="catId" value="${catId}"/>
			<input type="hidden" name="catName" id="catName" value="${catName}"/>
		</form>
</body>
</html>