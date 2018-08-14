package com.uifuture.unpack.protocol;

import com.uifuture.unpack.codec.FstSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TinyDecoder extends ByteToMessageDecoder {
    /**
     * 头部长度字节数
     * 由于在TinyEncoder的encode方法中使用的是writeInt，int为4个字节
     */
    private Integer HEAD_LENGTH = 4;
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
        //头部信息是int类型，长度是4，所以信息长度不可能小于4的
        if (in.readableBytes() < HEAD_LENGTH) {
            return;
        }
        //标记当前的readIndex的位置
        in.markReaderIndex();
        //读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4。指针会向前移动4
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            //到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
            // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        //反序列化
        Object obj = FstSerializer.deserialize(data, genericClass);
        out.add(obj);
    }

}
