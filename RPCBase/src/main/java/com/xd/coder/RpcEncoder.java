package com.xd.coder;

import com.xd.serialization.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.FileOutputStream;

public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> operationClass;

    public RpcEncoder(Class<?> genericClass) {
        this.operationClass = genericClass;
    }

    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (operationClass.isInstance(msg)){
            byte[] datas = ByteObjConverter.objectToByte(msg);
            out.writeBytes(datas);
            ctx.flush();
        }
    }
}
