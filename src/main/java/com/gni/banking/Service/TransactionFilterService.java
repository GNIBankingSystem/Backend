package com.gni.banking.Service;

import com.gni.banking.Model.Transaction;
import com.gni.banking.Repository.TransactionFilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionFilterService {
    @Autowired
    private TransactionFilterRepository transactionFilterRepository;

    public List<Transaction> getTransactionByPerformedByWithFilter(long id, Date startDate, Date endDate, String ibanTo, String comparisonOperator, Double balance){
        return transactionFilterRepository.findAll(Specification
                .where(hasPerformedById(id))
                .and(betweenDates(startDate, endDate))
                .and(hasIbanTo(ibanTo))
                .and(comparingBalance(comparisonOperator, balance)));
    }
    private Specification<Transaction> hasPerformedById(long id) {
        return (root, query, cb) -> cb.equal(root.get("performedBy"), id);
    }

    private Specification<Transaction> betweenDates(Date startDate, Date endDate) {
        return (root, query, cb) -> {
            if (startDate != null && endDate != null) {
                return cb.between(root.get("timestamp"), startDate, endDate);
            } else {
                return null;
            }
        };
    }

    private Specification<Transaction> hasIbanTo(String ibanTo) {
        return (root, query, cb) -> {
            if (ibanTo != null) {
                return cb.equal(root.get("accountTo"), ibanTo);
            } else {
                return null;
            }
        };
    }

    private Specification<Transaction> comparingBalance(String comparisonOperator, Double balance) {
        return (root, query, cb) -> {
            if (balance != null && comparisonOperator != null) {
                switch (comparisonOperator) {
                    case "<":
                        return cb.lessThan(root.get("amount"), balance);
                    case "==":
                        return cb.equal(root.get("amount"), balance);
                    case ">":
                        return cb.greaterThan(root.get("amount"), balance);
                    default:
                        return null;
                }
            } else {
                return null;
            }
        };
    }
}
