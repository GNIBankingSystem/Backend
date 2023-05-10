package com.gni.banking.Model;

import com.gni.banking.Enums.AccountType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
@Entity
@Data
@RequiredArgsConstructor
@Table(name = "transactions")
public class TransactionResponseDTO {
    @Id
    @GeneratedValue
    private int id;

    private String ibanTo;

    private String ibanFrom;

    private String description;

    private Date timeStamp;

    private int performedById;

    private AccountType type;
}
