/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.unpack.server;

import com.uifuture.unpack.model.User;
import com.uifuture.unpack.protocol.Request;
import com.uifuture.unpack.protocol.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chenhx
 * @version ServerHandler.java, v 0.1 2018-08-08 下午 2:52
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        System.out.println("服务端接收到的消息 : " + request);
        Response response = new Response();
        response.setRequestId(2L);
        User user = new User();
        user.setUsername("测试");
        user.setPassword("1234");
        user.setAge(21);
        response.setResult(user);
        //addListener是非阻塞的，异步执行。它会把特定的ChannelFutureListener添加到ChannelFuture中，然后I/O线程会在I/O操作相关的future完成的时候通知监听器。
        ctx.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture ->
                System.out.println("接口响应:" + request.getRequestId())
        );
    }
}