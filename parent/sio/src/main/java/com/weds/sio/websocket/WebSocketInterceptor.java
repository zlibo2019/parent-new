package com.weds.sio.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor {

    private Logger log = LogManager.getLogger();

    //进入hander之前的拦截
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        log.info("WebSocket-beforeHandshake拦截器");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            // HttpSession session = serverHttpRequest.getServletRequest().getSession();
            // map.put(WebSocketConstants.WEBSOCKET_USER_ID, session.getId());
            String query = serverHttpRequest.getURI().getQuery();
            String[] params = query.split("[&]");
            for (String param : params) {
                String[] temp = param.split("[=]");
                map.put(temp[0], temp[1]);
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
        log.info("WebSocket-afterHandshake拦截器");
    }
}
