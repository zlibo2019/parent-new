package com.weds.sio.websocket;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public interface HandleMessageService {
    void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage);
}
