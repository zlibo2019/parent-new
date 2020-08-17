package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class PayQueryRstResp {
    // 交易订单状态，SUCCESS支付成功，REFUND转入退款，NOTPAY未支付，CLOSED已关闭，USERPAYING用户支付中，REVOKED已撤销，NOPAY未支付支付超时，PAYERROR支付失败
    @JSONField(name = "trade_state")
    private String tradeState;
    // 通道订单号，微信订单号、支付宝订单号等，返回时不参与签名
    @JSONField(name = "channel_trade_no")
    private String channelTradeNo;
    // 银行渠道订单号，微信支付时显示在支付成功页面的条码，可用作扫码查询和扫码退款时匹配
    @JSONField(name = "channel_order_no")
    private String channelOrderNo;
    // 付款方用户id，“微信openid”、“支付宝账户”、“qq号”等，返回时不参与签名
    @JSONField(name = "user_id")
    private String userId;
    // 附加数据，原样返回，返回时不参与签名
    @JSONField(name = "attach")
    private String attach;
    // 实收金额，pay_type为010、020、090时必填
    @JSONField(name = "receipt_fee")
    private String receiptFee;
    // 当前支付终端流水号
    @JSONField(name = "pay_trace")
    private String payTrace;
    // 当前支付终端交易时间，yyyyMMddHHmmss，全局统一时间格式
    @JSONField(name = "pay_time")
    private String payTime;

    private PayQueryResp payQueryResp;

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getChannelTradeNo() {
        return channelTradeNo;
    }

    public void setChannelTradeNo(String channelTradeNo) {
        this.channelTradeNo = channelTradeNo;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public PayQueryResp getPayQueryResp() {
        return payQueryResp;
    }

    public void setPayQueryResp(PayQueryResp payQueryResp) {
        this.payQueryResp = payQueryResp;
    }
}
