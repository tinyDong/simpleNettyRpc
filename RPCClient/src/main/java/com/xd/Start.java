package com.xd;

import com.xd.rpcClient.RPCClient;
import com.xd.service.AccountService;

public class Start {
    public static void main(String[] args) {
        RPCClient.init();
        AccountService accountService=RPCClient.createInstance(AccountService.class);
        accountService.add();
    }
}