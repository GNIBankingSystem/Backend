package com.gni.banking.Controller;

import com.gni.banking.Service.TransactionService;

public class TransactionController {

    private TransactionService service;

    public TransactionController() {
        this.service = new TransactionService();
    }
}
