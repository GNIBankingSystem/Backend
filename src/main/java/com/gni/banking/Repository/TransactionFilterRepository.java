package com.gni.banking.Repository;

import com.gni.banking.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionFilterRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

}
