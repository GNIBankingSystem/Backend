package com.gni.banking.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionResponseDTO {
    private long id;
    private String timestamp;
    private String accountFrom;
    private String accountTo;
    private double amount;
    private long performedBy;
}
