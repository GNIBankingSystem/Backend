package com.gni.banking.Model;

import com.gni.banking.Enums.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Transaction {
        @Id
        @GeneratedValue
        private int id;
        private Date timestamp;
        private String accountFrom;
        private String accountTo;
        private double amount;
        private long performedBy;
        private boolean archived;
        private TransactionType type;



        public void setAmount(double amount) {
             if(amount <= 0) {
                 throw new IllegalArgumentException("Amount cannot be negative");
             } else {
                 this.amount = amount;
             }
        }

        public void setPerformedBy(long performedBy) {
            if(performedBy < 0) {
                throw new IllegalArgumentException("Invalid user id");
            } else {
                this.performedBy = performedBy;
            }
        }

        public void setAccountFrom(String accountFrom) {
            if(accountFrom.matches("NL\\d{2}INHO\\d{10}")) {
                this.accountFrom = accountFrom;
            } else {
                throw new IllegalArgumentException("Invalid IBAN");
            }
        }

        public void setAccountTo(String accountTo) {
            if(accountTo.matches("NL\\d{2}INHO\\d{10}")) {
                this.accountTo = accountTo;
            } else {
                throw new IllegalArgumentException("Invalid IBAN");
            }
        }

        public void setTimestamp(Date timestamp) {
            if(timestamp != null) {
                this.timestamp = timestamp;
            } else {
                throw new IllegalArgumentException("Invalid timestamp");
            }
        }


}
