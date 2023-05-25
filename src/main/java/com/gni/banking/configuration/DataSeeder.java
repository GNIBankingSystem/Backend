package com.gni.banking.configuration;


import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.gni.banking.Enums.AccountType.Current;
import static com.gni.banking.Enums.Currency.EUR;
import static com.gni.banking.Enums.Status.Open;

@Component
public class DataSeeder implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Override
    public void run(ApplicationArguments args) {
        Account account1 = new Account(1, "NL01INHO0000000001", 1000, Current, 1000, EUR, Open);
        accountService.add(account1);

        Account account2 = new Account(2, "NL01INHO0000000002", 2000, Current, 2000, EUR, Open);
        accountService.add(account2);
    }
}
