package com.gni.banking.Model;
import com.gni.banking.Enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class UserResponseDTO {

    @Id
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private int phonenumber;

    private String password;

    private Role role;

    private double transactionLimit;

    private double dayLimit;

    private int accountCount;


}
