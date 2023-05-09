package com.gni.banking.Model;

import com.gni.banking.Enums.Role;

public class UserRequestDTO {

    private String firstname;

    private String lastname;

    private String email;

    private int phonenumber;

    private String password;

    private Role role;

    private double transactionLimit;

    private double dayLimit;

    private int accountCount;
}
