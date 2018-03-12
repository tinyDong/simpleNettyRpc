package com.xd.coder;

import com.xd.serialization.ByteBufToBytes;
import com.xd.serialization.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> operationClass;

    public RpcDecoder(Class<?> genericClass) {
        this.operationClass = genericClass;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBufToBytes read = new ByteBufToBytes();
        Object obj = ByteObjConverter.byteToObject(read.read(in));
        out.add(obj);
    }
}
