/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.hearbest.server;

import com.uifuture.hearbest.model.RequestInfo;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chenhx
 * @version ServerHandler.java, v 0.1 2018-08-08 下午 2:52
 */
public class ServerHandler extends SimpleChannelInboundHandler {
    private static final String SUCCESS_KEY = "auth_success_key";
    private static HashMap<String, String> AUTH_IP_MAP = new HashMap<>();
    private static Set<String> AUTH_IP_SET = new HashSet<>();

    static {
        AUTH_IP_MAP.put("192.168.21.89", "1234");
    }

    /**
     * 进行认证
     * 在这里是根据ip和密码进行认证。也可以用账号和密码进行认证获取进行签名等等
     *
     * @param ctx
     * @param msg
     * @return
     */
    private boolean auth(ChannelHandlerContext ctx, Object msg) {
        String[] ret = ((String) msg).split(",");
        String auth = AUTH_IP_MAP.get(ret[0]);
        if (!StringUtil.isNullOrEmpty(auth) && auth.equals(ret[1])) {
            // 认证成功, 返回确认信息
            ctx.writeAndFlush(SUCCESS_KEY);
            //添加到已认证机器上
            AUTH_IP_SET.add(ret[0]);
            return true;
        } else {
            ctx.writeAndFlush("auth failure !").addListener(ChannelFutureListener.CLOSE);
            return false;
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            auth(ctx, msg);
        } else if (msg instanceof RequestInfo) {
            RequestInfo info = (RequestInfo) msg;
            if (!AUTH_IP_SET.contains(info.getIp())) {
                System.out.println("尚未认证的机器..." + info.getIp());
                return;
            }
            System.out.println("--------------------" + System.currentTimeMillis() + "------------------------");
            System.out.println("当前主机ip为: " + info.getIp());
            System.out.println("当前主机cpu情况: ");
            HashMap<String, Object> cpu = info.getCpuPercMap();
            System.out.println("总使用率: " + cpu.get("combined"));
            System.out.println("用户使用率: " + cpu.get("user"));
            System.out.println("系统使用率: " + cpu.get("sys"));
            System.out.println("等待率: " + cpu.get("wait"));
            System.out.println("空闲率: " + cpu.get("idle"));

            System.out.println("当前主机memory情况: ");
            HashMap<String, Object> memory = info.getMemoryMap();
            System.out.println("内存总量: " + memory.get("total"));
            System.out.println("当前内存使用量: " + memory.get("used"));
            System.out.println("当前内存剩余量: " + memory.get("free"));
            System.out.println("--------------------------------------------");

            ctx.writeAndFlush("success");
        } else {
            ctx.writeAndFlush("error").addListener(ChannelFutureListener.CLOSE);
        }
    }
}