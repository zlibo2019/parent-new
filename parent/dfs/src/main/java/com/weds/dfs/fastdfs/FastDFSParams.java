package com.weds.dfs.fastdfs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.fastdfs")
public class FastDFSParams {
    private int connectTimeout;
    private int networkTimeout;
    private String charset;
    private boolean antiStealToken;
    private String secretKey;
    private int trackerHttpPort;
    private String trackerServer;
    private boolean active;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getNetworkTimeout() {
        return networkTimeout;
    }

    public void setNetworkTimeout(int networkTimeout) {
        this.networkTimeout = networkTimeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isAntiStealToken() {
        return antiStealToken;
    }

    public void setAntiStealToken(boolean antiStealToken) {
        this.antiStealToken = antiStealToken;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getTrackerHttpPort() {
        return trackerHttpPort;
    }

    public void setTrackerHttpPort(int trackerHttpPort) {
        this.trackerHttpPort = trackerHttpPort;
    }

    public String getTrackerServer() {
        return trackerServer;
    }

    public void setTrackerServer(String trackerServer) {
        this.trackerServer = trackerServer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
