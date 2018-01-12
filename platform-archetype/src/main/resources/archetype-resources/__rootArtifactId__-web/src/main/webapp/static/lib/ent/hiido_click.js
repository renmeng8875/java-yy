(function () {

    var version = 1.1;
    var win = window;
    var _hiidoDebug = win._hiidoDebug || false;
    var logger = {
        log: function () {
            if (_hiidoDebug) {
                win.console && win.console.log(arguments);
            }
        }
    };

    var hiido = {
        domain: "ylog.hiido.com",
        ipPrefix: "183.61.2.",
        ips: [91, 92, 94, 95, 96, 97, 98],
        getServerUrl: function (host) {
            host = host || this.domain;
            var ptl = location.protocol;
            var path = "j.gif?";
            return ptl + "//" + host + "/" + path;
        },

        randomIp: function () {
            var Rand = Math.random();
            var index = Math.round(Rand * (this.ips.length - 1));
            var suff = this.ips[index];
            return this.ipPrefix + suff;
        },

        getParam: function (opt) {
            var obj = opt;
            var param = [];
            obj.time = parseInt(1 * new Date() / 1000);
            obj.uid = this.getCookie("yyuid")||obj.uid;
            obj.ui = this.getCookie("hiido_ui")||obj.ui;
            obj.username = this.getCookie("username")||obj.username;
            for (h in obj) {
                if (obj.hasOwnProperty(h)) {
                    param.push(encodeURIComponent(h) + "=" + (obj[h] === undefined || obj[h] === null ? "" :
                        encodeURIComponent(obj[h])))
                }
            }
            return param.join("&");
        },

        send: function (url, backurl, times) {
            var reties = times || 0;
            var img = new Image();
            var self = this;
            img.onerror = function () {
                if (reties <= 1) {
                    self.send(url, backurl, ++reties);
                } else if (reties == 2) {
                    self.send(backurl, backurl, ++reties);
                }
            }
            img.src = url;
        },

        getCookie: function (name) {
            var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
            if (arr = document.cookie.match(reg)) {
                return unescape(arr[2]);
            } else {
                return null
            }
            ;
        }
    };

    var iface = {
        stat: function (opt) {
            if (!opt) {
                return false;
            }
            var svr = hiido.getServerUrl();
            var param = hiido.getParam(opt);
            var url = svr + param
            var backurl = hiido.getServerUrl(hiido.randomIp()) + param;
            hiido.send(url, backurl)
        }
    };

    if (typeof(module) === "object") {
        module.exports = iface;
    }
    window.appHiido = iface;
}).apply(this);


/**
 type=1："我的订阅"tab每日PV UV，type=2: 我与主播详情页PV UV ，type=3：在详情页点击主播头像去主播个人页的PV、UV，type=4: “正在直播”模块PV、UV，
 type=5：通过“正在直播”去到直播间的PV、UV，type=6: 粉丝PP里任务中心的总PV UV，type=7：粉丝PP里各个任务被点击的次数从多到少排序，
 taskid 只有页面类型type=7时，才传这个参数
 */
//function hiido_statPP(type,taskid) {
//    var params={
//        "act":"webyymusicpp",
//        "time":"",
//        "uid":"",
//        "type":type,
//        "taskid":taskid
//    };
//    window.appHiido.stat(params);
//}

/**
 *
 * @param anchoruid 主播uid
 * @param type 点击事件标识
 * 1、直播间点击分享    2、PC网页点击分享      3、手机网页点击分享
 * 4、点击视频       5、点击“更多详情”
 */
function hiido_statPP(anchoruid, type, source) {
    var params = {
        "eventid": 10005681,
        "act": "webevent",
        "eventype": 1,
        "time": "",
        "uid": 0,
        "class1": 'ent',
        "class2": 'agencyzc',
        "bak1": anchoruid,
        "bak2": type,
        "bak3": source
    };
    window.appHiido.stat(params);
}


/**
 * 众筹预购
 * @param imei 设备唯一标识码 (手机端)
 * @param mac MAC地址 设备唯一标识码(手机端)
 * @param sys 分端标识 移动端必备，0=IOS /2=Android/3=PC
 * @param act_type 1 =手机专题页 / 2=手机商品列表页/3=手机订购页/4=手机分享 /5=pc专题页 /6=pc订购页/7=pc分享/8=pc直播间预购pv
 * @param pid 项目ID
 */
function hiido_stat_preorder(imei,mac,sys,act_type,pid,uid) {
    var params = {
        "eventid": 10007005,
        "act": "webevent",
        "eventype": 1,
        "uid":uid,
        "imei": imei,
        "mac": mac,

        "sys": sys,
        "act_type": act_type,
        "bak1": pid,
        "bak2":0
    };
    window.appHiido.stat(params);
}
