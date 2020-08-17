package com.weds.sio.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @ComponentScan({"com.weds.bean.socket"})
@ConditionalOnProperty(name = "weds.socket.active", havingValue = "true")
@EnableConfigurationProperties(SocketParams.class)
public class SocketConfig {
    private Logger log = LogManager.getLogger();

    @Bean
    @ConditionalOnMissingBean(name = "socketClientMsgServer")
    public SocketClientMsgServer socketClientMsgServer() {
        return response -> log.info("receive:" + response);
    }

    @Bean
    @ConditionalOnMissingBean(name = "socketServerMsgServer")
    public SocketServerMsgServer socketServerMsgServer() {
        return req -> req;
    }

    @ConditionalOnProperty(value = {"weds.socket.serverActive"})
    @Bean
    public SocketServerService socketServerService() {
        return new SocketServerService();
    }

    @ConditionalOnProperty(value = {"weds.socket.clientActive"})
    @Bean
    public SocketClientService socketClientService() {
        return new SocketClientService();
    }
}
