package com.weds.sio.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketIOService {
    @Resource
    private SocketIOServer socketIOServer;

    @Resource
    private SocketIOParams socketIOParams;

    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    private Logger log = LogManager.getLogger();

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam(socketIOParams.getUserId());
        if (!StringUtils.isEmpty(userId)) {
            log.info("用户{}连接, sessionId: {}", userId, client.getSessionId().toString());
            clientMap.put(userId, client);
            client.sendEvent(socketIOParams.getLoginEvent(), "connect success");
        }
    }

    @OnDisconnect
    public void onDisConnect(SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam(socketIOParams.getUserId());
        if (!StringUtils.isEmpty(userId)) {
            log.info("用户{}离线", userId);
            clientMap.remove(userId);
            client.disconnect();
        }
    }

    public void pushMessage(String type, String userId, Object content) {
        if (!StringUtils.isEmpty(userId)) {
            SocketIOClient client = clientMap.get(userId);
            if (client != null) {
                client.sendEvent(type, content);
            }
        }
    }

    public void pushAll(String type, Object content) {
        // 获取全部客户端
        Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
        for (SocketIOClient socket : allClients) {
            socket.sendEvent(type, content);
        }
    }

    public void startSocketIO() {
        if (socketIOServer != null) {
            socketIOServer.start();
        }
    }

    public void stopSocketIO() {
        if (socketIOServer != null) {
            socketIOServer.stop();
        }
    }
}
