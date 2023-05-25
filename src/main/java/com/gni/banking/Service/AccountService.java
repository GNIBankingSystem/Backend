package com.gni.banking.Service;

import com.gni.banking.Model.Account;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repository;

    public Account add(Account account) {
        return repository.save(account);
    }
}
