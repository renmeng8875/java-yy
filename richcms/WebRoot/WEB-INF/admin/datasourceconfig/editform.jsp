<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
 <link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerGrid.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerTab.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDrag.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerToolBar.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
<!-- <script src="../grid/CustomersData.js" type="text/javascript"></script> -->
 <script type="text/javascript">
        var navtab = null;
        $(function ()
        {
            $("#navtab").ligerTab({ 
               onBeforeSelectTabItem: function (tabid) {
                 if(tabid=='tab1'){
                    $("#fieldlist").html("<iframe src='${ctx }/dataconfig/formfieldlists.do?formid=${formid}&type=${forminfo.formtype }'></iframe>");     
                 }
               }, onAfterSelectTabItem: function (tabid){
//                  if(tabid=='tab1'){
//                     $("#fieldlist").html("<iframe src='${ctx }/dataconfig/formfieldlists.do?formid=${formid}&type=${forminfo.formtype }'></iframe>");     
//                  }
               },
               dblClickToClose: false
            });
            
            
            navtab = $("#navtab").ligerGetTabManager();
            $("#form1").Validform({
				tiptype:2,
				datatype:{
							"zhs3-20":/^[a-z]{2}[a-z0-9]{1,19}$/,
							"ns1-20":/^[1-9]\d{0,3}$/,
							"ns1-99":/^[1-9]\d{0,1}$/,
						}
			});
        });
    
         
    </script>
    <style type="text/css">
        body{ font-size:11px;}
        .tdinput{width:180px;}
    </style>
</head>
<body >
    <h2 class="adm-title clearfix">
		<a class="fl title">数据源管理
		 <c:choose>
			<c:when test="${forminfo.formid!=null}">-修改</c:when>
			<c:otherwise>-添加</c:otherwise>
		</c:choose>
		</a> 
	</h2>
	<div id="navtab" style="overflow:hidden; border:1px solid #A3C0E8;width:99% ">
        <div tabid="home" title="数据源信息" lselected="true" > 
            <form action="${ctx }/dataconfig/saveform.do" method="post" id="form1">
              <input type="hidden" name="csrfToken" value="${csrfToken}"/>
              <table style="font-size:12px;color:#444;line-height:32px; margin:10px;">
				 <c:if test="${not empty forminfo.formid }">
					 <tr>
						 <td width="100">ID：</td>
						 <td>${forminfo.formid }</td>
						 <td><div class="Validform_checktip"></div></td>
					 </tr>
				 </c:if>
				  <tr>
					 <td width="100">数据源名称：</td>
					 <td><input class="tdinput" type="text" name="formdesc" value="${forminfo.formdesc }" datatype="s3-20" nullmsg="请输入数据源名称" errormsg="数据源名称为3~20位字符"  ></td>
					 <td><div class="Validform_checktip"></div></td>
				 </tr>
				  <tr>
					 <td width="100">数据源标识：</td>
					 <td><select class="tdinput" name="datatype">
					    <c:if test="${forminfo.formtype=='extends' || forminfo.formtype==null}" >
					    <c:forEach items="${datalist }" var="v" varStatus="status">
					        <option <c:if test="${forminfo.datatype== v}">selected="selected" </c:if> value="${v }">${v }</option>
					    </c:forEach>
					    </c:if>
					    <c:if test="${forminfo.formtype=='base' }" >
					       <option selected="selected"  value="data">data</option>
					    </c:if>
					 </select></td>
					 <td><div class="Validform_checktip"></div></td>
				 </tr>
				 <tr>
					 <td width="100">数据源类型：</td>
					 <td><select class="tdinput" name="formtype" >
					       <c:choose>
						       <c:when test="${forminfo.formtype=='extends' }">
						           <option selected="selected" value="${forminfo.formtype }" >数据源表</option>
						           <option value="${forminfo.formtype }" disabled="disabled">栏目数据表</option>
						       </c:when>
						       <c:when test="${forminfo.formtype=='base' }">
						           <option value="${forminfo.formtype }" disabled="disabled">数据源表</option>
						           <option selected="selected" value="${forminfo.formtype }" >栏目数据表</option>
						       </c:when>
						       <c:otherwise>
						           <option value="extends" selected="selected" >数据源表</option>
						           <option value="base" disabled="disabled">栏目数据表</option>
						       </c:otherwise>
					       </c:choose>
					     </select>
					 </td>
					 <td><div class="Validform_checktip"></div></td>
				</tr>	 
				 
				 <tr>
					 <td width="100">顺序：</td>
					 <td><input class="tdinput" type="text" name="formorder" value="${forminfo.formorder }" datatype="ns1-99"  errormsg="顺序为1～99数字"></td>
					 <td><div class="Validform_checktip"></div></td>
				 </tr>
				  <tr>
					 <td colspan="2" align="center">
					   <c:if test="${forminfo.formid!=null }">
					   <input type="hidden" name="formid" value="${forminfo.formid }">
					   </c:if>
					   <input type="submit" value="提交" id="Button1" class="l-button l-button-submit" /> 
					 </td>
				 </tr>
              </table>
              </form>
        </div>

        <div title="字段列表" tabid="tab1" id="fieldlist" style="height: 450px;" <c:if test="${lselected=='1' }">lselected="true"</c:if>>
            
        </div>
        
         <div title="添加字段" tabid="tab2" style="height: 450px;">
            <iframe src="${ctx}/dataconfig/addfield.do?formid=${formid}&formtype=${forminfo.formtype}"></iframe>
        </div>
     </div>
</body>
</html>
