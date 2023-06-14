package com.gni.banking.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.PutAccountDTO;
import com.gni.banking.Repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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
    public void testAccountAdd_Success() throws Exception {
        // Arrange
        PostAccountDTO accountRequest = new PostAccountDTO();
        accountRequest.setUserId(2);
        accountRequest.setType(AccountType.Savings);

        Account expectedAccount = new Account();
        expectedAccount.setId("NL01INHO0000000091");
        expectedAccount.setUserId(accountRequest.getUserId());
        expectedAccount.setType(accountRequest.getType());
        expectedAccount.setAbsoluteLimit(0.00);
        expectedAccount.setCurrency(Currency.EUR);
        expectedAccount.setBalance(20.00);
        expectedAccount.setStatus(Status.Open);

        Mockito.when(ibanService.GenerateIban()).thenReturn("NL01INHO0000000092");
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(expectedAccount);

        // Act
        Account createdAccount = accountService.add(accountRequest);

        // Assert
        Assertions.assertEquals(expectedAccount, createdAccount);
        Mockito.verify(ibanService, Mockito.times(1)).GenerateIban();
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testAccountAdd_Failure() {
        // Arrange
        PostAccountDTO accountRequest = new PostAccountDTO();
        accountRequest.setUserId(1);
        accountRequest.setType(AccountType.Savings);

        Mockito.when(ibanService.GenerateIban()).thenReturn("NL01INHO0000000092");
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenThrow(new RuntimeException());

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> accountService.add(accountRequest));
        Mockito.verify(ibanService, Mockito.times(1)).GenerateIban();
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testAccountUpdate_Success() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        PutAccountDTO accountRequest = new PutAccountDTO();
        accountRequest.setAbsoluteLimit(1);
        accountRequest.setUserId(1);
        accountRequest.setType(AccountType.Current);

        Mockito.when(accountRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(existingAccount));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(existingAccount);

        // Act
        Account updatedAccount = accountService.update(accountRequest, existingAccount.getId());

        // Assert
        Assertions.assertEquals(existingAccount, updatedAccount);
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testAccountUpdate_Failure() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        PutAccountDTO accountRequest = new PutAccountDTO();
        accountRequest.setAbsoluteLimit(21);
        accountRequest.setUserId(1);
        accountRequest.setType(AccountType.Current);

        Mockito.when(accountRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(existingAccount));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenThrow(new RuntimeException());

        // Act & Assert
        Assertions.assertThrows(Exception.class, () -> accountService.update(accountRequest, existingAccount.getId()));
    }

    @Test
    public void testAccountDelete_Success() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);

        Mockito.when(accountRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(existingAccount));
        Mockito.doNothing().when(accountRepository).delete(Mockito.any(Account.class));

        // Act
        accountService.changeStatus(existingAccount.getId());

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyString());
        Assertions.assertEquals(Status.Closed, existingAccount.getStatus());
    }

    @Test
    public void testAccountGetAll_Success() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findAll(pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, null, null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findAll(pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserIdAndTypeAndStatus("2", AccountType.Savings, Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, "2", "Savings", "Open");

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserIdAndTypeAndStatus("2", AccountType.Savings, Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullUserId() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByTypeAndStatus(AccountType.Savings, Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, "Savings", "Open");

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByTypeAndStatus(AccountType.Savings, Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullType() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserIdAndStatus("2", Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, "2", null, "Open");

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserIdAndStatus("2", Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullStatus() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserIdAndType("2", AccountType.Savings, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, "2", "Savings", null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserIdAndType("2", AccountType.Savings, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullUserIdAndType() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByStatus(Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, null, "Open");

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByStatus(Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullUserIdAndStatus() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByType(AccountType.Savings, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, "Savings", null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByType(AccountType.Savings, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullTypeAndStatus() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserId("2", pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, "2", null, null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserId("2", pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testUpdateBalance_Success() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1, AccountType.Savings);
        Mockito.when(accountRepository.findById("1")).thenReturn(Optional.of(existingAccount));
        existingAccount.setBalance(1000.0);

        // Act
        accountService.updateBalance(existingAccount, "1");

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).save(existingAccount);
        Assertions.assertEquals(1000.0, existingAccount.getBalance());
    }
}
