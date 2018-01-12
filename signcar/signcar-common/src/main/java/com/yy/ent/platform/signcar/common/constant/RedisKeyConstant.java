package com.yy.ent.platform.signcar.common.constant;

/**
 * RedisKeyConstant
 *
 * @author suzhihua
 * @date 2015/10/27.
 */
public enum RedisKeyConstant {
    SYS_DATE,
    /**
     * 频道车数,只用于排名算法 chid,num
     */
    CHID_CAR_SORT_SET,
    /**
     * 已放排名,前99名的，Map<chid,rank>
     */
    CHID_CAR_RANK_MAP,
    /**
     * 已放排名所有数据,List<RankData>，榜单从这里取数据即可
     */
    CHID_CAR_RANK_ALL,
    CHID_CAR_RANK_TOP5,
    /**
     * 频道各种车的数量,car1,car2,car3,time
     */
    CHID_CAR_MAP,
    CHID_USER_SORTHASH,
    /**
     * 单位时间内需要通知的频道列表，目前每分钟建一个set
     */
    CHID_NOTITY_SET,
    /**
     * 已级处理的通知 单位时间
     */
    CHID_NOTITY_HAS_HANDLE,
    /**
     * 是否广播过活动开启消息
     */
    HAS_OPEN,
    /**
     * 是否广播过活动结束消息
     */
    HAS_CLOSE,
    /**
     * 用户是否获取过碎片或入队卡
     */
    HAS_PRIZE,

    /**
     * 用户是否拥有座驾
     */
    HAS_CAR,
    /**
     * 用户与频道关系 成员为uid_chid拼装
     */
    UID_CHID_CAR_SET,
    /**
     * 票数限制
     */
    TICKETNUMLIMIT,

    /**
     * 防刷限制
     */
    HAS_ADD_GOLD_COIN_SORT_SET,
    /**
     * 抢到金币的用户uid->goldcoin
     */
    GOLD_COIN_MAP,
    /**
     * 金币总数
     */
    GOLD_COIN_TOTAL,
    /**
     * 集体巡游
     */
    CARTEAM_CRUISE,
    /**
     * 跑马灯记录_时间数字,json对象->time
     */
    HEART_MARQUEE_SORTED_SET,
    /**
     * 跑马灯处理时间
     */
    HEART_MARQUEE_HANDLE_TIME,

    /**
     * 同步心数量通知 topchid_subchid_uid
     */
    HEART_NOTIFY_SET,
    /**
     * 同步心数量通知处理时间
     */
    HEART_NOTIFY_HANDLE_TIME,

    /**
     * 用户拥有心记录，对应heartTotal表，每天心有变化的用户_拼上yyyymmdd:hash(map),fansuid->num
     */
    HEART_TOTAL,
    /**
     * 用户每日送心记录_拼上yyyymmdd:hash(map),idoluid_fansuid->num
     */
    HEART_CONSUME_TOTAL,
    /**
     * 主播每日收心粉丝uid记录_拼上yyyymmdd:idoluid(set) fansuid
     */
    HEART_CONSUME_FANSUID_SET,
    /**
     * 主播每日收心总数记录_拼上yyyymmdd(map),idoluid ->num
     */
    HEART_CONSUME_TOTAL_NUM,
    /**
     * 粉丝每日送心总数记录_拼上yyyymmdd:hash(map),fansuid ->num
     */
    HEART_CONSUME_FANSUID_TOTAL_NUM,
    /**
     * 页游流水号
     */
    HEART_CONSUME_SERIAL,
    HEART_FANS_INTIMACY_LIST,
    HEART_FANS_INTIMACY_MAP;
    public static final String PREV_KEY = "signcar:";
    private String name;

    RedisKeyConstant() {
        name = PREV_KEY + name().toLowerCase();
    }

    RedisKeyConstant(String name) {
        this.name = PREV_KEY + name.toLowerCase();
    }

    public String getKey() {
        return name;
    }

    public String getKey(Object key) {
        return name + ":" + key;
    }

    public String getKey(Object key1, Object key2) {
        return name + ":" + key1 + ":" + key2;
    }

    public String getKey(Object key1, Object key2, Object key3) {
        return name + ":" + key1 + ":" + key2 + ":" + key3;
    }
}

