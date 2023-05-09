package com.gni.banking.Model;
import com.gni.banking.Enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class UserResponseDTO {
    @Id
    @GeneratedValue
    private int id;
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
