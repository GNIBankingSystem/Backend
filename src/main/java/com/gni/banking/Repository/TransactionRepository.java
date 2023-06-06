package com.gni.banking.Repository;

import com.gni.banking.Model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("UPDATE Transaction t SET t.archived = true WHERE t.id = ?1")
    void archiveById(long id);

    @Query("SELECT t FROM Transaction t WHERE t.archived = false")
    Iterable<Transaction> findAll();

    @Query("SELECT t.amount FROM Transaction t WHERE t.accountFrom = :iban AND t.timeStamp >= :beginDate AND t.timeStamp <= :endDate")
    List<Double> todaysTransactionOfUser(@Param("iban") String iban, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

}
