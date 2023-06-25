package com.gni.banking.Configuration;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Enums.TransactionType;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
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
        user1.setDayLimit(3000.00);
        user1.setEmail("PieterVenema@gmail.com");
        user1.setUsername("admin");
        user1.setFirstName("pieter");
        user1.setLastName("venema");
        user1.setPassword("P@ssw0rdAdmin1!");
        user1.setPhoneNumber(612345678);
        user1.setRoles(ROLE_ADMIN);
        user1.setTransactionLimit(1000.00);
        user1.setDailyTransaction(0);
        userService.add(user1);

        User user2 = new User();
        user2.setId(1);
        user2.setDayLimit(3000.00);
        user2.setEmail("pietervenema@gmail.com");
        user2.setUsername("customer");
        user2.setFirstName("pieter");
        user2.setLastName("jhonsen");
        user2.setPassword("Cust0m3r@cc3ss");
        user2.setPhoneNumber(612345678);
        user2.setRoles(ROLE_CUSTOMER);
        user2.setTransactionLimit(1000.00);
        user2.setDayLimit(3000.00);
        user2.setDailyTransaction(1000.00);
        userService.add(user2);

        User user3 = new User();
        user3.setId(3);
        user3.setDayLimit(3000.00);
        user3.setEmail("pietervenema@gmail.com");
        user3.setUsername("employee");
        user3.setFirstName("pieter");
        user3.setLastName("van der venema");
        user3.setPassword("3mpl0yee#S3cur3");
        user3.setPhoneNumber(612345678);
        user3.setRoles(ROLE_EMPLOYEE);
        user3.setTransactionLimit(1000.00);
        user3.setDailyTransaction(0);
        userService.add(user3);


        Account account = new Account();
        account.setUserId(0);
        account.setType(AccountType.Current);
        account.setCurrency(Currency.EUR);
        account.setBalance(550000000);
        account.setAbsoluteLimit(1000000);
        account.setStatus(Status.Open);
        account.setId("NL01INHO0000000001");
        accountService.addCompleteAccount(account);

        Account account2 = new Account();
        account2.setUserId(3);
        account2.setType(AccountType.Current);
        account2.setCurrency(Currency.EUR);
        account2.setBalance(550000000);
        account2.setAbsoluteLimit(10000000);
        account2.setStatus(Status.Open);
        account2.setId("NL01INHO0000000011");
        accountService.addCompleteAccount(account2);

        Account account3 = new Account();
        account3.setUserId(2);
        account3.setType(AccountType.Current);
        account3.setCurrency(Currency.EUR);
        account3.setBalance(29.40);
        account3.setAbsoluteLimit(0);
        account3.setStatus(Status.Open);
        account3.setId("NL01INHO0000000012");
        accountService.addCompleteAccount(account3);

        PostAccountDTO postAccountDTO = new PostAccountDTO();
        postAccountDTO.setUserId(1L);
        postAccountDTO.setType(AccountType.Current);
        accountService.add(postAccountDTO);

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

        Transaction transaction2 = new Transaction();
        transaction2.setId(2);
        transaction2.setTimestamp(new Date());
        transaction2.setAccountFrom("NL01INHO0000000001");
        transaction2.setAccountTo("NL01INHO0000000011");
        transaction2.setAmount(100);
        transaction2.setPerformedBy(1);
        transaction2.setType(TransactionType.TRANSFER);
        transaction2.setArchived(false);
        transactionService.add(transaction2);


    }
}
