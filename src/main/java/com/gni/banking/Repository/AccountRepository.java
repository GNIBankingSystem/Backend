package com.gni.banking.Repository;

import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Iterable<Account> findByIban(String iban);

    @Override
    Iterable<Account> deleteById();
}