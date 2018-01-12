<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>main</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript">
			//2014.5.20
			var $mymaindiv = $('#my_main');
			var wh = 0;
			var mh = $mymaindiv.height();
			var $tasklist = $('#tasklist');
			var autoheight = function(){
				wh = $(window).height();
				var mymh = ($tasklist.size())?wh-22:wh;
				$mymaindiv.css('height',mymh+'px');
				$mymaindiv.find('.leftcat').css('height',(wh-27)+'px');	
			};
			
			$(function(){
				var checkboxs = $(":checkbox[param='item']");
				$("#inpselall").click(function(){
					if(this.checked){
						checkboxs.attr("checked",true);
					}
					else{
						checkboxs.attr("checked",false);
					}
					
				});
				
				$("a[data-id]").click(function(){
					var menuid = $(this).attr("data-id");
					var selector = "#tr_"+menuid;
					if(confirm("是否确定删除此快捷键？")){
						$.ajax({
						   type: "POST",
						   url: "${ctx}/menu/deleteshortcut.do?csrfToken=${csrfToken}",
						   data: "ids="+menuid,
						   success: function(msg)
						   {
						     if(msg.status=='0')
						     {
						    	 alert("删除成功！"); 
						    	 $(selector).remove();
						     }
							  
						   }
						});
					}
				});
				
				$("#batchDelete").click(function(){
					var ids = [];
					checkboxs.each(function(){
						if(this.checked)
						{
							ids.push(this.value);	
						}
					});
				
					if(ids.length==0)
					{
						alert("请先选择一个或多个要删除的快捷菜单！");
						return false;
					}
					if(confirm("是否确定批量删除所选项？")){
					$.ajax({
					   type: "POST",
					   url: "${ctx}/menu/deleteshortcut.do?csrfToken=${csrfToken}",
					   data: "ids="+ids.join(","),
					   success: function(msg)
					   {
					     if(msg.status=='0')
					     {
					    	 alert("删除成功！");
					    	 for(var i=0;i<ids.length;i++)
					    	 {
					    		 var selector = "#tr_"+ids[i];
					    		 $(selector).remove();
					    	 }
					    	 
					     }
						  
					   }
					});
				   }		
				});
				
				//2014.5.20
				autoheight();	
				$(window).resize(autoheight);
				//点击跳转	
				var my_topmenu = null,my_leftmenu = null;
				if(self != top){
					my_topmenu=window.parent.frames['topmenu'];
					my_leftmenu=window.parent.frames['leftmenu'];
					$('.gourl').click(function(){
						var $topMenu = my_topmenu.$('#topmenu');
						var topid=$(this).data('topid');
						var id=$(this).data('menuid');
						$topMenu.find('#menu_'+topid).addClass('on').removeClass('nav2').siblings('.on').removeClass('on').addClass('nav2');
						my_leftmenu.getmenu(topid,id);
					});	
				}
				
			});

		</script>
	</head>
	<body>
		<div class="main_border" style="border: 0;">
			<h2 class="adm-title clearfix">
				<a class="fl title">我的快捷键</a><a class="fr btn"
					href="${ctx}/menu/addShortcut.do">+添加快捷方式</a>
			</h2>
			<table class="adm-table">
				<thead>
					<tr class="tr1">
						<td width="30">
							<input class="input1" type="checkbox" id="inpselall" />
						</td>
						<td width="90">
							快捷菜单ID
						</td>
						<td>
							菜单名称
						</td>
						<td>
							操作
						</td>
					</tr>
				</thead>
				<tbody id="tbmain">
					
					<c:forEach items="${shortcutList}" var="menu" varStatus="status">
					<tr id="tr_${menu.menuId}" <c:if test="status.index%2==1">class="tr2"</c:if>>
						<td>
							<input type="checkbox" value="${menu.menuId}" param="item"/>
						</td>
						<td>
							${menu.menuId}
						</td>
						<td>
							${menu.menu}
						</td>
						<td>
							<a
								href="${ctx}/${menu.control}/${menu.action}"
								class="gourl" id="shortcut_${menu.menuId}" data-topid="${menu.parent.parent.menuId}" data-menuid="${menu.menuId}"
								>跳转</a><a href="javascript:;"
								data-m="SysShortcuts" data-id="${menu.menuId}" class="dodel" >删除</a>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<br/>
			<div class="adm-quence">
				<a href="javascript:;" class="dodel" id="batchDelete">批量删除</a>
			</div>
		</div>
	</body>
	
</html>
