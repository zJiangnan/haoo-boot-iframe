package com.haoo.iframe.template.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOEchoServer {

    private static class EchoClientHandle implements Runnable {

        //客户端
        private SocketChannel clientChannel;
        // 循环结束标记
        private boolean flag = true;
        public EchoClientHandle(SocketChannel clientChannel){
            this.clientChannel = clientChannel;
        }

        @Override
        public void run() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            try {
                while (this.flag){
                    byteBuffer.clear();
                    int read = this.clientChannel.read(byteBuffer);
                    String msg = new String(byteBuffer.array(), 0, read).trim();
                    String outMsg = "【Echo】" + msg + "\n"; // 回应信息
                    if("byebve".equals(msg)){
                        outMsg = "会话结束，下次再见！";
                        this.flag = false;
                    }
                    byteBuffer.clear();
                    byteBuffer.put(outMsg.getBytes());  //回传信息放入缓冲区
                    byteBuffer.flip();
                    this.clientChannel.write(byteBuffer);// 回传信息
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception{
        // 为了性能问题及响应时间，设置固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // NIO基于Channel控制，所以有Selector管理所有的Channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 设置监听端口
        serverSocketChannel.bind(new InetSocketAddress(HostInfo.PORT));
        // 设置Selector管理所有Channel
        Selector selector = Selector.open();
        // 注册并设置连接时处理
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功，监听端口为：" + HostInfo.PORT);
        // NIO使用轮询，当有请求连接时，则启动一个线程
        int keySelect = 0;
        while ((keySelect = selector.select()) > 0){
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                if(next.isAcceptable()){    //  如果是连接的
                    SocketChannel accept = serverSocketChannel.accept();
                    if(accept != null){
                        executorService.submit(new EchoClientHandle(accept));
                    }
                    iterator.remove();
                }
            }
        }
        executorService.shutdown();
        serverSocketChannel.close();
    }
}