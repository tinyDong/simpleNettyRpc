package com.xd.rpcClient;

import com.xd.coder.RpcDecoder;
import com.xd.proxy.JDKDynamicProxy;
import com.xd.service.AccountService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

public class RPCClient {

    private static AtomicBoolean connecterStarted;
    @SuppressWarnings("unchecked")
    public static <T> T createInterface(Class<T> interfaceClassz) {
        init();
        return (T)Proxy.newProxyInstance(interfaceClassz.getClassLoader(), new Class[]{interfaceClassz},
                JDKDynamicProxy.getInstance());
    }

    public static void init() {
        if (!connecterStarted.get()){
            startServer();
        }

    }

    private static void startServer() {
        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup(4))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(RpcDecoder)
                    }
                });
        ChannelFuture channelFuture = b.connect(remotePeer);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    connecterStarted.compareAndSet(false,true);
                    System.out.println();
                    RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
                    addHandler(handler);
                }
            }
        });
    }
}
