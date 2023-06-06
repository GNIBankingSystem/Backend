package com.gni.banking.Controller;

import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.TransactionRequestDTO;
import com.gni.banking.Model.TransactionResponseDTO;
import com.gni.banking.Service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")

public class TransactionController {
    @Autowired
    private TransactionService service;
    private ModelMapper modelMapper;
    public TransactionController() {
        this.service = new TransactionService();
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    public List<TransactionResponseDTO> getAll(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        int page = offset / limit;
        Page<Transaction> transactions = service.getAll(limit, page);
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
    public ResponseEntity<?> add(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        try {
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
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
    public void delete(@PathVariable long id){
        service.delete(id);
    }

}
