package com.gni.banking.Controller;

import com.gni.banking.Model.User;
import com.gni.banking.Model.UserRequestDTO;
import com.gni.banking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserRequestDTO userRequest) {
        User createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(201).body(createdUser);
    }

    // Implement other CRUD endpoints (e.g., getUser, updateUser, deleteUser) as needed
}
