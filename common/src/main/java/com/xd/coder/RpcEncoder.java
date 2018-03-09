package com.xd.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

    }
}
