package com.uifuture.hearbest.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Encoder
 */
@ChannelHandler.Sharable
public class TinyEncoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    public TinyEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    /**
     * 编码
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) {
        byte[] data = FstSerializer.serialize(in);
        out.writeBytes(data);
    }

}
