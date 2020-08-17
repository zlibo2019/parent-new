package com.weds.bean.jwt;

import com.alibaba.fastjson.JSONObject;

public class JwtEntity {
    private JSONObject pdata;
    private JwtParams jwtParams;

    public JSONObject getPdata() {
        return pdata;
    }

    public void setPdata(JSONObject pdata) {
        this.pdata = pdata;
    }

    public JwtParams getJwtParams() {
        return jwtParams;
    }

    public void setJwtParams(JwtParams jwtParams) {
        this.jwtParams = jwtParams;
    }
}
