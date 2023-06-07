package com.gni.banking.Controller;

import com.gni.banking.Model.Account;
import com.gni.banking.Service.UserService;
import com.gni.banking.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController() {

    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/noAccounts")
    public List<User> getAllUsersWithoutAccount() {
        return userService.findUsersWithoutAccount();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public ResponseEntity<User> add(@RequestBody User a) {

        return ResponseEntity.status(201).body(userService.add(a));
    }

    @PutMapping("/{userid}")
    public User update(@RequestBody User user, @PathVariable long userid) throws Exception {
        return userService.update(user, userid);
    }

    @DeleteMapping("/{userid}")
    public User changeStatus(@PathVariable long userid) throws Exception {

        return userService.changeStatus(userid);
    }





}
