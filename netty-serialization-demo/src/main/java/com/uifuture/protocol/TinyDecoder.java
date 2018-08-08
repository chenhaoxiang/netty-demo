package com.uifuture.protocol;

import com.uifuture.codec.FstSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TinyDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public TinyDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    /**
     * 解码
     *
     * @param ctx
     * @param in
     * @param out
     */
    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int size = in.readableBytes();
        byte[] data = new byte[size];
        in.readBytes(data);
        Object obj = FstSerializer.deserialize(data, genericClass);
        out.add(obj);
    }

}
