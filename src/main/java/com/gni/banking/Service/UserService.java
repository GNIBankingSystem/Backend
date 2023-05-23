package com.gni.banking.Service;

import com.gni.banking.Model.User;
import com.gni.banking.Model.UserRequestDTO;
import com.gni.banking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

    // Implement other CRUD methods (e.g., getUser, updateUser, deleteUser) as needed
}
