package com.gni.banking.Model;

import com.gni.banking.Enums.AccountType;
import lombok.Data;

@Data
public class PostAccountDTO {
    private int userId;
    private AccountType type;
}
