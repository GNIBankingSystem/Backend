package com.gni.banking.Exceptions.Handler;

import com.gni.banking.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({InvalidAccountTypeOnTransactionException.class,
            NotEnoughBalanceException.class,
            LimitOnTransactionExceededException.class,
            InvalidAccountCurrenyOnTransactionException.class})
    public ResponseEntity<Map<String,String>> handleBadRequestExceptions(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountStatusException.class)
    public ResponseEntity<Map<String,String>> handleForbiddenException(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleUnknownException(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
