package com.gni.banking.Controller;

import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.TransactionRequestDTO;
import com.gni.banking.Model.TransactionResponseDTO;
import com.gni.banking.Service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
    public List<TransactionResponseDTO> getAll() {
        List<Transaction> transactions = service.getAll();
        return Arrays.asList(modelMapper.map(transactions, TransactionResponseDTO[].class));
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getById(@PathVariable long id) {
        Transaction transaction = service.getById(id);
        return modelMapper.map(transaction, TransactionResponseDTO.class);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        try {
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            Transaction addedTransaction = service.add(transaction);
            return ResponseEntity.ok(modelMapper.map(addedTransaction, TransactionResponseDTO.class));
        } catch (Exception e) {
            // Here, we return an error status (such as BAD_REQUEST) and the error message as the response body.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable long id){
        try{
            Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
            return  ResponseEntity.ok(modelMapper.map(service.update(transaction, id), TransactionResponseDTO.class));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        service.delete(id);
    }

}
