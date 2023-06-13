package com.gni.banking.Model;

import com.gni.banking.Enums.Role;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private long id;
    private String username;
    private String token;
    private Role role;
}
