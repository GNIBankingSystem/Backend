package com.gni.banking.Controller;

import com.gni.banking.Service.AccountService;

public class AccountController {
    private AccountService service;

    public AccountController() {
        this.service = new AccountService();
    }
}
