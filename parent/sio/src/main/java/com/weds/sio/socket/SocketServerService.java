package com.weds.sio.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerService {
    private Logger log = LogManager.getLogger();

    @Resource
    private SocketServerMsgServer socketServerMsgServer;

    @Resource
    private SocketParams socketParams;

    /**
     * 初始化Socket连接并监听返回信息
     */
    public void handleSocketServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(socketParams.getServerPort());
        serverSocket.setSoTimeout(socketParams.getReadTimeout());
        log.info("socket server start");

        while (true) {
            //侦听并接受到此套接字的连接,返回一个Socket对象
            Socket socket = serverSocket.accept();
            new SocketHandler(socket).start();
        }
        // socket.shutdownOutput();//关闭输出流
    }

    class SocketHandler extends Thread {
        private Socket socket;

        SocketHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //得到一个输入流，接收客户端传递的信息
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), socketParams.getCharset()));
                //获取一个输出流，向客户端发送信息
                PrintWriter  pw = new PrintWriter (new OutputStreamWriter(socket.getOutputStream(), socketParams.getCharset()));
                while (true) {
                    String receive = br.readLine();
                    log.info("receive:" + receive);
                    String send = socketServerMsgServer.handleMsg(receive);
                    log.info("send:" + send);
                    pw.println(send);
                    pw.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
