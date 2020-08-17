package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class NoticeReq {
    // 响应码：01成功 ，02失败，响应码仅代表通信状态，不代表业务结果
    @JSONField(name = "return_code")
    private String returnCode;
    // 返回信息提示，“签名失败”，“参数格式校验错误"等
    @JSONField(name = "return_msg")
    private String returnMsg;
    // 业务结果：01成功 ，02失败
    @JSONField(name = "result_code")
    private String resultCode;
    // 支付方式，010微信，020 支付宝，060qq钱包，080京东钱包，090口碑，100翼支付
    @JSONField(name = "pay_type")
    private String payType;
    // 付款方用户id，“微信openid”、“支付宝账户”、“qq号”等
    @JSONField(name = "user_id")
    private String userId;
    // 商户名称
    @JSONField(name = "merchant_name")
    private String merchantName;
    // 商户号
    @JSONField(name = "merchant_no")
    private String merchantNo;
    // 终端号
    @JSONField(name = "terminal_id")
    private String terminalId;
    // 终端流水号，此处传商户发起预支付或公众号支付时所传入的交易流水号
    @JSONField(name = "terminal_trace")
    private String terminalTrace;
    // 终端交易时间，yyyyMMddHHmmss，全局统一时间格式（01时参与拼接）
    @JSONField(name = "terminal_time")
    private String terminalTime;
    // 当前支付终端流水号，与pay_time同时传递，返回时不参与签名
    @JSONField(name = "pay_trace")
    private String payTrace;
    // 当前支付终端交易时间，yyyyMMddHHmmss，全局统一时间格式，与pay_trace同时传递
    @JSONField(name = "pay_time")
    private String payTime;
    // 金额，单位分
    @JSONField(name = "total_fee")
    private String totalFee;
    // 支付完成时间，yyyyMMddHHmmss，全局统一时间格式
    @JSONField(name = "end_time")
    private String endTime;
    // 利楚唯一订单号
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    // 通道订单号，微信订单号、支付宝订单号等
    @JSONField(name = "channel_trade_no")
    private String channelTradeNo;
    // 附加数据，原样返回
    @JSONField(name = "attach")
    private String attach;
    // 实收金额，pay_type为010、020、090时必填
    @JSONField(name = "receipt_fee")
    private String receiptFee;
    // 签名字符串,拼装所有必传参数+令牌，32位md5加密转换
    @JSONField(name = "key_sign")
    private String keySign;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getChannelTradeNo() {
        return channelTradeNo;
    }

    public void setChannelTradeNo(String channelTradeNo) {
        this.channelTradeNo = channelTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getReceiptFee() {
        return receiptFee;
    }

    public void setReceiptFee(String receiptFee) {
        this.receiptFee = receiptFee;
    }

    public String getKeySign() {
        return keySign;
    }

    public void setKeySign(String keySign) {
        this.keySign = keySign;
    }
}
