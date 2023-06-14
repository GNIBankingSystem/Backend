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
        account = new Account("NL02INHO0000000001", 1, AccountType.Current, 1000, Currency.EUR, 1000, Status.Open);
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
    void typeShouldBeValid() {
        assert account.getType() != null;
    }

    @Test
    void absoluteLimitShouldBeValid() {
        assert account.getAbsoluteLimit() >= 0;
    }

    @Test
    void setId() {
        assertThrows(Exception.class, () -> account.setId("NL01INH0000000001"));
    }

    @Test
    void setUserId() {
    }

    @Test
    void setType() {
    }

    @Test
    void setAbsoluteLimit() {
    }

    @Test
    void setCurrency() {
    }

    @Test
    void setBalance() {
    }

    @Test
    void setStatus() {
    }
}
