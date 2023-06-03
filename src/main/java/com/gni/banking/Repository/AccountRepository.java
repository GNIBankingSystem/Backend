package com.gni.banking.Repository;

import com.gni.banking.Model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Iterable<Account> findByUserId(long userId);

    Optional<Object> findByIban(String iban);
}
