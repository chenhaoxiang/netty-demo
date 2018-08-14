/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.hearbest.cilent;

import com.uifuture.hearbest.model.RequestInfo;
import com.uifuture.hearbest.utils.SigarUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhx
 * @version ClientHandler.java, v 0.1 2018-08-10 上午 9:58
 */
public class ClientHandler extends SimpleChannelInboundHandler {
    private static final String SUCCESS_KEY = "auth_success_key";
    /**
     * 开一个线程进行心跳包
     */
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    /**
     * 定时任务
     */
    private ScheduledFuture<?> heartBeat;
    /**
     * 主动向服务器发送认证信息
     */
    private InetAddress addr;

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
    public void channelActive(ChannelHandlerContext ctx) throws UnknownHostException {
        addr = InetAddress.getLocalHost();
        System.out.println("addr=" + addr);
        String ip = "192.168.21.89";
        String key = "1234";
        //证书
        String auth = ip + "," + key;
        // 发送认证
        ctx.writeAndFlush(auth);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof String) {
                String ret = (String) msg;
                if (SUCCESS_KEY.equals(ret)) {
                    // 收到认证 确认信息，设置每隔5秒发送心跳消息
                    this.heartBeat = this.scheduler.scheduleWithFixedDelay(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
                    System.out.println("接收到信息:" + msg);
                } else {
                    // 收到心跳包 确认信息
                    System.out.println("接收到信息:" + msg);
                }
            }
        } finally {
            // 只读, 需要手动释放引用计数
            ReferenceCountUtil.release(msg);
        }
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


    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;
        private Integer times = 0;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                if (times++ > 10) {
                    //取消定时任务
                    closeHeartBeat();
                    return;
                }
                System.out.println("第" + times + "次请求...");
                RequestInfo info = new RequestInfo();
                //ip
                info.setIp(addr.getHostAddress());
                Sigar sigar = SigarUtil.getInstance();
                //cpu prec
                CpuPerc cpuPerc = sigar.getCpuPerc();
                HashMap<String, Object> cpuPercMap = new HashMap<>();
                cpuPercMap.put("combined", cpuPerc.getCombined());
                cpuPercMap.put("user", cpuPerc.getUser());
                cpuPercMap.put("sys", cpuPerc.getSys());
                cpuPercMap.put("wait", cpuPerc.getWait());
                cpuPercMap.put("idle", cpuPerc.getIdle());
                // memory
                Mem mem = sigar.getMem();
                HashMap<String, Object> memoryMap = new HashMap<>();
                memoryMap.put("total", mem.getTotal() / 1024L / 1024L);
                memoryMap.put("used", mem.getUsed() / 1024L / 1024L);
                memoryMap.put("free", mem.getFree() / 1024L / 1024L);
                info.setCpuPercMap(cpuPercMap);
                info.setMemoryMap(memoryMap);

                ctx.writeAndFlush(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 出现异常调用
         *
         * @param ctx
         * @param cause
         * @throws Exception
         */
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            // 取消定时发送心跳包的任务
            if (heartBeat != null) {
                heartBeat.cancel(true);
                heartBeat = null;
            }
            ctx.fireExceptionCaught(cause);
        }

        /**
         * 取消定时任务
         */
        public void closeHeartBeat() {
            // 取消定时发送心跳包的任务
            if (heartBeat != null) {
                heartBeat.cancel(true);
                heartBeat = null;
            }
        }
    }

}