package com.gni.banking.Service;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Status;
import com.gni.banking.Exceptions.*;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.TodaysDate;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionFilterService transactionFilterService;

    @Autowired
    private UserService userService;



    public Page<Transaction> getAll(int limit, int offset, String iban) {
        Pageable pageable = PageRequest.of(offset, limit);
        if (Objects.equals(iban, "") || iban == null){
            return repository.findAll(pageable);
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            Date cutoffDate = calendar.getTime();
            return repository.findTransactionsByIban(iban, cutoffDate, pageable);
        }

    }
    public Page<Transaction> castListToPage(List<Transaction> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
    public Page<Transaction> getTransacionsByUserId(long userId, int limit, int offset) {
        //lijst van alle accounts van de user
        List<Account> accounts = accountService.getAllByUserId(userId);
        //lijst van alle transacties met iban van de accounts
        List<Transaction> allTransactions = new ArrayList<>();
        for (Account account: accounts){
            List<Transaction> transactions = getAll(limit, offset, account.getId()).getContent();
            allTransactions.addAll(transactions);
        }
        int pageNumber = 0; // Example page number
        int pageSize = 10; // Example page size

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return castListToPage(allTransactions, pageable);
    }

    public Boolean accountFromIsOfUser(Transaction transaction, long userId){
        Account accountFrom = accountService.getByIban(transaction.getAccountFrom());
        return accountFrom.getUserId() == userId;
    }

    public Boolean accountToIsOfUser(Transaction transaction, long userId){
        Account accountTo = accountService.getByIban(transaction.getAccountTo());
        return accountTo.getUserId() == userId;
    }

    public Transaction getById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Transaction add(Transaction transaction) throws Exception {
        //checks on account type and amount
        checksOnMakingAndEditingTransaction(transaction);
        transaction.setTimestamp(new Date());
        //update balance on accounts
        updateBalanceOfAccounts(accountService.getByIban(transaction.getAccountFrom()), accountService.getByIban(transaction.getAccountTo()), transaction.getAmount());
        //userService.updateDailyTransaction(transaction.getAccountFrom(), transaction.getAmount());
        return repository.save(transaction);
    }

    public void checksOnMakingAndEditingTransaction(Transaction transaction) throws Exception {
        checkIbans(transaction.getAccountFrom(), transaction.getAccountTo());
        checkAccountStatus(accountService.getByIban(transaction.getAccountFrom()), accountService.getByIban(transaction.getAccountTo()));
        checkAccountTypes(transaction.getAccountFrom(), transaction.getAccountTo());
        checkAmountsOnTransaction(transaction.getAccountFrom(), transaction.getAccountTo(), transaction.getAmount());
        checkNegativeAmount(transaction.getAmount());
    }

    public Transaction update(Transaction transaction, long id) throws Exception {
        checksOnMakingAndEditingTransaction(transaction);
        return repository.save(transaction);
    }

    @Transactional
    public void delete(long id) {
        repository.archiveById(id);
    }


    public boolean checkAccountTypes(String ibanFrom, String ibanTo) throws Exception {
        Account accountFrom = accountService.getByIban(ibanFrom);
        Account accountTo = accountService.getByIban(ibanTo);
        if (accountFrom.getType() == AccountType.Savings && accountTo.getType() == AccountType.Savings)
            throw new InvalidAccountTypeOnTransactionException("You can't transfer money between two savings accounts");
        if (accountFrom.getType() == AccountType.Savings && accountTo.getType() == AccountType.Current) {
            if (accountFrom.getUserId() == accountTo.getUserId()) {
                return true;
            }
            throw new InvalidAccountTypeOnTransactionException("You can't transfer money from savings account to another user's current account");
        }
        if (accountFrom.getType() == AccountType.Current && accountTo.getType() == AccountType.Savings) {
            if (accountFrom.getUserId() == accountTo.getUserId()) {
                return true;
            }
            throw new InvalidAccountTypeOnTransactionException("You can't transfer money from current account to another user's savings account");
        }
        return true;
    }

    public boolean checkAmountsOnTransaction(String ibanFrom, String ibanTo, double amount) throws Exception {
        String bankIban = "NL01INHO0000000001";
        Account accountFrom = accountService.getByIban(ibanFrom);
        Account accountTo = accountService.getByIban(ibanTo);
        if (accountFrom.getBalance() < amount)
            throw new NotEnoughBalanceException("Not enough money on account");
        if (accountFrom.getCurrency() != accountTo.getCurrency())
            throw new InvalidAccountCurrenyOnTransactionException("You can't transfer money between accounts with different currencies");
        if(!Objects.equals(accountFrom.getId(), bankIban)){
            if(!underDayLimitWithIban(ibanFrom, amount))
                throw new LimitOnTransactionExceededException("Day limit exceeded");
            if(accountFrom.getAbsoluteLimit() > accountFrom.getBalance() - amount)
                throw new LimitOnTransactionExceededException("Absolute limit exceeded");
        }
        return true;
    }

    private void updateBalanceOfAccounts(Account accountFrom, Account accountTo, double amount) throws Exception {
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountService.updateBalance(accountFrom, accountFrom.getId());
        accountService.updateBalance(accountTo, accountTo.getId());
    }

    private boolean checkAccountStatus(Account accountFrom, Account accountTo) throws Exception {
        if (accountFrom.getStatus().equals(Status.Closed) || accountTo.getStatus().equals(Status.Closed))
            throw new InvalidAccountStatusException("Cannot perform transaction on closed account");
        return true;
    }

    private void checkIbans(String accountFrom, String accountTo) {
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

    public boolean underDayLimitWithIban(String iban, double amount){
        long userId = (long) accountService.getById(iban).getUserId();
        return underDayLimit(userId, amount);
    }

    public boolean underDayLimit(long userId, double amount){
        double amountTransferredToday = amountTransferredToday(userId);
        return amountTransferredToday + amount <= userService.getDayLimitById(userId);
    }

    public double amountTransferredToday(long userId){

        //list met accounts --> hier de iban van gebruiken
        List<Account> accountsOfUser = accountService.getCurrentAndOpenAccountsByUserId(userId);
        double totalAmountTransferred = 0;
        TodaysDate date = new TodaysDate();
        for(Account account : accountsOfUser){
            List<Double> amountOfTransactions = repository.todaysTransactionOfUser(account.getId(), date.getBeginDate(), date.getEndDate());
            for(Double amount : amountOfTransactions){
                totalAmountTransferred += amount;
            }
        }
        return totalAmountTransferred;
    }

    public Transaction deposit(Transaction transaction) throws Exception {

        transaction.setTimestamp(new Date());
        validDeposit(transaction);
        checksOnMakingAndEditingTransaction(transaction);

        //update balance on accounts
        updateBalanceOfAccounts(accountService.getByIban(transaction.getAccountFrom()), accountService.getByIban(transaction.getAccountTo()), transaction.getAmount());
        return repository.save(transaction);
    }

    public Transaction withdraw(Transaction transaction) throws Exception{
        transaction.setTimestamp(new Date());
        validWithdraw(transaction);
        checksOnMakingAndEditingTransaction(transaction);

        //update balance on accounts
        updateBalanceOfAccounts(accountService.getByIban(transaction.getAccountFrom()), accountService.getByIban(transaction.getAccountTo()), transaction.getAmount());
        return repository.save(transaction);
    }

    public boolean validDeposit(Transaction transaction) throws Exception{
        String bankIban = "NL01INHO0000000001";
        if(Objects.equals(transaction.getAccountFrom(), bankIban)){
            return true;
        }else{
            throw new IllegalArgumentException("You can only deposit money to the bank");
        }

    }

    public boolean validWithdraw(Transaction transaction) throws Exception{
        String bankIban  = "NL01INHO0000000001";
        if(Objects.equals(transaction.getAccountTo(), bankIban)){
            return true;
        }else{
            throw new IllegalArgumentException("You can only withdraw money from the bank");
        }
    }

    public List<Transaction> getTransactionsByUserId(long id,Date startDate,Date endDate,String ibanTo, String comparisonOperator, Double balance) {
        //gets all without filter
        if(startDate == null && endDate == null && ibanTo == null && comparisonOperator == null && balance == null){
            return repository.getTransactionsByPerformedBy(id);
        }
        return transactionFilterService.getTransactionByPerformedByWithFilter(id,startDate,endDate,ibanTo,comparisonOperator,balance);
    }

    private boolean checkNegativeAmount(double amount) throws Exception{
        if(amount <= 0){
            throw new IllegalArgumentException("Amount cant be negative or 0");
        }
        return true;
    }
}

