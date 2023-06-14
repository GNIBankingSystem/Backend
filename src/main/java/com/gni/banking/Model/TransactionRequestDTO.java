package com.gni.banking.Model;

import com.gni.banking.Enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequestDTO {
    private String accountFrom;
    private String accountTo;
    private double amount;
    private TransactionType type;
}
