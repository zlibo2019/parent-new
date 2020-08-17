package com.weds.sio.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Set;

public class WebSocketHandlerService extends TextWebSocketHandler {
    private Logger log = LogManager.getLogger();

    @Resource
    private HandleMessageService handleMessageService;

    //新增socket
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.info("成功建立连接");
        WebSocketPool.addUser(webSocketSession);
        // webSocketSession.sendMessage(new TextMessage("成功建立Socket连接"));
        log.info("当前连接数：" + WebSocketPool.getUsers().size());
    }

    //接收socket信息
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        handleMessageService.handleMessage(webSocketSession, webSocketMessage);
    }

    /**
     * 发送信息给指定用户
     *
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String clientId, TextMessage message) {
        if (WebSocketPool.getUsers().get(clientId) == null) return false;
        WebSocketSession session = WebSocketPool.getUsers().get(clientId);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 广播信息
     *
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> clientIds = WebSocketPool.getUsers().keySet();
        WebSocketSession session = null;
        for (String clientId : clientIds) {
            try {
                session = WebSocketPool.getUsers().get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }

        return allSendSuccess;
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        log.info("连接出错");
        WebSocketPool.delUser(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("连接已关闭：" + status);
        WebSocketPool.delUser(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
