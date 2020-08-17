package com.weds.sio.socketio;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(SocketIOParams.class)
@ConditionalOnProperty(name = "weds.socket.io.active", havingValue = "true")
public class SocketIOConfig {
    @Resource
    private SocketIOParams socketIOParams;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(socketIOParams.getHostname());
        config.setPort(socketIOParams.getPort());

        // // 设置最大的WebSocket帧内容长度限制
        // config.setMaxFramePayloadLength( 1024 * 1024 );
        // // 设置最大HTTP内容长度限制
        // config.setMaxHttpContentLength( 1024 * 1024 );

        config.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData handshakeData) {
                return true;
            }
        });
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

    @Bean
    @ConditionalOnBean(name = "socketIOServer")
    public SocketIOService socketService() {
        return new SocketIOService();
    }
}
