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
		<title>数据编辑</title>
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
		</style>
		
		<c:set var="currentimage" value=""/>
		<c:set var="currentlogo" value=""/>
		<c:choose>
			<c:when test="${info.preImages ne ''&&!empty info.preImages}">
				<c:set var="currentimage" value="${info.preImages}"/>
			</c:when>
			<c:otherwise>
				<c:set var="currentimage" value="${info.image}"/>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${info.preLogo ne ''&&!empty info.preLogo}">
				<c:set var="currentlogo" value="${info.preLogo}"/>
			</c:when>
			<c:otherwise>
				<c:set var="currentlogo" value="${info.logo}"/>
			</c:otherwise>
		</c:choose>
		
		<script type="text/javascript">
			
			var imageJson=<c:if test="${currentimage ne ''&&!empty currentimage}">${currentimage}</c:if><c:if test="${currentimage eq ''||empty currentimage}">''</c:if>;
			var logoJson=<c:if test="${currentlogo ne ''&&!empty currentlogo}">${currentlogo}</c:if><c:if test="${currentlogo eq ''||empty currentlogo}">''</c:if>;
				 
			function backList()
			{
				window.location.href="${ctx}/Content/listtpl.do?catId=${catId}";
			}
			
			function comparisonFun() {   
			    return function(object1, object2) {  
			        var value1,value2;  
			        for(var i in object1) { 
			          value1 = i; 
			        } 
			        for(var j in object2) { 
			          value2 = j;
			        }         
			        if(value1 < value2) {   
			            return -1;   
			        } else if (value1 > value2) {   
			            return 1;   
			        } else {   
			            return 0;   
			        }   
    			};   
			}  
			
			function getJsonMinKey(o)
			{
				var isJson=false;
				try{
					JSON.stringify(o);
					isJson=true;
				}catch(e){
					isJson=false;
				}
				if(isJson)
				{
					var arr=[];
					$.each(o, function(i, dom) { 
						  var obj = {}; 
						  obj[i] = dom; 
						  arr.push(obj);
					});
					arr.sort(comparisonFun());
					if(arr.length>0)
					{
						for(var jsonkey in arr[0]){
							return jsonkey;
						}
					}
					return '';
				}
				return '';
			}
			
			function addstaticdomain(url){
				var regex= /^(http|https)/;
				var regex2=/\\^/;
				if(!regex.test(url)){
					if(regex2.test(url)){
						url=url.substring(1);
						url = '${contextPath}'+url;
					}else{
						url = '${contextPath}'+url;
					}
				}
		        return url;
			}
			
			function getJsonLength(imageJson){
				var length=0;
				for(var o in imageJson){
					length++;
				}
				return length;
			}
		
			function loadForm(){
				 if(imageJson!=''){
				 	 var isJson=false;
					 try{
						JSON.stringify(imageJson);
						isJson=true;
					 }catch(e){
						isJson=false;
					 }
					 if(isJson)
					 {
					 	 var firstKey=getJsonMinKey(imageJson);
					     var imageFirstObj=imageJson[firstKey];
					 	 var imageObj=$("#imageId");
						 var imgSizeIdObj=$("#imgSizeId");
						 var imageSizeObj=$("#imagesize");
						 var imagealtObj=$("#imagealt");
						 var imagepathObj=$("#imagepath");
						 var pickimgObj=$("#pickimg");
						 
						 var length=getJsonLength(imageJson);
						 if(length>1){
						 	$("#choseImageId").show();
						 }else{
						 	$("#choseImageId").hide();
						 }
						 if(imageFirstObj){
						 	imageObj.attr("src",addstaticdomain(imageFirstObj.path));
						 	imagepathObj.val(imageFirstObj.path);
						 	imageObj.attr("alt",imageFirstObj.alt);
						 	imagealtObj.val(imageFirstObj.alt);
						 
						 	imgSizeIdObj.html(imageFirstObj.size);
						 	imageSizeObj.val(imageFirstObj.size);
						 } 
						  var paths="";
						  var arr=[];
						  $.each(imageJson, function(i, dom) { 
							  var obj = {}; 
							  obj[i] = dom; 
							  arr.push(obj);
						 });
						 arr.sort(comparisonFun());
						  $.each(arr,function(i,arrItem){
							 $.each(arrItem,function(j,item){
							 	paths+='<img src="'+addstaticdomain(item.path)+'" style=" cursor: pointer;margin:0 10px;" width="90" alt="'+item.alt+'" data-size="'+item.size+'" data-rp="'+item.path+'" class="previewpop"/>';
							 });
						 });
						 
						 pickimgObj.html(paths);
					 }
				 }
				 
				
				
				 if(logoJson!='')
				 {
				 	 var isJsonFlag=false;
					 try{
						JSON.stringify(logoJson);
						isJsonFlag=true;
					 }catch(e){
						isJsonFlag=false;
					 }
					 if(isJsonFlag)
					 {
					 	 var firstKey=getJsonMinKey(logoJson);
					 	 var logoFirstObj=logoJson[firstKey];
					 	 var logoIdObj=$("#logoId");
						 var logoSizeIdObj=$("#logoSizeId");
						 var logosizeObj=$("#logosize");
						 var logoaltObj=$("#logoalt");
						 var logopathObj=$("#logopath");
						 
						 logoIdObj.attr("src",addstaticdomain(logoFirstObj.path));
						 logopathObj.val(logoFirstObj.path);
						 logoIdObj.attr("alt",logoFirstObj.alt);
						 logoaltObj.val(logoFirstObj.alt);
						 
						 logoSizeIdObj.html(logoFirstObj.size);
						 logosizeObj.val(logoFirstObj.size);
					 }
				 }
				
			}
		
			$(function(){
					loadForm();
					
					$(".showpickimg").click(function(){
						$("#pickimg").show('fast');
					});
					$("#pickimg > img").click(function(){
						$(this).css("border","3px solid #39F").siblings().css("border","none");
						$("#imagepath").val($(this).data('rp'));
						$("#imagesize").val($(this).data('size'));
						$("#imagealt").val($(this).attr('alt'));
					});
					
					 $("#fm_es").Validform({
						tiptype:3,
						label:".label",
						showAllError:false,
						datatype:{
							"zh2-50":/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,50}$/,
							"zhs2-50":/^[a-z]{1}[a-z0-9]{1,49}$/
						},
						beforeSubmit:function(){
							if(imageJson!='')
							{
								var imageJsonObjs = {},index=1,isExist=false;
								var firstEle={"alt":$("#imagealt").val(),"path":$("#imagepath").val(),"size":$("#imagesize").val()};
							
								imageJsonObjs[""+index+""]=firstEle;
								$("#pickimg > img").each(function(i,item){
							    	var _me=$(this);
									if(_me.css("border")){
										isExist=true;
									}
								});
								if(isExist)
								{
									$("#pickimg > img").each(function(i,item){
								    	var _me=$(this);
										if(!_me.css("border")){
											index++;
											imageJsonObjs[""+index+""]={"alt":_me.attr('alt'),"path":_me.data('rp'),"size":_me.data('size')};
										}
								    });
								}else{
									$("#pickimg > img").each(function(i,item){
								    	var _me=$(this);
								    	if(index!=1)
								    	{
								    		imageJsonObjs[""+index+""]={"alt":_me.attr('alt'),"path":_me.data('rp'),"size":_me.data('size')};
								    	}
										index++;
								    });
								}
								var imageText=JSON.stringify(imageJsonObjs);
								$("#image").val(imageText);
							}
							if(logoJson!='')
				 			{
								var logoJsonObj={"1":{"alt":$("#logoalt").val(),"path":$("#logopath").val(),"size":$("#logosize").val()}}
								var logoText=JSON.stringify(logoJsonObj);
								$("#logo").val(logoText);
							}
							return true;
						}
					});
			});
	
			var previewimgpop = {};
		
			//调用
			previewimgpop.a = function(obj){
				var imgpath = obj.attr('src');
				var imageobj = new Image();
				imageobj.src = obj.attr('src');//imageobj.width,imageobj.height
				var htmls = this.b(imgpath,imageobj.width,imageobj.height);
				
				if(obj.siblings('div').length<=0){
					obj.parent().append(htmls);
				}
			};
			
			//绘制弹出框
			previewimgpop.b = function(src,w,h){
				var bodys = '<div style="position:relative;top:0px;border:1px solid #999999;background:#ffffff;width:'+w+'px;height:'+h+'px;">'+
							'<img src='+src+' border="0">'+
							'</div>';
				return bodys;
			}
		
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
			$(".previewpop").mouseenter(function(){
				previewimgpop.a($(this));
			});
			$(".previewpop").mouseleave(function(){
				$(this).siblings('div').remove();
			});
		});
			
	</script>
	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">数据编辑</a><a class="fr btn"
				href="javascript:void(0);" onClick="backList();">返回</a>
		</h2>
		<form id="fm_es" method="post" action="${ctx}/Content/updatePublishedContent.do">
		<input type="hidden" name="csrfToken" value="${csrfToken}"/>
			<div class="pad10">
				<div class="adm-text ul-float">
					<ul class="clearfix">
						<li>
							<span class="b">标题：</span>
							<input type="text" name="txtHead" id="txthead" class="l" value="${info.txtHead}" datatype="*2-50" nullmsg="请输入标题!" errormsg="标题名长度必须在2～50范围内!"/>
							<span id="txtheadTip"></span>
						</li>
						<c:if test="${currentimage ne '' &&!empty currentimage}">
							<li>
								<span class="b">图片：</span>
								<img id="imageId" src="" width="90" alt="" class="previewpop" />
								<input type="hidden" name="imagepath" id="imagepath" class="l" readonly="readonly" value="" />
								<a href="javascript:;" class="showpickimg" id="choseImageId" style="display:none">选择图片</a>
							</li>
	
						<li id="pickimg" style="display: none;">
								
							</li>
							<li>
								<span class="b">图片尺寸：</span><span id="imgSizeId"></span>
								<input type="hidden" name="imagesize" id="imagesize" class="l" readonly="readonly" value="" />
							</li>
							<li>
								<span class="b">图片描述：</span>
								<input type="text" name="imagealt" id="imagealt" class="l" value="" datatype="*2-100" errormsg="描述长度必须在2～100范围内!"/>
								<span id="usernameTip"></span>
							</li>
						</c:if>

						<c:if test="${currentlogo!=''&&!empty currentlogo}">
							<li>
								<span class="b">LOGO：</span>
								<img id="logoId" src="" width="90" alt="" class="previewpop" />
								<input type="hidden" name="logopath" id="logopath" class="l" readonly="readonly" value="" />
							</li>
							<li>
								<span class="b">LOGO尺寸：</span><span id="logoSizeId"></span>
								<input type="hidden" name="logosize" id="logosize" class="l" readonly="readonly" value="" />
							</li>
							<li>
								<span class="b">LOGO描述：</span>
								<input type="text" name="logoalt" id="logoalt" class="l" value="" datatype="*2-100" errormsg="描述长度必须在2～100范围内!"/>
								<span id="usernameTip"></span>
							</li>
						</c:if>
                       <li>
							<div class="b mr20" style="float:left">活动时间：</div>
							<div style="float: left;">
								<input type="text" id="sTime" name="stime" value="${info.stime}" /> </div>
							<div style="float: left;"> 
								<input type="text" id="eTime" name="etime" value="${info.etime}" />
							</div>
							<div style="display: none;" id="activityTimeTip"
								class="Validform_checktip Validform_wrong">活动时间选择不合法,开始时间必须小于结束时间且都不为空！
							</div>
							<div class="clear"></div>
						</li>
						<li>
							<span class="b">小编语：</span>
							<textarea name="editorLanguage" id="editorLanguage" rows="5" cols="40" class="l" datatype="*2-100" nullmsg="请输入小编语!" errormsg="小编语长度必须在2～100范围内!" >${info.editorLanguage}</textarea>
							<span id="editorlanguageTip"></span>
						</li>

					</ul>
				</div>
			</div>
			<div class="adm-btn-big">
				<input type="hidden" name="image" id="image"/>
				<input type="hidden" name="logo" id="logo"/>
				<input type="hidden" name="contentDataId" id="contentDataId" value="${info.contentDataId}" />
				<input type="hidden" name="catId" id="catId" value="${catId}"/>
				<input type="hidden" name="catName" id="catName" value="${catName}"/>
				<input type="submit" id="submit" value="提&nbsp;&nbsp;交" />
			</div>
		</form>
	</body>
</html>