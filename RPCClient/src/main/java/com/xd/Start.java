package com.xd;

import com.xd.connecter.client.RPCClient;
import com.xd.service.AccountService;
import com.xd.utils.InterfaceUtils;

public class Start {
    public static void main(String[] args) {

        RPCClient.newInstance().connectToServer(3333);
        AccountService accountService= InterfaceUtils.createInterface(AccountService.class);
        accountService.add();
    }
}
