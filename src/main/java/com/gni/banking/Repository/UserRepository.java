package com.gni.banking.Repository;


import com.gni.banking.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    Iterable<User> findByUserId(long userId);
}
