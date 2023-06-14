package com.gni.banking.Model;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTest {
    Transaction transaction;
    @BeforeEach
    void setUp() {
        transaction = new Transaction(1,new Date(),"NL02INHO0000000001","NL02INHO0000000002",100,1,false, TransactionType.TRANSFER);
    }
    @Test
    void setAmountNegative() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setAmount(-1));
    }

    @Test
    void setAmountZero() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setAmount(0));
    }

    @Test
    void setInvalidAccountFrom() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setAccountFrom("NL02INHO000000000"));
    }
    @Test
    void setInvalidAccountTo() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setAccountTo("NL02INHO000000000"));
    }
    @Test
    void setInvalidPerformedBy() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setPerformedBy(-1));
    }
    @Test
    void setInvalidAccountFromFormat() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setAccountFrom("dksjbfdksjbfksd"));
    }
    @Test
    void setInvalidAccountToFormat() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setAccountTo("dksjbfdksjbfksd"));
    }
}
