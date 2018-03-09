package com.xd.rpcClient;

import com.xd.proxy.JDKDynamicProxy;
import com.xd.service.AccountService;

import java.lang.reflect.Proxy;

public class RPCClient {

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<T> interfaceClassz) {
        return (T)Proxy.newProxyInstance(interfaceClassz.getClassLoader(), new Class[]{interfaceClassz},
                JDKDynamicProxy.getInstance());
    }

    public static void init() {

    }
}
