package com.gni.banking.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.PutAccountDTO;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.AccountRepository;
import com.gni.banking.Repository.UserRepository;
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

import static com.gni.banking.Enums.Role.ROLE_EMPLOYEE;

public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IbanService ibanService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private ObjectMapper mapper;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    private Account getMockAccount(Long userId, AccountType type) throws Exception {
        Account account = new Account();
        account.setId("NL01INHO0000000091");
        account.setBalance(10000);
        account.setCurrency(Currency.EUR);
        account.setStatus(Status.Open);
        account.setType(type);
        account.setUserId(userId);
        return account;
    }

    private User getMockUser(long userId) throws Exception {
        User user = new User();
        user.setId(userId);
        user.setNumberofaccounts(1);
        user.setDayLimit(3000.00);
        user.setEmail("PieterVenema@gmail.com");
        user.setUsername("kip");
        user.setFirstName("pieter");
        user.setLastName("venema");
        user.setPassword("kip");
        user.setPhoneNumber(612345678);
        user.setRoles(ROLE_EMPLOYEE);
        user.setTransactionLimit(1000.00);
        user.setDailyTransaction(0);
        return user;
    }

    @Test
    public void testAccountAdd_Success() throws Exception {

        // Arrange
        PostAccountDTO accountRequest = new PostAccountDTO();
        accountRequest.setUserId(1L);
        accountRequest.setType(AccountType.Savings);

        Mockito.when(ibanService.GenerateIban()).thenReturn("NL01INHO0000000091");
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(getMockAccount(1L, AccountType.Savings));
        Mockito.when(userService.getById(1L)).thenReturn(getMockUser(1L));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(getMockUser(1L));

        // Act
        Account account = accountService.add(accountRequest);

        // Assert
        Assertions.assertEquals(account.getId(), "NL01INHO0000000091");
        Assertions.assertEquals(account.getBalance(), 10000.0);
        Assertions.assertEquals(account.getCurrency(), Currency.EUR);
        Assertions.assertEquals(account.getStatus(), Status.Open);
        Assertions.assertEquals(account.getType(), AccountType.Savings);
        Assertions.assertEquals(account.getUserId(), 1L);
    }

    @Test
    public void testAccountAdd_Failure() {
        // Arrange
        PostAccountDTO accountRequest = new PostAccountDTO();
        accountRequest.setUserId(2L);
        accountRequest.setType(AccountType.Savings);

        Mockito.when(ibanService.GenerateIban()).thenReturn("NL01INHO0000000092");
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenThrow(new RuntimeException());

        // Act & Assert
        Assertions.assertThrows(Exception.class, () -> accountService.add(accountRequest));
    }

    @Test
    public void testAccountUpdate_Success() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
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
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
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
        Account existingAccount = getMockAccount(1L, AccountType.Savings);

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
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByIdNot(pageable, "NL01INHO0000000001")).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, null, null, null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByIdNot(pageable, "NL01INHO0000000001");
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserIdAndTypeAndStatus(2, AccountType.Savings, Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, 2L, "Savings", "Open", null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserIdAndTypeAndStatus(2, AccountType.Savings, Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullUserId() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByTypeAndStatus(AccountType.Savings, Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, "Savings", "Open", null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByTypeAndStatus(AccountType.Savings, Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullType() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserIdAndStatus(2, Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, 2L, null, "Open", null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserIdAndStatus(2, Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullStatus() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserIdAndType(2, AccountType.Savings, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, 2L, "Savings", null, null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserIdAndType(2, AccountType.Savings, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullUserIdAndType() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByStatus(Status.Open, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, null, "Open", null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByStatus(Status.Open, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullUserIdAndStatus() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByType(AccountType.Savings, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, null, "Savings", null, null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByType(AccountType.Savings, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testAccountGetAll_Success_WithAllFilters_WithNullTypeAndStatus() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(accountRepository.findByUserId(2, pageable)).thenReturn(java.util.List.of(existingAccount));

        // Act
        java.util.List<Account> accounts = accountService.getAll(10, 1, 2L, null, null, null);

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserId(2, pageable);
        Assertions.assertEquals(1, accounts.size());
    }

    @Test
    public void testUpdateBalance_Success() throws Exception {
        // Arrange
        Account existingAccount = getMockAccount(1L, AccountType.Savings);
        Mockito.when(accountRepository.findById("1")).thenReturn(Optional.of(existingAccount));
        existingAccount.setBalance(1000.0);

        // Act
        accountService.updateBalance(existingAccount, "1");

        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).save(existingAccount);
        Assertions.assertEquals(1000.0, existingAccount.getBalance());
    }
}
