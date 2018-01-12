#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page language="java" pageEncoding="UTF-8" %>
<div class="header">
	<div class="header-con" style="font-size: 14px; width: 1160px;">
		<div class="header-logo" onclick="location.href='http://idol.yy.com'" style="cursor:pointer;">&nbsp;</div>
		<div style="float:left;cursor:pointer;margin-left:40px" onclick="location.href='/agency/index.html'" >服务平台首页</div>
		<div class="no-login" style="display:none;" id="loginBox">
			<a href="/agency/ticket/agencyCenter.action" class="line-lr jia-meng" id="agencyCenter">个人中心</a><span class="line-lr user-name" id="login_user_nick"></span><a href="javascript:;" onclick="logout();" class="line-lr login-out">退出</a>
		</div>
		<div class="login" id="noLoginBox">
			<a href="javascript:;" class="line-r sign-in" id="loginId">登录</a><a target="_blank" href="https://aq.yy.com/p/reg/account.do" class="line-lr register">注册</a>
		</div>
	</div>
</div>