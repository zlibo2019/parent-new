package com.weds.pay.constants;

public enum PayType {
    WX("010"), ZFB("020"), QQQB("060"), KB("090"), YZF("100"), SB("900");
    private String type;

    private PayType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}