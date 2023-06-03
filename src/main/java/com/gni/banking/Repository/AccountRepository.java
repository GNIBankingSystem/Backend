package com.gni.banking.Repository;

import com.gni.banking.Model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Iterable<Account> findByUserId(long userId);

    @Override
    Optional<Account> findById(String id);


    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.id = :iban")
    boolean ibanExists(@Param("iban") String iban);
}
