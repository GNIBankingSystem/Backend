package com.gni.banking.Repository;

import com.gni.banking.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Modifying
    @Query("UPDATE Transaction t SET t.archived = true WHERE t.id = ?1")
    void archiveById(long id);

    @Query("SELECT t FROM Transaction t WHERE t.archived = false")
    Page<Transaction> findAll(Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.archived = false AND t.id = ?1")
    Optional<Transaction> findById(long id);

}
