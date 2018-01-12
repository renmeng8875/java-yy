//检测用户是否安装了客户端
var _yyClientInstalled = null;
function checkYYClientInstalled() {
	if (_yyClientInstalled !== null) { // 已检测过
		return _yyClientInstalled;
	}	
	var installed = false,
		UNDEF = "undefined",
		OBJECT = "object",
		YY_CHECKER_NAME = "歪歪",
		YY_CHECKER_AX = "yy_checker.Checker.1",
		YY_MIME_TYPE = "application/x-checker",
		
		win = window,
		nav = navigator;
	if (typeof nav.plugins != UNDEF && typeof nav.plugins[YY_CHECKER_NAME] == OBJECT) { // non IE
		var description = nav.plugins[YY_CHECKER_NAME].description;
		if (description && !(typeof nav.mimeTypes != UNDEF && nav.mimeTypes[YY_MIME_TYPE] && !nav.mimeTypes[YY_MIME_TYPE].enabledPlugin)) {
			installed = true;
		}
	} else if (typeof win.ActiveXObject != UNDEF) { // IE
		try {
			var a = new ActiveXObject(YY_CHECKER_AX);
			installed = true;
		} catch (e) {}
	}
	_yyClientInstalled = installed;
	return installed;
}

//进入YY客户端
function gotoYYClientChannel(topChannelId, subChannelId,yyNum) {
	if (checkYYClientInstalled()) {
		try {
			var href = "yy://join:room_id=" + topChannelId;
			if(subChannelId){
				href += "&sub_room_id=" + subChannelId;
			}
			location.href = href;
		} catch (e) {
			if(yyNum && !isNaN(yyNum)){
				window.open("http://m.yy.com/live/"+yyNum);
			}else{
				window.open("http://m.yy.com");
			}			
		}
	} else {
		if(yyNum && !isNaN(yyNum)){
			window.open("http://m.yy.com/live/"+yyNum);
		}else{
			window.open("http://m.yy.com");
		}
	}
}