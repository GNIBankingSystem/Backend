package com.gni.banking.Configuration;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.User;
import com.gni.banking.Service.AccountService;
import com.gni.banking.Service.TransactionService;
import com.gni.banking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.gni.banking.Enums.Role.Customer;
import static com.gni.banking.Enums.Role.Employee;

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
        user1.setUsername("username");
        user1.setFirstName("pieter");
        user1.setLastName("venema");
        user1.setPassword("password");
        user1.setPhoneNumber(612345678);
        user1.setRoles(Employee);
        user1.setTransactionLimit(1000.00);
        user1.setDailyTransaction(0);
        userService.add(user1);

        User user2 = new User();
        user2.setId(1);
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


/*        PostAccountDTO account = new PostAccountDTO();
        account.setUserId(1);
        account.setType(AccountType.Current);
        Account acc = accountService.add(account);
        acc.setAbsoluteLimit(1000);
        acc.setCurrency(Currency.EUR);
        acc.setBalance(550000000);
        acc.setStatus(Status.Open);
        acc.setId("NL01INHO0000000001");
        accountService.update(acc, "NL01INHO0000000001");*/


        PostAccountDTO account1 = new PostAccountDTO();
        account1.setUserId(1);
        account1.setType(AccountType.Savings);
        accountService.add(account1);

        PostAccountDTO account2 = new PostAccountDTO();
        account2.setUserId(2);
        account2.setType(AccountType.Current);
        accountService.add(account2);

        PostAccountDTO account3 = new PostAccountDTO();
        account2.setUserId(2);
        account2.setType(AccountType.Current);
        accountService.add(account3);

        PostAccountDTO account4 = new PostAccountDTO();
        account2.setUserId(2);
        account2.setType(AccountType.Savings);
        accountService.add(account4);

        /*Transaction transaction1 = new Transaction();
        transaction1.setId(1);
        transaction1.setTimeStamp(new Date());
        transaction1.setAccountFrom("NL01INHO0000000001");
        transaction1.setAccountTo("NL01INHO45687431812");
        transaction1.setAmount(50);
        transaction1.setPerformedBy(1);
        transaction1.setArchived(false);
        transactionService.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2);
        transaction2.setTimeStamp(new Date());
        transaction2.setAccountFrom("NL01INHO45687431812");
        transaction2.setAccountTo("NL01INHO0000000001");
        transaction2.setAmount(100);
        transaction2.setPerformedBy(2);
        transaction2.setArchived(false);
        transactionService.add(transaction2);*/

    }
}
