package com.yy.ent.platform.signcar.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

/**
 * RankData
 *
 * @author suzhihua
 * @date 2015/11/11.
 */
public class RankData implements Comparable<RankData> {
    private int car1;
    private int car2;
    private int car3;
    private int total;
    private long time;
    private int rank;
    private String uid;
    private String chid;
    private String nick;

    public RankData() {
    }

    public RankData(Map<String, String> map) {
        this.car1 = getInt(map.get("car1"));
        this.car2 = getInt(map.get("car2"));
        this.car3 = getInt(map.get("car3"));
        total = car1 + car2 + car3;
        this.time = getLong(map.get("time"));
        this.uid = map.get("uid");
        this.chid = map.get("chid");
        this.nick = map.get("nick");
        this.rank = getInt(map.get("rank"));
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    private int getInt(String str) {
        if (str == null) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    private long getLong(String str) {
        if (str == null) {
            return 0;
        }
        return Long.parseLong(str);
    }

    public int getCar1() {
        return car1;
    }

    public int getCar2() {
        return car2;
    }

    public int getCar3() {
        return car3;
    }

    public int getTotal() {
        return total;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUid() {
        return uid;
    }

    public void setCar1(int car1) {
        this.car1 = car1;
    }

    public void setCar2(int car2) {
        this.car2 = car2;
    }

    public void setCar3(int car3) {
        this.car3 = car3;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRank() {
        return rank;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChid() {
        return chid;
    }

    public void setChid(String chid) {
        this.chid = chid;
    }

    @Override
    public int compareTo(RankData that) {
        //先按车总数
        int result = that.total - total;
        if (result != 0) {
            return result;
        }
        //再按顶级车总数
        result = that.car3 - car3;
        if (result != 0) {
            return result;
        }
        //再按豪华车总数
        result = that.car2 - car2;
        if (result != 0) {
            return result;
        }
        //再按基本车总数
        result = that.car1 - car1;
        if (result != 0) {
            return result;
        }
        //再按先达到时间
        result = (int) (time - that.time);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
