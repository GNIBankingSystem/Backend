package com.gni.banking.Controller;

import com.gni.banking.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")

public class TransactionController {
    @Autowired

    private TransactionService service;

    public TransactionController() {
        this.service = new TransactionService();
    }
}
