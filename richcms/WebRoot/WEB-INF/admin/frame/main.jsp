<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>main</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/base.js" type="text/javascript"></script>
<script src="${ctx}/static/admin/js/ligerui/plugins/ligerDialog.js"	type="text/javascript"></script>
<script type="text/javascript">
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
			$topMenu.find('#menu_'+topid).addClass('on').removeClass('nav2').siblings('.on').removeClass('on').addClass('nav2');
			my_leftmenu.getmenu(topid,this.id);
		});	
	}
});

window.onload = function(){
	//$.ligerDialog.warn("只测玩家争霸模块！");
}
</script>
</head>
<body>
	<div class="main_border" style="border:0;">
		<div id="my_main">
			<h2 class="adm-title">
				<a class="title">我的个人信息</a>
			</h2>
			<div class="ul1">
				<ul class="clearfix">
					<li>您好：<a href="javascript:void(0);">${userinfo.nickName}</a>
					</li>
					<li>所属角色：<a href="javascript:void(0);">${userrole.roleName}</a>
					</li>
					<li>上次登录时间：<a href="javascript:void(0);">${userinfo.lastLoginStr}</a>
					</li>
					<li>上次登录IP：<a href="javascript:void(0);">${userinfo.ipstr}</a>
					</li>
				</ul>
			</div>

			<h2 class="adm-title">
				<a class="fl title">我的快捷键</a><a class="fr btn"
					href="${ctx}/menu/addShortcut.do">+添加快捷方式</a>
			</h2>
			<ul class="clearfix">
			<c:forEach items="${shortcutList}" var="menu" varStatus="status">
				<li style="margin:10px;border:solid 1px #777;padding:10px;">
					<a
						class="gourl" id="${menu.menuId}"
						data-topid="${menu.parent.parent.menuId}"
						href="${ctx}/${menu.control}/${menu.action}">
						${menu.menu}
					</a>
				</li>
			</c:forEach>	
			</ul>

			<h2 class="adm-title ">
				<a class="title">开发团队</a>
			</h2>
			<ul class="ul1">
				<li><span style="font-size: 15px"><b>项目经理：</b></span><a href="javascript:void(0);"><span style="font-size: 15px"><b>张生存</b></span></a>
				</li>
				<li>UI设计&nbsp;&nbsp;： <a href="javascript:void(0);">张生存、吴志勇、张伟力、任萌</a>
				</li>
				<li>前端开发：<a href="javascript:void(0);">张生存、吴志勇、张伟力、任萌</a>
				</li>
				<li>后台开发：<a href="javascript:void(0);">张生存、吴志勇、张伟力、任萌</a>
				</li>
				<li>开发团队：<a href="javascript:void(0);">张生存、吴志勇、张伟力、任萌</a>
				</li>
				<li>运维：<a href="javascript:void(0);">兰雄、曾冬仁</a>
				</li>
			</ul>
		</div>
	</div>
	
</body>
</html>
