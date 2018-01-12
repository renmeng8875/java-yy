package com.yy.ent.platform.signcar.common.constant;
public enum PrizeType {
    FRAGMENT(1), CARD(2);
    private int value;

    PrizeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}