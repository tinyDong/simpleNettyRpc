package com.xd.impl;

import com.xd.service.AccountService;

public class AccountServiceImpl implements AccountService{
    public String add() {
        System.out.println("请求成功");
        return "请求成功";
    }

    public Integer delete(Integer a,Integer b) {
        System.out.println("accountDelete=========="+a+b);
        return a+b;
    }
}
