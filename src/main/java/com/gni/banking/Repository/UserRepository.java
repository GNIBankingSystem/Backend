package com.gni.banking.Repository;

import com.gni.banking.Model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

	@Query("SELECT dayLimit FROM User WHERE id = :userId")
	double getDayLimitById(@Param("userId") int userId);

	List<User> findByFirstNameAndLastName(String firstName, String lastName);
    // Add custom repository methods if needed

	@Query("SELECT u FROM User u WHERE u.id = :id")
	Optional<User> findById(long id);

	@Query("SELECT u FROM User u WHERE u.numberofaccounts = 0")
	List<User> findUsersWithoutAccount();

	@Query("SELECT dailyTransaction FROM User WHERE id = :userId")
	double getDailyTransaction(@Param("userId") int userId);

	@Query("UPDATE User SET dailyTransaction = :newDailyTransaction WHERE id = :userId")
	@Modifying
	void updateDailyTransaction(@Param("userId")int userId, @Param("newDailyTransaction") double newDailyTransaction);

	Optional<User> findUserByUsername(String username);

	boolean existsByUsername(String username);

}
