package com.gni.banking.Repository;

<<<<<<< Updated upstream
=======

import com.gni.banking.Enums.Userstatus;
import com.gni.banking.Model.Account;
>>>>>>> Stashed changes
import com.gni.banking.Model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

<<<<<<< Updated upstream
import java.lang.reflect.Array;
=======

import java.util.Collection;
>>>>>>> Stashed changes
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

	@Query("SELECT dayLimit FROM User WHERE id = :userId")
	double getDayLimitById(@Param("userId") int userId);

	List<User> findByFirstNameAndLastName(String firstName, String lastName);
    // Add custom repository methods if needed

<<<<<<< Updated upstream
	@Query("SELECT u FROM User u WHERE u.id = :id")
	Optional<User> findById(long id);

	@Query("SELECT u FROM User u WHERE u.numberofaccounts = 0")
	List<User> findUsersWithoutAccount();
=======

>>>>>>> Stashed changes

	@Query("SELECT dailyTransaction FROM User WHERE id = :userId")
	double getDailyTransaction(@Param("userId") int userId);

<<<<<<< Updated upstream
	@Query("UPDATE User SET dailyTransaction = :newDailyTransaction WHERE id = :userId")
=======
	List<User> findByUserIdAndStatus(String userId, Userstatus userStatus, Pageable pageable);

	List<User> findByUserIdAndUsername(String userId, String username, Pageable pageable);


	@Query("SELECT u FROM User u WHERE u.id = :userId")
	List<User> findByUserId(String userId, Pageable pageable);

	List<User> findByStatus(Userstatus userStatus, Pageable pageable);

	List<User> findByUsername(String username, Pageable pageable);



	List<User> findAll(Pageable pageable);

	Optional<User> findUserByUsername(String username);

	

	/*@Query("UPDATE User SET dailyTransaction = :newDailyTransaction WHERE id = :userId")
>>>>>>> Stashed changes
	@Modifying
	void updateDailyTransaction(@Param("userId")int userId, @Param("newDailyTransaction") double newDailyTransaction);



	boolean existsByUsername(String username);
}
