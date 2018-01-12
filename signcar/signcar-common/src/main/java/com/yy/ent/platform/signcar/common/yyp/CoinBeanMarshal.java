package com.yy.ent.platform.signcar.common.yyp;

import java.util.Map;

import com.yy.ent.commons.protopack.marshal.BeanMarshal;

public class CoinBeanMarshal extends BeanMarshal{
    public Integer result ;
    public Long uid;
    public Integer coinNum;
    public Long selfTicketNum;
    public Long channelTicketNum;
    public String rules;
    public Map<String,String> extendInfo;
    @Override
    public String toString() {
        return "CoinBeanMarshal [result=" + result + ", uid=" + uid + ", coinNum=" + coinNum
                + ", selfTicketNum=" + selfTicketNum + ", chinnelTicketNum=" + channelTicketNum
                + ", rules=" + rules + ", extendInfo=" + extendInfo + "]";
    }
   
}
