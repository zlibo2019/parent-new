package com.weds.pay.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weds.pay")
public class PayParams {
    // 商户号
    private String type = PayType.WX.getType();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
