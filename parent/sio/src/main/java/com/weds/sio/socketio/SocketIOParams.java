package com.weds.sio.socketio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.socket.io")
public class SocketIOParams {
    private boolean active;
    private String hostname = "127.0.0.1";
    private int port = 20078;
    private String userId = "userId";
    private String loginEvent = "login";

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginEvent() {
        return loginEvent;
    }

    public void setLoginEvent(String loginEvent) {
        this.loginEvent = loginEvent;
    }
}
