package com.gni.banking.Service;

import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.TransactionResponseDTO;
import com.gni.banking.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    public List<Transaction> getAll() {
        return (List<Transaction>) repository.findAll();
    }

    public Transaction getById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Transaction add(Transaction transaction) {
        return repository.save(transaction);
    }

    public Transaction update(Transaction transaction, long id) {
        return repository.save(transaction);
    }

    public void delete(long id) {
        repository.archiveById(id);
    }
}

