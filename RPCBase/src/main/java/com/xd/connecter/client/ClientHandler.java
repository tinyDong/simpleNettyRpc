package com.xd.connecter.client;

import com.xd.dto.RequestDto;
import com.xd.dto.ResponseDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ClientHandler extends SimpleChannelInboundHandler<ResponseDto> {
    private Channel channel;

    private ConcurrentHashMap<String,RpcFuture> requestIdMap=new ConcurrentHashMap<String, RpcFuture>();
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("通道已经激活");
//        ctx.writeAndFlush("通道激活");
    }
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        System.out.println("channelRead");
//        ctx.write(msg);
//    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ResponseDto response) throws Exception {
        System.out.println("channelRead0channelRead0");
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
        final CountDownLatch countDownLatch=new CountDownLatch(1);
        channel.writeAndFlush(requestDto).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                countDownLatch.countDown();
                System.out.println("writeRequest   操作完成");
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
