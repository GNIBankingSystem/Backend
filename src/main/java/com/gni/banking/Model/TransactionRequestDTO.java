package com.gni.banking.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequestDTO {
    private long id;
    private String accountFrom;
    private String accountTo;
    private double amount;
}
