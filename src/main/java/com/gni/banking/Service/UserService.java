package com.gni.banking.Service;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

import com.gni.banking.Model.UserRequestDTO;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;



    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameAndLastName(firstName, lastName);
    }


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequestDTO userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());
        user.setTransactionLimit(userRequest.getTransactionLimit());
        user.setDayLimit(userRequest.getDayLimit());
        user.setAccountCount(userRequest.getAccountCount());

        return userRepository.save(user);
    }

    public User add(User user) {
        return repository.save(user);
    }

    public double getDayLimitById(int userId){
        return repository.getDayLimitById(userId);
    }


    // Implement other CRUD methods (e.g., getUser, updateUser, deleteUser) as needed
}
