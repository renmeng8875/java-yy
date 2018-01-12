<%@include file="../public/header.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>数据模板页面</title>

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
<link href="${ctx}/static/admin/css/alert.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/js/kindeditor/themes/default/default.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/json2.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ArrayDistinct.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>

<style>
.adm-table td input{
  width:480px;
  height: 20px;
  text-align:left;
  line-height: 20px;
  padding: 0 5px;
}
.adm-table td{
	text-align:left;
}
.adm-text span {
    height: 17px;
    line-height: 24px;
}

#dragarea {
width:600px;
border: 1px solid #ffffff;
overflow: hidden;
}
#dragarea li input{
  width: 20px;
}
#dragarea li{
  margin-right: 20px;
}
.l-text{
 width:150px;
}

.display-image{height: 145px;width: 104px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
.display-image-check{height: 145px;width: 104px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
</style>

<script>
$(function(){
	KindEditor.create('textarea[id^="ta_"]', 
		{
			uploadJson      : '${ctx}/fileupload/kindeditor.do?region=${datatype}&csrfToken=${csrfToken}',
			fileManagerJson : '${ctx}/fileupload/fileHandler.do?region=${datatype}&csrfToken=${csrfToken}',
			allowFileManager : true,
			mode:'absolute',
			width:'660px',
			height : '200px', 
			afterCreate : function() 
			{
				var self = this;
				KindEditor.ctrl(document, 13, function() {
					self.sync();
					document.forms['datasourceeditform'].submit();
				});
				KindEditor.ctrl(self.edit.doc, 13, function() {
					self.sync();
					document.forms['datasourceeditform'].submit();
				});
			}
		});
		

		 
		$("div[wrap='imageContainer']").delegate("[attr='displayImage']",{
			mouseover:function() {
				$(this).removeClass("display-image").addClass("display-image-check");
				$(".l-icon-delete",$(this)).css("display","block");
			},
			mouseout:function() {
				$(this).removeClass("display-image-check").addClass("display-image");
				$(".l-icon-delete",$(this)).css("display","none");
			}
	    });
		
		$("div[wrap='imageContainer']").delegate(".l-icon-delete",{click:function(){
			var imageName = $(this).parent().attr("dname");
			var divObj = $(this).parent();
			var inputObj = $(this).parent().siblings('input[value="'+imageName+'"]');
			 
		    $.ajax({
              type: "post",
		      url: "${ctx}/fileupload/deleteImg.do?csrfToken=${csrfToken}",
		      data:{"pathimg":imageName},
		      success: function(rs){
		        if(rs.status=="1"){
		          divObj.remove();
			      inputObj.remove();
		        }else{
		          alert("删除失败");
		        }
		      }
            });
			
	    }});
  
      
});

</script>
<style type="text/css">
.required{color:red;}
</style>
</head>

<body>
	<h2 class="adm-title">
		<a class="title">数据源管理 - 修改数据栏目</a><a class="fr btn"
			href="javascript:;" onclick="history.back(-1);">+管理数据</a>
	</h2>
	<form id="datasourceeditform" name="datasourceeditform" method="post" enctype="multipart/form-data" action="${ctx}/datasource/add.do?csrfToken=${csrfToken}" onsubmit="return checksubmit()" method="post">
	<input type="hidden" name="csrfToken" value="${csrfToken}"/>
	<div class="pad10" id="new_mark">
		<div class="adm-text ul-float">
			<table class="adm-table" width="100%">
				<thead>
					<tr class="tr1">
						<td width="25%" style="height:0px;"></td>
						<td width="75%" style="height:0px;"></td>
					</tr>
				</thead>
				
				<tbody id="tbmain">
				 <c:forEach items="${fieldvalue.fieldlist }" var="v">
				    <tr>
						<td width="25%" style="height:0px;"><c:if test="${v.isrequired==1 }"><span data-fid="${v.fieldid }" class="required">*</span></c:if>${v.labelname }</td>
						<td width="75%" style="height:0px;">
							<c:choose>
							<c:when test="${v.htmltype==1 }">
							   <c:if test="${v.isedit==1 }"><input type="text" id="${v.fieldid }" onblur="validField(this.value,'${v.fieldcheck }','${v.fieldid }')" name="${v.fieldname }" value="${v.fieldvalue }"> 
							  		 <c:if test="${v.fieldcheck!='' }"><div class="Validform_checktip" id="checktip_${v.fieldid }" style="display: none;color:red;">${v.checktips }</div></c:if>
							   </c:if>
							   <c:if test="${v.isedit==0 }">${v.fieldvalue }</c:if>
							</c:when>
							<c:when test="${v.htmltype==2 }">
								<c:if test="${v.isedit==1 }">
								<textarea name="${v.fieldname }" id="${v.fieldid }"  rows="3" cols="80" style="margin:0px;">${v.fieldvalue }</textarea>
								</c:if>
							    <c:if test="${v.isedit==0 }">${v.fieldvalue }</c:if>
							</c:when>
							<c:when test="${v.htmltype==3 }">
							    <c:if test="${v.isedit==1 }">
							     <textarea name="${v.fieldname }" id ="ta_${v.fieldid }" >${v.fieldvalue }</textarea>
							    </c:if>
							    <c:if test="${v.isedit==0 }">${v.fieldvalue }</c:if>
							</c:when>
							<c:when test="${v.htmltype==4 }"></c:when>
							<c:when test="${v.htmltype==5 }">
							    <c:if test="${v.isedit==1 }">
							    	<div wrap="imageContainer" id="container_${v.fieldid }" style="height: 150px;width: 660px;overflow: hidden;padding: 2px;border:2px solid gray;margin: 2px;margin-bottom: 4px;"></div>
							        <input id="jsonimg_${v.fieldid }"  type="file" />
							     
							        <span id="usernameTip"></span>
							        <input type="hidden" id="img_${v.fieldid }" name="${v.fieldname }_jsonimg" value="" />
							        <script>
							        
							          $(function(){
							                var jsessionid = "<%=session.getId()%>";
  
						             	    $('#jsonimg_${v.fieldid }').uploadify({
										           'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
												   'uploader'  : '${ctx}/fileupload/uploadify.do?region=${datatype}&csrfToken=${csrfToken}',
												   'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
											       'fileTypeExts'  : '*.png; *.jpg; *.gif; *.jpeg; *.bmp', 
												   'auto'      : true,
												   'buttonText': '批量上传',
												   'removeTimeout':0,
												   'onUploadStart':function(item){
											        	var container = $("#container_${v.fieldid }");
											        	var children = $("div[class='display-image']",container);
											        	if(children&&children.length>=6)
											        	{
											        		alert("最多只能上传6张图片！");
															$('#jsonimg_${v.fieldid }').uploadify('cancel','*');	
											        	}
												    },
												   'onUploadSuccess':function(file, data, response){
											        	var container = $("#container_${v.fieldid }");
											        	var jsonObj = JSON2.parse(data);
											        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute; margin:0;" href="<c:url value="/upload/"/>'+jsonObj.imageName+'" target="blank"><img width="104px" height="145px" src="<c:url value="/upload/"/>'+jsonObj.imageName+'"/></a><div style="position: absolute; margin-left:82px;display:none;cursor:hand;" class="l-icon l-icon-delete" ></div></div>');
											        	container.append('<input type="hidden" id="img_${v.fieldid }" name="${v.fieldname }_jsonimg" value="'+jsonObj.imageName+'" />');
											       }
											});
											var storageUrl='<c:url value="/upload/"/>';
										 
											var imgstr = '${v.fieldvalue}';
											if(imgstr!=''&&imgstr!='null') {
												var imgs = JSON2.parse(imgstr);
												for(var i=1;i<7 ;i++) {
													if(imgs[i]) {
														var item=imgs[i];
														var path= storageUrl+item.path;
														var imagePath=item.path;
														$("#container_${v.fieldid }").append('<div class="display-image" attr="displayImage" dname="'+imagePath+'" title="点击显示原图"><input type="hidden" name="${v.fieldname }_jsonimg" id="img_${v.fieldid }" value="'+imagePath+'" /><a style="position: absolute;margin:0;" href="'+path+'" target="blank"><img width="104px" height="145px" src="'+path+'"/></a><div style="position: absolute; margin-left:84px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
													}
												}
											}
							          });
							        </script>
							    </c:if>
							    <c:if test="${v.isedit==0 }">
							    <ul id="img_${v.fieldid }" class="previewpop" style="list-style:none;"> </ul>
							      <script>
							        $(function(){
							           var imgstr = '${v.fieldvalue}';
							       	   if(imgstr!=''&&imgstr!='null') {
											var datatype = '${datatype}';
											var width = 90;
											var storageUrl='${storageUrl}';
											var imgs = JSON2.parse(imgstr);
											for(var i=1;i<7 ;i++) {
												if(imgs[i]) {
													var item=imgs[i];
													if(datatype=="ios"){
														var path= storageUrl+item.path;
													}else{
														var path= item.path;
													}
													var html = "<li style='display:inline;height:90px;margin-right: 20px;' id='li_${v.fieldid}'><img src='"+path+"' title='"+item.title+"' width='110' alt='"+item.alt+"'></li>";
													$("#img_${v.fieldid }").append(html);
												}
											}
										}
							        });
							     </script>
							    </c:if>
							    
							</c:when>
							<c:when test="${v.htmltype==6 }">
								<c:if test="${v.isedit==1 }">
								<input style="width:150px;" type="text" id="time_${v.fieldid }" name="t_${v.fieldname }" readonly="readonly" value="${v.fieldvalue }"/>
								<script>
								  $(function(){
								    var startTime = $("#time_${v.fieldid}").ligerDateEditor({
							    	   format: "yyyy-MM-dd hh:mm:ss",
							           labelWidth: 80,
							           labelAlign: 'left',
							           cancelable : false,
							           showTime:true,
							           onAfterSelect:function(){
							    			$("#time_${v.fieldid}").removeClass("Validform_error");
							           } 
								    });
								  });
								</script>
								</c:if>
								<c:if test="${v.isedit==0 }">${v.fieldvalue }</c:if>
							</c:when>
							<c:when test="${v.htmltype==7 }">
							   <c:if test="${v.isedit==1 }">
							       <c:set value="${fn:indexOf(v.fieldvalue, ';')}" var="index" />
							       <c:set value="${fn:length(v.fieldvalue)}" var="length" />
							       <c:set var="name" value="${fn:substring(v.fieldvalue, 0, index)}" />
							       <c:set var="ids" value="${fn:substring(v.fieldvalue, index+1,length)}" />
						  	       <input type="text" readonly="readonly" name="v_${v.fieldname }" id="v_${v.fieldid }" value="${name }"> 
							   	   <input type="hidden" name="${v.fieldname }_choice" id="${v.fieldid }" value="${ids}">
								   <a style="cursor: pointer;" onclick="javascript:choiceDialog(${v.choiceid},${v.fieldid });">选择</a>
							   </c:if>
							   <c:if test="${v.isedit==0 }">${v.fieldvalue }</c:if>
							</c:when>
		               
							<c:otherwise></c:otherwise>
							</c:choose>
						</td>
					 
					</tr>
					
				 </c:forEach>
				    <tr>
						<td width="25%" style="height:0px;">上线时间</td>
						<td width="75%" style="height:0px;">${fieldvalue.ctime }</td>
					</tr>
					 <tr>
						<td width="25%" style="height:0px;">更新时间</td>
						<td width="75%" style="height:0px;">${fieldvalue.utime }</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!--弹框样式开始-->
	    <div class="alertbg" style="display:none;">&nbsp;</div>
	    <div class="alert" style="display:none;">
	    	<div class="title">
	        	<h2>添加标签</h2>
	            <span>X</span>
	        </div>
	        <div class="cont">
	        	<ul id="choicelist">
	        	   <li>游戏</li>
	        	</ul>
	        </div>
	        <div class="btn">
	        	<input type="button" onclick="javascript:closeAlert(this)" value="确   &nbsp;&nbsp;&nbsp;定">
	        </div>
	    </div>
	    <!--弹框样式结束-->
	</div>
	<div class="adm-btn-big">
	  	 <input type="hidden" name="dataid" value="${dataid }">
	  	 <input type="hidden" name="formid" value="${formid }">
	  	 <input type="hidden" name="datatype" value="${datatype }">
	     <input type="submit" value="修&nbsp;&nbsp;改" />
    </div> 
    </form>
    <script type="text/javascript">
    
	
       //验证英文名称
       function validField(value,regstr,fieldid){
		  var reg = eval(regstr);//转成正则
		  if(value.match(reg)!=null){   
		    //存在不是字母和下滑线的字符
		     $("#checktip_"+fieldid).hide();
		  }else{
		     $("#checktip_"+fieldid).css("display","inline");
		  }
	   }
	   
       function inArray(stringToSearch, arrayToSearch) {
			for (s = 0; s < arrayToSearch.length; s++) {
				thisEntry = arrayToSearch[s].toString();
				if (thisEntry == stringToSearch) {
					return true;
				}
			}
			return false;
	   };
       var fieldid = '';
       var choiceid = '';
       function choiceDialog(id,inputid){
           fieldid = inputid;
           choiceid = id;
           $.ajax({
              type: "post",
		      url: "${ctx}/choiceconfig/getChoiceListById.do?csrfToken=${csrfToken}",
		      data: {"choiceid":choiceid},
		      success: function(rs){
		         if(rs.status=='0'){
		            var html = '';
		            var ids = $("#"+fieldid).val();
		            var vs = "";
		            if(ids==""){
		               vs = new Array();
		            }else{
		               vs = ids.split("&")[0].split(",");
		            }
		            $.each(rs.choicelist,function(i,item){
		                if(inArray(item.choiceid, vs)){
		                	html+='<li id='+item.choiceid+' style="width:152px;height:25px;"><input type="checkbox" checked="checked" value='+item.choiceid+','+item.choicename+' name="checkbox"/>'+item.choicename+'</li>';
		                }else{
		                    html+='<li id='+item.choiceid+' style="width:152px;height:25px;"><input type="checkbox" value='+item.choiceid+','+item.choicename+' name="checkbox"/>'+item.choicename+'</li>';
		                }
		            });
		            $("#choicelist").html(html);
		         }
		      }
           });
 		   $('.alert').show();
 		   $('#new_mark .alertbg').show();
       }
        //弹框关闭按钮
	   $('#new_mark .alert .title span').click(function(){
		   $(this).parents('.alert').hide();
		   $('#new_mark .alertbg').hide();
	   });
	   
	   function closeAlert() {
	   	   var ids = new Array();
	   	   var names = new Array();
	       $("input[name='checkbox']").each(function(){
	   			if(this.checked){
	   			   var cv = $(this).val();
	   			   ids.push(cv.split(",")[0]);
	   			   names.push(cv.split(",")[1]);
	   			}
    	   });
    	   if(ids.length>10){
    	      alert("标签最多只能选择十个");
    	      return false;
    	   }
    	   $("#"+fieldid).val(ids.join(",")+"&"+choiceid+","+fieldid);
    	   $("#v_"+fieldid).val(names.join(","));
		   $('.alert').hide();
		   $('#new_mark .alertbg').hide();
	   }
	   
	   function checksubmit(){
	       var flag = true;
	       var valid = true;
	       $(".required").each(function(i,item){
	            var id = $(item).attr("data-fid");
	            if($("#"+id).val()==""||$("#img_"+id).val()==""||$("#v_"+id).val()==""||$("#time_"+id).val()==""){
	               flag =  false;
	            }
	       });
	       if(!flag){
	           alert("带 * 的部分请填写完整");
	       }
	       $(".Validform_checktip").each(function(i,item){
	           if($(item).is(":visible")){
	               valid =  false;
	           }
	       });
	       if(!valid){
	           alert("表单验证不通过，请重新输入");
	       }
	       return flag&&valid;
	   }
    </script>
</body>
</html>
