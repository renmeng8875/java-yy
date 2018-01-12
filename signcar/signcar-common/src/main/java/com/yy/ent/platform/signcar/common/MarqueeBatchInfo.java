package com.yy.ent.platform.signcar.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MarqueeInfo
 *
 * @author suzhihua
 * @date 2015/12/29.
 */
public class MarqueeBatchInfo extends BaseModel {
    private static final int MAX_MARQUEE_NUM = 10;//每个频道限制个数
    //其他用户送心了更新首麦相关信息
    private Long topChid;
    private Long subChid;
    private Long idolUid;//首麦uid
    private Long idolHeartNum; //首麦今天收到的心数
    private Long idolFansNum; //首麦今天送心粉丝数


    private Map<String, MarqueeInfo> map = new HashMap<String, MarqueeInfo>();
    private List<MarqueeInfo> marquee = new ArrayList<MarqueeInfo>();

    public void addMarqueeInfo(MarqueeInfo _info) {
        String key = _info.getFansUid().toString();
        if (_info.getIdolUid().equals(idolUid) && map.size() < MAX_MARQUEE_NUM && !map.containsKey(key)) {
            map.put(key, _info);
            marquee.add(_info);
        }
    }

    public List<MarqueeInfo> getMarquee() {
        return marquee;
    }

    public void setMarquee(List<MarqueeInfo> marquee) {
        this.marquee = marquee;
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

    public Long getIdolHeartNum() {
        return idolHeartNum;
    }

    public void setIdolHeartNum(Long idolHeartNum) {
        this.idolHeartNum = idolHeartNum;
    }

    public Long getIdolFansNum() {
        return idolFansNum;
    }

    public void setIdolFansNum(Long idolFansNum) {
        this.idolFansNum = idolFansNum;
    }

    public Map<String, MarqueeInfo> getMap() {
        return map;
    }

    public void setMap(Map<String, MarqueeInfo> map) {
        this.map = map;
    }
}
