package com.weds.sio.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class WebSocketPool {

    //在线用户列表
    private static final Map<String, WebSocketSession> users = new HashMap<>();

    public static Map<String, WebSocketSession> getUsers() {
        return users;
    }

    public static String getClientId(WebSocketSession session) {
        try {
            return session.getAttributes().get(WebSocketConstants.WEBSOCKET_USER_ID).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void delUser(WebSocketSession session) {
        users.remove(getClientId(session));
    }

    public static void addUser(WebSocketSession session) {
        users.put(getClientId(session), session);
    }
}
