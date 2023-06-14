package com.gni.banking.Model.DTO;

import com.gni.banking.Enums.AccountType;
import lombok.Data;

@Data
public class PostAccountDTO {
    private int userId;
    private AccountType type;

    public void setUserId(int userId) {
        if (userId > 0) {
            this.userId = userId;
        } else {
            throw new IllegalArgumentException("Invalid user id");
        }
    }
}
