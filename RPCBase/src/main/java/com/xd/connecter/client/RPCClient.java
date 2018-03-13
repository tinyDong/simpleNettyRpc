package com.xd.connecter.client;

import com.xd.coder.RpcDecoder;
import com.xd.coder.RpcEncoder;
import com.xd.dto.RequestDto;
import com.xd.dto.ResponseDto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCClient implements Runnable{
    static final String HOST = System.getProperty("host", "127.0.0.1");
    private ClientHandler clientHandler;
    private static RPCClient rpcClient;
    private static Executor executor;
    private static int sercerPort;
    private static CountDownLatch startLatch;


    public static RPCClient newInstance(){
        if (rpcClient == null) {
            synchronized (RPCClient.class) {
                if (rpcClient == null) {
                    rpcClient = new RPCClient();
                }
            }
        }
        return rpcClient;
    }

    public void connectToServer(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcDecoder(RequestDto.class));
                            p.addLast(new ClientHandler());
                            p.addLast(new RpcEncoder(ResponseDto.class));
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(HOST, port).sync();
            f.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    startLatch.countDown();
                    System.out.println("已经链接");
                    if (future.isSuccess()){
                        relatedHandler(future);
                    }
                }
            });
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        }catch (Exception e){
          e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    public ClientHandler getHandler(){
        return clientHandler;
    }

    private void relatedHandler(ChannelFuture future){
        this.clientHandler= future.channel().pipeline().get(ClientHandler.class);
    }

    public static void startWithPort(int port, CountDownLatch latch) {
        if (executor==null){
            executor= Executors.newSingleThreadExecutor();
            executor.execute(newInstance());
            sercerPort=port;
            startLatch=latch;
        }
    }

    public void run() {
        connectToServer(sercerPort);
    }
}
