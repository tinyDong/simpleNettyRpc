package com.xd.impl;

import com.xd.service.AccountService;

public class AccountServiceImpl implements AccountService{
    public String add() {
        System.out.println("请求成功");
        return "请求成功";
    }

    public void delete() {
        System.out.println("accountDelete");
    }
}
