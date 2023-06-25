package com.gni.banking.Model;

import com.gni.banking.Enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private long id;
    private String username;
    private Role roles;
    private String token;

}

