<%@include file="../public/header.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.richinfo.common.TokenMananger"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>数据列表模板页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/page.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/jscal2.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/page.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/jscal2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"	type="text/javascript"></script>
  </head>
  
  <body>
    <h2 class="adm-title clearfix"> <a class="fl title">系统参数管理</a><c:if test="${datatype=='news'|| datatype=='ad' }"> <a href="${ctx }/datasource/datainfo.do?dataid=${dataid}&datatype=${datatype}" class="fr btn">+添加</a></c:if></h2>
	<div>
		<form id="fm_es" method="post" action="${ctx }/datasource/lists.do?module=${datatype}" onsubmit="return checkmysearch()">
<!-- 		<input type="hidden" name="csrfToken" value="${csrfToken}"/> -->
		<input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
		<ul class="clearfix" >
		  <li class="adm-page">
		      <c:forEach items="${searchlist }" var="vo">
		        <c:choose>
		       	  <c:when test="${vo.htmltype==1 || vo.htmltype==2 || vo.htmltype==3}"> <span class="b">${vo.labelname}: </span>
		       	       <c:choose>
		       	         <c:when test="${fn:indexOf(vo.fieldname, 'price')>=0}">
		       	           <select name="${vo.fieldname }">
		       	                <option value="0" <c:if test="${vo.fieldvalue=='0'||vo.fieldvalue==null }"> selected="selected"</c:if>>全部</option>
					            <c:if test="${datatype=='ios' }">
					            <option value="1" <c:if test="${vo.fieldvalue=='1'}"> selected="selected"</c:if>>限时免费</option>
					            <option value="2" <c:if test="${vo.fieldvalue=='2'}"> selected="selected"</c:if>>限时打折</option>
					            </c:if>
					            <option value="3" <c:if test="${vo.fieldvalue=='3'}"> selected="selected"</c:if>>付费</option>
					            <option value="4" <c:if test="${vo.fieldvalue=='4'}"> selected="selected"</c:if>>免费</option>
		       	           </select>
		       	         </c:when>
		       	         <c:when test="${vo.fieldname=='ismmtoevent' }">
				       	     <input type="radio" name="${vo.fieldname }" value="1"  <c:if test="${vo.fieldvalue==1}">checked="checked"</c:if>/>是
				       	     <input type="radio" name="${vo.fieldname }" value="0"  <c:if test="${vo.fieldvalue==0}">checked="checked"</c:if>/>否
				       	 </c:when>
		       	         <c:otherwise> <textarea id="keywd" name ="${vo.fieldname }" cols="40" rows="1">${vo.fieldvalue }</textarea></c:otherwise>
		       	       </c:choose>
		       	  </c:when> 
		       
		       	  <c:when test="${vo.htmltype==6 }">
		       	    <span class="b">${vo.labelname}</span> 
		       	  	<span class="b">开始：</span>  <input style="width:110px;" type="text" id="s_${vo.fieldid }" name="t_${vo.fieldname }"  <c:if test="${vo.fieldvalue!=''}"> value="${fn:substring(vo.fieldvalue,0,16) }" </c:if>  />
		       	  	<span class="b">结束：</span>  <input style="width:110px;" type="text" id="e_${vo.fieldid }" name="t_${vo.fieldname }" <c:if test="${vo.fieldvalue!=''}"> value="${fn:substring(vo.fieldvalue,17,33) }" </c:if> value="${vo.fieldvalue }"  />
	       	    	<script type="text/javascript">
		       	    	 $(function(){
			       	    	 if('undefined' != typeof(Calendar)){
								var START_CAL = Calendar.setup({
									inputField : "s_${vo.fieldid }",
									trigger : "s_${vo.fieldid }",
									dateFormat: "%Y-%m-%d %H:%M",
									max: new Date(),
									showTime: true,
									onSelect : function() {this.hide();}
								}),END_CAL = Calendar.setup({
									inputField : "e_${vo.fieldid }",
									trigger : "e_${vo.fieldid }",
									dateFormat: "%Y-%m-%d %H:%M",
									max: new Date(),
									showTime: true,
									onSelect : function() {
									var ds_${vo.fieldid } = $("#s_${vo.fieldid }").val();
									ds_${vo.fieldid } = ds_${vo.fieldid }.replace(/-/g,"/");
									var date = new Date(ds_${vo.fieldid });
									var ts_${vo.fieldid } = Date.parse(date); 
									var de_${vo.fieldid } =  $("#e_${vo.fieldid }").val();
									de_${vo.fieldid } = de_${vo.fieldid }.replace(/-/g,"/");
									var te_${vo.fieldid } = Date.parse(new Date(de_${vo.fieldid })); 
									if(te_${vo.fieldid }-ts_${vo.fieldid }>=0||ds_${vo.fieldid }==""){
										 this.hide();
									}else{
									     alert("起始时间大于结束时间 ");
									}
									this.hide();}
								}); 
							   }	
		       	    	 });
					</script>
		       	  </c:when>
		       	  <c:otherwise></c:otherwise>
		        </c:choose>
		      </c:forEach>
		       <div>
		        <div class="b" style="float:left;">时间：
		        <select name="ttype"> 
		       	 <option <c:if test="${ttype=='onlinetime' }"> selected="selected"</c:if> value="onlinetime">上架时间</option> 
		       	 <option <c:if test="${ttype=='utime' }"> selected="selected"</c:if> value="utime">更新时间</option>
		        </select>
		        </div>
		       <div style="float:left;"> <input type="text"  id="stime" name="stime" class="s mr5 ml5" value="${stime }" style="width:110px;"></div>
               <div style="float:left;"> <input type="text"  id="etime" name="etime" class="s mr5 ml5" value="${etime }" style="width:110px;"></div>
		       </div>
		        
               <script type="text/javascript">
				  $(function(){
				  var startTime = $("#stime").ligerDateEditor( {
				           label: '开始',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 50,
				           labelAlign: 'center',
				           cancelable : false,
				           showTime:true,
				           onAfterSelect:function() {
				    			$("#stime").removeClass("Validform_error");
				    			$("#etime").removeClass("Validform_error");
				    			$("#activityTimeTip").css("display","none");
				           } 
				    });
					var endTime = $("#etime").ligerDateEditor( {
					       label: '结束',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 50,
				           labelAlign: 'center',
				           cancelable : false,
				           showTime:true,
				           onAfterSelect:function() {
				    			$("#etime").removeClass("Validform_error");
				    			$("#stime").removeClass("Validform_error");
				    			$("#activityTimeTip").css("display","none");
				    			if(startTime.getValue()){
				    				if(startTime.getValue().getTime()>endTime.getValue().getTime()) {
				    					alert("开始时间不能大于结束时间!");
				    					$("#etime").addClass("Validform_error");
				    					endTime.setValue(null);
				    					return false;
				    				}
				    			}
				    			if(startTime.getValue()==null) {
				    				alert("请先选择开始时间！");
				    				endTime.setValue(null);
				    				$("#stime").addClass("Validform_error");
				    				return false;
				    			}
				           }
				         }); 
	       	    	 });
				</script>
		      &nbsp;&nbsp;<input type="submit" class="btn" value="搜索">
		      <span class="adm-quence"><a href="javascript:;" class="publish" data-id="all" >批量发布</a></span>
		  </li>
		</ul>
<!-- 		<input type="hidden" name="datatype" value="${datatype }"> -->
		</form>
    </div>
	<table class="adm-table">
    <thead>
    <tr class="tr1">
      <td width="2%"><input type="checkbox"  class="adm-chk" id="inpselall" /></td>
      <td width="3%">编号</td>
      <c:set value="${fn:length(fieldlist)}" var="fieldlength"></c:set>
      <c:forEach items="${fieldlist }" var="field"> 
          <td width="10%">${field.labelname }</td>
      </c:forEach>
      <td width="6%">上架时间</td>
      <td width="6%">更新时间</td>
      <td width="9%">操作</td>
    </tr>
    </thead>
    <tbody id="tbmain" >
      <c:choose>
         <c:when test="${fn:length(listvalue)==0 }"><tr><td colspan="${fieldlength+5 }"><span style="color:red;">不存在相关数据</span></td> </tr></c:when>
         <c:otherwise>
            <c:forEach items="${listvalue }" var="vs" varStatus="status">
		       <tr> 
		        <td><input type="checkbox" name="checkbox" class="adm-chk" value="${vs.dataid }" /></td>
		        <td>${ status.index + 1}</td>
		        <c:set value="${vs.dataid }" var="dataid"></c:set>
		        <c:forEach items="${vs.fieldlist }" var="v">
		         <td>
		          <c:choose>
		             <c:when test="${fn:length(v.fieldvalue)>=20 }">${fn:substring(v.fieldvalue, 0, 20)}...</c:when>
		             <c:otherwise>${v.fieldvalue }</c:otherwise>
		          </c:choose>
		         </td>
		        </c:forEach>
		        <td>${vs.ctime }</td>
		        <td>${vs.utime }</td>
		        <td><a href="javascript:;" class="publish" data-id="${dataid }">发布</a><br/>
			      <a href="${ctx }/datasource/datainfo.do?dataid=${dataid}&datatype=${datatype}" class="edit" data-id="${dataid }">编辑</a><br/>
			      <a href="javascript:void(0)" onclick="delapp('${dataid}','${datatype }')" class="delappresource" data-dataid="${dataid }">删除</a>
		      </td>
		       </tr>
		    </c:forEach>
         </c:otherwise>
      </c:choose>
   
    </tbody>
    </table>
    <input type="hidden" id="url" value="${ctx }/datasource/lists.do?module=${datatype}&csrfToken=${csrfToken}">
    <div class="page" id="pagediv">
    <c:if test="${count >0 }">
    	<script>setPage(document.getElementById("pagediv"),${count},${currentpage},$("#url").val());</script>
    </c:if>
    </div>
    
  <script type="text/javascript">
	  function delapp(dataid, datatype) {
		if(confirm("删除后连同栏目下该应用的信息一并删除，确定继续？")) {
		       
			   $.ajax({
					   type: "post",
					   url: "${ctx}/datasource/delappresource.do?csrfToken=<%=TokenMananger.getTokenFromSession(session)%>",
					   data: {"dataid":dataid,"datatype":datatype},
					   success: function(rs){
					     if(rs.status=='1') {
					    	 alert(rs.msg); 
					    	 window.location.href="${ctx }/datasource/lists.do?module=${datatype}";
					     }else{
					    	 alert(rs.msg); return false;
					     }
					   }
			  });
		}
	  }
	  $(function(){
	    
	    $("#inpselall").change(function() {
			if (this.checked) { // 全选
			   $("input[name='checkbox']").attr("checked", true);
			} else { // 取消全选
			   $("input[name='checkbox']").attr("checked", false);
			}
	    });
        
        $(".publish").click(function(){
            var $tbmain = $("#tbmain");
			var pubid=$(this).data("id");
			if(pubid == "all"){
				pubid = "";
				$tbmain.find(":checkbox:checked").each(function(){
					pubid += ","+this.value;
				});
				if(pubid){
					pubid = pubid.substr(1);
				}else{
					alert('请至少选择一条数据。');
					return false;
				}	
			}
			window.location.href="${ctx}/datasource/topublish.do?type=${datatype }&ids="+pubid;
		});
	  });
	  	function checkmysearch() {
			var keywd = $("#keywd").val();
			var keytype = $("#stype").val();
			if(keywd && (keytype == "dataid" || keytype=="contentid")) {
				var Reg = new RegExp("^[0-9]{1,12}$");
				var keyarr = keywd.split(",");
				if(keyarr.length > 20) {
					alert("不能超过20个ID");
					return false;
				}
				var idstr = "";
				$.each(keyarr, function(i, n) {
					if(!Reg.test(n)) {
						idstr += " [" + n.toString() + "] ";
					}		
				})
				if(idstr) {
					alert("ID串： " + idstr + " 格式或者长度不正确");	
					return false;
				}
			}
			return true;
		}
  </script>
  
  </body>
</html>
