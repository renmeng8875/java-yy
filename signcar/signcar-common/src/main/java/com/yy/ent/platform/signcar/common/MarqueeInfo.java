package com.yy.ent.platform.signcar.common;

/**
 * MarqueeInfo
 *
 * @author suzhihua
 * @date 2015/12/29.
 */
public class MarqueeInfo extends BaseModel{
    private Long fansUid;//送心用户uid
    private Integer currentNum;//送心用户本次送心数量

    private Integer totalNum;//送心用户本次送心数量
    private String fansNick;//送心用户本次送心数量

    private Long topChid;
    private Long subChid;
    private Long idolUid;

    public String getFansNick() {
        return fansNick;
    }

    public void setFansNick(String fansNick) {
        this.fansNick = fansNick;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Long getFansUid() {
        return fansUid;
    }

    public void setFansUid(Long fansUid) {
        this.fansUid = fansUid;
    }

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Long getSubChid() {
        return subChid;
    }

    public void setSubChid(Long subChid) {
        this.subChid = subChid;
    }

    public Long getTopChid() {
        return topChid;
    }

    public void setTopChid(Long topChid) {
        this.topChid = topChid;
    }

    public Long getIdolUid() {
        return idolUid;
    }

    public void setIdolUid(Long idolUid) {
        this.idolUid = idolUid;
    }
}
