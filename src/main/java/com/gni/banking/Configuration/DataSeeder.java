package com.gni.banking.Configuration;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.AccountRequestDTO;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.User;
import com.gni.banking.Service.AccountService;
import com.gni.banking.Service.TransactionService;
import com.gni.banking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.gni.banking.Enums.Role.Customer;

@Component
public class DataSeeder implements ApplicationRunner {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        User user1 = new User();
        user1.setId(1);
        user1.setNumberofaccounts(1);
        user1.setDayLimit(3000.00);
        user1.setEmail("PieterVenema@gmail.com");
        user1.setUsername("username");
        user1.setFirstName("pieter");
        user1.setLastName("venema");
        user1.setPassword("password");
        user1.setPhoneNumber(612345678);
        user1.setRoles(Customer);
        user1.setTransactionLimit(1000.00);
        user1.setDailyTransaction(0);
        userService.add(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setNumberofaccounts(1);
        user2.setDayLimit(3000.00);
        user2.setEmail("pietervenema@gmail.com");
        user2.setUsername("pietervenema");
        user2.setFirstName("pieter");
        user2.setLastName("venema");
        user2.setPassword("Pieter123");
        user2.setPhoneNumber(612345678);
        user2.setRoles(Customer);
        user2.setTransactionLimit(1000.00);
        user2.setDailyTransaction(0);
        userService.add(user2);


        Account account = new Account();
        account.setId("NL01INHO0000000001");
        account.setUserId(1);
        account.setType(AccountType.Current);
        account.setAbsoluteLimit(1000);
        account.setCurrency(Currency.EUR);
        account.setBalance(550000000);
        account.setStatus(Status.Open);
        accountService.add(account);

        Account account1 = new Account();
        account1.setId("NL01INHO456874318");
        account1.setUserId(1);
        account1.setType(AccountType.Current);
        account1.setAbsoluteLimit(1000);
        account1.setCurrency(Currency.EUR);
        account1.setBalance(55.20);
        account1.setStatus(Status.Open);
        accountService.add(account1);

        Account account2 = new Account();
        account2.setId("NL01INHO456874319");
        account2.setUserId(2);
        account2.setType(AccountType.Current);
        account2.setAbsoluteLimit(1000);
        account2.setCurrency(Currency.EUR);
        account2.setBalance(55.22);
        account2.setStatus(Status.Open);
        accountService.add(account2);

        Account account3 = new Account();
        account3.setId("NL01INHO456874320");
        account3.setUserId(2);
        account3.setType(AccountType.Savings);
        account3.setAbsoluteLimit(1000);
        account3.setCurrency(Currency.EUR);
        account3.setBalance(55);
        account3.setStatus(Status.Open);
        accountService.add(account3);


        Transaction transaction1 = new Transaction();
        transaction1.setId(1);
        transaction1.setTimeStamp(new Date());
        transaction1.setAccountFrom("NL01INHO0000000001");
        transaction1.setAccountTo("NL01INHO456874318");
        transaction1.setAmount(50);
        transaction1.setPerformedBy(1);
        transaction1.setArchived(false);
        transactionService.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2);
        transaction2.setTimeStamp(new Date());
        transaction2.setAccountFrom("NL01INHO456874318");
        transaction2.setAccountTo("NL01INHO0000000001");
        transaction2.setAmount(100);
        transaction2.setPerformedBy(2);
        transaction2.setArchived(false);
        transactionService.add(transaction2);

    }
}
