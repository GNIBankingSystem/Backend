package com.gni.banking.Repository;

import com.gni.banking.Model.UserResponseDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserResponseDTO,Long> {
}
