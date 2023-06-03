package com.gni.banking.Configuration;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.AccountRequestDTO;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Service.AccountService;
import com.gni.banking.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataSeeder implements ApplicationRunner {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        account.setUserId(1);
        account.setType(AccountType.Current);
        account.setAbsoluteLimit(1000);
        account.setCurrency(Currency.EUR);
        account.setBalance(550000000);
        account.setStatus(Status.Open);
        accountService.add(account);

        Account account1 = new Account();
        account1.setIban("NL01INHO456874318");
        account1.setUserId(2);
        account1.setType(AccountType.Current);
        account1.setAbsoluteLimit(1000);
        account1.setCurrency(Currency.EUR);
        account1.setBalance(55);
        account1.setStatus(Status.Open);
        accountService.add(account1);
//
//        Transaction transaction1 = new Transaction();
//        transaction1.setId(1);
//        transaction1.setTimeStamp(new Date());
//        transaction1.setAccountFrom("NL01INHO0000000001");
//        transaction1.setAccountTo("NL01INHO456874318");
//        transaction1.setAmount(50);
//        transaction1.setPerformedBy(1);
//        transaction1.setArchived(false);
//        transactionService.add(transaction1);
//
//        Transaction transaction2 = new Transaction();
//        transaction2.setId(2);
//        transaction2.setTimeStamp(new Date());
//        transaction2.setAccountFrom("NL01INHO456874318");
//        transaction2.setAccountTo("NL01INHO0000000001");
//        transaction2.setAmount(100);
//        transaction2.setPerformedBy(2);
//        transaction2.setArchived(false);
//        transactionService.add(transaction2);

    }
}
