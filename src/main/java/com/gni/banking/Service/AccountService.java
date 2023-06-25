package com.gni.banking.Service;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.IbanAccountDTO;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.PutAccountDTO;
import com.gni.banking.Model.User;
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

    private static void CheckUserId(Long userId) {
        if (userId == 0) {
            throw new IllegalArgumentException("UserId is inaccessible");
        } else if (userId < 0) {
            throw new IllegalArgumentException("UserId is negative");
        }
    }

    private List<Account> getAccounts(Long userId, AccountType accountType, Status accountStatus, Pageable pageable) {
        //check for parameters and return the correct list
        if (userId != null && accountType != null && accountStatus != null) {
            return GetAccountsWithAllFilters(userId, accountType, accountStatus, pageable);
        } else if (userId != null && accountType != null) {
            return GetAccountsWithUserIdAndAccountTypeFilters(userId, accountType, pageable);
        } else if (userId != null && accountStatus != null) {
            return getAccountsWithUserIdAndAccountStatusFilters(userId, accountStatus, pageable);
        } else if (accountType != null && accountStatus != null) {
            return getAccountsWithAccountTypeAndAccountStatusFilters(accountType, accountStatus, pageable);
        } else if (accountStatus != null) {
            return getAccountsWithAccountStatusFilter(accountStatus, pageable);
        } else if (userId != null) {
            return getAccountsWithUserIdFilter(userId, pageable);
        } else if (accountType != null) {
            return getAccountsWithAccountTypeFilter(accountType, pageable);
        } else {
            return accountRepository.findByIdNot(pageable, "NL01INHO0000000001");
        }
    }

    private List<Account> getAccountsWithAccountTypeFilter(AccountType accountType, Pageable pageable) {
        return accountRepository.findByType(accountType, pageable);
    }

    private List<Account> getAccountsWithUserIdFilter(Long userId, Pageable pageable) {
        CheckUserId(userId);
        return accountRepository.findByUserId(userId, pageable);
    }

    private List<Account> getAccountsWithAccountStatusFilter(Status accountStatus, Pageable pageable) {
        return accountRepository.findByStatus(accountStatus, pageable);
    }

    private List<Account> getAccountsWithAccountTypeAndAccountStatusFilters(AccountType accountType, Status accountStatus, Pageable pageable) {
        return accountRepository.findByTypeAndStatus(accountType, accountStatus, pageable);
    }

    private List<Account> getAccountsWithUserIdAndAccountStatusFilters(Long userId, Status accountStatus, Pageable pageable) {
        CheckUserId(userId);
        return accountRepository.findByUserIdAndStatus(userId, accountStatus, pageable);
    }

    private List<Account> GetAccountsWithUserIdAndAccountTypeFilters(Long userId, AccountType accountType, Pageable pageable) {
        CheckUserId(userId);
        return accountRepository.findByUserIdAndType(userId, accountType, pageable);
    }

    private List<Account> GetAccountsWithAllFilters(Long userId, AccountType accountType, Status accountStatus, Pageable pageable) {
        CheckUserId(userId);
        return accountRepository.findByUserIdAndTypeAndStatus(userId, accountType, accountStatus, pageable);
    }

    public List<IbanAccountDTO> findByFirstNameLastName(String firstNameLastName) throws Exception {
        List<IbanAccountDTO> ibans = getIbanByName(firstNameLastName);
        return ibans;
    }

    public List<Account> getAll(int limit, int offset, Long userId, String type, String status) throws Exception {
        //initialize variables
        Pageable pageable = PageRequest.of(offset, limit);
        AccountType accountType = getAccountType(type);
        Status accountStatus = getStatus(status);

        List<Account> accounts = getAccounts(userId, accountType, accountStatus, pageable);
        return CheckListForBanksOwnAccount(accounts);
    }

    private List<Account> CheckListForBanksOwnAccount(List<Account> accounts) {
        if (accounts.size() != 0) {
            if (accounts.get(0).getId().equals("NL01INHO0000000001")) {
                accounts.remove(0);
            }
        }
        return accounts;
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
        CheckAndSetUserId(accountRequest, a);
        a.setType(accountRequest.getType());
        a.setAbsoluteLimit(0.00);
        a.setCurrency(Currency.EUR);
        a.setBalance(0.00);
        a.setStatus(Status.Open);

        return accountRepository.save(a);
    }

    private void CheckAndSetUserId(PostAccountDTO accountRequest, Account a) {
        //checks if the user exists
        if (userService.getById(accountRequest.getUserId()) == null) {
            throw new IllegalArgumentException("UserId does not have a user");
        } else {
            a.setUserId(accountRequest.getUserId());
        }
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
        existingAccount.setStatus(com.gni.banking.Enums.Status.Closed);
        return accountRepository.save(existingAccount);
    }

    public List<IbanAccountDTO> getIbanByName(String name) {
        String[] names = name.split(" ");
        String firstname = names[0];
        String lastName = names[1];
        for (int i = 2; i < names.length; i++) {
            lastName += " " + names[i];
        }

        List<User> Users = userService.findByFirstNameAndLastName(firstname, lastName);
        if (Users.isEmpty()) {
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
        if (usableAccounts.isEmpty()) {
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

        if (ibans.isEmpty()) {
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
