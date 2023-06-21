package com.gni.banking.Controller;



import com.gni.banking.Configuration.Jwt.JwtTokenDecoder;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.PutAccountDTO;
import com.gni.banking.Service.AccountService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

import java.sql.SQLOutput;
import java.util.List;

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
    public List<Account> getAllAccounts(HttpServletRequest request,
                                        @RequestParam(defaultValue = "0") int offset,
                                        @RequestParam(defaultValue = "10") int limit,
                                        @RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(required = false) String status) throws Exception {

            String userRole = jwtTokenDecoder.getRoleInToken(request);

            if(userRole.equals("ROLE_EMPLOYEE")){
                return service.getAll(limit, offset, userId, type, status);
            }else if(userRole.equals("ROLE_CUSTOMER")){
                long idOfUser = jwtTokenDecoder.getIdInToken(request);
                return service.getAll(limit, offset, idOfUser, type, status);
            }else{
                throw new Exception("You are not authorized to access this resource");
            }
    }

    //@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public Account getAccountById(HttpServletRequest request, @PathVariable String id) throws Exception {
        String userRole = jwtTokenDecoder.getRoleInToken(request);
        if(userRole.equals("ROLE_CUSTOMER")){
            long idOfUser = jwtTokenDecoder.getIdInToken(request);
            if(service.getById(id).getUserId() == idOfUser){
                return service.getById(id);
            }else{
                throw new Exception("You are not authorized to access this resource");
            }
        }else if(userRole.equals("ROLE_EMPLOYEE")) {
            return service.getById(id);
        }
        return null;
    }


    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Account> add(@RequestBody PostAccountDTO a) throws Exception {

        return ResponseEntity.status(201).body(service.add(a));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/addAccount")
    public ResponseEntity<Account> add(@RequestBody Account a) {

        return ResponseEntity.status(201).body(service.addCompleteAccount(a));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{iban}")
    public Account update(@RequestBody PutAccountDTO account, @PathVariable String iban) throws Exception {
        return service.update(account, iban);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{iban}")
    public Account changeStatus(@PathVariable String iban) {
        return service.changeStatus(iban);
    }

    //TODO deze mothode overzetten en iban als parameter gebruiken bij getall
    @GetMapping("/getIban/{name}")
    public List<String> getIbanByName(@PathVariable String name) throws Exception {
        return service.getIbanByName(name);
    }
}
