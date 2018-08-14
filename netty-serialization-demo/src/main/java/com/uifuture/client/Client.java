/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.client;

import com.uifuture.protocol.Request;
import com.uifuture.protocol.Response;
import com.uifuture.protocol.TinyDecoder;
import com.uifuture.protocol.TinyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author chenhx
 * @version Client.java, v 0.1 2018-08-08 下午 3:07
 */
public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        final String host = "127.0.0.1";
        final int port = 8081;

        new Client(host, port).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline()
                                    // 添加编解码. 发送自定义的类型, 而Handler的方法接收的msg参数的实际类型也是相应的自定义类了
                                    .addLast(new TinyEncoder(Request.class))
                                    .addLast(new TinyDecoder(Response.class))
                                    .addLast(new ClientHandler());
                        }
                    });

            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}