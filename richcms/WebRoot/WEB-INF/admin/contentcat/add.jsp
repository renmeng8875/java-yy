<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="keywords" content=" " />
		<meta name="description" content="" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">  
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>栏目管理 - <c:choose><c:when test="${info.catId!=null}">修改</c:when><c:otherwise>添加</c:otherwise></c:choose>栏目</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
		
		<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/json2.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/base.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerComboBox.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"></script>
		<script src="${ctx}/static/admin/js/TreeSelector.js" type="text/javascript"></script>
		<style type="text/css">
			.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
			.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
			strong{font-style:normal;font-weight:bold;} 
		</style>
		<script type="text/javascript">
			var startTime;
			var endTime; 
			function _escape(val) {
				return val.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
			}
			function candisp() {
				var selectCagtogyText=$("#selectCagtogyid").find("option:checked").text();
				var str = "cloudchen";
				var c = selectCagtogyText.split("|");
				var catLevel=c?c.length:0;

				if(catLevel >=4){
					$("#candispli").show();
					if($("#catidinput").val() && $("#catidinput").val() != "undefinded") {
						var flag = ($("#oldhidden").val()==1) ? false : true;
						$("#ishidden0").attr("checked", flag);
						$("#ishidden1").attr("checked", !flag);
					} else {
						$("#ishidden0").attr("checked", false);
						$("#ishidden1").attr("checked", true);
					}
			
				} else {
					$("#candispli").hide();
					$("#ishidden0").attr("checked", true);
					$("#ishidden1").attr("checked", false);
				}
				
			}
			
			function loadStyle()
			{
				try{
					$.ajax({
					   type: "POST",
					   url: "${ctx}/ContentCat/styleList.do",
					   data:'csrfToken=${csrfToken}',
					   dataType:"json",
					   async:false,
					   success: function(data){
					   		var styleList=data.styleList;
					   		if(styleList && styleList.length>0) {
					   			 $("#mystyle").empty();
					   			 var styleId="";
					   			 $("#mystyle").append("<option styleid='' value='' selected>--请选择--</option>");
	                    	     $.each(styleList, function(i, item) {
	                    	          if('${info.style}'==item.style){
	                    	          		styleId=item.styleid;
	                    	          		$("#mystyle").append("<option styleid='" + styleId+ "' value='" + item.style+ "' selected>" +item.stylename+ "</option>");
	                    	          }else{
	                    	          		$("#mystyle").append("<option styleid='" + item.styleid+ "' value='" + item.style+ "'>" +item.stylename+ "</option>");
	                    	          }
	                             });
	                             loadTpl('${info.style}'==''?styleList[0].styleid:styleId);
					   		}else{
					   			$("#mystyle").empty();
					   			$("#mystyle").append("<option styleid='' value='' selected>--请选择--</option>");
					   		} 
					   }
				 	});
				}catch(e){
					$("#mystyle").empty();
					$("#mystyle").append("<option styleid='' value='' selected>--请选择--</option>");
				}
			}
			
			function loadTpl(v)
			{
				var styleId=v==''?$("#mystyle").find("option:selected").attr("styleid"):v;
				try{
					$.ajax({
					   type: "POST",
					   url: "${ctx}/ContentCat/tplList.do",
					   data:'styleId='+styleId+"&csrfToken=${csrfToken}",
					   dataType:"json",
					   async:false,
					   success: function(data){
					   		var tplList=data.tplList;
					   		if(tplList && tplList.length>0) {
					   			 $("#tpl").empty();
					   			 $("#tpl").append("<option value=''>无</option");
	
	                    	     $.each(tplList, function(i, item) {
		                    	        var paths={};
		                    	        if(item.listPath!=''){
		                    	             paths=item.listPath;
		                    	        }else{
		                    	        	 paths=item.indexPath;
		                    	        }
		                    	        for(var k in paths){
		                    	            var tplPath='${info.tplpath}';
		                    	            if(tplPath==paths[k]){
		                    	            	$("#tpl").append("<option value='" + paths[k]+ "' selected>" +k+ "</option>");
		                    	            }else{
		                    	            	$("#tpl").append("<option value='" + paths[k]+ "'>" +k+ "</option>");
		                    	            }
		                    	        	
		                    	        }
	                             });
					   		}else{
					   			$("#tpl").empty();
					   			$("#tpl").append("<option value=''>无</option>");
					   		} 
					   }
				 	});
				}catch(e){
					$("#tpl").empty();
					$("#tpl").append("<option value=''>无</option>");
				}
			}
			
			$(function(){
				var treedata = ${treeData};
				var settings = {selectName:"pid",
								selectId:"selectCagtogyid",
								data:treedata,
								containerId:"pid",
								selectLevel:"catLevel",
								optionValue:"catId",
								subSet:"children",
								selectedId :'${pid}',
								onChange:candisp,
								optionText:"name"};
				var treeSelector = new TreeSelector(settings);
				loadStyle();
			});
			
			$(function(){
					startTime=$("#sTime").ligerDateEditor(
				    {
				           label:'',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 0,
				           labelAlign: 'left',
				           cancelable : false,
				           showTime:true,
				           onAfterSelect:function()
				           {
				    			$("#sTime").removeClass("Validform_error");
				    			$("#eTime").removeClass("Validform_error");
				    			$("#activityTimeTip").css("display","none");
				           } 
				    });
					endTime =$("#eTime").ligerDateEditor(
				    {
				           label: '~',
				    	   format: "yyyy-MM-dd",
				           labelWidth: 5,
				           labelAlign: 'center',
				           cancelable : false,
				           showTime:true,
				           onAfterSelect:function()
				           {
				    			$("#eTime").removeClass("Validform_error");
				    			$("#sTime").removeClass("Validform_error");
				    			$("#activityTimeTip").css("display","none");
				    			if(startTime.getValue()){
				    				if(startTime.getValue().getTime()>endTime.getValue().getTime())
				    				{
				    					alert("开始时间不能大于结束时间!");
				    					$("#eTime").addClass("Validform_error");
				    					endTime.setValue(null);
				    					return false;
				    				}
				    			}
				    			if(startTime.getValue()==null)
				    			{
				    				alert("请先选择开始时间！");
				    				endTime.setValue(null);
				    				$("#sTime").addClass("Validform_error");
				    				return false;
				    			}
				           } 
				    });
				    
				    if(!$("#licatdesc").is(":hidden")){
							$('#catDesc').bind('click', function() {
  									$('#catDesc').removeClass("Validform_error");
									$("#catDescTip").css("display","none");
							});
							$('#catDesc').bind('focus', function() {
  									$('#catDesc').removeClass("Validform_error");
									$("#catDescTip").css("display","none");
							});
					}
				    
		            $("#fm_es").Validform({
						tiptype:3,
						label:".label",
						showAllError:false,
						datatype:{
							"zh2-50":/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,50}$/,
							"zhs2-50":/^[a-z]{1}[a-z0-9]{1,49}$/
						},
						beforeSubmit:function(){
						
							var startDate = startTime.getValue(),endDate = endTime.getValue();
							var tip = $("#activityTimeTip"),startInput = $("#sTime"),endInput = $("#eTime");
							
							if(startInput.val()!='' && endInput.val()!='' && startDate.getTime()>endDate.getTime())
							{
								$("#activityTimeTip").css("display","inline");
								startInput.addClass("Validform_error");
								endInput.addClass("Validform_error");
								return false;
							}
							
							var container = $("#container2"),children=$(".display-image",container);
							for(var i=0;i<children.length;i++)
							{
								var inputobj = $("input",$(children[i]));
								var imageProperties = inputobj.val();
								
							}
							
							if(!$("#candispli").is(":hidden")){
								var isHiddenFieldVal=$("input:radio[name='isHiddenRadio']:checked").val()
								$("#isHidden").val(isHiddenFieldVal);
							}


							var imageJson={};
							$("input[name='jsonimg']").each(function(i,item){
								var index=i+1;
								imageJson[""+index+""]={"path":""+item.value+"","alt":"","size":$(this).attr("imagePx")};
							});
							var imageText=JSON.stringify(imageJson);
							$("#image").val(_escape(imageText));
							
							if(!$("#licatdesc").is(":hidden")){
								var catdescInput=$('#catDesc');
								if(catdescInput.val()==null || catdescInput.val()==''){
									catdescInput.addClass("Validform_error");
									$("#catDescTip").html("专题导语不能为空!");
									$("#catDescTip").css("display","inline");
									return false;
								}else{
									$("#catDescTip").css("display","none");
								}
								if(catdescInput.val().length>50){
									catdescInput.addClass("Validform_error");
									$("#catDescTip").html("专题导语长度最长50位字符!");
									$("#catDescTip").css("display","inline");
									return false;
								}else{
									$("#catDescTip").css("display","none");
								}
							}
							return true;
						}
						
					});
					
					$('#mystyle').change(function(){
						var $this=$(this),$licatdesc = $('#licatdesc');
						if($this.val()=='topic'){
							$licatdesc.show();
						}else{
							$licatdesc.hide();
						}
					});		
			});
			
			$(function(){
				candisp();
	
				$("#temporjump1").click(function(){
					$("#templateset1").css('display','');
					$("#templateset11").css('display','');
					$("#templateset2").css('display','none');
					if($("#mystyle").val()=='topic'){$("#licatdesc").css('display','');}
				});
				$("#temporjump2").click(function(){
					$("#templateset2").css('display','');
					$("#templateset1").css('display','none');
					$("#templateset11").css('display','none');
					$("#licatdesc").css('display','none');
				});			
				
				$('.catuploadimg').click(function(){
					var randnumber = parseInt(Math.random()*(9999-1+1)+1); 
					uploadDialog('/multi_upload/uploadimg/dataid/'+randnumber+'/datatype/catimagepath', 'getimgpath()');//上传多张图片
				});	
				
				var contextPath='<c:url value="/upload/"/>';
				var imgs ='${imageJson}';
				if(imgs!=''){
					var imageJson=JSON.parse('${imageJson}');
					
					$.each(imageJson,function(i,v){
						var path=v.path;
						var imageRealUrl=contextPath+path;
						$("#container2").append('<div class="display-image" attr="displayImage" dname="'+path+'" title="点击显示原图"><a style="position: absolute;" href="'+imageRealUrl+'" target="blank"><img width="120px" height="120px" src="'+imageRealUrl+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
			        	$("#container2").append('<input type="hidden" name="jsonimg" value="'+path+'" />');	
					});				
					
				}
				$('#jsonimg').uploadify({
				    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
				    'uploader'  : '${ctx}/fileupload/uploadify.do?region=catmanager&csrfToken=${csrfToken}',
				    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
			        'fileTypeExts'  : '*.png; *.jpg; *.gif', 
				    'auto'      : true,
				    'fileSizeLimit':'5MB',
				    'buttonText': '多张上传',
				    'removeTimeout':0,
				    'onUploadStart':function(item){
				        	var container = $("#container2");
				        	var children = $("div[class='display-image']",container);
				        	if(children&&children.length>=3)
				        	{
				        		alert("最多只能上传3张图片！");
								$('#jsonimg').uploadify('cancel','*');	
				
				        	}
	    			},
				    'onUploadSuccess':function(file, data, response)
				    {
			        	var container = $("#container2");
			        	var jsonObj = JSON2.parse(data);
			 
			        	var imagePropertyis=(jsonObj.imagePx!="")?{"path":jsonObj.url,"alt":"","size":jsonObj.imagePx}:{"path":jsonObj.url,"alt":""};
			        	
			        	container.append('<div class="display-image" attr="displayImage" dname="'+jsonObj.imageName+'" title="点击显示原图"><a style="position: absolute;" href="'+jsonObj.url+'" target="blank"><img width="120px" height="120px" src="'+jsonObj.url+'"/></a><div style="position: absolute; margin-left:104px;display:none;cursor:hand;" class="l-icon l-icon-delete"></div></div>');
			        	container.append('<input type="hidden" name="jsonimg" value="'+jsonObj.imageName+'" imagePx="'+jsonObj.imagePx+'"/>');
				    }
	  			});
	  			
	  			$("div[wrap='imageContainer']").delegate("[attr='displayImage']",
				{
					mouseover:function()
					{
						$(this).removeClass("display-image").addClass("display-image-check");
						$(".l-icon-delete",$(this)).css("display","block");
					},
					mouseout:function()
					{
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
				
				$('#jsonimg').css("margin-left","120px");
			});

	function getimgpath(){
		//$('#inp_'+id).data('img',$("#upimglist").val());
		var upimglist = $("#upimglist").val();
		$("#uploadcatimgjson").val(upimglist);
		$("#upimglist").val('');	
		
	}
			
			
		</script>

	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">栏目管理 - <c:choose><c:when test="${info.catId!=null}">修改</c:when><c:otherwise>添加</c:otherwise></c:choose>栏目</a>
			<a class="fr btn" href="${ctx}/ContentCat/list.do">+管理栏目</a>
		</h2>
		<form id="fm_es" method="post" enctype="multipart/form-data" action="${ctx}/ContentCat/edit.do">
		<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<div class="pad10">
				<div class="adm-text ul-float">
					<ul class="clearfix">
						<li>
							<strong>栏目基础信息</strong>
						</li>
						<li>
							<div class="b mr20" style="float:left">上级菜单：</div>
							<div id="pid" style="float:left"></div>
							<div class="Validform_checktip"></div>
							<div class="clear"></div>
						</li>
						<li>
							<div class="need" style="float:left">*</div>
							<div class="b mr10" style="float:left">栏目名称：</div>
							<div style="float:left">
								<input name="name" type="text" value="${info.name}"id="name" class="adm-text" datatype="*2-50" nullmsg="栏目名称不能为空!" errormsg="栏目名称长度必须在2~50位字符范围内!" />
							</div>
							<div class="Validform_checktip"></div>
							<div class="clear"></div>
						</li>
						<li>
							<div class="need" style="float:left">*</div>
							<div class="b mr10" style="float:left">英文名称：</div>
							<div style="float:left">
								<input type="text" name="cat" value="${info.cat}" id="cat"
								datatype="*2-50" nullmsg="英文名称不能为空!"
								errormsg="英文名称长度必须在2~50位字符范围内!" />
							</div>	
							<div class="Validform_checktip"></div>
							<div class="clear"></div>
						</li>
						<li>
							<div class="b mr20" style="float:left">图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片：</div>
							<div wrap="imageContainer" id="container2"
								style="height: 125px; width: 490px; overflow: hidden; padding: 2px; border: 2px solid gray; margin: 2px; margin-bottom: 4px;"></div>
							<div><input id="jsonimg" type="file" style="margin-left: 120px;"/></div>
							<div class="clear"></div>
						</li>
						<li>
							<div class="need" style="float:left">*</div>
							<div class="b mr10" style="float:left">栏目描述：</div>
							<div style="float: left;">
								<textarea name="mem" id="mem" nullmsg="栏目描述不能为空!" cols="80" rows="8" datatype="*2-100" errormsg="栏目描述长度必须在2~100位字符范围内!">${info.mem}</textarea>
							</div>
							<div class="Validform_checktip"></div>
							<div class="clear"></div>
						</li>
						<li>
							<div class="b mr20" style="float:left">活动时间：</div>
							<div style="float: left;">
								<input type="text" id="sTime" name="sTime" value="${info.startTimeStr}" /> </div>
							<div style="float: left;"> 
								<input type="text" id="eTime" name="eTime" value="${info.endTimeStr}" />
							</div>
							<div style="display: none;" id="activityTimeTip"
								class="Validform_checktip Validform_wrong">活动时间选择不合法,开始时间必须小于结束时间且都不为空！
							</div>
							<div class="clear"></div>
						</li>
						<li>
							<div class="b mr20" style="float:left">栏目工作流：</div>
							<div style="float: left">
								<select name="workflowId">
									<option value="0">
										不需要审核
									</option>
									<c:forEach var="item" items="${flowList}" varStatus="status">
										<option value="${item.flowid}" title="${item.mem}"
											<c:if test="${info.workflowId==item.flowid}">selected="selected"</c:if>>
											${item.workflow}
										</option>
									</c:forEach>
								</select>
							</div>
							<div class="Validform_checktip"></div>
							<div class="clear"></div>
						</li>
						<li id="candispli" style="display: none;">
							<div class="b mr20" style="float:left">显示/隐藏：</div>
							<div >
								<input type="radio" id="ishidden0" name="isHiddenRadio" value="0" />
								显示&nbsp;&nbsp;
								<input type="radio" id="ishidden1" name="isHiddenRadio" value="1" />
								隐藏
							</div>
							</li>
							<li>
									<strong>栏目SEO设置</strong>
									<div class="clear"></div>
							</li>
							<li>
								<div class="b mr20" style="float:left">标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题：</div>
								<div style="float: left"><input name="seoTitle" type="text" value="${info.seoTitle}"
									id="seotitle" maxlength=30 /></div>
								<div class="Validform_checktip"></div>
								<div class="clear"></div>
							</li>
							<li>
								<div class="b mr20" style="float:left">关&nbsp;&nbsp;键&nbsp;&nbsp;字：</div>
								<div style="float: left"><input name="seoKeyword" type="text" value="${info.seoKeyword}"
									id="seoKeyword" maxlength=100 />
									</div>
								<div class="Validform_checktip"></div>
								<div class="clear"></div>
							</li>
							<li>
								<div class="need" style="float:left;">*</div><div class="b mr10" style="float:left">描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述：</div>
								<div style="float: left">
									<textarea name="seoDesc" id="seoDesc"  datatype="*2-200" cols="80" rows="8" nullmsg="描述不能为空!" errormsg="描述长度必须在2~200位字符范围内!">${info.seoDesc}</textarea>
								</div>
								<div class="Validform_checktip"></div>
								<div class="clear"></div>
							</li>
							<li>
								<div style="float: left">
								<input type="radio" name="temporjump" id="temporjump1" value="1"
									checked="checked" />
								显示模板设置&nbsp;&nbsp;
								<input type="radio" name="temporjump" id="temporjump2" value="0" />
								显示跳转设置
								</div>
								<div class="clear"></div>
							</li>

							<!--<li><strong>模板设置</strong></li>-->
							<li id="templateset1">
								<div class="b mr20" style="float:left">风&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;格：</div>
								<div >
									<select id="mystyle" name="style"
										onchange="javascript:loadTpl('');">
									</select>
								</div>
								<div class="clear"></div>
							</li>
							<li id="templateset11">
								<div class="b mr20" style="float:left">模&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;板：</div>
								<div >
									<select id="tpl" name="tplpath">
										<option value="">
											无
										</option>
									</select>
								</div>
								<div class="clear"></div>
							</li>
							<li id="templateset2" style="display: none;">
								<div class="b mr20" style="float:left">跳转地址：</div>
								<div style="float: left">
									<input type="text" name="jumpurl" value="${info.jumpurl}" id="jumpurl"  datatype="*0-300"  errormsg="跳转地址长度必须在300位字符范围内!"/>
									</div>
								</div>
								<div class="clear"></div>
							</li>
							<li id="licatdesc" <c:if test="${info.style ne 'topic'}"> style="display:none;"</c:if>>
								<div class="b mr20" style="float:left">专题导语：</div>
								<div style="float: left">
									<input type="text" name="catDesc" id="catDesc" maxsize="100" size="100" value="${info.catDesc}"/>
								</div>
								<div style="float: left; display: none;" id="catDescTip" class="Validform_checktip Validform_wrong"></div>
								<div class="clear"></div>
							</li>
					</ul>
				</div>
			</div>
			<div class="adm-btn-big">
					<input type="hidden" id="oldhidden" value="${info.isHidden}" />
					<input type="hidden" id="isHidden" name="isHidden" value="0" />
					<input type="hidden" id="image" name="image"/>
					
					<c:choose>
						<c:when test="${info.catId!=null}">
							<input id="catidinput" name="catId" type="hidden"
								value="${info.catId}" />
							<input type="submit" value="修&nbsp;&nbsp;改" />
						</c:when>
						<c:otherwise>
							<input type="submit" value="提&nbsp;&nbsp;交" />
						</c:otherwise>
					</c:choose>
			</div>
		</form>
	</body>
</html>