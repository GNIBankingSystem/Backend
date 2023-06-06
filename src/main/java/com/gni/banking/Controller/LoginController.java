package com.gni.banking.Controller;

import com.gni.banking.Model.LoginRequestDTO;
import com.gni.banking.Model.LoginResponseDTO;
import com.gni.banking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RequestMapping("/auth/login")
@RestController
public class LoginController {

@Autowired
private UserService userService;

    @PostMapping
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) throws AuthenticationException {
        return userService.login(loginRequestDTO);
    }

}
