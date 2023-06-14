package com.gni.banking.Service;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.DTO.PostAccountDTO;
import com.gni.banking.Model.DTO.PutAccountDTO;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IbanService ibanService;

    @Autowired
    private UserService userService;

    public List<Account> getAll(int limit, int offset, String userId, String type, String status) throws Exception {
        //initialize variables
        Pageable pageable = PageRequest.of(offset, limit);
        AccountType accountType = getAccountType(type);
        Status accountStatus = getStatus(status);

        //check for parameters and return the correct list
        if (userId != null && accountType != null && accountStatus != null)
            return accountRepository.findByUserIdAndTypeAndStatus(userId, accountType, accountStatus, pageable);

        if (userId != null && accountType != null)
            return accountRepository.findByUserIdAndType(userId, accountType, pageable);

        if (userId != null && accountStatus != null)
            return accountRepository.findByUserIdAndStatus(userId, accountStatus, pageable);

        if (accountType != null && accountStatus != null)
            return accountRepository.findByTypeAndStatus(accountType, accountStatus, pageable);

        if (accountStatus != null)
            return accountRepository.findByStatus(accountStatus, pageable);

        if (userId != null)
            return accountRepository.findByUserId(userId, pageable);

        if (accountType != null)
            return accountRepository.findByType(accountType, pageable);

        if (Objects.equals(userId, "1")) {
            //TODO - make it so you get an error in insomnia because its a the bank account
            return null;
        }

        return accountRepository.findAll(pageable);
    }

    private static Status getStatus(String status) throws Exception {
        Status accountStatus = null;
        if (status != null) {
            switch (status.toLowerCase()) {
                case "open" -> accountStatus = Status.Open;
                case "closed" -> accountStatus = Status.Closed;
                default -> accountStatus = null;
            }
        }
        return accountStatus;
    }

    private static AccountType getAccountType(String type) throws Exception {
        AccountType accountType = null;
        //fill variables if necessary
        if (type != null) {
            switch (type.toLowerCase()) {
                case "current" -> accountType = AccountType.Current;
                case "savings" -> accountType = AccountType.Savings;
                default -> accountType = null;
            }
        }
        return accountType;
    }


    public Account getById(String id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public Account getByIban(String iban) {
        return (Account) accountRepository.findById(iban).orElse(null);

    }

    public Account add(PostAccountDTO accountRequest) throws Exception {
        Account a = new Account();
        a.setId(ibanService.GenerateIban());
        String iban = ibanService.GenerateIban();
        a.setId(iban);
        a.setUserId(accountRequest.getUserId());
        a.setType(accountRequest.getType());
        a.setAbsoluteLimit(0.00);
        a.setCurrency(Currency.EUR);
        a.setBalance(0.00);
        a.setStatus(Status.Open);

        return accountRepository.save(a);
    }

    public Account addCompleteAccount(Account account){
        return accountRepository.save(account);
    }


    public Account update(PutAccountDTO account, String iban) throws Exception {
        Account existingAccount = getByIban(iban);

        existingAccount.setUserId(account.getUserId());
        existingAccount.setType(account.getType());
        existingAccount.setAbsoluteLimit(account.getAbsoluteLimit());

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
        List<User> Users = userService.findByFirstNameAndLastName(firstname, lastName);
        List<Account> accounts = new ArrayList<>();
        Users.forEach(user -> {
            List<Account> account = accountRepository.getIdByUserId((int) user.getId());
            accounts.addAll(account);
        });

        List<String> ibans = new ArrayList<>();
        List<Account> usableAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            if (account.getStatus() == com.gni.banking.Enums.Status.Open && account.getType() == com.gni.banking.Enums.AccountType.Current) {
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

    public double totalBalance(int userId) {
        List<Account> accounts = accountRepository.getTotalBalanceOfAccounts(userId, Status.Open);
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    public int getUserIdByIban(String iban) {
        return accountRepository.getUserIdById(iban);
    }

    public void updateBalance(Account account, String id) {
        Account existingAccount = getByIban(id);
        existingAccount.setBalance(account.getBalance());
        accountRepository.save(existingAccount);
    }
}
