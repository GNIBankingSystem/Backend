package com.gni.banking.Repository;



import com.gni.banking.Enums.Userstatus;

import com.gni.banking.Model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



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



	@Query("SELECT dailyTransaction FROM User WHERE id = :userId")
	double getDailyTransaction(@Param("userId") int userId);





	@Query("SELECT u FROM User u WHERE u.id = :userId AND u.active = :userStatus")
	List<User> findByUserIdAndStatus(@Param("userId") String userId, @Param("userStatus") Userstatus userStatus, Pageable pageable);


	@Query("SELECT u FROM User u WHERE u.id = :userId AND u.active = :username")
	List<User> findByUserIdAndUsername(@Param("userId") String userId, @Param("username") String username, Pageable pageable);



	@Query("SELECT u FROM User u WHERE u.id = :userId")
	List<User> findByUserId(String userId, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.active = :userStatus")
	List<User> findByStatus(@Param("userStatus") Userstatus userStatus, Pageable pageable);


	List<User> findByUsername(String username, Pageable pageable);


	List<User> findAll(Pageable pageable);

	Optional<User> findUserByUsername(String username);

	boolean existsByUsername(String username);

}


