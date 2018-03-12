package com.xd.connecter.client;

import com.xd.dto.RequestDto;
import com.xd.dto.ResponseDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends SimpleChannelInboundHandler<ResponseDto> {
    private Channel channel;

    private ConcurrentHashMap<String,RpcFuture> requestIdMap=new ConcurrentHashMap<String, RpcFuture>();
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("通道已经激活");
//        ctx.writeAndFlush("通道激活");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ResponseDto response) throws Exception {
        String requestId = response.getRequestId();
        RpcFuture rpcFuture = requestIdMap.get(requestId);
        if (rpcFuture != null) {
            requestIdMap.remove(requestId);
            rpcFuture.process(response);
        }
    }

    public RpcFuture writeRequest(RequestDto requestDto){
        RpcFuture rpcFuture=new RpcFuture(requestDto);
        requestIdMap.put(requestDto.getRequestId(),rpcFuture);
        channel.writeAndFlush(requestDto).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("writeRequest   操作完成");
            }
        });
        return rpcFuture;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
