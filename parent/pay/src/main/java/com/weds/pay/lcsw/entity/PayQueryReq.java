package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class PayQueryReq {
    // 版本号，当前版本100
    @JSONField(name = "pay_ver")
    private String payVer;
    // 支付方式，010微信，020 支付宝，060qq钱包，080京东钱包，090口碑，100翼支付，110银联二维码，000自动识别类型
    @JSONField(name = "pay_type")
    private String payType;
    // 接口类型，当前类型020
    @JSONField(name = "service_id")
    private String serviceId;
    // 商户号
    @JSONField(name = "merchant_no")
    private String merchantNo;
    // 终端号
    @JSONField(name = "terminal_id")
    private String terminalId;
    // 终端查询流水号，填写商户系统的查询流水号
    @JSONField(name = "terminal_trace")
    private String terminalTrace;
    // 终端查询时间，yyyyMMddHHmmss，全局统一时间格式
    @JSONField(name = "terminal_time")
    private String terminalTime;
    // 当前支付终端流水号，与pay_time同时传递
    @JSONField(name = "pay_trace")
    private String payTrace;
    // 当前支付终端交易时间，yyyyMMddHHmmss，全局统一时间格式，与pay_trace同时传递
    @JSONField(name = "pay_time")
    private String payTime;
    // 订单号，查询凭据，可填利楚订单号、微信订单号、支付宝订单号、银行卡订单号任意一个
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    // 签名字符串,拼装所有必传参数+令牌，UTF-8编码，32位md5加密转换
    @JSONField(name = "key_sign")
    private String keySign;

    public String getPayVer() {
        return payVer;
    }

    public void setPayVer(String payVer) {
        this.payVer = payVer;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalTrace() {
        return terminalTrace;
    }

    public void setTerminalTrace(String terminalTrace) {
        this.terminalTrace = terminalTrace;
    }

    public String getTerminalTime() {
        return terminalTime;
    }

    public void setTerminalTime(String terminalTime) {
        this.terminalTime = terminalTime;
    }

    public String getPayTrace() {
        return payTrace;
    }

    public void setPayTrace(String payTrace) {
        this.payTrace = payTrace;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getKeySign() {
        return keySign;
    }

    public void setKeySign(String keySign) {
        this.keySign = keySign;
    }
}
