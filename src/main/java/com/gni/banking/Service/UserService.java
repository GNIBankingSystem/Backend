package com.gni.banking.Service;

import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service

public class UserService {
    @Autowired
    private UserRepository repository;


    public User findByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameAndLastName(firstName, lastName);
    }

    public User add(User user) throws Exception {
        //checks on account type and amount
        return repository.save(user);
}
