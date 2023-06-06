package com.gni.banking.Service;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private AccountService accountService;

    public List<Transaction> getAll() {
        return (List<Transaction>) repository.findAll();
    }

    public Transaction getById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Transaction add(Transaction transaction) throws Exception {
        //checks on account type and amount
        checksOnMakingAndEditingTransaction(transaction);
        transaction.setTimeStamp(new Date());
        //update balance on accounts
        updateBalanceOfAccounts(accountService.getByIban(transaction.getAccountFrom()), accountService.getByIban(transaction.getAccountTo()), transaction.getAmount());
        return repository.save(transaction);
    }

    private void checksOnMakingAndEditingTransaction(Transaction transaction) throws Exception {
        checkIfIbansExists(transaction.getAccountFrom(), transaction.getAccountTo());
        checkAccountStatus(accountService.getByIban(transaction.getAccountFrom()), accountService.getByIban(transaction.getAccountTo()));
        checkAccountTypes(transaction.getAccountFrom(), transaction.getAccountTo());
        checkAmountsOnTransaction(transaction.getAccountFrom(), transaction.getAccountTo(), transaction.getAmount());
    }

    public Transaction update(Transaction transaction, long id) throws Exception {
        checksOnMakingAndEditingTransaction(transaction);
        return repository.save(transaction);
    }

    public void delete(long id) {
        repository.archiveById(id);
    }


    private boolean checkAccountTypes(String ibanFrom, String ibanTo) throws Exception {
        Account accountFrom = accountService.getByIban(ibanFrom);
        Account accountTo = accountService.getByIban(ibanTo);
        if (accountFrom.getType() == AccountType.Savings && accountTo.getType() == AccountType.Savings)
            throw new Exception("You can't transfer money between two savings accounts");
        if (accountFrom.getType() == AccountType.Savings && accountTo.getType() == AccountType.Current) {
            if (accountFrom.getUserId() == accountTo.getUserId()) {
                return true;
            }
            throw new Exception("You can't transfer money from savings account to another user's current account");
        }
        if (accountFrom.getType() == AccountType.Current && accountTo.getType() == AccountType.Savings) {
            if (accountFrom.getUserId() == accountTo.getUserId()) {
                return true;
            }
            throw new Exception("You can't transfer money from current account to another user's savings account");
        }
        return true;
    }

    private boolean checkAmountsOnTransaction(String ibanFrom, String ibanTo, double amount) throws Exception {
        Account accountFrom = accountService.getByIban(ibanFrom);
        Account accountTo = accountService.getByIban(ibanTo);
        if (accountFrom.getBalance() < amount)
            throw new Exception("Not enough money on account");
        if(accountFrom.getAbsoluteLimit() < amount)
            throw new Exception("Absolute limit exceeded");
        if (accountFrom.getCurrency() != accountTo.getCurrency())
            throw new Exception("You can't transfer money between accounts with different currencies");
        return true;
    }

    private void updateBalanceOfAccounts(Account accountFrom, Account accountTo, double amount) throws Exception {
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountService.update(accountFrom, accountFrom.getId());
        accountService.update(accountTo, accountTo.getId());

    }

    private boolean checkAccountStatus(Account accountFrom, Account accountTo) throws Exception {
        if (accountFrom.getStatus().equals(Status.Closed) || accountTo.getStatus().equals(Status.Closed))
            throw new Exception("Cannot perform transaction on closed account");
        return true;
    }

    private void checkIfIbansExists(String accountFrom, String accountTo) {
        //check if iban is in valid format
        if (!isValidIbanFormat(accountFrom) || !isValidIbanFormat(accountTo))
            throw new IllegalArgumentException("Invalid IBAN");

        //check if iban exists
        if (accountService.getById(accountFrom) == null || accountService.getByIban(accountTo) == null)
            throw new IllegalArgumentException("IBAN does not exist");
    }


    public boolean isValidIbanFormat(String iban) {
        // Remove all the spaces from the iban string
        iban = iban.replace(" ", "");

        // Define the pattern for the IBAN
        String regex = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$";

        // Check if the IBAN matches the pattern
        return Pattern.matches(regex, iban);
    }

}

