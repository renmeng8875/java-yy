package com.yy.ent.platform.signcar.common.yyp;

import com.yy.ent.commons.protopack.marshal.BeanMarshal;

public class CoinResultMarshal extends BeanMarshal {
    public Integer result;
    public Integer coinNum;

    @Override
    public String toString() {
        return "CoinBeanMarshal [result=" + result + ",coinNum=" + coinNum + "]";
    }

    public Integer getResult() {
        return result == null ? 0 : result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getCoinNum() {
        return coinNum == null ? 0 : coinNum;
    }

    public void setCoinNum(Integer coinNum) {
        this.coinNum = coinNum;
    }


}
