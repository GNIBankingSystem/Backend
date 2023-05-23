package com.gni.banking.Controller;

import com.gni.banking.Model.Account;
import com.gni.banking.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService service;

    public AccountController() {
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable long id) {
        return service.getById(id);
    }


    @PostMapping
    public ResponseEntity<Account> add(@RequestBody Account a) {

        return ResponseEntity.status(201).body(service.add(a));
    }

    @PutMapping("/{id}")
    public Account update(@RequestBody Account account, @PathVariable long id) throws Exception {
        return service.update(account, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

}
