package com.gni.banking.Service;

import com.gni.banking.Model.Account;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IbanService ibanService;

    public List<Account> getAll() {
        return (List<Account>) accountRepository.findAll();
    }


    public Account getById(String id) {
        return accountRepository.findById(id).orElseThrow();

    public Account getByIban(String iban) {
        return (Account) accountRepository.findByIban(iban).orElse(null);

    }

    public List<Account> getAccountByUserId(long userId) throws Exception {
        try {
            return (List<Account>) accountRepository.findByUserId(userId);
        } catch (Exception ex) {
            throw new Exception("Required fields missing");
        }

    }

    public Account add(Account a) {
        String iban = ibanService.GenerateIban();
        a.setId(iban);
        return accountRepository.save(a);
    }


    public Account update(Account account, String iban) throws Exception {
        Account existingAccount = getByIban(iban);

        existingAccount.setUserId(account.getUserId());
        existingAccount.setType(account.getType());
        existingAccount.setAbsoluteLimit(account.getAbsoluteLimit());
        existingAccount.setCurrency(account.getCurrency());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setStatus(account.getStatus());

        try {
            return accountRepository.save(existingAccount);
        } catch (Exception ex) {
            throw new Exception("Required fields missing");
        }
    }

    public void delete(String id) {
        accountRepository.deleteById(id);

    public Account changeStatus(String iban) {
        Account existingAccount = getByIban(iban);
        if (existingAccount.getStatus().toString().equals("Open")) {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Closed);
        } else {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Open);
        }
        return accountRepository.save(existingAccount);

    }
}
