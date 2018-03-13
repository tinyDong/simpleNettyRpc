package com.xd;

import com.xd.connecter.client.RPCClient;
import com.xd.service.AccountService;
import com.xd.utils.InterfaceUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Start {
    public static void main(String[] args) {

        CountDownLatch latch=new CountDownLatch(1);
        RPCClient.startWithPort(3333,latch);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AccountService accountService= InterfaceUtils.createInterface(AccountService.class);
        String result=accountService.add();
        System.out.println("client求情结果-======"+result);
        Scanner scan = new Scanner(System.in) ;
        String str = scan.next(); // 接收数据
        do{
            System.out.println("输入的数据为：" + str) ;
            String result1=accountService.add();
            System.out.println("String result=accountService.add();======="+result1);
        } while((str= scan.next())!=null);

    }
}
