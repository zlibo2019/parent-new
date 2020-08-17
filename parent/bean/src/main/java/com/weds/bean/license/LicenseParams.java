package com.weds.bean.license;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.license")
public class LicenseParams {
    private boolean active = true;

    private String customer = "user";

    private String version = "1.0";

    private String product = "WEDS";

    private String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC804QTGLVhpBTMNZICjbc0I0j0VlCUsca5uKsTjdrokN0Crbz0G78MzU6tQ6oAiP1cOCh5opujMvjx5MjodWy6rhp/XnIZ92jsiimcOJGaQHXIc4gnPQfkPoz0mpSCRijvrdMpvRCssqaGT+7M5M9AKpAd6CGxvTc0gsvtOnnHBQIDAQAB";

    private String pollCode;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPollCode() {
        return pollCode;
    }

    public void setPollCode(String pollCode) {
        this.pollCode = pollCode;
    }
}
