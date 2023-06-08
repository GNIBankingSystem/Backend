package com.gni.banking.Model;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String username;
    private String token;
    private long id;
}
