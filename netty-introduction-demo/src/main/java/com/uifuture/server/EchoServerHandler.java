/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 处理服务器端通道
 * ChannelInboundHandlerAdapter继承自ChannelHandlerAdapter，实现了ChannelInboundHandler接口
 * ChannelInboundHandler接口提供了不同的事件处理方法，可进行重写
 * 实现了服务器的业务逻辑，决定了连接创建后和接收到信息后该如何处理
 *
 * @author chenhx
 * @version DiscardServerHandler.java, v 0.1 2018-07-13 下午 4:05
 */

//Sharable注解 标识这类的实例之间可以在 channel 里面共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 每个信息入站都会调用
     * 接收数据进行处理
     * 事件处理程序方法。每当从客户端接收到新数据时，使用该方法来接收客户端的消息。 在此示例中，接收到的消息的类型为ByteBuf。
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("客户端发来消息: " + in.toString(CharsetUtil.UTF_8));
        //ChannelHandlerContext提供各种不同的操作用于触发不同的I/O时间和操作
        //调用write方法来逐字返回接收到的信息
        //这里我们不需要调用释放，因为Netty会在写的时候自动释放
        //只调用write是不会释放的，它会缓存，直到调用flush
        //将所接收的消息返回给发送者。注意，这还没有冲刷数据
        ctx.write(in);
    }

    /**
     *  当前读操作读取的最后一个消息被channelRead()方法消费时调用. 如果ChannelOption.AUTO_READ 属性被设置为off,
     *  不会再尝试从当前channel中读取inbound数据, 直到ChannelHandlerContext.read()方法被调用.
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("channel 通道读取完成");
        //冲刷所有待审消息到远程节点。关闭通道后，操作完成
        //第一种写法 写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
        //ctx.flush(); // 第二种方法：在client端关闭channel连接，这样的话，会触发两次channelReadComplete方法。
        //ctx.flush().close().sync(); // 第三种：改成这种写法也可以，但是这中写法，没有第一种方法的好。
    }

    /**
     * 读操作时捕获到异常时调用
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        //打印异常堆栈跟踪
        cause.printStackTrace();
        //关闭通道
        ctx.close();
    }


}