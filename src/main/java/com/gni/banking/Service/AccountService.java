package com.gni.banking.Service;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.*;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IbanService ibanService;

    @Autowired
    private UserService userService;

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

    private List<Account> getAccounts(Long userId, AccountType accountType, Status accountStatus, Pageable pageable, String firstNameLastName) {
        //check for parameters and return the correct list
        if (userId != null && accountType != null && accountStatus != null){
            CheckUserId(userId);
            return accountRepository.findByUserIdAndTypeAndStatus(userId, accountType, accountStatus, pageable);
        } else if (userId != null && accountType != null) {
            CheckUserId(userId);
            return accountRepository.findByUserIdAndType(userId, accountType, pageable);
        } else if (userId != null && accountStatus != null){
            CheckUserId(userId);
            return accountRepository.findByUserIdAndStatus(userId, accountStatus, pageable);
        } else if (accountType != null && accountStatus != null) {
            return accountRepository.findByTypeAndStatus(accountType, accountStatus, pageable);
        } else if (accountStatus != null) {
            return accountRepository.findByStatus(accountStatus, pageable);
        } else if (userId != null) {
            CheckUserId(userId);
            return accountRepository.findByUserId(userId, pageable);
        } else if (accountType != null) {
            return accountRepository.findByType(accountType, pageable);
        } else {
            return accountRepository.findAll(pageable);
        }
    }

    public List<IbanAccountDTO> findByFirstNameLastName(String firstNameLastName) throws Exception {
        List<IbanAccountDTO> ibans = getIbanByName(firstNameLastName);
        //List<IbanAccountDTO> accounts = new ArrayList<>();
        /*for(String iban : ibans){
            IbanAccountDTO account = new IbanAccountDTO();
            account.setId(iban);
            account.set;
            accounts.add(account);
        }*/
        return ibans;
    }

    private static void CheckUserId(Long userId) {
        if (userId == 0) {
            throw new IllegalArgumentException("UserId is inaccessible");
        }
        else if (userId < 0) {
            throw new IllegalArgumentException("UserId is negative");
        }
    }

    public List<Account> getAll(int limit, int offset, Long userId, String type, String status, String firstNameLastName) throws Exception {
        //initialize variables
        Pageable pageable = PageRequest.of(offset, limit);
        AccountType accountType = getAccountType(type);
        Status accountStatus = getStatus(status);

        return getAccounts(userId, accountType, accountStatus, pageable, firstNameLastName);
    }


    public Account getById(String id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public Account getByIban(String iban) {
        return accountRepository.findById(iban).orElse(null);
    }

    public Account add(PostAccountDTO accountRequest) throws Exception {
        Account a = new Account();
        a.setId(ibanService.GenerateIban());
        a.setUserId(accountRequest.getUserId());
        a.setType(accountRequest.getType());
        a.setAbsoluteLimit(0.00);
        a.setCurrency(Currency.EUR);
        a.setBalance(1000.00);
        a.setStatus(Status.Open);

        return accountRepository.save(a);
    }

    public Account addCompleteAccount(Account account) {
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

    public Account changeStatus(String iban) {
        Account existingAccount = getByIban(iban);
        if (existingAccount.getStatus().toString().equals("Open")) {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Closed);
        } else {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Open);
        }
        return accountRepository.save(existingAccount);
    }

    public List<IbanAccountDTO> getIbanByName(String name) {
        String[] names = name.split(" ");
        String firstname = names[0];
        String lastName = names[1];
        for(int i = 2; i < names.length; i++){
            lastName += " " + names[i];
        }

        List<User> Users = userService.findByFirstNameAndLastName(firstname, lastName);
        if(Users.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        List<Account> accounts = new ArrayList<>();
        Users.forEach(user -> {
            List<Account> account = accountRepository.getIdByUserId((int) user.getId());
            accounts.addAll(account);
        });


        List<IbanAccountDTO> ibans = new ArrayList<>();
        List<Account> usableAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            if (account.getStatus() == com.gni.banking.Enums.Status.Open && account.getType() == com.gni.banking.Enums.AccountType.Current) {
                usableAccounts.add(account);
            }
        });
        if(usableAccounts.isEmpty()){
            throw new IllegalArgumentException("No accounts found");
        }

        String finalLastName = lastName;
        usableAccounts.forEach(account -> {
            IbanAccountDTO iban = new IbanAccountDTO();
            iban.setId(account.getId());
            iban.setFirstName(firstname);
            iban.setLastName(finalLastName);
            ibans.add(iban);
        });

        if(ibans.isEmpty()){
            throw new IllegalArgumentException("No accounts found");
        }
        return ibans;
    }

    public List<Account> getCurrentAndOpenAccountsByUserId(long userId) {
        return accountRepository.getCurrentAndOpenAccountsByUserId(userId, Status.Open, AccountType.Current);
    }

    public double totalBalance(long userId) {
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

    public List<Account> getAllByUserId(long userId) {
        return accountRepository.getAllByUserId(userId);
    }
}
