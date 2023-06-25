package com.gni.banking.Repository;

import com.gni.banking.Model.Account;
import com.gni.banking.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Modifying
    @Query("UPDATE Transaction t SET t.archived = true WHERE t.id = ?1")
    void archiveById(long id);

    @Query("SELECT t FROM Transaction t WHERE t.archived = false")
    Page<Transaction> findAll(Pageable pageable);


    @Query("SELECT t.amount FROM Transaction t WHERE t.accountFrom = :iban AND t.timestamp >= :beginDate AND t.timestamp <= :endDate")
    List<Double> todaysTransactionOfUser(@Param("iban") String iban, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    @Query("SELECT t FROM Transaction t WHERE t.archived = false AND t.id = ?1")
    Optional<Transaction> findById(long id);

    @Query("SELECT t FROM Transaction t WHERE (t.accountFrom = :iban OR t.accountTo = :iban) AND t.timestamp >= :cutoffDate ")
    Page<Transaction> findTransactionsByIban(@Param("iban") String iban, @Param("cutoffDate") Date cutoffDate, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.performedBy = ?1 AND t.archived = false")
    List<Transaction> getTransactionsByPerformedBy(long id);

}
