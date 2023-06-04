package com.gni.banking.Service;

import com.gni.banking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    @Autowired
    private UserRepository repository;


    public Integer getUserIdOnFirstName(String firstName) {
        return repository.getUserIdOnFirstName(firstName);
    }
}
