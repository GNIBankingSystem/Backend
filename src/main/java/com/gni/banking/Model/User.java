package com.gni.banking.Model;

import com.gni.banking.Enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class User {

    @Id
    private long id;

    private String username;
    private String password;
    private String email;
    private long phoneNumber;
    private String address;

    private String firstName;
    private String lastName;
    private Role roles;
    private boolean active;
    private double dayLimit;
    private double transactionLimit;
    private int numberofaccounts;
    private double dailyTransaction;

}
