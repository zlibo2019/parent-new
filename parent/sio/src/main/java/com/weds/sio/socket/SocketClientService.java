package com.weds.sio.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketClientService {
    private Logger log = LogManager.getLogger();

    private Socket socket;

    private PrintWriter pw;

    @Resource
    private SocketClientMsgServer socketMsgServer;

    @Resource
    private SocketParams socketParams;

    /**
     * 初始化Socket连接并监听返回信息
     */
    public void handleSocket() {
        while (true) {
            try {
                if (socket == null || socket.isClosed()) {
                    socket = new Socket();
                    // 设置发送地址
                    SocketAddress socketAddress = new InetSocketAddress(socketParams.getServerIp(), socketParams.getServerPort());
                    socket.connect(socketAddress, socketParams.getConnTimeout());
                    pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), socketParams.getCharset()));
                }
                //得到一个输入流，接收客户端传递的信息
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), socketParams.getCharset()));
                String responseMsg = br.readLine();
                socketMsgServer.handleMsg(responseMsg);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (socket != null) {
                        socket.close();
                    }
                    if (pw != null) {
                        pw.close();
                    }
                    Thread.sleep(5000);
                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送报文信息
     *
     * @param content
     * @return
     * @throws Exception
     */
    public boolean sendMsg(String content) {
        if (socket == null || socket.isClosed()) {
            log.info("wait for socket connect");
            return false;
        }
        try {
            pw.println(content);
            pw.flush();
            log.info("send：" + content);
        } catch (Exception e) {
            e.printStackTrace();
            socket = null;
            return false;
        }
        return true;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void initParam() {
        socket = null;
    }
}
