package com.gni.banking.Repository;

import com.gni.banking.Model.User;

import com.gni.banking.Model.UserResponseDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.List;

@Repository

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByFirstNameAndLastName(String firstName, String lastName);
    // Add custom repository methods if needed

	@Query("SELECT dayLimit FROM User WHERE id = :userId")
	double getDayLimitById(@Param("userId") int userId);



}
