package com.gni.banking.Model;


import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String id;

    private long userId;

    private AccountType type;

    private double absoluteLimit;

    private Currency currency;

    private double balance;

    private Status status;


    public void setId(String id) throws Exception {
        if (id.matches("NL\\d{2}INHO\\d{10}") && this.id == null) {
            this.id = id;
        } else if (this.id != null) {
            throw new Exception("Account already contains an id");
        } else {
            throw new Exception("Invalid IBAN");
        }
    }

    public void setUserId(long userId) {
        if (userId > 0) {
            this.userId = userId;
        } else {
            throw new IllegalArgumentException("Invalid user id");
        }
    }

    public void setBalance(double balance) {
        if (balance < absoluteLimit) {
            throw new IllegalArgumentException("Balance cannot be lower than absolute limit");
        } else if (balance >= 0) {
            this.balance = balance;
        } else {
            throw new IllegalArgumentException("Invalid balance");
        }
    }

    public void setAbsoluteLimit(double absoluteLimit) {
        if (absoluteLimit >= 0) {
            this.absoluteLimit = absoluteLimit;
        } else {
            throw new IllegalArgumentException("Invalid absolute limit must be positive");
        }
    }

}
