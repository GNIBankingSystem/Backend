package com.gni.banking.Service;

import com.gni.banking.Model.Account;
import com.gni.banking.Model.User;
import com.gni.banking.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IbanService ibanService;

    @Autowired
    private UserService userService;

    public List<Account> getAll(int limit, int offset, String userId) throws Exception {
        Pageable pageable = PageRequest.of(offset, limit);
        if (Objects.equals(userId, "") || userId == null){
            return accountRepository.findAll(pageable);
        } else if (Objects.equals(userId, "1")) {
            //TODO - make it so you get an error in insomnia because its a the bank account
            return null;
        } else{
            return accountRepository.findByUserId(userId, pageable);
        }
    }


    public Account getById(String id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public Account getByIban(String iban) {
        return (Account) accountRepository.findById(iban).orElse(null);

    }

    public Account add(Account a) {
        if(a.getId() == null || a.getId().isEmpty()) {
            a.setId(ibanService.GenerateIban());
            String iban = ibanService.GenerateIban();
            a.setId(iban);
        }
        return accountRepository.save(a);
    }


    public Account update(Account account, String iban) throws Exception {
        Account existingAccount = getByIban(iban);

        existingAccount.setUserId(account.getUserId());
        existingAccount.setType(account.getType());
        existingAccount.setAbsoluteLimit(account.getAbsoluteLimit());
        existingAccount.setCurrency(account.getCurrency());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setStatus(account.getStatus());

        try {
            return accountRepository.save(existingAccount);
        } catch (Exception ex) {
            throw new Exception("Required fields missing");
        }
    }

    public void delete(String id) {
        accountRepository.deleteById(id);
    }

    public Account changeStatus(String iban) {
        Account existingAccount = getByIban(iban);
        if (existingAccount.getStatus().toString().equals("Open")) {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Closed);
        } else {
            existingAccount.setStatus(com.gni.banking.Enums.Status.Open);
        }
        return accountRepository.save(existingAccount);

    }

    public String getIbanByUserId(long userId) {
        return accountRepository.getIdByUserId(userId);
    }

    public String getIbanByName(String name) {
        String firstname = name.split("-")[0];
        String lastName = name.split("-")[1];
        User user =  userService.findByFirstNameAndLastName(firstname, lastName);
        String iban = accountRepository.getIdByUserId(user.getId());
        return iban;
    }
}
