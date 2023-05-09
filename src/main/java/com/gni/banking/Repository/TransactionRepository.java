package com.gni.banking.Repository;

import com.gni.banking.Model.TransactionResponseDTO;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionResponseDTO,Long> {
}
