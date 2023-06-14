package com.gni.banking.Model;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    Account account;

    @BeforeEach
    void setUp() {
        account = new Account("NL02INHO0000000001", 1, AccountType.Current, 0.0, Currency.EUR, 1000, Status.Open);
    }

    @Test
    void ibanShouldBeValid() {
        assert account.getId().matches("NL\\d{2}INHO\\d{10}");
    }

    @Test
    void userIdShouldBeValid() {
        assert account.getUserId() > 0;
    }

    @Test
    void setIdWhileItIsAlreadySet() {
        assertThrows(Exception.class, () -> account.setId("NL01INH0000000001"));
    }

    @Test
    void setIdWhileItIsNotValid() {
        assertThrows(Exception.class, () -> account.setId("NL01INH000000"));
    }

    @Test
    void setUserIdNegative() {
        assertThrows(IllegalArgumentException.class, () -> account.setUserId(-1));
    }

    @Test
    void setAbsoluteLimit() {
        assertThrows(IllegalArgumentException.class, () -> account.setAbsoluteLimit(-1));
    }

    @Test
    void setAbsoluteLimitHigherThanBalance() {
        account.setBalance(20);
        assertThrows(IllegalArgumentException.class, () -> account.setAbsoluteLimit(30));
    }

    @Test
    void setBalanceLowerThanAbsoluteLimit() {
        account.setBalance(20);
        account.setAbsoluteLimit(10);
        assertThrows(IllegalArgumentException.class, () -> account.setBalance(2));
    }

    @Test
    void setBalanceNegative() {
        assertThrows(IllegalArgumentException.class, () -> account.setBalance(-1));
    }
}
