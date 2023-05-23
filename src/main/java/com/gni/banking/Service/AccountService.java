package com.gni.banking.Service;

import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.AccountRequestDTO;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAll() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account add(Account a) {
        return accountRepository.save(a);
    }

    public Account getById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account update(Account account, long id) throws Exception {
        Account existingAccount = getById(id);

        existingAccount.setIban(account.getIban());
        existingAccount.setUserId(account.getUserId());
        existingAccount.setType(account.getType());
        existingAccount.setAbsoluteLimit(account.getAbsoluteLimit());
        existingAccount.setCurrency(account.getCurrency());
        existingAccount.setStatus(account.getStatus());

        try {
            return accountRepository.save(existingAccount);
        } catch(Exception ex) {
            throw new Exception("Required fields missing");
        }
    }

    public void delete(long id) {
        accountRepository.deleteById(id);
    }
}
