package com.xd.connecter.server;

import com.xd.coder.RpcDecoder;
import com.xd.coder.RpcEncoder;
import com.xd.dto.RequestDto;
import com.xd.dto.ResponseDto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ConcurrentHashMap;

public class RpcServer {

    private ConcurrentHashMap<String,Object> beanMap;

    public void startServer(ConcurrentHashMap beanmap){
        this.beanMap=beanmap;
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
//                            p.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                            p.addLast(new RpcDecoder(RequestDto.class));
                            p.addLast(new RpcEncoder(ResponseDto.class));
                            p.addLast(new ServerHandler(beanMap));
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(3333).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
