package com.gni.banking.Configuration;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.AccountRequestDTO;
import com.gni.banking.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    @Autowired
    AccountService accountService;


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
    }
}
