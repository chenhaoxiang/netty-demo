/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.unpack.client;

import com.uifuture.unpack.model.User;
import com.uifuture.unpack.protocol.Request;
import com.uifuture.unpack.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chenhx
 * @version ClientHandler.java, v 0.1 2018-08-08 下午 3:07
 */
public class ClientHandler extends SimpleChannelInboundHandler<Response> {

    /**
     * 通道注册
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * 服务器的连接被建立后调用
     * 建立连接后该 channelActive() 方法被调用一次
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Request request = new Request();
        request.setRequestId(3L);
        User user = new User();
        user.setUsername("测试客户端");
        user.setPassword("4567");
        user.setAge(21);
        request.setParameter(user);
        //当被通知该 channel 是活动的时候就发送信息
        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
        System.out.println("服务器发来消息 : " + response);
    }

    /**
     * 捕获异常时调用
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        //记录错误日志并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }

}