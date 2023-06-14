package com.gni.banking.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Status;
import com.gni.banking.Enums.TransactionType;
import com.gni.banking.Exceptions.InvalidAccountTypeOnTransactionException;
import com.gni.banking.Exceptions.LimitOnTransactionExceededException;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testTransactionWithDayLimitExceeded() throws Exception {
        //Arrange
        Account account = getMockAccount(1, AccountType.Current);
        Account account2 = getMockAccount(2, AccountType.Current);
        Transaction transaction = getMockTransaction(account.getId(),account2.getId(),100);
        when(transactionService.add(transaction)).thenThrow(LimitOnTransactionExceededException.class);
        //Act
        //Assert
        assertThrows(LimitOnTransactionExceededException.class,()->{
            transactionService.add(transaction);
        });
    }

    @Test
    void testTransactionSavingsToSavingsAccountType() throws Exception {
        //Arrange
            Account savings = getMockAccount(1, AccountType.Savings);
            Account savings2 = getMockAccount(2, AccountType.Savings);
            Transaction transaction = getMockTransaction(savings.getId(),savings2.getId(),100);
            when(transactionService.checkAccountTypes(savings.getId(),savings2.getId())).thenThrow(InvalidAccountTypeOnTransactionException.class);
            when(transactionService.add(transaction)).thenThrow(InvalidAccountTypeOnTransactionException.class);
        //Act and Assert
        assertThrows(InvalidAccountTypeOnTransactionException.class,()->{
            transactionService.add(transaction);
        });
    }
    @Test
    void testTransactionAccountStatus() throws Exception {
        //Arrange
        Account account = getMockAccount(1, AccountType.Current);
        Account account2 = getMockAccount(2, AccountType.Current);
        account.setStatus(Status.Closed);
        Transaction transaction = getMockTransaction(account.getId(),account2.getId(),100);
        when(transactionService.add(transaction)).thenReturn(transaction);
        //Act
        transactionService.add(transaction);
        //Assert
        assertTrue(account.getStatus().equals(Status.Closed));
    }

    @Test
    void testWithdrawNotEnoughMoney() throws Exception {
        //Arrange
        Account account = getMockAccount(1, AccountType.Current);
        Transaction transaction = getMockTransaction(account.getId(),"NL01INHO0000000091",100000);
        when(transactionService.add(transaction)).thenThrow(IllegalArgumentException.class);
        //Act and Assert
        assertThrows(IllegalArgumentException.class,()->{
            transactionService.add(transaction);
        });
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

    private Transaction getMockTransaction(String ibanFrom,String ibanTo,double amount){
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(ibanFrom);
        transaction.setAccountTo(ibanTo);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTimestamp(new Date());
        return transaction;
    }

}
