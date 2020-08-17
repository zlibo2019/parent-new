package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class PayPreReq {
    // 版本号，当前版本100
    @JSONField(name = "pay_ver")
    private String payVer;
    // 支付方式，010微信，020支付宝，060qq钱包，090口碑，100翼支付，140和包支付（仅限和包通道）
    @JSONField(name = "pay_type")
    private String payType;
    // 接口类型，当前类型012
    @JSONField(name = "service_id")
    private String serviceId;
    // 商户号
    @JSONField(name = "merchant_no")
    private String merchantNo;
    // 终端号
    @JSONField(name = "terminal_id")
    private String terminalId;
    // 终端流水号，填写商户系统的订单号
    @JSONField(name = "terminal_trace")
    private String terminalTrace;
    // 终端交易时间，yyyyMMddHHmmss，全局统一时间格式
    @JSONField(name = "terminal_time")
    private String terminalTime;
    // 金额，单位分
    @JSONField(name = "total_fee")
    private String totalFee;
    // 公众号appid，公众号支付时使用的appid（若传入，则open_id需要保持一致）
    @JSONField(name = "sub_appid")
    private String subAppid;
    // 用户标识（微信openid，支付宝userid），pay_type为010及020时需要传入
    @JSONField(name = "open_id")
    private String openId;
    // 订单描述
    @JSONField(name = "order_body")
    private String orderBody;
    // 外部系统通知地址
    @JSONField(name = "notify_url")
    private String notifyUrl;
    // 附加数据，原样返回
    @JSONField(name = "attach")
    private String attach;
    // 订单包含的商品列表信息，Json格式。pay_type为010，020，090时，可选填此字段
    @JSONField(name = "goods_detail")
    private String goodsDetail;
    // 订单优惠标记，代金券或立减优惠功能的参数（字段值：cs和bld）
    @JSONField(name = "goods_tag")
    private String goodsTag;
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

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getSubAppid() {
        return subAppid;
    }

    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrderBody() {
        return orderBody;
    }

    public void setOrderBody(String orderBody) {
        this.orderBody = orderBody;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getKeySign() {
        return keySign;
    }

    public void setKeySign(String keySign) {
        this.keySign = keySign;
    }
}
