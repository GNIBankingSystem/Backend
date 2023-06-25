package com.gni.banking.Controller;

import com.gni.banking.Configuration.Jwt.JwtTokenDecoder;
import com.gni.banking.Enums.TransactionType;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.TransactionPutDto;
import com.gni.banking.Model.TransactionRequestDTO;
import com.gni.banking.Model.TransactionResponseDTO;
import com.gni.banking.Service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transactions")

public class TransactionController {
    @Autowired
    private TransactionService service;
    @Autowired
    private JwtTokenDecoder jwtTokenDecoder;
    private ModelMapper modelMapper;
    public TransactionController() {
        this.service = new TransactionService();
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    public List<TransactionResponseDTO> getAll(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit, @RequestParam(required = false) String iban) {
        int page = offset / limit;
        Page<Transaction> transactions = service.getAll(limit, page, iban);
        return transactions.getContent().stream()
                .map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class))
                .collect(Collectors.toList());
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
         if((id <= 0)) {
            throw new IllegalArgumentException("Illegal id ,please enter a valid id ");
         }
            Transaction transaction = service.getById(id);
            return ResponseEntity.ok(modelMapper.map(transaction, TransactionResponseDTO.class));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> add(@RequestBody TransactionRequestDTO transactionRequestDTO, HttpServletRequest request) throws Exception {

        String userRole = jwtTokenDecoder.getRoleInToken(request);
        long idOfUser = jwtTokenDecoder.getIdInToken(request);
        Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setPerformedBy(jwtTokenDecoder.getIdInToken(request));
        if(userRole.equals("ROLE_EMPLOYEE") || (userRole.equals("ROLE_CUSTOMER") && service.accountFromIsOfUser(transaction, idOfUser))){
            Transaction addedTransaction = service.add(transaction);
            return ResponseEntity.ok(modelMapper.map(addedTransaction, TransactionResponseDTO.class));
        }else{
            throw new IllegalArgumentException("You are not allowed to perform this action");
        }

    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TransactionPutDto transactionPutDto, @PathVariable long id) throws Exception {
            Transaction transaction = modelMapper.map(transactionPutDto, Transaction.class);
            return  ResponseEntity.ok(modelMapper.map(service.update(transaction, id), TransactionResponseDTO.class));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        service.delete(id);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequestDTO transactionRequestDTO) throws Exception {
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            transaction.setType(TransactionType.WITHDRAW);
            Transaction addedTransaction = service.withdraw(transaction);
            return ResponseEntity.ok(modelMapper.map(addedTransaction, TransactionResponseDTO.class));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequestDTO transactionRequestDTO) throws Exception {
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            transaction.setType(TransactionType.DEPOSIT);
            Transaction addedTransaction = service.deposit(transaction);
            return ResponseEntity.ok(modelMapper.map(addedTransaction, TransactionResponseDTO.class));
    }

}
