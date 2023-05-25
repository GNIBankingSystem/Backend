package com.gni.banking.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
public class Transaction {
        @Id
        @GeneratedValue
        private int id;
        private Date timeStamp;
        private String accountFrom;
        private String accountTo;
        private double amount;
        private long performedBy;
        private boolean archived;
}
