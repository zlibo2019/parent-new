package com.weds.sio.socket;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.socket")
public class SocketParams {
    private String serverIp;
    private int serverPort;
    private String charset;
    private int connTimeout;
    private int readTimeout;
    private boolean serverActive;
    private boolean clientActive;
    private boolean active;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isServerActive() {
        return serverActive;
    }

    public void setServerActive(boolean serverActive) {
        this.serverActive = serverActive;
    }

    public boolean isClientActive() {
        return clientActive;
    }

    public void setClientActive(boolean clientActive) {
        this.clientActive = clientActive;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
