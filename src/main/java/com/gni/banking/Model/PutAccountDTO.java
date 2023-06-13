package com.gni.banking.Model;

import com.gni.banking.Enums.AccountType;
import lombok.Data;

@Data
public class PutAccountDTO {
    private int userId;
    private double absoluteLimit;
    private AccountType type;
}
