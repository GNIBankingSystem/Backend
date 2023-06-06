package com.gni.banking.Repository;


import com.gni.banking.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.lang.reflect.Array;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    Iterable<User> findByUserId(long userId);


	@Query("SELECT dayLimit FROM User WHERE id = :userId")
	double getDayLimitById(@Param("userId") int userId);

	List<User> findByFirstNameAndLastName(String firstName, String lastName);
    // Add custom repository methods if needed

	Optional<User> findUserByUsername(String username);
}
