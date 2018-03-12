package com.xd.connecter.server;

import com.xd.dto.RequestDto;
import com.xd.dto.ResponseDto;
import com.xd.service.AccountService;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends SimpleChannelInboundHandler<RequestDto> {

    private final Map<String, Object> beanMap;

    public ServerHandler(Map<String, Object> handlerMap) {
        this.beanMap = handlerMap;
    }


    protected void channelRead0(ChannelHandlerContext ctx, RequestDto requestDto) throws Exception {
        ResponseDto responseDto=new ResponseDto();
        responseDto.setRequestId(responseDto.getRequestId());
        Object result=handleResult(requestDto);
        responseDto.setData(result);
        ctx.writeAndFlush(responseDto).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("操作完成");
            }
        });
    }

    private Object handleResult(RequestDto requestDto) {
        Object bean=beanMap.get(requestDto.getMethodName());

        Class<?> serviceClass = bean.getClass();
        String methodName = requestDto.getMethodName();
        Class<?>[] parameterTypes = requestDto.getParameterTypes();
        Object[] parameters = requestDto.getParameters();


        Method method = null;
        try {
            method = serviceClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            return method.invoke(bean, parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
