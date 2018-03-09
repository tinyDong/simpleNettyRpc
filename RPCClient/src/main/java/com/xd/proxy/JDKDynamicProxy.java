package com.xd.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

public class JDKDynamicProxy implements InvocationHandler{

    private static JDKDynamicProxy instance= null;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("Method:" + method.getName() +"==="+method.getDeclaringClass().getName()+"==="+method.getParameterTypes());

//        Class aClass= classMap.get(key);
//        Method me = aClass.getDeclaredMethod(method.getName(), null);
//        return me.invoke(aClass.newInstance(),null);
        return null;
    }

    public static JDKDynamicProxy getInstance(){
        if (instance == null){
            initInstance();
        }

        return instance;
    }

    /**
     * 初始化示例。
     */
    private static synchronized void initInstance(){
        if (instance == null){
            instance = new JDKDynamicProxy();
        }
    }



//    public static void main(String[] args) {
//        BankDao bankDao = (BankDao)Proxy.newProxyInstance(BankDao.class.getClassLoader(), new Class[]{BankDao.class},
//                JDKDynamicProxy.getInstance());
//
//        System.out.println(bankDao.transfer());
//    }
}
