package com.weds.sio.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class SocketNIOServerService {
    private Logger log = LogManager.getLogger();

    @Resource
    private SocketServerMsgServer socketServerMsgServer;

    @Resource
    private SocketParams socketParams;

    //解码buffer
    private Charset cs;
    //发送数据缓冲区
    private ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    //选择器（叫监听器更准确些吧应该）
    private Selector selector;

    public void handleSocketServer() throws IOException {
        //打开通信信道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //获取套接字
        ServerSocket serverSocket = serverSocketChannel.socket();
        //绑定端口号
        serverSocket.bind(new InetSocketAddress(socketParams.getServerPort()));
        //打开监听器
        selector = Selector.open();
        //将通信信道注册到监听器
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        cs = Charset.forName(socketParams.getCharset());

        //监听器会一直监听，如果客户端有请求就会进入相应的事件处理
        while (true) {
            //select方法会一直阻塞直到有相关事件发生或超时
            selector.select();
            //监听到的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key，以防重复处理
                ite.remove();
                // 连接事件发生
                handle(key);
            }
            // //清除处理过的事件
            // selectionKeys.clear();
        }
    }

    /**
     * 处理不同的事件
     *
     * @param selectionKey
     * @throws IOException
     */
    private void handle(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel;
        SocketChannel socketChannel;
        String requestMsg = "";
        int count;
        if (selectionKey.isAcceptable()) {
            //每有客户端连接，即注册通信信道为可读
            serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            rBuffer.clear();
            count = socketChannel.read(rBuffer);
            //读取数据
            if (count > 0) {
                rBuffer.flip();
                requestMsg = String.valueOf(cs.decode(rBuffer).array());
            }

            log.info("receive:" + requestMsg);
            String responseMsg = socketServerMsgServer.handleMsg(requestMsg);
            log.info("send:" + responseMsg);

            //返回数据
            //接受数据缓冲区
            ByteBuffer sBuffer = ByteBuffer.allocate(responseMsg.getBytes(socketParams.getCharset()).length);
            sBuffer.put(responseMsg.getBytes(socketParams.getCharset()));
            sBuffer.flip();
            socketChannel.write(sBuffer);
            socketChannel.close();
        }
    }
}
