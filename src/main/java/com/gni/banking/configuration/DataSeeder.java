package com.gni.banking.Configuration;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Enums.TransactionType;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.User;
import com.gni.banking.Service.AccountService;
import com.gni.banking.Service.TransactionService;
import com.gni.banking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.gni.banking.Enums.Role.*;

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
        user1.setId(2);
        user1.setNumberofaccounts(1);
        user1.setDayLimit(3000.00);
        user1.setEmail("PieterVenema@gmail.com");
        user1.setUsername("admin");
        user1.setFirstName("pieter");
        user1.setLastName("venema");
        user1.setPassword("admin");
        user1.setPhoneNumber(612345678);
        user1.setRoles(ROLE_EMPLOYEE);
        user1.setTransactionLimit(1000.00);
        user1.setDailyTransaction(0);
        userService.add(user1);

        User user2 = new User();
        user2.setId(1);
        user2.setNumberofaccounts(1);
        user2.setDayLimit(3000.00);
        user2.setEmail("pietervenema@gmail.com");
        user2.setUsername("customer");
        user2.setFirstName("pieter");
        user2.setLastName("jhonsen");
        user2.setPassword("customer");
        user2.setPhoneNumber(612345678);
        user2.setRoles(ROLE_EMPLOYEE);
        user2.setTransactionLimit(1000.00);
        user2.setDailyTransaction(0);
        userService.add(user2);

        User user3 = new User();
        user3.setId(3);
        user3.setNumberofaccounts(1);
        user3.setDayLimit(3000.00);
        user3.setEmail("pietervenema@gmail.com");
        user3.setUsername("employee");
        user3.setFirstName("pieter");
        user3.setLastName("jhonsen");
        user3.setPassword("employee");
        user3.setPhoneNumber(612345678);
        user3.setRoles(ROLE_EMPLOYEE);
        user3.setTransactionLimit(1000.00);
        user3.setDailyTransaction(0);
        userService.add(user3);



        Account account = new Account();
        account.setUserId(1);
        account.setType(AccountType.Current);
        account.setCurrency(Currency.EUR);
        account.setBalance(550000000);
        account.setAbsoluteLimit(100000);
        account.setStatus(Status.Open);
        account.setId("NL01INHO0000000001");
        accountService.addCompleteAccount(account);

        Account account2 = new Account();
        account2.setUserId(1);
        account2.setType(AccountType.Current);
        account2.setCurrency(Currency.EUR);
        account2.setBalance(550000000);
        account2.setAbsoluteLimit(100000000);
        account2.setStatus(Status.Open);
        account2.setId("NL01INHO0000000011");
        accountService.addCompleteAccount(account2);

        PostAccountDTO account1 = new PostAccountDTO();
        account1.setUserId(1);
        account1.setType(AccountType.Savings);
        accountService.add(account1);

        PostAccountDTO account22 = new PostAccountDTO();
        account2.setUserId(2);
        account2.setType(AccountType.Current);
        accountService.add(account22);

        PostAccountDTO account3 = new PostAccountDTO();
        account3.setUserId(2);
        account3.setType(AccountType.Current);
        accountService.add(account3);

        PostAccountDTO account4 = new PostAccountDTO();
        account4.setUserId(2);
        account4.setType(AccountType.Savings);
        accountService.add(account4);

        Transaction transaction1 = new Transaction();
        transaction1.setId(1);
        transaction1.setTimestamp(new Date());
        transaction1.setAccountFrom("NL01INHO0000000001");
        transaction1.setAccountTo("NL01INHO0000000011");
        transaction1.setAmount(50);
        transaction1.setPerformedBy(1);
        transaction1.setType(TransactionType.TRANSFER);
        transaction1.setArchived(false);
        transactionService.add(transaction1);


    }
}
