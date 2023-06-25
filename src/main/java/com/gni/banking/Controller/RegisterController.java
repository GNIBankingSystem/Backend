package com.gni.banking.Controller;

import com.gni.banking.Model.User;
import com.gni.banking.Model.LoginRequestDTO;
import com.gni.banking.Model.LoginResponseDTO;
import com.gni.banking.Model.UserResponse;
import com.gni.banking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Date;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class RegisterController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody User a) {

        userService.add(a);
        Date creationdate = new Date();
        creationdate.getTime();




        UserResponse userResponse = new UserResponse();
        userResponse.setId(a.getId());
        userResponse.setUsername(a.getUsername());
        userResponse.setToken("Successfully registered");
        userResponse.setRoles(a.getRoles());
        userResponse.setUsercreated(creationdate);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}

