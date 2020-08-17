package com.weds.core.socket;

public class ConnectInfo {
    private String host;
    private int port;
    private String keystorePath;
    private String keystorePassword;
    private String trustKeystorePath;
    private String caPassword;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTrustKeystorePath() {
        return trustKeystorePath;
    }

    public void setTrustKeystorePath(String trustKeystorePath) {
        this.trustKeystorePath = trustKeystorePath;
    }

    public String getCaPassword() {
        return caPassword;
    }

    public void setCaPassword(String caPassword) {
        this.caPassword = caPassword;
    }
}
