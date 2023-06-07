package com.gni.banking.Service;

<<<<<<< Updated upstream
=======
import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Enums.Role;
import com.gni.banking.Model.LoginRequestDTO;
import com.gni.banking.Model.LoginResponseDTO;
>>>>>>> Stashed changes
import com.gni.banking.Model.User;
import com.gni.banking.Model.UserRequestDTO;
import com.gni.banking.Repository.UserRepository;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< Updated upstream
=======
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    // Implement other CRUD methods (e.g., getUser, updateUser, deleteUser) as needed
=======


    public User getById(long id) {
        return (User) userRepository.findById(id).orElse(null);
    }

    public User add(User user) {
        Optional<User> existingUser = userRepository.findUserByUsername(user.getUsername());
        if (existingUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRoles() == null) {
                user.setRoles(Role.Customer);
            }
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Username is already taken");
        }
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
            return response;

        } else {
            throw new AuthenticationException("Invalid username/password");
        }
    }

    public LoginResponseDTO register(User user) throws AuthenticationException {
        // Check if the username is already taken

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AuthenticationException("Username is already taken");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Set any default role or permissions
        user.setRoles(Role.Customer); //

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Return a JWT to the client
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("Successfully registered");

        return response;


    }





>>>>>>> Stashed changes
}
