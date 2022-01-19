package com.haoo.iframe.template.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOEchoClient {

    public static void main(String[] args) throws Exception{
        SocketChannel clientChannel = SocketChannel.open();
        clientChannel.connect(new InetSocketAddress(HostInfo.HOST_NAME,HostInfo.PORT));
        ByteBuffer buffer = ByteBuffer.allocate(50);
        boolean flag = true;
        while (flag){
            buffer.clear();
            String input = InputUtil.getString("请输入待发送的信息：").trim();
            buffer.put(input.getBytes());   //将数据存入缓冲区
            buffer.flip();  //  重置缓冲区
            clientChannel.write(buffer);    //发送数据
            buffer.clear();
            int read = clientChannel.read(buffer);
            buffer.flip();
            System.err.print(new String(buffer.array(), 0, read));
            if("byebye".equalsIgnoreCase(input)){
                flag = false;
            }
        }
        clientChannel.close();
    }
}
