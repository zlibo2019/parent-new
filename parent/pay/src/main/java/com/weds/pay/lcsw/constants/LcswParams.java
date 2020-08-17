package com.weds.pay.lcsw.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weds.pay.lcsw")
public class LcswParams {
    // 商户号
    private String merchantNo;
    // 终端号
    private String terminalId;
    // 令牌
    private String accessToken;
    // 公众号预支付Url
    private String wxPrePayUrl;
    // 支付查询
    private String payQueryUrl;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getWxPrePayUrl() {
        return wxPrePayUrl;
    }

    public void setWxPrePayUrl(String wxPrePayUrl) {
        this.wxPrePayUrl = wxPrePayUrl;
    }

    public String getPayQueryUrl() {
        return payQueryUrl;
    }

    public void setPayQueryUrl(String payQueryUrl) {
        this.payQueryUrl = payQueryUrl;
    }
}
