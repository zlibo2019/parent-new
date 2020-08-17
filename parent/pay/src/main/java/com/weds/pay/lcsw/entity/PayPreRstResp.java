package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class PayPreRstResp {
    //	微信公众号支付返回字段，公众号id
    @JSONField(name = "appId")
    private String appId;
    //	微信公众号支付返回字段，时间戳，示例：1414561699，标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。
    @JSONField(name = "timeStamp")
    private String timeStamp;
    //	微信公众号支付返回字段，随机字符串
    @JSONField(name = "nonceStr")
    private String nonceStr;
    //	微信公众号支付返回字段，订单详情扩展字符串，示例：prepay_id=123456789，统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=
    @JSONField(name = "package_str")
    private String packageStr;
    //	微信公众号支付返回字段，签名方式，示例：MD5,RSA
    @JSONField(name = "signType")
    private String signType;
    //	微信公众号支付返回字段，签名
    @JSONField(name = "paySign")
    private String paySign;
    //	支付宝JSAPI支付返回字段用于调起支付宝JSAPI
    @JSONField(name = "ali_trade_no")
    private String aliTradeNo;
    //	qq钱包公众号支付
    @JSONField(name = "token_id")
    private String tokenId;

    private PayPreResp payPreResp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getAliTradeNo() {
        return aliTradeNo;
    }

    public void setAliTradeNo(String aliTradeNo) {
        this.aliTradeNo = aliTradeNo;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public PayPreResp getPayPreResp() {
        return payPreResp;
    }

    public void setPayPreResp(PayPreResp payPreResp) {
        this.payPreResp = payPreResp;
    }
}
