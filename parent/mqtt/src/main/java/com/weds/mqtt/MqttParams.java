package com.weds.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.mqtt")
public class MqttParams {
    private MqttInboundParams inbound;
    private MqttOutboundParams outbound;
    private boolean active;

    public MqttInboundParams getInbound() {
        return inbound;
    }

    public void setInbound(MqttInboundParams inbound) {
        this.inbound = inbound;
    }

    public MqttOutboundParams getOutbound() {
        return outbound;
    }

    public void setOutbound(MqttOutboundParams outbound) {
        this.outbound = outbound;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
