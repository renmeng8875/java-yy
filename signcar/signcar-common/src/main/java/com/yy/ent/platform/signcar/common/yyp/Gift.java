package com.yy.ent.platform.signcar.common.yyp;

import com.yy.ent.commons.protopack.marshal.BeanMarshal;
import com.yy.ent.commons.protopack.util.Uint;

import java.util.Map;

/**
 * Gift
 *
 * @author suzhihua
 * @date 2015/11/12.
 */
public class Gift extends BeanMarshal {
//    uint32_t fromId;
//    uint32_t toId;
//    uint32_t type;
//    uint32_t num;
//    string fromName;
//    string toName;
//    string appData;
//    map<string, string> extendInfo;

    public Uint fromId;
    public Uint toId;
    public Uint type;
    public Uint num;
    public String fromName;
    public String toName;
    public String appData;
    public Map<String, String> extendInfo;

    @Override
    public String toString() {
        return "Gift{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", type=" + type +
                ", num=" + num +
                ", fromName='" + fromName + '\'' +
                ", toName='" + toName + '\'' +
                ", appData='" + appData + '\'' +
                ", extendInfo=" + extendInfo +
                '}';
    }
}
