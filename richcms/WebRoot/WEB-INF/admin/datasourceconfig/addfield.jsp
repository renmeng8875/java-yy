<%@include file="../public/header.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>测试新增字段</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>

  </head>
  
  <body>
     <div class="main_border" style="border:0;"><div id="my_main">
<div class="pad10">
        <form action="${ctx }/dataconfig/addfield.do" method="post" id="form1">
           <input type="hidden" name="csrfToken" value="${csrfToken}"/>
           <table style="font-size:12px;color:#444;line-height:32px;">
              <tr><td width="80">数据源表:</td><td><select style="width:200px;" name="formid" disabled="disabled">
                                       <c:forEach items="${formlist }" var="form">
                                         <option <c:if test="${form.formid==formid }">selected="selected"</c:if> value="${form.formid }">${form.formname }</option>
                                       </c:forEach>
                                       <input type="hidden" name="formid" value="${formid }"/>
                                     </select></td></tr>
              <tr><td width="80">字段名称:</td>
                  <td>
                  <c:choose>
                    <c:when test="${field!=null }"><input style="width:200px;" datatype="zhs2-20" nullmsg="请输入字段名称" errormsg="字段名称必须是以字母开头的2~20为字符" sucmsg="验证通过!" type="text"  name="fieldname" value="${field.fieldname }"></c:when>
                    <c:otherwise><input style="width:200px;" datatype="zhs2-20" nullmsg="请输入字段名称" errormsg="字段名称必须是以字母开头的2~20为字符" sucmsg="验证通过!" ajaxurl="${ctx}/dataconfig/checkFieldName.do?csrfToken=${csrfToken}&formid=${formid }" type="text"  name="fieldname" value="${field.fieldname }"></c:otherwise>
                  </c:choose>
                  </td>
                  <td><div class="Validform_checktip"></div></td>
              </tr>
              <tr><td width="80">字段类型:</td><td><select name="fieldtype" style="width:200px;">
                                     <option value="nvarchar2">nvarchar2</option>
                                     <option value="number">number</option>
                                     <option value="clob">clob</option>
                                     <option value="nclob">nclob</option>
              						</select></td></tr>
              <tr><td width="80">字段长度:</td>
                  <td><input style="width:200px;" datatype="ns1-20" nullmsg="请输入字段长度" errormsg="字段长度范围为1~9999" type="text" name="fieldattr" value="${field.fieldattr }"></td>
                  <td><div class="Validform_checktip"></div></td>
              </tr>
              <tr><td width="80">字段验证:</td>
                  <td><textarea style="width:200px;"  type="text" name="fieldcheck" rows="4" cols="50">${field.fieldcheck }</textarea></td>
                  <td><div class="Validform_checktip"></div></td>
              </tr>
              <tr><td width="80">字段验证提示语:</td>
                  <td><input style="width:200px;"  type="text" name="checktips" value="${field.checktips }"></td>
                  <td><div class="Validform_checktip"></div></td>
              </tr>
              
              <tr><td width="80">显示名称:</td>
                  <td><input style="width:200px;" datatype="s2-20" nullmsg="请输入字段显示名称" errormsg="字段显示名称长度为2~20位字符" type="text" name="labelname" value="${field.labelname }"></td>
                  <td><div class="Validform_checktip"></div></td>
              </tr>
              <tr><td width="80">表现形式:</td><td><select id="htmltype" name="htmltype" style="width:200px;">
                                     <option <c:if test="${field.htmltype==1 }">selected="selected" </c:if> value="1">单行文本</option>
                                     <option <c:if test="${field.htmltype==2 }">selected="selected" </c:if> value="2">多行文本</option>
                                     <option <c:if test="${field.htmltype==3 }">selected="selected" </c:if> value="3">大文本</option>
                                     <option <c:if test="${field.htmltype==4 }">selected="selected" </c:if> value="4">附件</option>
                                     <option <c:if test="${field.htmltype==5 }">selected="selected" </c:if> value="5">图片</option>
                                     <option <c:if test="${field.htmltype==6 }">selected="selected" </c:if> value="6">时间</option>
                                     <option <c:if test="${field.htmltype==7 }">selected="selected" </c:if> value="7">选择项</option>
              						</select></td></tr>
              <tr id="choicetr" 
              <c:choose>
                <c:when test="${field.htmltype==7 }"> style="display:block;"</c:when>
                <c:otherwise>style="display:none;"</c:otherwise>
              </c:choose>>
                 <td width="80">选择对象</td>
                 <td><select name="choiceid" style="width:200px;">
                     <option value="0">选择项列表</option>
                     <c:forEach items="${choicelist }" var="choice">
                     	<option <c:if test="${choice.choiceid eq field.choiceid }">selected="selected" </c:if>value="${choice.choiceid }">${choice.choicename }</option>
                     </c:forEach>
                 </select></td>
              </tr>
               <tr><td width="80">显     示 :</td><td>
                <c:choose>
                      <c:when test="${field!=null }">
                         <input type="checkbox" value="1" <c:if test="${fn:indexOf(field.status, '1')>=0}">checked="checked"</c:if> name="status">列表显示   &nbsp;&nbsp;  
                  		 <input type="checkbox" value="2" <c:if test="${fn:indexOf(field.status, '2')>=0}">checked="checked"</c:if> name="status">明细显示   &nbsp;&nbsp;
                         <input type="checkbox" value="3" <c:if test="${fn:indexOf(field.status, '3')>=0}">checked="checked"</c:if> name="status">搜索显示
                      </c:when>
                      <c:otherwise>
                         <input type="checkbox" value="1"  name="status">列表显示   &nbsp;&nbsp;  
                  		 <input type="checkbox" value="2" checked="checked" name="status">明细显示   &nbsp;&nbsp;
                         <input type="checkbox" value="3" name="status">搜索显示
                      </c:otherwise>
                </c:choose>
               </td></tr>
             
               <c:if test="${formtype=='base' }">
               <tr><td width="110">是否推送到数据源 :</td>
                   <td>
                    <c:choose>
                      <c:when test="${field!=null }">
                           <input type="radio" value="1" <c:if test="${field.isedit==1 }">checked="checked"</c:if> name="ispush">是  &nbsp;&nbsp;  
                           <input type="radio" value="0" <c:if test="${field.isedit==0 }">checked="checked"</c:if> name="ispush">否
                      </c:when>
                      <c:otherwise>
                           <input type="radio" value="1" name="ispush">是  &nbsp;&nbsp;  
                           <input type="radio" value="0" checked="checked" name="ispush">否
                      </c:otherwise>
                    </c:choose>
                   </td>
               </tr>
               </c:if>
               
               <tr>
                 <td width="80">是否可编辑 :</td>
                 <td>
                  <c:choose>
                      <c:when test="${field!=null }">
                         <input type="radio" name="isedit" value="0" <c:if test="${field.isedit==0 }"> checked="checked" </c:if>>否 &nbsp;&nbsp;
                  		 <input type="radio" name="isedit" value="1" <c:if test="${field.isedit==1 }"> checked="checked" </c:if>>是</td>
                      </c:when>
                      <c:otherwise>
                         <input type="radio" name="isedit" value="0" >否 &nbsp;&nbsp;
                   	     <input type="radio" name="isedit" value="1" checked="checked"> 是</td>
                      </c:otherwise>
                  </c:choose>
                    
               </tr>
               <tr>
                 <td width="80">是否必填 :</td>
                 <td>
                   <c:choose>
                      <c:when test="${field!=null }">
                         <input type="radio" value="0" <c:if test="${field.isrequired==0 }"> checked="checked" </c:if> name="isrequired">否 &nbsp;&nbsp;
                  		 <input type="radio" name="isrequired" <c:if test="${field.isrequired==1 }"> checked="checked" </c:if> value="1"> 是 
                      </c:when>
                      <c:otherwise>
                         <input type="radio" value="0" name="isrequired">否 &nbsp;&nbsp;
                   	     <input type="radio" name="isrequired" checked="checked" value="1"> 是
                      </c:otherwise>
                  </c:choose>
                 </td> 
               </tr>
               
               <tr><td width="80">显示顺序:</td>
                  <td><input style="width:200px;" datatype="ns1-99" nullmsg="请输入字段显示顺序" errormsg="顺序为1～99数字" type="text" name="fieldorder" value="${field.fieldorder }"></td>
                  <td><div class="Validform_checktip"></div></td>
              </tr>
              
               <tr>
				    <td colspan="2" align="center">
				     <c:if test="${formtype=='extends' }">
				       <c:if test="${field.ispush==null }">
	                   	 <input name="ispush" type="hidden" value="0">
				       </c:if>
				       <c:if test="${field.ispush!=null }">
	                   	 <input name="ispush" type="hidden" value="${field.ispush }">
				       </c:if>
	                </c:if>
						<div class="adm-btn-big"><input id="btnSub" type="submit" value="保&nbsp;&nbsp;存" /></div>
					</td>
			  </tr>                   
           </table>
           </form>
     </div>
     </div>
     </div>
     
     <script>
		$(function(){
			 $("#form1").Validform({
				tiptype:2,
				datatype:{
							"zhs2-20":/^[a-zA-Z]{1}[a-zA-Z0-9_-]{1,19}$/,
							"ns1-20":/^[1-9]\d{0,3}$/,
							"ns1-99":/^[1-9]\d{0,1}$/
						}
			});
			
			$("#htmltype").change(function(){
			   if($(this).val()==7){
			      $("#choicetr").show();
			   }else{
			      $("#choicetr").hide();
			   }
			});
			
		});
    </script>
  </body>
</html>
