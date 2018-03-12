package com.xd.utils;

import com.xd.proxy.JDKDynamicProxy;

import java.lang.reflect.Proxy;

public class InterfaceUtils {
    @SuppressWarnings("unchecked")
    public static <T> T createInterface(Class<T> interfaceClassz) {
        return (T) Proxy.newProxyInstance(interfaceClassz.getClassLoader(), new Class[]{interfaceClassz},
                JDKDynamicProxy.getInstance());
    }
}
