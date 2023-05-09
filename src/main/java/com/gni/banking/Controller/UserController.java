package com.gni.banking.Controller;

import com.gni.banking.Service.UserService;

public class UserController {
    private UserService service;

    public UserController() {
        this.service = new UserService();
    }
}
