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

@Service
public class IbanService {

    @Autowired
    private AccountRepository accountRepository;


    public String GenerateIbanNewIban() {
        // NLxxINHO0xxxxxxxxx
            int rndFirstNumber = (int) (Math.random() * 100);
            int rndSecondNumber = (int) (Math.random() * 1000000000);
            String iban = "NL" + rndFirstNumber + "INHO0" + rndSecondNumber;
        return iban;
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
