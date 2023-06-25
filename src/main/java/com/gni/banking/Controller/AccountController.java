package com.gni.banking.Controller;


import com.gni.banking.Configuration.Jwt.JwtTokenDecoder;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.PutAccountDTO;
import com.gni.banking.Service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService service;
    @Autowired
    private JwtTokenDecoder jwtTokenDecoder;

    private ModelMapper modelMapper;

    public AccountController() {
        this.service = new AccountService();
        this.modelMapper = new ModelMapper();
        this.jwtTokenDecoder = new JwtTokenDecoder();
    }


    @GetMapping
    public List<?> getAllAccounts(HttpServletRequest request,
                                  @RequestParam(defaultValue = "0") int offset,
                                  @RequestParam(defaultValue = "10") int limit,
                                  @RequestParam(required = false) Long userId,
                                  @RequestParam(required = false) String type,
                                  @RequestParam(required = false) String status,
                                  @RequestParam(required = false) String firstNameLastName) throws Exception {

        String userRole = jwtTokenDecoder.getRoleInToken(request);
        if (firstNameLastName != null) {
            firstNameLastName = firstNameLastName.toLowerCase();
            return service.findByFirstNameLastName(firstNameLastName);
        }

        if (userRole.equals("ROLE_EMPLOYEE") || userRole.equals("ROLE_ADMIN")) {
            return service.getAll(limit, offset, userId, type, status, firstNameLastName);
        } else if (userRole.equals("ROLE_CUSTOMER")) {
            long idOfUser = jwtTokenDecoder.getIdInToken(request);
            return service.getAll(limit, offset, idOfUser, type, status, firstNameLastName);
        } else {
            throw new Exception("You are not authorized to access this resource");
        }
    }

    //@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(HttpServletRequest request, @PathVariable String id) throws Exception {
        String userRole = jwtTokenDecoder.getRoleInToken(request);
        Account account = service.getById(id);

        if (Objects.equals(id, "NL01INHO0000000001")) {
            if (!userRole.equals("ROLE_ADMIN")) {
                throw new IllegalArgumentException("You are not authorized to access this resource");
            }
        } else if (userRole.equals("ROLE_CUSTOMER")) {
            long idOfUser = jwtTokenDecoder.getIdInToken(request);
            if (account.getUserId() != idOfUser) {
                throw new IllegalArgumentException("You are not authorized to access this resource");
            }
        }
        return ResponseEntity.ok(modelMapper.map(account, Account.class));
    }


    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<?> add(@RequestBody PostAccountDTO a) throws Exception {

        return ResponseEntity.status(201).body(service.add(a));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/addAccount")
    public ResponseEntity<?> add(@RequestBody Account a) {

        return ResponseEntity.status(201).body(service.addCompleteAccount(a));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{iban}")
    public ResponseEntity<?> update(@RequestBody PutAccountDTO putAccount, @PathVariable String iban) throws Exception {
        Account account = service.update(putAccount, iban);
        return ResponseEntity.ok(modelMapper.map(account, Account.class));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{iban}")
    public ResponseEntity<?> changeStatus(@PathVariable String iban) {
        Account account = service.changeStatus(iban);
        return ResponseEntity.ok(modelMapper.map(account, Account.class));
    }
}
