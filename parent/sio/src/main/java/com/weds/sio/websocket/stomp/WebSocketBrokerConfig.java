package com.weds.sio.websocket.stomp;

import com.weds.sio.websocket.WebSocketInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import javax.annotation.Resource;
import javax.websocket.ContainerProvider;
import java.util.List;

@Configuration
// @ComponentScan({"com.weds.bean.websocket.stomp"})
@ConditionalOnProperty(name = "weds.websocket.stomp.active", havingValue = "true")
@EnableConfigurationProperties(WebSocketStompParams.class)
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private Logger log = LogManager.getLogger();

    @Resource
    private WebSocketStompParams webSocketStompParams;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(webSocketStompParams.getBroker());  // 推送消息前缀
        registry.setApplicationDestinationPrefixes(webSocketStompParams.getAppPrefix()); // 应用请求前缀
        registry.setUserDestinationPrefix(webSocketStompParams.getUserPrefix());//推送用户前缀
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
        // .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
        registry.addEndpoint(webSocketStompParams.getEndpoint())
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketInterceptor())
                .withSockJS();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new org.springframework.messaging.converter.StringMessageConverter());
        return true;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(15 * 1000);
        registration.setMessageSizeLimit(16 * 1024);
        registration.setSendBufferSizeLimit(16 * 1024);
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
