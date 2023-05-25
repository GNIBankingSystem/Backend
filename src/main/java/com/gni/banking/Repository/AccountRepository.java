package com.gni.banking.Repository;

import com.gni.banking.Model.AccountResponseDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountResponseDTO,Long> {
}
