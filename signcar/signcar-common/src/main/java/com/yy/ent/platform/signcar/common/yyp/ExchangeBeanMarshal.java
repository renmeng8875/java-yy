package com.yy.ent.platform.signcar.common.yyp;

import java.util.Map;

import com.yy.ent.commons.protopack.marshal.BeanMarshal;

public class ExchangeBeanMarshal extends BeanMarshal{
    public Integer result ;
    public Long uid;
    public Integer coinNum;
    public Map<String,String> extendInfo;
    @Override
    public String toString() {
        return "ExchangeBeanMarshal [result=" + result + ", uid=" + uid + ", coinNum=" + coinNum
                + ", extendInfo=" + extendInfo + "]";
    }

}
