package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class PayQueryResp {
    // 业务结果：01成功 ，02失败 ，03支付中。支付失败和退款成功状态均返回02，具体状态和原因会在return_msg中给出解释
    @JSONField(name = "result_code")
    private String resultCode;
    // 支付方式，010微信，020 支付宝，060qq钱包，080京东钱包，090口碑，100翼支付，110银联二维码
    @JSONField(name = "pay_type")
    private String payType;
    // 商户名称
    @JSONField(name = "merchant_name")
    private String merchantName;
    // 商户号
    @JSONField(name = "merchant_no")
    private String merchantNo;
    // 终端号
    @JSONField(name = "terminal_id")
    private String terminalId;
    // 终端流水号，商户系统的查询流水号，扫呗系统原样返回
    @JSONField(name = "terminal_trace")
    private String terminalTrace;
    // 终端交易时间，yyyyMMddHHmmss，全局统一时间格式
    @JSONField(name = "terminal_time")
    private String terminalTime;
    // 金额，单位分
    @JSONField(name = "total_fee")
    private String totalFee;
    // 支付完成时间，yyyyMMddHHmmss，全局统一时间格式
    @JSONField(name = "end_time")
    private String endTime;
    // 利楚唯一订单号
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
