<%@include file="../public/header.jsp"%>
<%@page import="com.richinfo.common.TokenMananger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="keywords" content=" " />
<meta name="description" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>玩家管理</title>
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
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerSpinner.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerTextBox.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerComboBox.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerButton.js" type="text/javascript"></script>
 
<style type="text/css">
a:link{color:green;font-size:14px;text-decoration: underline;}
a:visited{color:red;font-size:14px;text-decoration: underline;} 

.alert{
	overflow:hidden;
	border:1px solid #cccccc;
	width:286px;
	position:fixed;
	_position:absolute;
	top:20%;
	_top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight/2-this.offsetHeight/2));
	left:50%;
	margin-left:-230px;
	background:#fff;
	z-index:110;
}
 
 
 .alert .cont{
	padding:20px;
	overflow:hidden;
}
 .alert .cont p{
	text-align:left;
	line-height:22px;
	font-size:16px;
	padding:10px;
}
 .alert .btn{
	text-align:center;
	overflow:hidden;
	padding:10px 0px 20px;
}
 .alert .btn .sure{
	width:104px;
	height:29px;
	display:inline-block;
}
 .alertbg{
	width:100%;
	_background:none;
	position:fixed;
	_position:absolute;                    
	_height:expression(document.documentElement.scrollHeight);
	top:0px;
	left:0px;
	right:0px;
	bottom:0px;
	z-index:100;
}
.searchbtn{
	background: url('${ctx}/static/admin/images/photo.png') no-repeat;
	color: #FFF;
    background-position: 0 -38px;
    border: 0 none;
    line-height: 19px;
    width: 38px;
}
</style>
<script type="text/javascript">
	    
        $(f_initGrid);
        var manager, g ;
        function f_initGrid()
        { 
           g =  manager = $("#maingrid").ligerGrid({
                columns: [
                { display: '大神ID', name: 'playerid', width: 70, type: 'text' },	
                { display: '用户名',  width: 80, name: 'playername',editor: { type: 'text' }},
                { display: '玩家争霸名称',  width: 120, name: 'pkname',editor: { type: 'text' }},
                { display: '大神介绍',  width: 200, name: 'playerdesc',  editor: { type: 'text' }},
                { display: '手机号',  width: 90, name: 'phonenumber',  editor: { type: 'text' }},
                { display: 'QQ',  width: 80, name: 'qq',  editor: { type: 'text' }},
                { display: '微博',  width: 80, name: 'weibo',  editor: { type: 'text' }},
                { display: '微信',  width: 80, name: 'weixin',  editor: { type: 'text' }},
                { display: '加赞数',   width: 60, name: 'praisenum',   editor: { type: 'text'} },
                { display: '视频id',   width: 50, name: 'videoid' ,   editor: { type: 'text'}},
                { display: '图片地址',   width: 300, name: 'imgpath',   editor: { type: 'text'}},
                { display: '操作', name : 'operation', width : 100, align : 'center',
					render:function(item){
						return "<div id=u_"+item.playerid+" ><input type='button' value='修改' onclick='edit("+item.playerid+")'/>&nbsp;&nbsp;<input type='button' value='删除' onclick='deletePlayer("+item.__index+")'/></div>";
					}
				}
                ],
                data : { Rows : ${playersList} },
                width: '100%',
				rowHeight: 28,
				pageSize: 5,
				pageSizeOptions : [ 5, 10, 15, 20 ],
				sortName: 'score',
	            width: '100%', height: '80%', pageSize: 10,rownumbers:true,
	            checkbox : true,
	             //应用灰色表头
	            cssClass: 'l-grid-gray', 
	            heightDiff: -6,
	            width : '100%',
	            fixedCellHeight:false,
	            
	            toolbar : {
				items : [
				{
					line : true
				}, {
					text : '删除',
					click : deletePlayer,
					icon : 'delete'
				}
				]
			}
                
            });   
        }
        
        function edit(rowIndex){
        	window.location.href = "${ctx}/gameGod/toEdit.do?playerid="+rowIndex;
        }
        function deletePlayer(rowIndex){
		   	var idstr = '';
		   	if(rowIndex!='undefined'&&!isNaN(rowIndex)){
		   		var currRow=manager.getRow(rowIndex);
		   		idstr = currRow.playerid;
		   	}else{
		   		var rows = manager.getSelectedRows();
				if(rowIndex&&rowIndex.icon!='delete'){
					rows.push(manager.getRow(rowIndex));
				}
		   		if(rows.length==0){
					alert("请至少选中一条玩家数据！");
					return false;
				}
		   		var ids = [];
			   	for(var i=0;i<rows.length;i++){
			   		ids.push(rows[i].playerid);	
			   	}
		   		idstr = ids.join(",");
		   	}
		   	$.ligerDialog.confirm('是否确定删除?',function(btn){
		   		if(btn){
				    $.ajax({
					   type: "POST",
					   url: "${ctx}/gameGod/del.do?csrfToken=${csrfToken}",
					   data: {idstr:idstr},
					   success: function(msg){
					     if(msg.status=='0') {
					    	 alert("删除成功！"); 
					    	 if(rowIndex!='undefined'&&rowIndex.icon!='delete'){
					    		manager.deleteRow(rowIndex); 
					    	 }else{
		   						manager.deleteSelectedRow();
					    	 }
					     }
					   }
					});
			    }
			});
	   }
	   
	   
      function addPlayer(){
           var players = $("#players").val();
           
           if(players==null||players==""){
              alert("请填写玩家列表");return false;
           }
           var strs= players.split("##"); //字符分割   
           if(strs.length<=1){
          	  alert("玩家列表请用英文##分隔");return false;
           }
           if(strs.length>=21){
              if(strs.length==21&&strs[20]==""){
              }else{
          	 	 alert("添加玩家每次最多支持20行");return false;
              }
           }
           $.ajax({
				   type: "POST",
				   url: "${ctx}/gameGod/add.do?csrfToken=${csrfToken}",
				   data: {players:$.trim(players),pkid:$("#pkid").val()},
				   success: function(msg){
				     if(msg.status=='0') {
				         var html = "添加成功";
				    	 if(msg.errorplayers.length>0){
				    	   html = "以下玩家添加不成功<br/>";
				    	   $.each(msg.errorplayers,function(i,e){
				    	     html += e+"<br/>";
				    	   });
				    	   html += "其他玩家已成功添加";
				    	   window.localStorage.setItem('gods',players);
				    	 }else{
				    	    window.localStorage.setItem('gods',"");
				    	 }
				    	 $(".alert .cont p").html(html);
				         $('.alert').show();
					     $('.alertbg').show();
				    	 //window.location.href = "${ctx}/gameGod/lists.do?pkid=${pkid}";
				     }
				   }
		   });
      }
		 
	function closeAlert(){
	    $('.alert').hide();
		$('.alertbg').hide();
		window.location.href = "${ctx}/gameGod/lists.do?pkid=${pkid}";
	}
	
	function submitForm(){
		var phonenumber=$("#phonenumber").val();
		if(phonenumber!=''&&isNaN(phonenumber)){
			alert("号码请填写数字!");
			return false;
		}
	 
		document.fm_es.submit();
	}
	
	$(function(){
	    var players = window.localStorage.getItem("gods");
	    $("#players").val(players);
	});
</script>

</head>
<body style="overflow-x: hidden; padding: 4px;">


	<h2 class="adm-title clearfix"><a class="fl title">玩家争霸-大神列表</a></h2>
	<div style=" height: 35px; padding-top: 10px;"> 
	    <form name="fm_es" id="fm_es" action="${ctx }/gameGod/lists.do" method="post">
	    <span style="font-weight: bold;font-size: 14px;"> 大神搜索</span>
		<span style="padding-left:40px;">大神用户名：</span><input type="text" name="playername" id="playername"> 
		<span>玩家争霸名称：</span><input type="text"  name="pkname" id="pkname">  
		<span>手机号：</span><input type="text"  name="phonenumber" id="phonenumber">  
	    <input type="hidden" name="pkid" value="${pkid}">
	    <input type="hidden" name="csrfToken" value="<%=TokenMananger.getTokenFromSession(session)%>"/>
		<input type="button" onclick="submitForm();" class="searchbtn" value="搜索">
	 
		</form></div>
	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>

	<a class="l-button" style="width: 120px; float: left; margin-left: 10px; display: none;"></a>
		
    <div id="addplayer" style="margin:10px;">
        <p style="line-height: 22px;padding-bottom: 10px;"><span style="font-weight: bold;">添加大神名单</span>   <span style="padding-left:20px;">每次最多支持20行</span></p>
          <div style="float:left; width:55%"> <textarea name="players" id="players" cols="70" rows="8">${players }</textarea> </div>
          <div style="float:right;line-height: 22px;  width:40%">
	          <p>(1)格式为：用户名+&&+玩家争霸名称+&&+大神介绍+&&+玩家的加赞数+&&+视频id（可为空）+&&+手机号（可为空）+&&+QQ（可为空）+&&+微博（可为空）+&&+微信（可为空）+ &&+ 图片地址（可为空）+##</p>
	          <p>(2)所有符号为半角英文符号，每一行一个用户信息，用##分割；</p>
	          <p>(3)如果无该项内容，则为空(<span style="color:red">需要用空格占位</span>)，但仍需要用英文&&分隔开；</p>
	          <p>例如；</p>
	          <p>张三&&第一期玩家争霸&&大神介绍&&10001&&8901&&18688447523&&374039103&&微博&&微信&&a.jpg##</p> 
	          <p>张三&&第一期玩家争霸&&大神介绍&&10001&& && && && && && ##</p> 
	          
          </div>
          <div style="clear: both;"></div>
          <input type="hidden" name="pkid" id="pkid" value="${pkid }">
<!--           <div style="margin-top:10px;"><input type="button" onclick="addPlayer();" value="提交"></div> -->
         <div class="adm-btn-big" style="float:left;margin-top:10px;"><input id="btnSub" type="button"  onclick="addPlayer();" value="提交" /></div>
        </div>
    </div>
    
      <!--弹框样式开始-->
    <div class="alertbg" style="display:none;">&nbsp;</div>
    <div class="alert" style="display:none;">
    	 
        <div class="cont">
        	<p>添加成功</p>
        </div>
        <div class="btn">
            <input type="button" value="确   定" onclick="closeAlert()">
        </div>
    </div>		
</body>
</html>