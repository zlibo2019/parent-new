package com.weds.dfs.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration
// @ComponentScan({"com.weds.bean.fastdfs"})
@ConditionalOnProperty(name = "weds.fastdfs.active", havingValue = "true")
@EnableConfigurationProperties(FastDFSParams.class)
public class FastDFSConfig {
    @Resource
    private FastDFSParams fastDFSParams;

    // @ConditionalOnProperty(value = {"weds.fastdfs.active"})
    @Bean
    public FastDFSService fastDFSService() {
        return new FastDFSService();
    }

    // @ConditionalOnProperty(value = {"weds.fastdfs.active"})
    @Bean
    public StorageClient storageClient() throws IOException, MyException {
        int g_connect_timeout = fastDFSParams.getConnectTimeout();
        if (g_connect_timeout < 0) {
            g_connect_timeout = 5 * 1000;
        }
        ClientGlobal.g_connect_timeout = g_connect_timeout;

        int g_network_timeout = fastDFSParams.getNetworkTimeout();
        if (g_network_timeout < 0) {
            g_network_timeout = 30 * 1000;
        }
        ClientGlobal.g_network_timeout = g_network_timeout;

        String g_charset = fastDFSParams.getCharset();
        if (g_charset == null || g_charset.length() == 0) {
            g_charset = "ISO8859-1";
        }
        ClientGlobal.g_charset = g_charset;

        String strTrackerServer = fastDFSParams.getTrackerServer();
        if (strTrackerServer == null) {
            throw new MyException("item \"tracker_server\" not found");
        } else {
            String[] szTrackerServers = strTrackerServer.split(";");
            InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];

            for (int i = 0; i < szTrackerServers.length; ++i) {
                String[] parts = szTrackerServers[i].split(":", 2);
                if (parts.length != 2) {
                    throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
                }

                tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }

            ClientGlobal.g_tracker_group = new TrackerGroup(tracker_servers);

            ClientGlobal.g_tracker_http_port = fastDFSParams.getTrackerHttpPort();
            ClientGlobal.g_anti_steal_token = fastDFSParams.isAntiStealToken();
            if (fastDFSParams.isAntiStealToken()) {
                ClientGlobal.g_secret_key = fastDFSParams.getSecretKey();
            }
        }

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        return new StorageClient(trackerServer, storageServer);
    }
}
