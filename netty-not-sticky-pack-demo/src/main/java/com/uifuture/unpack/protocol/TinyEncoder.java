package com.uifuture.unpack.protocol;

import com.uifuture.unpack.codec.FstSerializer;
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
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) {
        //判断解码类型
        if (genericClass.isInstance(in)) {
            //序列化
            byte[] data = FstSerializer.serialize(in);
            //设置消息头为长度。消息头类型为int
            out.writeInt(data.length);
            //消息体
            out.writeBytes(data);
        }
    }

}
