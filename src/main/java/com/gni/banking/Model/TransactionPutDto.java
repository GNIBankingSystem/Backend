package com.gni.banking.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TransactionPutDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss zzz yyyy", locale = "us")
    private Date timestamp;
    private String accountFrom;
    private String accountTo;
    private double amount;
    private long performedBy;
}
