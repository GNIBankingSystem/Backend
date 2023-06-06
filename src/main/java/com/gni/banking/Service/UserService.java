package com.gni.banking.Service;

import com.gni.banking.Enums.Role;
import com.gni.banking.Model.Account;
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
    private UserRepository userRepository;




    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameAndLastName(firstName, lastName);

    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }



    public User getById(long id) {
        return (User) userRepository.findById(id).orElse(null);
    }

    public User add(User a) {
        return userRepository.save(a);
    }

    public double getDayLimitById(int userId){
        return repository.getDayLimitById(userId);
    }


    // Implement other CRUD methods (e.g., getUser, updateUser, deleteUser) as needed

    public User update(User a, long id) throws Exception {
        User existingUser = getById(a.getId());

        existingUser.setUsername(a.getUsername());
        existingUser.setEmail(a.getEmail());
        existingUser.setPhoneNumber(a.getPhoneNumber());
        existingUser.setAddress(a.getAddress());
        existingUser.setFirstName(a.getFirstName());
        existingUser.setLastName(a.getLastName());
        existingUser.setRoles(a.getRoles());
        existingUser.setActive(a.isActive());
        existingUser.setNumberofaccounts(a.getNumberofaccounts());

        try {
            return userRepository.save(existingUser);
        } catch (Exception ex) {
            throw new Exception("Inputs missing");
        }

    }

    public User changeStatus(long id) throws Exception {
        User existingUser = getById(id);
        try
        {
        if (existingUser.isActive()) {
            existingUser.setActive(false);
        } else {
            existingUser.setActive(true);
        }
        } catch (Exception ex) {
            throw new Exception("Cannot find User with that id");
        }
        return userRepository.save(existingUser);


    }

}
