package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class AuthWxResp {
    @JSONField(name = "return_code")
    private String merchantNo;

    @JSONField(name = "return_code")
    private String redirectUri;

    @JSONField(name = "return_code")
    private String terminalNo;

    @JSONField(name = "return_code")
    private String keySign;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getKeySign() {
        return keySign;
    }

    public void setKeySign(String keySign) {
        this.keySign = keySign;
    }
}
