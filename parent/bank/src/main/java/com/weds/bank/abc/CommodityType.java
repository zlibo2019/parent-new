package com.weds.bank.abc;

public enum CommodityType {
    // 充值类 0101:支付账户充值
    // 消费类 0201:虚拟类,0202:传统类,0203:实名类
    // 转账类 0301:本行转账,0302:他行转账
    // 缴费类 0401:水费,0402:电费,0403:煤气费,0404:有线电视费,0405:通讯费,
    // 0406:物业费,0407:保险费,0408:行政费用,0409:税费,0410:学费,0499:其他
    // 理财类 0501:基金,0502:理财产品,0599:其他
    CZZFZHCZ("0101"),
    XFXNL("0201"),
    XFCTL("0202"),
    XFSML("0203"),
    ZZBXZZ("0301"),
    ZZTXZZ("0302"),
    JFSF("0401"),
    JFDF("0402"),
    JFMQF("0403"),
    JFYXDSF("0404"),
    JFTXF("0405"),
    JFWYF("0406"),
    JFBXF("0407"),
    JFXZFY("0408"),
    JFSHF("0409"),
    JFXF("0410"),
    JFQT("0499"),
    LCJJ("0501"),
    LCLCCP("0502"),
    LCQT("0599");

    private String type;

    private CommodityType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
