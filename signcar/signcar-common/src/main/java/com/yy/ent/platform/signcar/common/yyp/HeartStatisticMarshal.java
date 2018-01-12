package com.yy.ent.platform.signcar.common.yyp;

import com.yy.ent.commons.protopack.marshal.BeanMarshal;

public class HeartStatisticMarshal extends BeanMarshal{
    public Integer result ;
    public String data;
    @Override
    public String toString() {
        return "HeartStatisticMarshal [result=" + result + ", data=" + data + "]";
    }

}
