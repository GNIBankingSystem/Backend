package com.gni.banking.Controller;

import com.gni.banking.Model.AccountRequestDTO;
import com.gni.banking.Model.TransactionResponseDTO;
import com.gni.banking.Service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService service;

    private ModelMapper modelMapper;

    public AccountController() {
        modelMapper = new ModelMapper();
    }

    /*@GetMapping("/{iban}")
    public AccountRequestDTO getByIban(@PathVariable String iban) {

    }*/
}
