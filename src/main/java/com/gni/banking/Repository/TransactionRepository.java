package com.gni.banking.Repository;

import com.gni.banking.Model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("UPDATE Transaction t SET t.archived = true WHERE t.id = ?1")
    void archiveById(long id);

    @Query("SELECT t FROM Transaction t WHERE t.archived = false")
    Iterable<Transaction> findAll();


}
