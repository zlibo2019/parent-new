package com.weds.core.socket;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;

public class SocketSSLClient {
    private static SSLSocket sslSocket;

    public static SSLSocket getSSLSocket(ConnectInfo connectInfo) throws Exception {
        if (sslSocket == null) {
            SocketSSLClient socketSSLClient = new SocketSSLClient();
            sslSocket = socketSSLClient.init(connectInfo);
        }
        return sslSocket;
    }

    private SSLSocket init(ConnectInfo connectInfo) throws Exception {
        System.setProperty("javax.net.debug", "ssl,handshake");

        // System.setProperty("javax.net.ssl.keyStore", "./cfg/client.jks");
        // System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        // System.setProperty("javax.net.ssl.trustStore", "./cfg/servertrust.jks");
        // System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        // SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory
        //         .getDefault();

        SSLContext context = SSLContext.getInstance("SSL");

        //客户端证书库
        KeyStore clientKeystore = KeyStore.getInstance("pkcs12");
        FileInputStream keystoreFis = new FileInputStream(connectInfo.getKeystorePath());
        clientKeystore.load(keystoreFis, connectInfo.getKeystorePassword().toCharArray());

        //信任证书库
        KeyStore trustKeystore = KeyStore.getInstance("jks");
        FileInputStream trustKeystoreFis = new FileInputStream(connectInfo.getTrustKeystorePath());
        trustKeystore.load(trustKeystoreFis, connectInfo.getCaPassword().toCharArray());

        //密钥库
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
        kmf.init(clientKeystore, connectInfo.getKeystorePassword().toCharArray());

        //信任库
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
        tmf.init(trustKeystore);

        //初始化SSL上下文
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        SSLSocket sslSocket = (SSLSocket) context.getSocketFactory().createSocket(connectInfo.getHost(), connectInfo.getPort());
        return sslSocket;
    }
}
