package com.xd;

import com.xd.connecter.server.RpcServer;
import com.xd.impl.AccountServiceImpl;
import com.xd.service.AccountService;

import java.util.concurrent.ConcurrentHashMap;

public class Start {
    private static ConcurrentHashMap<String,Object> beanMap=new ConcurrentHashMap<String, Object>();

    public static void main(String[] args) {
        AccountService accountService=new AccountServiceImpl();
        beanMap.put(AccountService.class.getName(),accountService);
        RpcServer rpcServer=new RpcServer();
        rpcServer.startServer(beanMap);
    }
}