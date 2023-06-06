package com.gni.banking.Controller;



import com.gni.banking.Model.Account;
import com.gni.banking.Service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService service;

    private ModelMapper modelMapper;

    public AccountController() {

    }

    @GetMapping
    public List<Account> getAllAccounts(@RequestParam(defaultValue = "0") int offset,
                                        @RequestParam(defaultValue = "10") int limit,
                                        @RequestParam(required = false) String userId) throws Exception {
        return service.getAll(limit, offset, userId);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Account> add(@RequestBody Account a) {

        return ResponseEntity.status(201).body(service.add(a));
    }

    @PutMapping("/{iban}")
    public Account update(@RequestBody Account account, @PathVariable String iban) throws Exception {
        return service.update(account, iban);
    }

    @DeleteMapping("/{iban}")
    public Account changeStatus(@PathVariable String iban) {
        return service.changeStatus(iban);
    }

    @GetMapping("/getIban/{name}")
    public List<String> getIbanByName(@PathVariable String name) throws Exception {
        return service.getIbanByName(name);
    }
}
