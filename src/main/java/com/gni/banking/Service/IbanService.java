package com.gni.banking.Service;

import com.gni.banking.Model.Account;
import com.gni.banking.Model.AccountResponseDTO;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class IbanService {

    @Autowired
    private AccountRepository accountRepository;


    public String GenerateIbanNewIban() {
        StringBuilder ibanBuilder = new StringBuilder("NL");

        // Generate random two-digit number
        Random random = new Random();
        int randomDigits = random.nextInt(100);
        ibanBuilder.append(String.format("%02d", randomDigits));

        // Append the constant characters "INHO0"
        ibanBuilder.append("INHO0");

        // Generate random nine-digit number
        for (int i = 0; i < 9; i++) {
            int randomNum = random.nextInt(10);
            ibanBuilder.append(randomNum);
        }

        return ibanBuilder.toString();
    }

    public String GenerateIban(){
        String iban = GenerateIbanNewIban();
        do{
            iban = GenerateIbanNewIban();
        }while (IbanExists(iban));
        return iban;
    }

    public boolean IbanExists(String id) {
        //iban getten op /accounts/{iban} --> 404 terug --> iban bestaat nog niet
        if (accountRepository.ibanExists(id)) {
            return true;
        } else {
            return false;
        }
    }




}
