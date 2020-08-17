package com.weds.sio.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import javax.annotation.Resource;
import javax.websocket.ContainerProvider;


@Configuration
@EnableWebSocket
// @ComponentScan({"com.weds.bean.websocket"})
@ConditionalOnProperty(name = "weds.websocket.active", havingValue = "true")
@EnableConfigurationProperties(WebSocketParams.class)
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WebSocketParams webSocketParams;

    private Logger log = LogManager.getLogger();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //handler是webSocket的核心，配置入口
        if (webSocketParams.isActive()) {
            registry.addHandler(webSocketHandlerService(), webSocketParams.getUrl()).setAllowedOrigins("*").addInterceptors(new WebSocketInterceptor());
        }
    }

    @ConditionalOnMissingBean(name = "handleMessageService")
    @Bean
    public HandleMessageService handleMessageService() {
        return (webSocketSession, webSocketMessage) -> {
            try {
                String sessionId = WebSocketPool.getClientId(webSocketSession);
                log.info(sessionId + "的消息:" + webSocketMessage.getPayload().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @ConditionalOnBean(name = "handleMessageService")
    @Bean
    public WebSocketHandlerService webSocketHandlerService() {
        return new WebSocketHandlerService();
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(16 * 1024);
        container.setMaxBinaryMessageBufferSize(16 * 1024);
        ContainerProvider.getWebSocketContainer().setDefaultMaxTextMessageBufferSize(20 * 1024);
        return container;
    }
}
