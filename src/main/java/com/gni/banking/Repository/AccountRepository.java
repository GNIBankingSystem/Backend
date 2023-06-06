package com.gni.banking.Repository;

import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Iterable<Account> findByUserId(long userId);




    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.id = :iban")
    boolean ibanExists(@Param("iban") String iban);
    @Override
    Optional<Account> findById(String iban);

    @Query("SELECT a FROM Account a WHERE a.userId = :userId AND a.status = :status AND a.type = :type")
    List<Account> getCurrentAndOpenAccountsByUserId(@Param("userId") int userId, @Param("status") Status status, @Param("type") AccountType type);

    @Query("SELECT a FROM Account a WHERE a.userId = :userId AND a.status = :status")
    List<Account> getTotalBalanceOfAccounts(@Param("userId")int userId, @Param("status") Status status);

    List<Account> getIdByUserId(int id);
}
