package com.gni.banking.Model;

import com.gni.banking.Enums.TransactionType;
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
    private TransactionType type;
}
