package com.weds.bean.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.jwt")
public class JwtParams {
    private String iss = "weds";
    private String sub = "login";
    private Long expMillis = 2592000000L;
    private String aud = "ed";
    private String signKey = "4OFGE6luXcTtnjLMcQn8JanoSl4i1yfb";
    private String coderKey = "01234567890123456789012345678901";
    private String coderIv = "0123456789012345";
    private String filter;
    private boolean active = true;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Long getExpMillis() {
        return expMillis;
    }

    public void setExpMillis(Long expMillis) {
        this.expMillis = expMillis;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getCoderKey() {
        return coderKey;
    }

    public void setCoderKey(String coderKey) {
        this.coderKey = coderKey;
    }

    public String getCoderIv() {
        return coderIv;
    }

    public void setCoderIv(String coderIv) {
        this.coderIv = coderIv;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
