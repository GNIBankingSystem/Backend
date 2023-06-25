package com.gni.banking.Service;

import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Enums.Userstatus;
import com.gni.banking.Model.LoginRequestDTO;
import com.gni.banking.Model.LoginResponseDTO;
import com.gni.banking.Enums.Role;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.UserRepository;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Collections;
import java.util.Optional;

@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;





    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;




    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<User> getAll(int limit, int offset, String userId, String username, String status) throws Exception {
        Pageable pageable = PageRequest.of(offset, limit);
        Userstatus userStatus = getUserStatus(status);

        if (userId != null && userStatus != null) {
            return userRepository.findByUserIdAndStatus(userId, userStatus, pageable);
        } else if (userId != null && username != null) {
            return userRepository.findByUserIdAndUsername(userId, username, pageable);
        } else if (userId != null) {

                if (Integer.parseInt(userId) == 1) {
                    throw new IllegalArgumentException("UserId is inaccessible");
                }
                return userRepository.findByUserId(userId, pageable);

        } else if (userStatus != null) {
            return userRepository.findByStatus(userStatus, pageable);
        } else if (username != null) {
            return userRepository.findByUsername(username, pageable);
        }
            return userRepository.findAll(pageable);
    }



    private static Userstatus getUserStatus(String status) {
        Userstatus userStatus = null;
        if (status != null) {
            switch (status.toLowerCase()) {
                case "active" -> userStatus = Userstatus.Active;
                case "inactive" -> userStatus = Userstatus.Inactive;

            }
        }
        return userStatus;
    }

    public User getById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User add(User a) {
        String username = a.getUsername();
        System.out.println(a.getUsername());
        a.setRoles(Role.ROLE_CUSTOMER);
        a.setDailyTransaction(1000);
        a.setDayLimit(1000);
        if (userRepository.findUserByUsername(username).isEmpty()) {
            // Validate username
            String usernameRegex = "^(?!.*\\s)(?=\\S+$).{4,12}$";
            if (!username.matches(usernameRegex)) {
                throw new IllegalArgumentException("Invalid username. Please check the requirements.");
            }

            String password = a.getPassword();
            // Validate password
            String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?!.*123)(?=\\S+$).{8,}";
            if (!password.matches(passwordRegex)) {
                throw new IllegalArgumentException("Password does not meet complexity requirements.");
            }

            a.setPassword(passwordEncoder.encode(password));

            // Generate and assign userId
            long userId = generateUserId();
            a.setId(userId);

            User savedUser = userRepository.save(a);
            return savedUser;
        }
        throw new IllegalArgumentException("Username is already taken.");
    }



    private long generateUserId() {
        long userId = 0;
        while (userRepository.findById(userId).isPresent()) {
            userId++;
        }
        return userId;
    }


    public double getDayLimitById(int userId){
        return userRepository.getDayLimitById(userId);
    }



    public User update(User a, long id) throws Exception {
        User existingUser = getById(id);

        existingUser.setUsername(a.getUsername());
        existingUser.setEmail(a.getEmail());
        existingUser.setPhoneNumber(a.getPhoneNumber());
        existingUser.setAddress(a.getAddress());
        existingUser.setFirstName(a.getFirstName());
        existingUser.setLastName(a.getLastName());
        existingUser.setRoles(a.getRoles());
        existingUser.setActive(Userstatus.Active);


        try {
            return userRepository.save(existingUser);
        } catch (Exception ex) {
            throw new Exception("Inputs missing");
        }

    }

    public User changeStatus(long id) throws Exception {
        User existingUser = getById(id);
        if (existingUser.getActive() == Userstatus.Active) {
            existingUser.setActive(Userstatus.Inactive);
            return userRepository.save(existingUser);
        } else {
            throw new Exception("User is already inactive.");
        }
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
            response.setId(user.getId());
            response.setToken(jwtTokenProvider.createToken(user.getUsername(), user.getRoles(),user.getId()));
            response.setUsername(user.getUsername());
            response.setRole(user.getRoles());
            return response;

        } else {
            throw new AuthenticationException("Invalid username/password");
        }
    }


}
