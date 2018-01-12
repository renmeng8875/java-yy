package com.yy.ent.platform.signcar.common.constant;


/**
 * YYPConstant
 *
 * @author suzhihua
 * @date 2015/10/26.
 */
public class YYPConstant {

    /**
     * 查所有直播间
     */
    public final static int REQUEST_LIVINGROOM_ROOM = 29 << 8 | 164;
    /**查询用户在哪个频道内*/
    public final static int REQUEST_CHID_USER = 18 << 8 | 170;
    public final static int REQUEST_ROOM_TYPE = 181 << 8 | 194;

    public static final int PROGRAM_TICKET_REQ_URI = 117 << 8 | 130;

    public interface SERVER {
        /**
         * 榜单接口
         */
        int REQUEST_RANK = 132 << 8 | 123;
        int RESPONSE_RANK = 133 << 8 | 123;

        /**
         * 榜单前5接口
         */
        int REQUEST_RANK_TOP5 = 134 << 8 | 123;
        int RESPONSE_RANK_TOP5 = 135 << 8 | 123;

        /**
         * 查询用户信息接口
         */
        int REQUEST_QUERYUSERINFO = 136 << 8 | 123;
        int RESPONSE_QUERYUSERINFO = 137 << 8 | 123;

        /**
         * 用户领取入队卡或碎片接口
         */
        int REQUEST_USERSIGN = 138 << 8 | 123;
        int RESPONSE_USERSIGN = 139 << 8 | 123;

        /**
         * 礼物回调
         */
        int REQUEST_GIFT = 140 << 8 | 123;
        int RESPONSE_GIFT = 141 << 8 | 123;

        /**
         * 心跳检测接口
         */
        int REQUEST_HEARTBEAT = 142 << 8 | 123;
        int RESPONSE_HEARTBEAT = 143 << 8 | 123;


        /**
         * 刷新兑换规则接口
         */
        int REQUEST_REFRESHRULE = 144 << 8 | 123;
        int RESPONSE_REFRESHRULE = 145 << 8 | 123;

        /**
         * 金币兑换接口
         */
        int REQUEST_EXCHANGE = 146 << 8 | 123;
        int RESPONSE_EXCHANGE = 147 << 8 | 123;

        /**
         * 查询用户票和金币信息接口
         */
        int REQUEST_QUERYCOINANDTICKET = 148 << 8 | 123;
        int RESPONSE_QUERYCOINANDTICKET = 149 << 8 | 123;

        /**
         * 点心签到接口
         */
        int REQUEST_SIGN_HEART = 150 << 8 | 123;
        int RESPONSE_SIGN_HEAET = 151 << 8 | 123;
        /**
         * 金币兑换心接口
         */
        int REQUEST_EXCHANGE_COIN_HEART = 152 << 8 | 123; 
        int RESPONSE_EXCHANGE_COIN_HEART = 153 << 8 | 123; 
        
        /**
         * 查询主播趋势图接口
         */
        int REQUEST_IDOL_STATISTICINFO = 154 << 8 | 123; 
        int RESPONSE_IDOL_STATISTICINFO = 155 << 8 | 123; 
        
        /**
         * 给小黑熊entProxy查询主播趋势图接口,原有的sid:123 已被占用
         */
        int REQUEST_IDOL_STATISTICINFO1 = 154 << 8 | 142; 
        int RESPONSE_IDOL_STATISTICINFO1 = 155 << 8 | 142;

        /**
         * 页游点心签到接口
         */
        int REQUEST_WEB_GAME_SIGN_HEART = 156 << 8 | 123;
        int RESPONSE_WEB_GAME_SIGN_HEAET = 157 << 8 | 123;
        
        /**
         * 送心通知
         */
        int REQUEST_CONSUME_NOTIFY = 1009 << 8 | 147;
        int RESPONSE_CONSUME_NOTIFY = 1010 << 8 | 147;
    }

    public interface PC {
        int MAX = 5002;

        /**
         * 活动是否开通接口
         */
        int REQUEST_CAR_IS_OPEN = 1000;
        int RESPONSE_CAR_IS_OPEN = 1001;

        /**
         * 用户进来直播间跑马灯接口
         */
        int REQUEST_USER_ENTRY = 1002;
        int RESPONSE_USER_ENTRY = 1003;

        /**
         * 排名车队数等信息更新接口
         */
        int REQUEST_INFO_NOTITY = 1004;
        int RESPONSE_INFO_NOTITY = 1005;

        /**
         * 车队数达到XX流光展示
         */
        int REQUEST_CAR_NOTITY = 1006;
        int RESPONSE_CAR_NOTITY = 1007;

        /**
         * 榜单接口
         */
        int REQUEST_RANK_TOP5 = 1008;
        int RESPONSE_RANK_TOP5 = 1009;

        /**
         * 查询用户信息接口
         */
        int REQUEST_QUERYUSERINFO = 1010;
        int RESPONSE_QUERYUSERINFO = 1011;

        /**
         * 用户领取入队卡或碎片接口
         */
        int REQUEST_USERSIGN = 1012;
        int RESPONSE_USERSIGN = 1013;


        /**
         * 关闭活动条接口
         */
        int REQUEST_CLOSE = 1014;
        int RESPONSE_CLOSE = 1015;


        /**
         * 用户送入队卡
         */
        int REQUEST_CAR_TEAM_NOTITY = 1016;
        int RESPONSE_CAR_TEAM_NOTITY = 1017;

        /**
         * 车队巡游的广播接口
         */
        int RESPONSE_CARNUM_CRUISE_NOTITY = 1018;


        /**
         * 监听用户是否进频道的接口
         */
        int REQUEST_CARNUM_CRUISE_LISTEN = 1019;
        int RESPONSE_CARNUM_CRUISE_LISTEN = 1020;

        /**
         * 用户进直播间广播接口
         */
        int RESPONSE_FANSENTER_NOTITY = 1021;


        //----------------------点心
        /**
         * 签到点心通知接口
         */
        int REQUEST_SIGN_HEART = 2000;
        int RESPONSE_SIGN_HEART = 2001;

        /**
         * 送心接口
         */
        int REQUEST_CONSUME_HEART = 2002;
        int RESPONSE_CONSUME_HEART = 2003;

        /**
         * 初始化任务条接口
         */
        int REQUEST_INIT_TASKBAR = 2004;
        int RESPONSE_INIT_TASKBAR = 2005;


        /**
         * 送心广播跑马灯接口
         */
        int REQUEST_MARQUEE = 2006;
        int RESPONSE_MARQUEE = 2007;
        
        
        /**
         * 主播开播前的调用接口
         */
        int REQUEST_IDOL_LIVE_BEGIN = 2008;
        int RESPONSE_IDOL_LIVE_BEGIN = 2009;
        
        /**
         * 主播结束后的调用接口
         */
        int REQUEST_IDOL_LIVE_END = 2010;
        int RESPONSE_IDOL_LIVE_END = 2011;
    }


    public interface MOBILE {
        int MAX = 5001;

        /**
         * 签到点心通知接口
         */
        int REQUEST_SIGN_HEART = 2000;
        int RESPONSE_SIGN_HEART = 2001;

        /**
         * 送心接口
         */
        int REQUEST_CONSUME_HEART = 2002;
        int RESPONSE_CONSUME_HEART = 2003;

        /**
         * 初始化任务条接口
         */
        int REQUEST_INIT_TASKBAR = 2004;
        int RESPONSE_INIT_TASKBAR = 2005;
        
    }
}
