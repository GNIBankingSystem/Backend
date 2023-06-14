package com.gni.banking.Controller;

import com.gni.banking.Model.LoginRequestDTO;
import com.gni.banking.Model.LoginResponseDTO;
import com.gni.banking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RequestMapping("/login")
@CrossOrigin(origins = "*")
@RestController
public class LoginController {

@Autowired
private UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) throws AuthenticationException {
        try {
            return new ResponseEntity<>(userService.login(loginRequestDTO), null, 200);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, 401);
        }
    }

}
