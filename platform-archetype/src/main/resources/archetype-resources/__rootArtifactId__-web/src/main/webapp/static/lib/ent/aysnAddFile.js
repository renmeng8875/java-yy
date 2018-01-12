function include(file) {
    var files = typeof file == "string" ? [file] : file;
    for (var i = 0; i < files.length; i++) {
        var isCSS = /\.css/.test(files[i]);
        if (isCSS) {
            var styleTag = document.createElement("link");
            styleTag.setAttribute('type', 'text/css');
            styleTag.setAttribute('rel', 'stylesheet');
            styleTag.setAttribute('href', files[i]);
            $("head")[0].appendChild(styleTag);
        } else {
            var script = document.createElement('script');
            script.type = "text/javascript";
            script.language = "javascript";
            script.src = files[i];
            $("head")[0].appendChild(script);
        }
    }
}

function aysnAddAllFile() {
    aysnAddUdbFile();
    aysnAddDwbFile();
    aysnAddDuowanJs();
}

//异步加入udb资源
function aysnAddUdbFile() {
//	include('http://res.udb.duowan.com/js/oauth/udbsdk/qlogin/udb.sdk.qlogin.all.min.js');
    include('http://res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/udb.sdk.pcweb.popup.min.js');
}

//异步加入多玩币资源
function aysnAddDwbFile() {
    var temp_js = ['http://res.duowan.com/pay/auth/web/style.css?v=2.0', 'http://res.duowan.com/pay/auth/js/auth.js'];
    include(temp_js);
}

//异步加入duowan.js
function aysnAddDuowanJs() {
    var temp_js = ['http://www.duowan.com/duowan.js'];
    include(temp_js);
}
