package com.gni.banking.Controller;

import com.gni.banking.Configuration.Jwt.JwtTokenDecoder;
import com.gni.banking.Enums.TransactionType;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.TransactionRequestDTO;
import com.gni.banking.Model.TransactionResponseDTO;
import com.gni.banking.Service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
        try{
            Transaction transaction = service.getById(id);
            return ResponseEntity.ok(modelMapper.map(transaction, TransactionResponseDTO.class));
        }catch (Exception e){
            ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, "Transaction not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody TransactionRequestDTO transactionRequestDTO, HttpServletRequest request) {
        try {
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            transaction.setType(TransactionType.TRANSFER);
            transaction.setPerformedBy(jwtTokenDecoder.getIdInToken(request));
            Transaction addedTransaction = service.add(transaction);
            return ResponseEntity.ok(modelMapper.map(addedTransaction, TransactionResponseDTO.class));
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable long id){
        try{
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            return  ResponseEntity.ok(modelMapper.map(service.update(transaction, id), TransactionResponseDTO.class));
        }
        catch (Exception e){
            ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable long id){
        service.delete(id);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        try {
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            transaction.setType(TransactionType.WITHDRAW);
            Transaction addedTransaction = service.withdraw(transaction);
            return ResponseEntity.ok(modelMapper.map(addedTransaction, TransactionResponseDTO.class));
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        try {
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            transaction.setType(TransactionType.DEPOSIT);
            Transaction addedTransaction = service.deposit(transaction);
            return ResponseEntity.ok(modelMapper.map(addedTransaction, TransactionResponseDTO.class));
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
