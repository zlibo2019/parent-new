package com.weds.sio.socket;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class SocketNIOClientService {
    @Resource
    private SocketClientMsgServer socketMsgServer;

    @Resource
    private SocketParams socketParams;

    //解码buffer
    private Charset cs;

    // 通道管理器
    private Selector selector;

    //发送数据缓冲区
    private ByteBuffer rBuffer = ByteBuffer.allocate(1024);

    /**
     * 初始化Socket连接并监听返回信息
     */
    public void handleSocket() throws IOException {
        // 获得一个Socket通道
        SocketChannel channel = SocketChannel.open();
        // 设置通道为非阻塞
        channel.configureBlocking(false);
        // 获得一个通道管理器
        this.selector = Selector.open();
        // 用channel.finishConnect();才能完成连接
        // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen()方法中调
        channel.connect(new InetSocketAddress(socketParams.getServerIp(), socketParams.getServerPort()));
        // 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件
        channel.register(selector, SelectionKey.OP_CONNECT);

        cs = Charset.forName(socketParams.getCharset());

        // 轮询访问selector
        while (true) {
            selector.select();//select方法会一直阻塞直到有相关事件发生或超时
            Set<SelectionKey> selectionKeys = selector.selectedKeys();//监听到的事件
            Iterator ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key，以防重复处理
                ite.remove();
                // 连接事件发生
                handle(key);
            }
        }
    }

    /**
     * 处理不同的事件
     *
     * @param selectionKey
     * @throws IOException
     */
    private void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel;
        String receiveMsg = "";
        int count;
        if (selectionKey.isConnectable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            if (socketChannel.isConnectionPending()) {
                socketChannel.finishConnect();
            }
            // 设置成非阻塞
            socketChannel.configureBlocking(false);
            // 在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
            socketChannel.register(selector, SelectionKey.OP_READ); // 获得了可读的事件
        } else if (selectionKey.isReadable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            rBuffer.clear();
            count = socketChannel.read(rBuffer);
            //读取数据
            if (count > 0) {
                rBuffer.flip();
                receiveMsg = String.valueOf(cs.decode(rBuffer).array());
            }
            socketMsgServer.handleMsg(receiveMsg);
            ByteBuffer outBuffer = ByteBuffer.wrap(receiveMsg.getBytes());
            socketChannel.write(outBuffer);
        }
    }
}
