package com.gni.banking.Service;

import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Model.LoginRequestDTO;
import com.gni.banking.Model.LoginResponseDTO;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.AccountRepository;
import com.gni.banking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.AuthenticationException;
import java.util.List;



@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AccountRepository accountRepository ;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;




    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<User> getAll() {
        //checken op paramaters
        return (List<User>) userRepository.findAll();
    }



    public User getById(long id) {
        return (User) userRepository.findById(id).orElse(null);
    }

    public User add(User a) {
        if (userRepository.findUserByUsername(a.getUsername()).isEmpty()) {
            a.setPassword(passwordEncoder.encode(a.getPassword()));
            return userRepository.save(a);
        }
        throw new IllegalArgumentException("Username is already taken");

    }

    public double getDayLimitById(int userId){
        return userRepository.getDayLimitById(userId);
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


    public List<User> findUsersWithoutAccount() {
    	return userRepository.findUsersWithoutAccount();
    }

    /*public void updateDailyTransaction(String accountFrom, double amount){

        //get user die bij account hoort -->
        int userId = accountRepository.getUserIdById(accountFrom);
        double currentDailyTransaction = userRepository.getDailyTransaction(userId);
        double newDailyTransaction = currentDailyTransaction + amount;
        userRepository.updateDailyTransaction(userId, newDailyTransaction);
    }*/

    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {

        // Get the user from the database
        User user = userRepository.findUserByUsername(loginRequest.getUsername()).orElseThrow(() -> new AuthenticationException("User not found"));

        // Check if the password hash matches
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            // Return a JWT to the client
            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(jwtTokenProvider.createToken(user.getUsername(), user.getRoles(),user.getId()));
            response.setUsername(user.getUsername());
            return response;

        } else {
            throw new AuthenticationException("Invalid username/password");
        }
    }



}
