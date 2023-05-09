package com.gni.banking.Model;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AccountResponseDTO {
    @Id
    @GeneratedValue
    private int id;

    private String iban;

    private int userId;

    private AccountType type;

    private double absoluteLimit;

    private Currency currency;

    private Status status;
}
