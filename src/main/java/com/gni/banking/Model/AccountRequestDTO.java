package com.gni.banking.Model;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;

public class AccountRequestDTO {
    private String iban;

    private int userId;

    private AccountType type;

    private double absoluteLimit;

    private Currency currency;

    private Status status;
}
