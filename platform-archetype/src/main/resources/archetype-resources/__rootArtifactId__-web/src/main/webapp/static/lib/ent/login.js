var loginCfg = {
    loginCallBack: null,
    logoutCallBack: null,
    closeLoginCallBack: null,
    isLoginCallBack: null,
    getLoginInfoCallBack: null
};

/**
 *  直播间登陆用户跳过来要调一下这个js方法
 * @param callback
 */
function authenticationTicket(callback) {
    var ticket = window.location.toString().split("#")[1];
    if (ticket && ticket.length > 100) {
        window.location.hash = "";
        var url = "http://udb.yy.com/authentication.do?action=authenticate&appid=5060&busiUrl="
            + encodeURIComponent(window.location.toString()) + "&ticket=" + ticket;
        //var url="http://www.baidu.com/";

        var element = '<iframe name="UDBiFrame" style="display:none;" frameborder="0" src="'
            + url
            + '"></iframe>';
        $("body").prepend(element);
        $("iframe[name='UDBiFrame']").on("load",function() {
            callback();
        })
    }
}

function showLoginInfo() {
	topMenuControl();
    $('#noLoginBox').show();
    $('.aside-nolog').show();
    var yynum = $.cookie('yynum');
    var nick = $.cookie('nick');
    if ((nick == null || nick == "" ) && getLoginUid() == "") {//如果没有登录
        $('#noLoginBox').show();
        $('#loginBox').hide();
        return;
    }
    window.loginUserNick = nick;
    $('#loginBox').show();
    $('#noLoginBox').hide();
    $.ajax({
        url: "/tieba2/ent/queryLoginYYInfos.json",
        type: "get",
        dataType: "json",
        data: {},
        excetpion: function (json) {
            $('#noLoginBox').show();
            $('.aside-nolog').show();
        },
        success: function (json) {
            yynum = json.yy_num;
            nick = arrowFilter(json.nick);
            if (yynum && nick) {
                if (nick == getLoginUserName()) {
                    combineAdminm.callFunc("setNickName");
                    if ($.cookie('nick') != null && $.cookie('nick') != "") {
                        nick = $.cookie('nick');
                    }
                }
                $.cookie('yynum', yynum, {path: '/', domain: 'yy.com'});
                $.cookie('nick', nick, {path: '/', domain: 'yy.com'});
                window.loginUserNick = nick;
                $('#noLoginBox').hide();
                $('#loginBox').show();
                renderLoginInfo(yynum, nick);
            } else {
                if (!window.loginUserNick) {
                    window.loginUserNick = getLoginUserName();
                    $.cookie('nick', window.loginUserNick, {path: '/', domain: 'yy.com'});
                }
                renderLoginInfo(yynum, window.loginUserNick);
            }
        }
    });
}

function renderLoginInfo(yynum, nick) {
    if (nick) {
        window.loginUserNick = nick;//设置全局变量
        $('#login_user_nick').text(nick);
        if (loginCfg.getLoginInfoCallBack) {
            loginCfg.getLoginInfoCallBack(nick);
        }
    }
}

/**
 *
 * @param callBack 回调方法
 * @param type 看后端回调请求里面的解释
 * @return
 */
function showLoginBox() {
    UDB.sdk.PCWeb.popupOpenLgn('/platform/udb/getSdkAuth.json');
    //reloadPage();
}

function reloadPage() {
    if (window.loginSuccessTimer) {
        clearInterval(window.loginSuccessTimer);
    }
    window.loginSuccessTimer = setInterval(function () {
        try {
            if (isLogin()) {
                clearInterval(window.loginSuccessTimer);
                window.location.reload();
            }
        } catch (error) {
        }
    }, 300);
}

/**
 * 登录后刷新的相关操作
 * @return
 */
function refreshOperate(cancel, writeCookieURL) {
    if (cancel == "1") {
        if (loginCfg.closeLoginCallBack) {
            loginCfg.closeLoginCallBack();
            return false;
        }
    } else {
        UDB.sdk.PCWeb.writeCrossmainCookieWithCallBack(writeCookieURL, function () {
            $(this).unbind("load"); //解决udb加载事件重复绑定的bug
            showLoginInfo();
            if (loginCfg.loginCallBack) {
                loginCfg.loginCallBack();
            }
        });
    }
}

/**
 * 退出登录
 */
function logout() {
    $.ajax({
        url: "/platform/udb/logout.json",
        type: "get",
        dataType: "json",
        success: function (json) {
            $('#noLoginBox').show();
            $('.aside-nolog').show();
            $('#loginBox').hide();
            $('.aside-haslog').hide();
            $('.login_user_nick').text('');
            window.loginUserNick = null;
            topMenuHide();
            UDB.sdk.PCWeb.deleteCrossmainCookieWithCallBack(json.deleteCookieURL, function () {
                if (loginCfg.logoutCallBack) {
                    loginCfg.logoutCallBack();
                }
            });
        }
    });
}

function isLogin() {
    var flag = false;
    var loginUserName = $.cookie('username');
    if (loginUserName && loginUserName != '') {
        flag = true;
    }
    return flag;
}

function getLoginUserName() {
    return $.cookie('username');
}

function getLoginUid() {
    return $.cookie('yyuid');
}

function funHasLiveRoom() {
    return $.cookie('hasLiveRoom');
}

//删除cookie
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - (1000 * 60 * 60 * 24 * 30));
    var cval = $.cookie(name);
    document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString() + "; path=/;domain=.yy.com";
}

function thisMovie(movieName) {
    if (navigator.appName.indexOf("Microsoft") != -1) {
        return window[movieName];
    } else {
        return document[movieName];
    }
}

//修改昵称
function changeMyNick(nick) {
    if (nick) {
        try {
            thisMovie("channel_contianer").callAs("updateMyInfo", {nickname: nick});
        } catch (e) {
        }
    }
}
function topMenuControl(){
	//var agencyOauthCookie=$.cookie('agency.oauthCookie')
	//var oauthCookie=$.cookie('oauthCookie')
	//if(oauthCookie != agencyOauthCookie){
	//	$.ajaxData("/agency/getIdentityStatus.action","",{
	//		autoAlert:false,
	//		success:function(){
	//			$.cookie('agency.oauthCookie',oauthCookie);
	//			_topMenuControl();
	//	}});
	//}else{
	//	_topMenuControl();
	//}
}
function _topMenuControl(){
	var isShowOrder=$.cookie('agency.isShowOrder');
	if(isShowOrder==1){
		$("#agencyOrder").show();
	}else{
		$("#agencyOrder").hide();
	}
	
	var companyId=$.cookie('agency.companyId');
	if(companyId!=-1){
		$("#agencyAdmin").show();
	}else{
		$("#agencyAdmin").hide();
	}
}
function topMenuHide(){
	$("#agencyAdmin").hide();
	$("#agencyOrder").hide();
}

loginCfg.loginCallBack=function(){
    //showLoginInfo();
    window.location.reload()
}
loginCfg.logoutCallBack=function(){
    //showLoginInfo();
    window.location.reload()
}
//init
$(document).ready(function() {
	//登录按钮
	$('#loginId').click(showLoginBox);
	if (isLogin()) {
		showLoginInfo();
		if (loginCfg.isLoginCallBack) {
			loginCfg.isLoginCallBack();
		}
	} else {
		$('#loginBox').hide();
		$('#noLoginBox').show();
	}
	//登录成功回调
//	loginCfg.loginCallBack = function() {
//	};
});
try{
aysnAddUdbFile();
aysnAddDuowanJs();
}catch(e){}