package com.gni.banking.Service;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IbanService ibanService;

    @Autowired
    private UserService userService;

    public List<Account> getAll() {
        return (List<Account>) accountRepository.findAll();
    }


    public Account getById(String id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public Account getByIban(String iban) {
        return (Account) accountRepository.findById(iban).orElse(null);

    }

    public List<Account> getAccountByUserId(long userId) throws Exception {
        try {
            return (List<Account>) accountRepository.findByUserId(userId);
        } catch (Exception ex) {
            throw new Exception("Required fields missing");
        }
    }

    public Account add(Account a) {
        if(a.getId() == null || a.getId().isEmpty()) {
            a.setId(ibanService.GenerateIban());
            String iban = ibanService.GenerateIban();
            a.setId(iban);
        }
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
    }

    public Account changeStatus(String iban) {
        Account existingAccount = getByIban(iban);
        if (existingAccount.getStatus().toString().equals("Open")) {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Closed);
        } else {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Open);
        }
        return accountRepository.save(existingAccount);

    }
    public List<String> getIbanByName(String name) {
        String firstname = name.split("_")[0];
        String lastName = name.split("_")[1];
        List<User> Users =  userService.findByFirstNameAndLastName(firstname, lastName);
        List<Account> accounts = new ArrayList<>();
        Users.forEach(user -> {
            List<Account> account = accountRepository.getIdByUserId(user.getId());
            accounts.addAll(account);
        });

        List<String> ibans = new ArrayList<>();
        List<Account> usableAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            if(account.getStatus() == com.gni.banking.Enums.Status.Open && account.getType() == com.gni.banking.Enums.AccountType.Current) {
                usableAccounts.add(account);
            }
        });

        usableAccounts.forEach(account -> {
            ibans.add(account.getId());
        });
        return ibans;
    }

    public List<Account> getCurrentAndOpenAccountsByUserId(int userId) {
        return accountRepository.getCurrentAndOpenAccountsByUserId(userId, Status.Open, AccountType.Current);
    }

    public double totalBalance(int userId){
        List<Account> accounts = accountRepository.getTotalBalanceOfAccounts(userId, Status.Open);
        double total = 0;
        for(Account account : accounts){
            total += account.getBalance();
        }
        return total;
    }
}
