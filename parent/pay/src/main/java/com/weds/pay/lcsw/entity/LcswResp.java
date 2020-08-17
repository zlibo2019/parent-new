package com.weds.pay.lcsw.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class LcswResp {
    @JSONField(name = "return_code")
    private String returnCode;
    @JSONField(name = "return_msg")
    private String returnMsg;
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

    public String getKeySign() {
        return keySign;
    }

    public void setKeySign(String keySign) {
        this.keySign = keySign;
    }
}
