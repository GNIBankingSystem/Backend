package com.gni.banking.Model;

import com.gni.banking.Enums.Role;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String username;
    private String token;
    private long id;
    private Role roles;
}
