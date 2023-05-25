package com.gni.banking.Service;

import com.gni.banking.Model.AccountResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class IbanService {



    public String GenerateIban() {
        // NLxxINHO0xxxxxxxxx
        int rndFirstNumber = (int) (Math.random() * 100);
        int rndSecondNumber = (int) (Math.random() * 1000000000);
        String iban = "NL" + rndFirstNumber + "INHO0" + rndSecondNumber;
        return iban;
    }

    public boolean IbanExists(String iban) {
        //iban getten op /accounts/{iban} --> 404 terug --> iban bestaat nog niet
    }

    @GetMapping("/accounts/{iban}")
    public AccountResponseDTO getAccountsByIban(String iban) {
        // /accounts/{iban}
    }

}
