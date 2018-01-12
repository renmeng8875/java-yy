package com.yy.ent.platform.signcar.common;

/**
 * TaskbarInfo
 *
 * @author suzhihua
 * @date 2015/12/19.
 */
public class TaskbarInfo extends BaseModel {
    private long idolHeartNum;
    private long idolFansNum;
    private long fansHeartConsumeNum;
    private long fansHeartNum;
    private String idolHead;
    private String idolNick;

    public long getIdolHeartNum() {
        return idolHeartNum;
    }

    public void setIdolHeartNum(long idolHeartTotal) {
        this.idolHeartNum = idolHeartTotal;
    }

    public long getIdolFansNum() {
        return idolFansNum;
    }

    public void setIdolFansNum(long idolFansNum) {
        this.idolFansNum = idolFansNum;
    }

    public long getFansHeartConsumeNum() {
        return fansHeartConsumeNum;
    }

    public void setFansHeartConsumeNum(long fansHeartConsumeNum) {
        this.fansHeartConsumeNum = fansHeartConsumeNum;
    }

    public long getFansHeartNum() {
        return fansHeartNum;
    }

    public void setFansHeartNum(long fansHeartNum) {
        this.fansHeartNum = fansHeartNum;
    }

    public String getIdolHead() {
        return idolHead;
    }

    public void setIdolHead(String idolHead) {
        this.idolHead = idolHead;
    }

    public String getIdolNick() {
        return idolNick;
    }

    public void setIdolNick(String idolNick) {
        this.idolNick = idolNick;
    }
}
