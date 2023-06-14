package com.gni.banking.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private IbanService ibanService;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private ObjectMapper mapper;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    private Account getMockAccount(int userId, AccountType type) throws Exception {
        Account account = new Account();
        account.setId("NL01INHO0000000091");
        account.setBalance(10000);
        account.setCurrency(Currency.EUR);
        account.setStatus(Status.Open);
        account.setType(type);
        account.setUserId(userId);
        return account;
    }

    @Test
    public void testAdd_AccountCreation_Success() throws Exception {
        // Arrange
        PostAccountDTO accountRequest = new PostAccountDTO();
        accountRequest.setUserId(1);
        accountRequest.setType(AccountType.Savings);

        Account expectedAccount = new Account();
        expectedAccount.setId("IBAN123");
        expectedAccount.setUserId(accountRequest.getUserId());
        expectedAccount.setType(accountRequest.getType());
        expectedAccount.setAbsoluteLimit(1000.00);
        expectedAccount.setCurrency(Currency.EUR);
        expectedAccount.setBalance(0.00);
        expectedAccount.setStatus(Status.Open);

        Mockito.when(ibanService.GenerateIban()).thenReturn("IBAN123");
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(expectedAccount);

        // Act
        Account createdAccount = accountService.add(accountRequest);

        // Assert
        Assertions.assertEquals(expectedAccount, createdAccount);
        Mockito.verify(ibanService, Mockito.times(2)).GenerateIban();
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testAdd_AccountCreation_Failure() {
        // Arrange
        PostAccountDTO accountRequest = new PostAccountDTO();
        accountRequest.setUserId(1);
        accountRequest.setType(AccountType.Savings);

        Mockito.when(ibanService.GenerateIban()).thenReturn("IBAN123");
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenThrow(new RuntimeException());

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> accountService.add(accountRequest));
        Mockito.verify(ibanService, Mockito.times(1)).GenerateIban();
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }
}
