package com.yy.ent.platform.signcar.common.yyp;

import com.yy.ent.commons.protopack.marshal.BeanMarshal;
import com.yy.ent.commons.protopack.util.Uint;

public class UserInfoBeanMarshal  extends BeanMarshal{
   
    public Uint result ;
    public String data;
    @Override
    public String toString() {
        return "UserInfoBeanMarshal [result=" + result + ", data=" + data + "]";
    }
}
